package com.example.pdaloiso.gttmobile;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by pdaloiso on 19/05/2015.
 */
public class NFCViewer extends Activity {

    public static final String TAG = "NfcDemo";

    private TextView mTextView;
    private NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mIntentFilters;
    private String[][] mNFCTechLists;

    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        Log.e("TagDispatch","onCreate");
        setContentView(R.layout.search);
         mTextView = (TextView)findViewById(R.id.editText);
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (mNfcAdapter != null) {
            Tag tag = getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String action = getIntent().getAction();
            String s = action + "\n\n" + tag.toString() ;

            mTextView.setText(s);
            new NFCVReader().execute(tag);

        } else {
            mTextView.setText("This phone is not NFC enabled.");
        }

        // create an intent with tag data and deliver to this activity
        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        // set an intent filter for all MIME data
//        IntentFilter ndefIntent = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
//        try {
//            ndefIntent.addDataType("*/*");
//            mIntentFilters = new IntentFilter[] { ndefIntent };
//        } catch (Exception e) {
//            Log.e("TagDispatch", e.toString());
//        }

        mNFCTechLists = new String[][] { new String[] { NfcF.class.getName() } };
    }

    @Override
    public void onNewIntent(Intent intent) {
        Log.e("TagDispatch","onNewIntent");
        String action = intent.getAction();
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        String s = action + "\n\n" + tag.toString();
        mTextView.setText(s);
        // parse through all NDEF messages and their records and pick text type only

        Parcelable[] data = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        Log.e("data",data.toString());
        if (data != null) {

            try {
                for (int i = 0; i < data.length; i++) {
                    NdefRecord[] recs = ((NdefMessage)data[i]).getRecords();
                    for (int j = 0; j < recs.length; j++) {
                        if (recs[j].getTnf() == NdefRecord.TNF_WELL_KNOWN &&
                                Arrays.equals(recs[j].getType(), NdefRecord.RTD_TEXT)) {
                            byte[] payload = recs[j].getPayload();
                            String textEncoding = null;
                            if((payload[0] & 0200) == 0) {
                                textEncoding = "UTF-8";
                            }else{
                                textEncoding = "UTF-16";
                            }
                            int langCodeLen = payload[0] & 0077;

                            s += ("\n\nNdefMessage[" + i + "], NdefRecord[" + j + "]:\n\"" +
                                    new String(payload, langCodeLen + 1, payload.length - langCodeLen - 1,
                                            textEncoding) + "\"");
                            Log.e("onNewIntent",s);
                        }
                    }
                }
            } catch (Exception e) {
                Log.e("TagDispatch", e.toString());
            }
        }


    }

    @Override
    public void onResume() {
        super.onResume();

        if (mNfcAdapter != null)
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, mIntentFilters, mNFCTechLists);
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mNfcAdapter != null)
            mNfcAdapter.disableForegroundDispatch(this);
    }

    private void resolveIntent(Intent intent) {

        if(NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {

            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            MifareClassic mfc = MifareClassic.get(tag);

            try {
                mfc.connect();

                Log.e("Size: ", String.valueOf(Integer.valueOf(mfc.getSize())));
                Log.e("Timeout: " , String.valueOf(Integer.valueOf(mfc.getTimeout())));
                Log.e("Type: " , String.valueOf(Integer.valueOf(mfc.getType())));
                Log.e("BlockCount: " , String.valueOf(Integer.valueOf(mfc.getBlockCount())));
                Log.e("MaxTransceiveLength: " , String.valueOf(Integer.valueOf(mfc.getMaxTransceiveLength())));
                Log.e("SectorCount: ", String.valueOf(Integer.valueOf(mfc.getSectorCount())));

                for(int i = 0; i < mfc.getSectorCount(); ++i) {

                    if(mfc.authenticateSectorWithKeyA(i, MifareClassic.KEY_MIFARE_APPLICATION_DIRECTORY)){
                        Log.e("authenticate","Authorization granted to sector " + String.valueOf(Integer.valueOf(i)) );
                    } else if(mfc.authenticateSectorWithKeyA(i, MifareClassic.KEY_DEFAULT)) {
                        Log.e("authenticate","Authorization granted to sector " +  String.valueOf(Integer.valueOf(i )));
                    } else if(mfc.authenticateSectorWithKeyA(i, MifareClassic.KEY_NFC_FORUM)) {
                        Log.e("authenticate","Authorization granted to sector " + String.valueOf(Integer.valueOf(i)) );
                    } else {
                        Log.e("authenticate","Authorization denied to sector " + String.valueOf(Integer.valueOf(i)));
                        continue;
                    }

                    for(int k = 0; k < mfc.getBlockCountInSector(i); ++k)
                    {
                        int block = mfc.sectorToBlock(i) + k;
                        byte[] data = null;

                        try {

                            data = mfc.readBlock(block);
                        } catch (IOException e) {
                            Log.e(" data: " , e.getMessage());
                            continue;
                        }

                    }
                }
                mfc.close();

            } catch (IOException e) {
                Log.e("", e.getMessage());
            } finally {
                try {
                    mfc.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private  class NFCVReader extends AsyncTask<Tag, Void, String> {

        @Override
        protected String doInBackground(Tag... params) {
            Tag tag = params[0];
            NfcV nfcvTag = NfcV.get(tag);
            byte[] tagID = nfcvTag.getTag().getId();
            Log.d("Hex ID", getHex(tagID));

            return Arrays.toString(tagID);
        }

        /**
         *
         * @param bytes	Array of bytes read from {@link NfcV.get(Tag)}
         * @return String representing ID as Hex
         */
        private String getHex(byte[] bytes) {
            StringBuilder sb = new StringBuilder();
            for (int x = 0; x < bytes.length; x++) {
                int bit = bytes[x] & 0xFF;
                if (bit < 0x10) {
                    sb.append('0');
                }
                sb.append(Integer.toHexString(bit));
                sb.append(" ");
            }
            return sb.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                mTextView.setText("Hex ID: " + result);
            }
        }
    }
}
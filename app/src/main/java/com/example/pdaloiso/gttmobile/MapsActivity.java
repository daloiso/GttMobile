package com.example.pdaloiso.gttmobile;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.pdaloiso.gttmobile.database.SqlController;
import com.example.pdaloiso.gttmobile.model.ModelParsedWithGson;
import com.example.pdaloiso.gttmobile.model.Percorso;
import com.example.pdaloiso.gttmobile.ws.GsonRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private SqlController sqlcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        sqlcon = new SqlController(this);

        setUpMapIfNeeded();


    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        Percorso p = sqlcon.getPercorso();
        LatLng latlon1 = new LatLng(p.getFermate().get(0).getX(),
                p.getFermate().get(0).getY());
        LatLng latlon2 = new LatLng(p.getFermate().get(1).getX(),
                p.getFermate().get(1).getY());
        mMap.addMarker(new MarkerOptions().position(latlon1).title("Marker1"));
        mMap.addMarker(new MarkerOptions().position(latlon2).title("Marker2"));

        mMap.addPolyline(new PolylineOptions().geodesic(true)
                        .add(latlon1)
                        .add(latlon2)
        );

        //TODO Northern > southern
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(((latlon1.latitude + latlon2.latitude) / 2),
                        (latlon1.longitude + latlon2.longitude) / 2),
                15));

        /*
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://api.androidhive.info/volley/person_object.json";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject response) {
                try {
                    String email = response.getString("email");
                    JSONObject phone = response.getJSONObject("phone");
                    String home = phone.getString("home");
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, null);

        GsonRequest gsonObjRequest = new GsonRequest<ModelParsedWithGson>(Request.Method.GET, url,
                ModelParsedWithGson.class, null, new Response.Listener<ModelParsedWithGson>() {
            @Override
            public void onResponse(ModelParsedWithGson response) {

            }
        },null);

        queue.add(jsObjRequest);
        */
    }

}

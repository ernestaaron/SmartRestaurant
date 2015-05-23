package com.engineeringbits.smartrestuarant;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.google.gson.Gson;

import java.util.List;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener  {
    private GoogleApiClient mGoogleApiClient; // declare Google Api Client Fusion Provider
    private LocationRequest mLocationRequest; // declare the Location Request
    private Location mCurrentLocation; // Location object
    private static final String TAG = "MainActivity";//logging results



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "before calling createRequest");
        createLocationRequest();// call func to create new location request
        Log.d(TAG, "after calling createRequest");
        buildGoogleApiClient(); // call the func fused location provider
        Log.d(TAG, "after calling buildGoogleApiClient");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //main entry point for fusion location provider
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

    }

    @Override
    public void onConnected(Bundle bundle) {
        //start to get the location update
        Log.d(TAG, "before calling startupdate");
        startLocationUpdates();
}

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    //create location request
    protected void createLocationRequest() {
        Log.d(TAG, "in calling createLocationRequest");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(60000);
        mLocationRequest.setFastestInterval(60000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void startLocationUpdates() {
    LocationServices.FusedLocationApi.requestLocationUpdates(
            mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        updateUI();
    }

    private void updateUI() {
        Log.d(TAG, "lat is"+String.valueOf(mCurrentLocation.getLatitude()));
        Log.d(TAG, "long is"+String.valueOf(mCurrentLocation.getLongitude()));
        ListView listView = (ListView) findViewById(R.id.listview);
        SmartRestaurantAdapter adapter = new SmartRestaurantAdapter();
        listView.setAdapter(adapter);
    }

    public class SmartRestaurantAdapter extends BaseAdapter {

        List<com.engineeringbits.smartrestuarant.ResponseData.Location> locations = getResults();

        @Override
        public int getCount() {
            return locations.size();
        }

        @Override
        public Object getItem(int position) {
            return locations.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null)
            {
                LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_item, parent, false);
            }

            TextView name = (TextView)convertView.findViewById(R.id.textView1);
            TextView vicinity = (TextView)convertView.findViewById(R.id.textView2);
            TextView open = (TextView)convertView.findViewById(R.id.textView3);

            ResponseData.Location location = locations.get(position);

            name.setText(location.getName());
            vicinity.setText(location.getVicinity());
            open.setText(location.getHours().openStatus());

            return convertView;
        }

    }

    private List<ResponseData.Location> getResults(){
        String json = "{\n" +
                "   \"html_attributions\" : [],\n" +
                "   \"results\" : [\n" +
                "      {\n" +
                "         \"name\" : \"Australian Cruise Group\",\n" +
                "         \"vicinity\" : \"32 The Promenade, King Street Wharf 5, Sydney\",\n" +
                "         \"opening_hours\" : {\n" +
                "            \"open_now\" : false,\n" +
                "            \"weekday_text\" : []\n" +
                "           }\n" +
                "      },\n" +
                "      {\n" +
                "         \"name\" : \"McDonalds\",\n" +
                "         \"vicinity\" : \"123 Some Building, 123 Fake Street, Victoria\",\n" +
                "         \"opening_hours\" : {\n" +
                "            \"open_now\" : true,\n" +
                "            \"weekday_text\" : []\n" +
                "           }\n" +
                "      }\n" +
                "    ]\n" +
                "}";
        ResponseData data = new Gson().fromJson(json, ResponseData.class);

        return data.getResults();
    }
}

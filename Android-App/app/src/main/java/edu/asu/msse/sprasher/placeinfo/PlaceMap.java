package edu.asu.msse.sprasher.placeinfo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import androidx.appcompat.app.AppCompatActivity;

/*
Copyright 2020 Sarvansh Prasher

Rights to use this code given to Arizona State University
& Professor Timothy Lindquist (Tim.Lindquist@asu.edu) for course SER 423

@author Sarvansh Prasher mailto:sprasher@asu.edu

@version 10 April,2020

*/

public class PlaceMap extends AppCompatActivity  implements OnMapReadyCallback, GoogleMap.OnMapClickListener, NetworkCallback{


    private GoogleMap mMap;
    private Marker marker;
    private MarkerOptions markerOptions;
    private CameraPosition cameraPosition;

    private SupportMapFragment mapFragment;

    private PlaceOverlayAdd addPlaceDialog;

    private boolean ifNewPlaceAdded = false;

    private PlaceDescription place1;
    private PlaceDescription place2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_map);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        place1 = (PlaceDescription)getIntent().getSerializableExtra("PLACE1");
        place2 = (PlaceDescription)getIntent().getSerializableExtra("PLACE2");
        mapFragment.getMapAsync(this);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(this);
        markerOptions = new MarkerOptions();
        markerOptions.draggable(true);
        addMarker();

    }


    private void addMarker(){

        LatLng toLatLng = new LatLng(place2.getLatitude(), place2.getLongitude());
        markerOptions.position(toLatLng).title(place2.getName());
        marker = mMap.addMarker(markerOptions);

        LatLng fromLatLng = new LatLng(place1.getLatitude(), place1.getLongitude());
        markerOptions.position(fromLatLng).title(place1.getName());
        marker = mMap.addMarker(markerOptions);

        lineBetween(fromLatLng, toLatLng);

        cameraPosition = new CameraPosition.Builder()
                .target(getMiddleLatLng(fromLatLng, toLatLng))
                .zoom(getZoomLevel(getDistance(place1, place2)))
                .build();

        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


    }

     private void lineBetween(LatLng fromLatLng, LatLng toLatLng){

        Polyline line = mMap.addPolyline(new PolylineOptions()
                .add(fromLatLng, toLatLng)
                .width(5)
                .color(Color.RED));

    }
    private int getZoomLevel(Double distance){
        if(distance<100){
            return 10;
        }else{
            return 4;
        }
    }

    private LatLng getMiddleLatLng(LatLng fromLatLng, LatLng toLatLng){
        double midLat = (fromLatLng.latitude+toLatLng.latitude)/2;
        double midLng = (fromLatLng.longitude+toLatLng.longitude)/2;
        return new LatLng(midLat, midLng);
    }


    @Override
    public void onMapClick(LatLng latLng) {


        PlaceDescription placeDescription = new PlaceDescription();
        placeDescription.setLatitude(latLng.latitude);
        placeDescription.setLongitude(latLng.longitude);

        Intent intent = new Intent(this, PlaceOverlayAdd.class);
        intent.putExtra("PLACE_CLICKED", placeDescription);
        startActivityForResult(intent, 7777);


    }

    @Override
    public void onActivityResult(int req, int res, Intent data){

        if(req==7777 && res== Activity.RESULT_OK){
            PlaceDescription placeClicked = (PlaceDescription)data.getSerializableExtra("DATA");
            drawMarkeronMap(placeClicked);
            addPlaceRemote(placeClicked);
            addPlace(placeClicked);
            addPlaceDB(placeClicked);
        }

    }

    private void drawMarkeronMap(PlaceDescription place){

        markerOptions.position(new LatLng(place.getLatitude(), place.getLongitude())).title(place.getName());
        marker = mMap.addMarker(markerOptions);
        Toast.makeText(this, "Place "+place.getName()+" Added", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed(){
        if(ifNewPlaceAdded){
            setResult(Activity.RESULT_OK);
        }
        super.onBackPressed();
    }


    private void addPlace(PlaceDescription place){
        Utility.returnList().add(Utility.returnList().size(), place);
    }

    private void addPlaceDB(PlaceDescription place){
        Utility.addToDB(place);
    }

    private void addPlaceRemote(PlaceDescription place){
        Utility.addItem(this, place);
    }



    public static Double getDistance(PlaceDescription currentPlace, PlaceDescription place) {

        Double lat1 = 0.0;
        Double lat2 = 0.0;
        Double lon1 = 0.0;
        Double lon2 = 0.0;

        lat1 = currentPlace.getLatitude();
        lon1 = currentPlace.getLongitude();

        lat2 = place.getLatitude();
        lon2 = place.getLongitude();

        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0.0;
        } else {
            double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2))
                    + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                    * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515 * 1.609344;
            return (dist);
        }
    }

    @Override
    public void getResultFromNetwork(Object result) {
        Toast.makeText(this, "Place saved in server", Toast.LENGTH_SHORT).show();
    }
}
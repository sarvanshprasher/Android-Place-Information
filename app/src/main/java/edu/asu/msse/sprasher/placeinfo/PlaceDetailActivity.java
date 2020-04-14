package edu.asu.msse.sprasher.placeinfo;

/*
Copyright 2020 Sarvansh Prasher

Rights to use this code given to Arizona State University
& Professor Timothy Lindquist (Tim.Lindquist@asu.edu) for course SER 423

@author Sarvansh Prasher mailto:sprasher@asu.edu

@version 20 January,2020

*/

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import java.util.ArrayList;
import java.util.List;

public class PlaceDetailActivity extends AppCompatActivity implements NetworkCallback{
    EditText answerName, answerDescription, answerCategory, answerAddressTitle, answerAddressStreet,
            answerElevation, answerLatitude, answerLogitude, answerDistance, answerBearing;
    Spinner spinner;

    private PlaceDescription currentPlace;
    private PlaceDescription selectedPlace;
    private int INDEX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViews();
        currentPlace = getPlaceFromIntent();
        Log.d("CP", currentPlace.toString());

        if (currentPlace == null) {
            Toast.makeText(getApplicationContext(), "Could not create object", Toast.LENGTH_LONG).show();
        } else {
            populateViews(currentPlace);
        }

        final List<PlaceDescription> places = PlaceLibrary.getAllPlacesFronJson(this);
        List<String> placesNames = new ArrayList<>();

        for (PlaceDescription place : places) {
            placesNames.add(place.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, placesNames);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                        PlaceDescription place = places.get(pos);
                        selectedPlace = place;

                        Double distance = getDistance(place);

                        Double bearing = getBearing(place);

                        answerDistance.setText(String.format("%s", distance) + " Km ");

                        answerBearing.setText(String.format("%s", bearing) + " ° ");

                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });


    }

    private void initializeViews() {

        answerName = findViewById(R.id.answerName);
        answerDescription = findViewById(R.id.answerDescription);
        answerCategory = findViewById(R.id.answerCategory);
        answerAddressTitle = findViewById(R.id.answerAddressTitle);
        answerAddressStreet = findViewById(R.id.answerAddressStreet);
        answerElevation = findViewById(R.id.answerElevation);
        answerLatitude = findViewById(R.id.answerLatitude);
        answerLogitude = findViewById(R.id.answerLogitude);
        spinner = findViewById(R.id.placeSelection);
        answerDistance = findViewById(R.id.distance);
        answerBearing = findViewById(R.id.bearing);
    }

    private void populateViews(PlaceDescription pd) {
        answerName.setText(pd.getName());
        answerDescription.setText(pd.getDescription());
        answerCategory.setText(pd.getCategory());
        answerAddressTitle.setText(pd.getAddressTitle());
        answerAddressStreet.setText(pd.getAddressStreet());
        answerElevation.setText(String.format("%s", pd.getElevation()));
        answerLatitude.setText(String.format("%s", pd.getLatitude()));
        answerLogitude.setText(String.format("%s", pd.getLongitude()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_item:
                Intent editIntent = new Intent(this, PlaceAddActivity.class);
                editIntent.setAction("MODIFY_PLACE");
                editIntent.putExtra("MODIFY_PLACE",currentPlace );
                editIntent.putExtra("INDEX",INDEX);
                startActivityForResult(editIntent,9000);
                return true;

            case R.id.remove_item:
                Intent intent = new Intent(this, PlaceRemoveActivity.class);
                startActivityForResult(intent, 8000);
                return true;

            case R.id.map:
                Intent openMapIntent = new Intent(this, PlaceMap.class);
                openMapIntent.putExtra("PLACE1", currentPlace);
                openMapIntent.putExtra("PLACE2", selectedPlace);
                startActivityForResult(openMapIntent, 8000);


                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private PlaceDescription getPlaceFromIntent() {
        PlaceDescription placeDescription = (PlaceDescription) getIntent().getSerializableExtra("CURRENT_PLACE");
        INDEX = getIntent().getIntExtra("INDEX",0);
        return placeDescription;
    }

    private Double getDistance(PlaceDescription place) {

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

    private Double getBearing(PlaceDescription place) {

        Double lat1 = 0.0;
        Double lat2 = 0.0;
        Double lon1 = 0.0;
        Double lon2 = 0.0;

        lat1 = currentPlace.getLatitude();
        lat1 = Math.toRadians(lat1);

        lon1 = currentPlace.getLongitude();

        lat2 = place.getLatitude();
        lat2 = Math.toRadians(lat2);

        lon2 = place.getLongitude();

        double longDiff = Math.toRadians(lon2 - lon1);
        double y = Math.sin(longDiff) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(longDiff);

        return (Math.toDegrees(Math.atan2(y, x)) + 360) % 360;
    }


    @Override
    public void onActivityResult(int req, int res, Intent data){
        if(req==8000 && res == Activity.RESULT_OK){
            String name = Utility.returnList().get(INDEX).getName();
            Utility.returnList().remove(INDEX);
            setResult(Activity.RESULT_OK);
            Utility.deleteItem(this, name);
            Utility.deleteItemDB(name);
            finish();
        }

        if(req==9000 && res == Activity.RESULT_OK){
            currentPlace = Utility.returnList().get(INDEX);
            populateViews(currentPlace);
        }
    }

    @Override
    public void getResultFromNetwork(Object result) {

        populateViews((PlaceDescription) result);
    }
}

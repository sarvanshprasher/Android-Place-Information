package edu.asu.msse.sprasher.placeinfo;

/*
Copyright 2020 Sarvansh Prasher

Rights to use this code given to Arizona State University
& Professor Timothy Lindquist (Tim.Lindquist@asu.edu) for course SER 423

@author Sarvansh Prasher mailto:sprasher@asu.edu

@version 7 February ,2020

*/

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class PlaceAddActivity extends AppCompatActivity implements NetworkCallback{

    EditText answerName, answerDescription, answerCategory, answerAddressTitle, answerAddressStreet,
            answerElevation, answerLatitude, answerLogitude;

    private PlaceDescription place;
    private int INDEX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_add);
        answerName = findViewById(R.id.answerName);
        answerDescription = findViewById(R.id.answerDescription);
        answerCategory = findViewById(R.id.answerCategory);
        answerAddressTitle = findViewById(R.id.answerAddressTitle);
        answerAddressStreet = findViewById(R.id.answerAddressStreet);
        answerElevation = findViewById(R.id.answerElevation);
        answerLatitude = findViewById(R.id.answerLatitude);
        answerLogitude = findViewById(R.id.answerLogitude);
        getDataFromIntent();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();

        if(intent.getAction() != null && intent.getAction().equals("MODIFY_PLACE")){

            place = (PlaceDescription) intent.getSerializableExtra("MODIFY_PLACE");

            if(place!=null){
                setData();
            }

        }
    }

    private void setData() {

        answerName.setEnabled(false);
        answerName.setText(place.getName());
        answerDescription.setText(place.getDescription());
        answerCategory.setText(place.getCategory());
        answerAddressTitle.setText(place.getAddressTitle());
        answerAddressStreet.setText(place.getAddressStreet());
        answerElevation.setText(String.format("%s", place.getElevation()));
        answerLatitude.setText(String.format("%s", place.getLatitude()));
        answerLogitude.setText(String.format("%s", place.getLongitude()));
        INDEX = getIntent().getIntExtra("INDEX",0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_place_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_item:
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
                saveDataToMemory();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void saveDataToMemory(){
        place =  getData();
        if(getIntent().getAction()!=null && getIntent().getAction().equals("MODIFY_PLACE")){

            List<PlaceDescription> places = Utility.returnList();
            Log.d("DBUPDATE", "saveDataToMemory: "+(places==null));
            places.set(INDEX,place);
            setResult(Activity.RESULT_OK);
            Utility.updateToDB(place);
        }else{
            Log.d("DBADD", "saveDataToMemory: ");
            Utility.returnList().add(Utility.returnList().size(),place);
            Utility.addToDB(place);

        }
        Utility.addItem(this, place);
        finish();
    }

    private PlaceDescription getData(){
        PlaceDescription placeDescription = new PlaceDescription();

        placeDescription.setName(answerName.getText().toString());
        placeDescription.setDescription(answerDescription.getText().toString());
        placeDescription.setCategory(answerCategory.getText().toString());
        placeDescription.setAddressTitle(answerAddressTitle.getText().toString());
        placeDescription.setAddressStreet(answerAddressStreet.getText().toString());
        placeDescription.setElevation(Double.parseDouble(answerElevation.getText().toString()));
        placeDescription.setLatitude(Double.parseDouble(answerLatitude.getText().toString()));
        placeDescription.setLongitude(Double.parseDouble(answerLogitude.getText().toString()));

        return placeDescription;
    }


    @Override
    public void getResultFromNetwork(Object result) {

    }
}

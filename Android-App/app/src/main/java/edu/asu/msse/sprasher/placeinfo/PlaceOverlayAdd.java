package edu.asu.msse.sprasher.placeinfo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

/*
Copyright 2020 Sarvansh Prasher

Rights to use this code given to Arizona State University
& Professor Timothy Lindquist (Tim.Lindquist@asu.edu) for course SER 423

@author Sarvansh Prasher mailto:sprasher@asu.edu

@version 10 April,2020

*/

public class PlaceOverlayAdd extends AppCompatActivity {


    private EditText name, desc, category, elevation, latitude, longitude, streetAddress, streetTitle;
    private Button save, cancel;
    private PlaceDescription place;


    @Override
    public void onCreate(Bundle sis){
        super.onCreate(sis);
        setContentView(R.layout.place_overlay_add);
        name = findViewById(R.id.name);
        desc = findViewById(R.id.desc);
        elevation = findViewById(R.id.elevation);
        category = findViewById(R.id.category);
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);
        save = findViewById(R.id.save);
        cancel = findViewById(R.id.cancel);
        streetAddress = findViewById(R.id.streetAddress);
        streetTitle = findViewById(R.id.streetTitle);

        place = (PlaceDescription)getIntent().getSerializableExtra("PLACE_CLICKED");

        latitude.setText(place.getLatitude().toString());
        longitude.setText(place.getLongitude().toString());

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                data.putExtra("DATA", getPlace());
                setResult(Activity.RESULT_OK, data);
                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });
    }


    private PlaceDescription getPlace(){
        place.setName(name.getText().toString());
        place.setDescription(desc.getText().toString());
        place.setCategory(category.getText().toString());
        place.setElevation(Double.parseDouble(elevation.getText().toString()));
        return place;
    }

}

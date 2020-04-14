package edu.asu.msse.sprasher.placeinfo;

/*
Copyright 2020 Sarvansh Prasher

Rights to use this code given to Arizona State University
& Professor Timothy Lindquist (Tim.Lindquist@asu.edu) for course SER 423

@author Sarvansh Prasher mailto:sprasher@asu.edu

@version 7 February ,2020

*/

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class PlaceLibraryListActivity extends AppCompatActivity implements NetworkCallback, ListClickListener{

    private static final String TAG = "PlaceLibraryListActivity";
    private RecyclerView placeListView;
    private LinearLayoutManager lm;
    private PlaceLibraryListItemAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_library_list);
        Utility.initList();
        placeListView = findViewById(R.id.recyclerView);
        lm = new LinearLayoutManager(this);
        setUpRV();
        loadDB();
        setUpRV();

    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d("RESUME","RESUME");
        adapter = new PlaceLibraryListItemAdapter(Utility.returnList(), this);
        placeListView.setAdapter(adapter);
        placeListView.setLayoutManager(lm);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_item:
                Intent intent = new Intent(this, PlaceAddActivity.class);
                startActivity(intent);
                return true;

            case R.id.sync:
                syncserver();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void getResultFromNetwork(Object result) {


        List<PlaceDescription> allplaces = (List<PlaceDescription>) result;

        for(PlaceDescription place:allplaces){
            Log.d("RETURNLIST",place.getName());
            Utility.addToDB(place);
        }

//        Utility.setMemoryList(allplaces);
        adapter = new PlaceLibraryListItemAdapter(Utility.returnList(), this);
        placeListView.setAdapter(adapter);
        placeListView.setLayoutManager(lm);

        Toast.makeText(this, "Sync completed", Toast.LENGTH_SHORT).show();
    }



    public void openDetailActivity(int position){

        PlaceDescription pd = Utility.returnList().get(position);
        Intent intent = new Intent(this, PlaceDetailActivity.class);
        intent.putExtra("CURRENT_PLACE", pd);
        intent.putExtra("INDEX",position);
        startActivityForResult(intent, 8080);
    }


    @Override
    public void onActivityResult(int req, int res, Intent data){

        if(req==8080 && res == Activity.RESULT_OK){
            adapter = new PlaceLibraryListItemAdapter(Utility.returnList(), this);
            placeListView.setAdapter(adapter);
            placeListView.setLayoutManager(lm);
        }
    }

    @Override
    public void onItemClicked(int position) {
        openDetailActivity(position);
    }


    private void setUpRV(){
        adapter = new PlaceLibraryListItemAdapter(Utility.returnList(), this);
        placeListView.setAdapter(adapter);
        placeListView.setLayoutManager(lm);
    }

    private void loadDB(){
        try {
            Utility.initDatabase(this);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Utility.loadAllPlacesFromDB();
    }

    private void syncserver(){
        Toast.makeText(this, "Syncing with server...", Toast.LENGTH_SHORT).show();
        Utility.deleteTableData();
        loadDataFromServer();

    }


    public void loadDataFromServer(){
        try{
            MethodInformation mi = new MethodInformation(this, getString(R.string.defaulturl),"getNames",
                    new Object[]{});
            AsyncCollection ac = (AsyncCollection) new AsyncCollection().execute(mi);
        } catch (Exception ex){
            android.util.Log.w(this.getClass().getSimpleName(),"Exception creating adapter: "+
                    ex.getMessage());
        }

        Utility.getPlacesFromJson(this);
    }

}

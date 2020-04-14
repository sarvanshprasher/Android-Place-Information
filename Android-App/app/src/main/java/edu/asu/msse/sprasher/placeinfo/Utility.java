package edu.asu.msse.sprasher.placeinfo;

/*
Copyright 2020 Sarvansh Prasher

Rights to use this code given to Arizona State University
& Professor Timothy Lindquist (Tim.Lindquist@asu.edu) for course SER 423

@author Sarvansh Prasher mailto:sprasher@asu.edu

@version 20 January,2020

*/


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class Utility {

    private static List<PlaceDescription> placeDescription;
    private static String[] places;
    private static PlacesDb placeDb;
    private static SQLiteDatabase sqLiteDatabase;
    private static Context context;


    public static void initList(){
        placeDescription = new ArrayList<>();
    }

    public static List<PlaceDescription> returnList(){

        return placeDescription;
    }


    public static void setMemoryList(List<PlaceDescription> allplaces){
        placeDescription = allplaces;
    }


    public static String readJSONFromAsset(Context context, String fileName) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static void getPlacesFromJson (Context context){
        placeDescription = PlaceLibrary.getAllPlacesFronJson(context);
    }



    public static void deleteItem(Context context, String placeName){

        MethodInformation mi = new MethodInformation((NetworkCallback) context, context.getString(R.string.defaulturl),"remove",
                new String[]{placeName});
        ModifyAsyncTask deletePlaceAsyncTask = new ModifyAsyncTask();
        deletePlaceAsyncTask.execute(mi);

    }


    public static void addItem(Context context, PlaceDescription placeDescription) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("address-title",placeDescription.getAddressTitle());
            jsonObject.put("address-street",placeDescription.getAddressStreet());
            jsonObject.put("elevation",placeDescription.getElevation());
            jsonObject.put("latitude",placeDescription.getLatitude());
            jsonObject.put("longitude",placeDescription.getLongitude());
            jsonObject.put("image","Image");
            jsonObject.put("name",placeDescription.getName());
            jsonObject.put("image",placeDescription.getName());
            jsonObject.put("description",placeDescription.getDescription());
            jsonObject.put("category",placeDescription.getCategory());
            JSONObject placejson = new JSONObject();
            placejson.put(placeDescription.getName(), jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MethodInformation mi = new MethodInformation((NetworkCallback) context, context.getString(R.string.defaulturl),"add",
                new Object[]{jsonObject});
        ModifyAsyncTask deletePlaceAsyncTask = new ModifyAsyncTask();
        deletePlaceAsyncTask.execute(mi);

    }



    public static void initDatabase(Context _context) throws java.sql.SQLException {
        placeDb = new PlacesDb(_context);
        context = _context;
        try {
            sqLiteDatabase = placeDb.openDB();
        } catch (SQLException e) {
            Log.d("YYY", "initDatabase: EXCEPTION");
            e.printStackTrace();
        }
    }

    public static void loadAllPlacesFromDB(){

        //List<PlaceDescription> allPlaces = new ArrayList<>();

        Cursor c= sqLiteDatabase.rawQuery("SELECT * FROM place", null);
        c.moveToFirst();

        int count = c.getCount();

        for(int i=0;i<count;i++){

            PlaceDescription place = new PlaceDescription();

            place.setName(c.getString(c.getColumnIndex("name")));
            place.setDescription(c.getString(c.getColumnIndex("description")));
            place.setCategory(c.getString(c.getColumnIndex("category")));
            place.setAddressStreet(c.getString(c.getColumnIndex("address_street")));
            place.setAddressTitle(c.getString(c.getColumnIndex("address_title")));
            place.setLongitude(Double.parseDouble(c.getString(c.getColumnIndex("longitude"))));
            place.setLatitude(Double.parseDouble(c.getString(c.getColumnIndex("latitude"))));
            place.setElevation(Double.parseDouble(c.getString(c.getColumnIndex("elevation"))));
            placeDescription.add(place);

            c.moveToNext();
        }

    }


    public static void addToDB(PlaceDescription placeDescription){
        String name = placeDescription.getName();
        String description = placeDescription.getDescription();
        String category = placeDescription.getCategory();
        String addresstitle = placeDescription.getAddressTitle();
        String addresstreet = placeDescription.getAddressStreet();
        Double elevation = placeDescription.getElevation();
        Double latitude = placeDescription.getLatitude();
        Double longitude = placeDescription.getLongitude();

        ContentValues contentValues = new ContentValues();
        contentValues.put("name",name);
        contentValues.put("description",description);
        contentValues.put("category",category);
        contentValues.put("address_title",addresstitle);
        contentValues.put("address_street",addresstreet);
        contentValues.put("elevation",elevation);
        contentValues.put("latitude",latitude);
        contentValues.put("longitude",longitude);

        long rownum = sqLiteDatabase.insert("place",null,contentValues);
        Log.d("DBADD", ""+rownum);
    }


    public static void updateToDB(PlaceDescription placeDescription){

        String name = placeDescription.getName();
        String description = placeDescription.getDescription();
        String category = placeDescription.getCategory();
        String addresstitle = placeDescription.getAddressTitle();
        String addressstreet = placeDescription.getAddressStreet();
        Double elevation = placeDescription.getElevation();
        Double latitude = placeDescription.getLatitude();
        Double longitude = placeDescription.getLongitude();

        ContentValues contentValues = new ContentValues();
        contentValues.put("name",name);
        contentValues.put("description",description);
        contentValues.put("category",category);
        contentValues.put("address_title",addresstitle);
        contentValues.put("address_street",addressstreet);
        contentValues.put("elevation",elevation);
        contentValues.put("latitude",latitude);
        contentValues.put("longitude",longitude);

        sqLiteDatabase.update("place", contentValues, "name = ?", new String[]{name});

    }


    public static void deleteItemDB(String place){
        String table = "place";
        String whereClause = "name=?";
        String[] whereArgs = new String[] { String.valueOf(place) };
        sqLiteDatabase.delete(table, whereClause, whereArgs);
    }


    public static void deleteTableData(){
        sqLiteDatabase.delete("place", "1", null);
    }

}

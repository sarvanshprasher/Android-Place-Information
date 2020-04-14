package edu.asu.msse.sprasher.placeinfo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
Copyright 2020 Sarvansh Prasher

Rights to use this code given to Arizona State University
& Professor Timothy Lindquist (Tim.Lindquist@asu.edu) for course SER 423

@author Sarvansh Prasher mailto:sprasher@asu.edu

@version 21 February,2020

*/

public class PlacesDb extends SQLiteOpenHelper {

    private static final boolean debugon = true;
    private static final int DATABASE_VERSION = 3;
    private static String dbName = "placesdb";
    private String dbPath;
    private SQLiteDatabase placesDb;
    List<PlaceDescription> places= new ArrayList<>();
    private final Context context;


    public PlacesDb(Context context) {
        super(context,dbName, null, DATABASE_VERSION);
        this.context = context;

        dbPath = context.getFilesDir().getPath()+"/";
        android.util.Log.d(this.getClass().getSimpleName(),"db path is: "+
                context.getDatabasePath("placesdb"));
        android.util.Log.d(this.getClass().getSimpleName(),"dbpath: "+dbPath);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private boolean checkDB(){
        SQLiteDatabase checkDB = null;
        boolean placesExists = false;
        try{
            Log.d("YYY", "checkDB: 1");
            String path = dbPath + dbName + ".db";
            File aFile = new File(path);
            if(aFile.exists()){
                Log.d("YYY", "checkDB: 2");
                checkDB = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
                if (checkDB!=null) {
                    Log.d("YYY", "checkDB: 3");
                    Cursor tabChk = checkDB.rawQuery("SELECT name FROM sqlite_master where type='table' and name='place';", null);
                    if(tabChk == null){
                        Log.d("YYY", "checkDB: is null");
                        android.util.Log.d(this.getClass().getSimpleName(),"table does not exist with name place "
                                + null);
                    }else{
                        Log.d("YYY", "checkDB: 4");
                        tabChk.moveToNext();
                        placesExists = !tabChk.isAfterLast();
                    }
                    Log.d("YYY", "checkDB: 7"+placesExists);
                    if(placesExists){
                        Cursor c= checkDB.rawQuery("SELECT * FROM place", null);
                        c.moveToFirst();
                        while(!c.isAfterLast()) {
                            String crsName = c.getString(0);
                            int crsid = c.getInt(1);
                            Log.d("CourseDB --> checkDB","Course table has CourseName: "+
                                    crsName+"\tCourseID: "+crsid);
                            c.moveToNext();
                        }
                        placesExists = true;
                        Log.d("YYY", "checkDB: 5"+placesExists);
                    }
                }
            }
        }catch(SQLiteException e){
            Log.d("YYY", "checkDB: EXCEPTION");
            android.util.Log.w("PlacesDb->checkDB",e.getMessage());
        }
        if(checkDB != null){
            checkDB.close();
        }
        return placesExists;
    }


    public void copyDB() throws IOException{
        try {
            if(!checkDB()){
                InputStream ip =  context.getResources().openRawResource(R.raw.placesdb);
                File aFile = new File(dbPath);
                if(!aFile.exists()){
                    aFile.mkdirs();
                }
                String op=  dbPath  +  dbName +".db";
                OutputStream output = new FileOutputStream(op);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = ip.read(buffer))>0){
                    output.write(buffer, 0, length);
                }
                output.flush();
                output.close();
                ip.close();
            }
        } catch (IOException e) {
            android.util.Log.w("CourseDB --> copyDB", "IOException: "+e.getMessage());
        }
    }

    public SQLiteDatabase openDB() throws SQLException {

        Log.d("YYY", "openDB: inside");
        String myPath = dbPath + dbName + ".db";
        if(checkDB()) {
            Log.d("YYY", "openDB: inside checkdb");
            placesDb = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        }else{
            Log.d("YYY", "openDB: inside else");
            try {
                this.copyDB();
                placesDb = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
            }catch(Exception ex) {
                Log.d("YYY", "openDB: inside EXCEPTION");
                android.util.Log.w(this.getClass().getSimpleName(),"unable to copy and open db: "+ex.getMessage());
            }
        }
        return placesDb;
    }


    @Override
    public synchronized void close() {
        if(placesDb != null)
            placesDb.close();
        super.close();
    }


}

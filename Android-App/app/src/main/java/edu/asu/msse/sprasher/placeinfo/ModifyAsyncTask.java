package edu.asu.msse.sprasher.placeinfo;

/*
Copyright 2020 Sarvansh Prasher

Rights to use this code given to Arizona State University
& Professor Timothy Lindquist (Tim.Lindquist@asu.edu) for course SER 423

@author Sarvansh Prasher mailto:sprasher@asu.edu

@version 19th February,2020

*/
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.MalformedURLException;
import java.net.URL;

public class ModifyAsyncTask  extends AsyncTask<MethodInformation, Integer, MethodInformation> {

        private static final String TAG = "DeletePlaceAsyncTask";

        @Override
        protected void onPreExecute(){
            android.util.Log.d(this.getClass().getSimpleName(),"in onPreExecute on "+
                    (Looper.myLooper() == Looper.getMainLooper()?"Main thread":"Async Thread"));
        }
        @Override
        protected MethodInformation doInBackground(MethodInformation... methodInformations) {

            JSONArray ja = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                try {
                    ja = new JSONArray(methodInformations[0].params);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            String requestData = "{ \"jsonrpc\":\"2.0\", \"method\":\""+methodInformations[0].method+"\", \"params\":"+ja.toString()+
                    ",\"id\":3}";


            JsonRPCRequestViaHttp conn = null;
            try {
                conn = new JsonRPCRequestViaHttp((new URL(methodInformations[0].urlString)));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                methodInformations[0].resultAsJson = conn.call(requestData);
                Log.d(TAG, "doInBackground: conn"+methodInformations[0].resultAsJson);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(MethodInformation methodInformation) {

        }

}

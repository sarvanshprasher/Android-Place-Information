package edu.asu.msse.sprasher.placeinfo;

/*
Copyright 2020 Sarvansh Prasher

Rights to use this code given to Arizona State University
& Professor Timothy Lindquist (Tim.Lindquist@asu.edu) for course SER 423

@author Sarvansh Prasher mailto:sprasher@asu.edu

@version 23 February,2020

*/


import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AsyncCollection extends AsyncTask<MethodInformation, Integer, MethodInformation> {


    public static int list_size =0;


    @Override
    protected void onPreExecute(){
        android.util.Log.d(this.getClass().getSimpleName(),"in onPreExecute on "+
                (Looper.myLooper() == Looper.getMainLooper()?"Main thread":"Async Thread"));
    }

    @Override
    protected MethodInformation doInBackground(MethodInformation... aRequest){
        android.util.Log.d(this.getClass().getSimpleName(),"in doInBackground on "+
                (Looper.myLooper() == Looper.getMainLooper()?"Main thread":"Async Thread"));
        try {
            JSONArray ja = new JSONArray(aRequest[0].params);
            android.util.Log.d(this.getClass().getSimpleName(),"params: "+ja.toString());
            String requestData = "{ \"jsonrpc\":\"2.0\", \"method\":\""+aRequest[0].method+"\", \"params\":"+ja.toString()+
                    ",\"id\":3}";
            android.util.Log.d(this.getClass().getSimpleName(),"requestData: "+requestData+" url: "+aRequest[0].urlString);
            JsonRPCRequestViaHttp conn = new JsonRPCRequestViaHttp((new URL(aRequest[0].urlString)));
            aRequest[0].resultAsJson = conn.call(requestData);
        }catch (Exception ex){
            android.util.Log.d(this.getClass().getSimpleName(),"exception in remote call "+
                    ex.getMessage());
        }
        return aRequest[0];
    }

    @Override
    protected void onPostExecute(MethodInformation res){
        android.util.Log.d(this.getClass().getSimpleName(), "in onPostExecute on " +
                (Looper.myLooper() == Looper.getMainLooper() ? "Main thread" : "Async Thread"));
        android.util.Log.d(this.getClass().getSimpleName(), " resulting is: " + res.resultAsJson);
        try {
            if (res.method.equals("getNames")) {

                Utility.setMemoryList(new ArrayList<PlaceDescription>());

                JSONObject jo = new JSONObject(res.resultAsJson);
                JSONArray ja = jo.getJSONArray("result");
                ArrayList<String> al = new ArrayList<String>();
                for (int i = 0; i < ja.length(); i++) {
                    al.add(ja.getString(i));
                }
                String[] names = al.toArray(new String[0]);
                list_size = names.length;
                List<PlaceDescription> list = new ArrayList<PlaceDescription>();

                for (int i = 0; i < names.length; i++) {
                    Log.d("REMOTE", names[i]);
                    MethodInformation mi = new MethodInformation(res.networkCallback, res.urlString, "get", new String[]{names[i]});
                    AsyncCollection ac = (AsyncCollection) new AsyncCollection().execute(mi);
                }

            } else if (res.method.equals("get")) {


                JSONObject jo = new JSONObject(res.resultAsJson);

                PlaceDescription place = PlaceLibrary.getPlaceHolderFromJsonObject(jo.getJSONObject("result"));




                Utility.returnList().add(place);

                Log.d("RESJSON", Utility.returnList().size()+" : "+ list_size);
                if(Utility.returnList().size()==list_size){
                    res.networkCallback.getResultFromNetwork(Utility.returnList());
                }

                res.networkCallback.getResultFromNetwork(jo);
            } else if (res.method.equals("add")){
                try{
                } catch (Exception ex){
                    android.util.Log.w(this.getClass().getSimpleName(),"Exception processing getNames: "+
                            ex.getMessage());
                }
            }
            else if(res.method.equals("remove")){
                try{
                } catch (Exception ex){
                    android.util.Log.w(this.getClass().getSimpleName(),"Exception processing getNames: "+
                            ex.getMessage());
                }
            }
        }catch (Exception ex){
            android.util.Log.d(this.getClass().getSimpleName(),"Exception: "+ex.getMessage());
        }
    }

}

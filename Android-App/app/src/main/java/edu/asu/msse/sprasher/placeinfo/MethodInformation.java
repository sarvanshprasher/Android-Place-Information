package edu.asu.msse.sprasher.placeinfo;

/*
Copyright 2020 Sarvansh Prasher

Rights to use this code given to Arizona State University
& Professor Timothy Lindquist (Tim.Lindquist@asu.edu) for course SER 423

@author Sarvansh Prasher mailto:sprasher@asu.edu

@version 19th February,2020

*/

public class MethodInformation {
    public String method;
    public Object[] params;
    public NetworkCallback networkCallback;
    public String urlString;
    public String resultAsJson;

    MethodInformation(NetworkCallback networkCallback, String urlString, String method, Object[] params){
        this.method = method;
        this.networkCallback = networkCallback;
        this.urlString = urlString;
        this.params = params;
        this.resultAsJson = "{}";
    }
}
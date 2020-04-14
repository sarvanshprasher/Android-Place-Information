package edu.asu.msse.sprasher.placeinfo;

/*
Copyright 2020 Sarvansh Prasher

Rights to use this code given to Arizona State University
& Professor Timothy Lindquist (Tim.Lindquist@asu.edu) for course SER 423

@author Sarvansh Prasher mailto:sprasher@asu.edu

@version 20 January,2020

*/

import android.content.Context;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class PlaceDescription implements Serializable {


    private String name;
    private String description;
    private String category;
    private String addressTitle;
    private String addressStreet;
    private Double elevation;
    private Double latitude;
    private Double longitude;

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("category")
    public String getCategory() {
        return category;
    }

    @JsonProperty("category")
    public void setCategory(String category) {
        this.category = category;
    }

    @JsonProperty("address-title")
    public String getAddressTitle() {
        return addressTitle;
    }

    @JsonProperty("address-title")
    public void setAddressTitle(String addressTitle) {
        this.addressTitle = addressTitle;
    }

    @JsonProperty("address-street")
    public String getAddressStreet() {
        return addressStreet;
    }

    @JsonProperty("address-street")
    public void setAddressStreet(String addressStreet) {
        this.addressStreet = addressStreet;
    }

    @JsonProperty("elevation")
    public Double getElevation() {
        return elevation;
    }

    @JsonProperty("elevation")
    public void setElevation(Double elevation) {
        this.elevation = elevation;
    }

    @JsonProperty("latitude")
    public Double getLatitude() {
        return latitude;
    }

    @JsonProperty("latitude")
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @JsonProperty("longitude")
    public Double getLongitude() {
        return longitude;
    }

    @JsonProperty("longitude")
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public static PlaceDescription jsonToObject(Context context, String fileName) {
        final ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(Utility.readJSONFromAsset(context, fileName), PlaceDescription.class);                               // read from json string
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        return name + " " + description;
    }



}

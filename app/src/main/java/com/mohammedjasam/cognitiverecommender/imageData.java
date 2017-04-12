package com.mohammedjasam.cognitiverecommender;


public class imageData {
    private String imageLink, label,location;

    public imageData(){

    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public imageData(String imageLink, String label, String location) {

        this.imageLink = imageLink;
        this.label = label;
        this.location = location;
    }
}
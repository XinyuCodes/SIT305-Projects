package com.example.lostandfound;

import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Advert {
    private int id;
    private String postType;
    private String name;
    private String phone;
    private String description;
    private String date;
    private String location;
    private String category;
    private String imagePath;
    private String timestamp;
    private Double Latitude;
    private Double longitude;

    public Advert() {
    }

    public static Advert createNew(String postType, String name, String phone, String description, 
                                 String date, String location, String category, String imagePath, Double Latitude, Double longitude) {
        Advert a = new Advert();
        a.setPostType(postType);
        a.setName(name);
        a.setPhone(phone);
        a.setDescription(description);
        a.setDate(date);
        a.setLocation(location);
        a.setCategory(category);
        a.setImagePath(imagePath);
        a.setTimestamp(new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date()));
        a.setLatitude(Latitude);
        a.setLongitude(longitude);
        return a;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getPostType() { return postType; }
    public void setPostType(String postType) { this.postType = postType; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public Double getLatitude() {return Latitude;}
    public void setLatitude(Double Latitude) {this.Latitude = Latitude;}
    public Double getLongitude() {return longitude;}
    public void setLongitude(Double longitude) {this.longitude = longitude;}

}
package com.example.task51c;

public class NewsItem {
    private int id;
    private String title;
    private String description;
    private int imageResId;
    private String category;
    private Boolean isBookmarked;
    private Boolean isFeatureStory;

    //get methods
    public int getId(){return id;}
    public String getTitle(){return title;}
    public String getDescription(){return description;}
    public int getImageResId(){return imageResId;}
    public String getCategory(){return category;}
    public Boolean getIsBookmarked(){return isBookmarked;}
    public Boolean getIsFeatureStory(){return isFeatureStory;}

    //constructor
    public NewsItem(int id, String title, String description, int imageResId, String category, Boolean isBookmarked, Boolean isFeatureStory){
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageResId = imageResId;
        this.category = category;
        this.isBookmarked = isBookmarked;
        this.isFeatureStory = isFeatureStory;
    }

}

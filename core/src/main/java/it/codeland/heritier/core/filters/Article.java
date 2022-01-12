package it.codeland.heritier.core.filters;

public class Article {
    private String title;
    private String url;
    private String  tag;
    private String date;
    private String  image;
    private String  description;



    public Article(String title2) {
        this.title = title2;
    }



    public void setValues(String title, String url, String tag, String date, String image, String description) {
        // this.title = title;
        // this.url = url;
        // this.tag = tag;
        // this.date = date;
        // this.image = image;
        // this.description = description;
    }

    
    // public void Article(String title, String url, String tag, String date, String image, String description) {
    //     this.title = title;
    //     this.url = url;
    //     this.tag = tag;
    //     this.date = date;
    //     this.image = image;
    //     this.description = description;
    // }


    // Getters and setters
    public String getTitle() {
        return title;
    }
    // get url
    public String getUrl() {
        return url;
    }
    // get tag
    public String getTag() {
        return tag;
    }
    // get date
    public String getDate() {
        return date;
    }
    // get image
    public String getImage() {
        return image;
    }
    // get description
    public String getDescription() {
        return description;
    }




}

package it.codeland.heritier.core.dto;

public class Article {
    public String title;
    public String link;
    public String[] tags;
    public  String image;   
    public  String description;

    public Article(String tittle,String link,String[] tags, String image,String description){
        this.title =tittle;
        this.link = link;
        this.tags = tags;
        this.image = image;
        this.description = description;
    }

}

package com.example.sadokey.tourism_guide.Classes;

/**
 * Created by Ahmed AboMazin on 9/9/2016.
 */
public class Suggestion
{
    private String subject;
    private String name;
    private String image;
    private String suggestionStatus;
    private String place;
    private String price;

    public Suggestion() {
    }

    public Suggestion(String subject, String name, String image, String suggestionStatus, String place, String price) {
        this.subject = subject;
        this.name = name;
        this.image = image;
        this.suggestionStatus = suggestionStatus;
        this.place = place;
        this.price = price;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSuggestionStatus() {
        return suggestionStatus;
    }

    public void setSuggestionStatus(String suggestionStatus) {
        this.suggestionStatus = suggestionStatus;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}

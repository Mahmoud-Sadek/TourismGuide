package com.example.sadokey.tourism_guide.Classes;

/**
 * Created by Ahmed AboMazin on 9/10/2016.
 */
public class offers {
    private String title;
    private String desc;
    private String time;
    private String image;
    private String placeID;
    private String tourismType;
    private String price;
    private String company_id;



    public offers(String title, String desc, String time, String image, String placeID, String tourismType, String price,String company_id) {
        this.title = title;
        this.desc = desc;
        this.time = time;
        this.image = image;
        this.placeID = placeID;
        this.tourismType = tourismType;
        this.price = price;
        this.company_id=company_id;
    }

    public offers(String title, String desc, String time, String image,String price) {
        this.title = title;
        this.desc = desc;
        this.time = time;
        this.image = image;
        this.price = price;
    }

    public offers()
    {

    }


    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTourismType() {
        return tourismType;
    }

    public void setTourismType(String tourismType) {
        this.tourismType = tourismType;
    }

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
package com.example.sadokey.tourism_guide.Classes;

/**
 * Created by Ahmed AboMazin on 9/7/2016.
 */
public class offers_item
{
    private String header;
    private String details;
    private String Time;
    private int img;

    public offers_item(String header, String details, String Time, int img)
    {
        this.header=header;
        this.details=details;
        this.Time=Time;
        this.img=img;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}

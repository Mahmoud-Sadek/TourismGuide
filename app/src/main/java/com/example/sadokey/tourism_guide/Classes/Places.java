package com.example.sadokey.tourism_guide.Classes;

/**
 * Created by Ahmed AboMazin on 9/26/2016.
 */
public class Places
{
    String name;
    String desc;
    String image;

    public Places()
    {

    }

    public Places(String name, String desc, String image) {
        this.name = name;
        this.desc = desc;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

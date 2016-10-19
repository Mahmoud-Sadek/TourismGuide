package com.example.sadokey.tourism_guide.Classes;

/**
 * Created by Ahmed AboMazin on 10/18/2016.
 */
public class company
{
    private String name;
    private String desc;
    private String image;

    public company(String name, String desc, String image) {
        this.name = name;
        this.desc = desc;
        this.image = image;
    }

    public company()
    {
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

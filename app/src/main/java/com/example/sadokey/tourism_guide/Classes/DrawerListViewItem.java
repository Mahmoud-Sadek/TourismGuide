package com.example.sadokey.tourism_guide.Classes;

/**
 * Created by Ahmed AboMazin on 9/3/2016.
 */
public class DrawerListViewItem
{
    private String headerText;
    private int imgResource;



    public DrawerListViewItem(String headerText,int imgResource)
    {
        this.headerText=headerText;
        this.imgResource=imgResource;
    }

    public String getHeaderText() {
        return headerText;
    }

    public void setHeaderText(String headerText) {
        this.headerText = headerText;
    }

    public int getImgResource() {
        return imgResource;
    }

    public void setImgResource(int imgResource) {
        this.imgResource = imgResource;
    }
}

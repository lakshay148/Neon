package com.truedev.neon.Model;

import com.truedev.R;
import com.truedev.neon.Utils.ApplicationController;

import java.io.Serializable;

/**
 * Created by lakshaygirdhar on 24/12/15.
 */
public class ImageModel implements Serializable {

    private int id;
    private String imageName;
    private String tagName;
    private String imagePath;
    private ImageTagsModel tagsModel;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ImageModel() {
        imageName = "IMG";
    }

    public ImageTagsModel getTagsModel() {
        return tagsModel;
    }

    public void setTagsModel(ImageTagsModel tagsModel) {
        this.tagsModel = tagsModel;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}

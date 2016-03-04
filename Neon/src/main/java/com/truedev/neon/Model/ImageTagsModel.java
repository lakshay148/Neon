package com.truedev.neon.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by lakshaygirdhar on 7/10/15.
 */
public class ImageTagsModel implements Serializable{

    @SerializedName("tagName")
    private String tag_name;

    @SerializedName("parent")
    private String parent_name;

    @SerializedName("tagType")
    private String image_type;

    @SerializedName("tagOrder")
    private String order;

    private String tagID;

    @SerializedName("isRequired")
    private String mandatory;

    private String updated_time;

    public String getMandatory() {
        return mandatory;
    }

    public void setMandatory(String mandatory) {
        this.mandatory = mandatory;
    }

    public String getTagId() {
        return tagID;
    }

    public void setTagId(String tagId) {
        this.tagID = tagId;
    }

    public String getImage_type() {
        return image_type;
    }

    public void setImage_type(String image_type) {
        this.image_type = image_type;
    }

    public String getTag_name() {
        return tag_name;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getUpdated_time() {
        return updated_time;
    }

    public void setUpdated_time(String updated_time) {
        this.updated_time = updated_time;
    }

    public void setTag_name(String tag_name) {

        this.tag_name = tag_name;
    }

    public String getParent_name() {
        return parent_name;
    }

    public void setParent_name(String parent_name) {
        this.parent_name = parent_name;
    }


}

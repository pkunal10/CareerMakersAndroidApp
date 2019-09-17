package com.jk.admin.careermakers.Modal;

/**
 * Created by ADMIN on 09-04-2018.
 */

public class AdmissionCategory {
    String Id,CategoryName,Image,Status;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    @Override
    public String toString() {
        return CategoryName;
    }
}

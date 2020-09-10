package com.mintu.payoassignment.Models;

import java.util.Comparator;

/**
 * Created By Mintu Giri on 9/10/2020.
 */
public class ItemModel {

    private String fname;
    private String lname;
    private String email;
    private String imageUrl;

    public ItemModel(String fname, String lname, String email, String imageUrl) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.imageUrl = imageUrl;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public String getEmail() {
        return email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public static Comparator<ItemModel> COMPARE_BY_NAME = new Comparator<ItemModel>() {
        @Override
        public int compare(ItemModel one, ItemModel other) {
            return one.fname.compareTo(other.fname);
        }
    };
}

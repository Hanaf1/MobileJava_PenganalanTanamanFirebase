package com.kelompok4.pengenalantanaman.model;

import com.google.gson.annotations.SerializedName;

public class tanamanClass {
    @SerializedName("nama")
    private String nama;

    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("deskripsi")
    private String deskripsi;

    public tanamanClass(String nama, String imageUrl, String deskripsi) {
        this.nama = nama;
        this.imageUrl = imageUrl;
        this.deskripsi = deskripsi;
    }

    public String getNama() {
        return nama;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDeskripsi() {
        return deskripsi;
    }
}

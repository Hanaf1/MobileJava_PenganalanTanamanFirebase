package com.kelompok4.pengenalantanaman.model;

public class Kategori {
    private String kategori; // Only the category name is needed

    // Default constructor (required for Firebase)
    public Kategori() {}

    public Kategori(String kategori) {
        this.kategori = kategori;
    }

    // Getter and setter
    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }
}

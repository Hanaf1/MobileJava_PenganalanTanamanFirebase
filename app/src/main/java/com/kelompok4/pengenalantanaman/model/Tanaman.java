package com.kelompok4.pengenalantanaman.model;

import java.io.Serializable;

public class Tanaman implements Serializable {
    private String key; // Attribut key untuk menandai kunci unik dari setiap entitas tanaman
    private String nama;
    private String bagianguna;
    private String kegunaan;
    private String gambar;
    private String kategori;

    // Default constructor

    // Default constructor required for Firebase deserialization
    public Tanaman() {
    }

    // Konstruktor
    public Tanaman(String nama, String bagianGuna, String kegunaan, String gambarUrl, String key) {
        this.nama = nama;
        this.bagianguna = bagianGuna;
        this.kegunaan = kegunaan;
        this.gambar = gambarUrl;
        this.key = key;
    }

    // Getter dan setter untuk semua atribut

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getBagianGuna() {
        return bagianguna;
    }

    public void setBagianGuna(String bagianGuna) {
        this.bagianguna = bagianGuna;
    }

    public String getKegunaan() {
        return kegunaan;
    }

    public void setKegunaan(String kegunaan) {
        this.kegunaan = kegunaan;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }
}

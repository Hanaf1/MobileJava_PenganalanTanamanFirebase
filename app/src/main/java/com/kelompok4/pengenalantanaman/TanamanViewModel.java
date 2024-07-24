package com.kelompok4.pengenalantanaman;

import androidx.lifecycle.ViewModel;

import com.kelompok4.pengenalantanaman.model.tanamanClass;

import java.util.List;

public class TanamanViewModel extends ViewModel {
    private List<tanamanClass> tanamanList;

    public List<tanamanClass> getTanamanList() {
        return tanamanList;
    }

    public void setTanamanList(List<tanamanClass> tanamanList) {
        this.tanamanList = tanamanList;
    }
}

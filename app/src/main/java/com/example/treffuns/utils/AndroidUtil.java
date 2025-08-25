package com.example.treffuns.utils;

import android.content.Intent;

import com.example.treffuns.model.TreffenModel;


public class AndroidUtil {
    public static void  passTreffenModelAsIntent(Intent intent, TreffenModel model){
        intent.putExtra("treffenName", model.getTreffenName());
        intent.putExtra("userId", model.getUserId());
        intent.putExtra("type", model.getTreffenType());
        intent.putExtra("location", model.getLocation());
        intent.putExtra("kategori", model.getKategorie());
    }

    public static TreffenModel getMeetingModelFromIntent(Intent intent){
        TreffenModel treffenModel = new TreffenModel();
        treffenModel.setTreffenName(intent.getStringExtra("treffenName"));
        treffenModel.setUserId(intent.getStringExtra("userId"));
        treffenModel.setTreffenType(intent.getStringExtra("type"));
        treffenModel.setLocation(intent.getStringExtra("location"));
        treffenModel.setKategorie(intent.getStringExtra("kategori"));
        return treffenModel;
    }
}

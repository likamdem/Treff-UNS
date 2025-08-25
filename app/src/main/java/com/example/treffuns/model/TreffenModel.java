package com.example.treffuns.model;

public class TreffenModel {
    private String treffenName;
    private String kategorie;
    private String location;
    private String treffenType;
    private String userId;

    public TreffenModel() {
    }

    public String getTreffenName() {
        return treffenName;
    }

    public void setTreffenName(String treffenName) {
        this.treffenName = treffenName;
    }

    public String getKategorie() {
        return kategorie;
    }

    public void setKategorie(String kategorie) {
        this.kategorie = kategorie;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTreffenType() {
        return treffenType;
    }

    public void setTreffenType(String treffenType) {
        this.treffenType = treffenType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public TreffenModel(String treffenName, String kategorie, String location, String treffenType, String userId) {
        this.treffenName = treffenName;
        this.kategorie = kategorie;
        this.location = location;
        this.treffenType = treffenType;
        this.userId = userId;
    }
}

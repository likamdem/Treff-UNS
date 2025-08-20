package com.example.treffuns;

public class Usermodel {
    private String vorname;
    private String nachname;

    private String studiengang;

    private int semester;

    private String Campus;

    private String pseudo;

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getStudiengang() {
        return studiengang;
    }

    public void setStudiengang(String studiengang) {
        this.studiengang = studiengang;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public String getCampus() {
        return Campus;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public void setCampus(String campus) {
        Campus = campus;
    }

    public Usermodel() {
    }

    public Usermodel(String pseudo, String vorname, String nachname, String studiengang, int semester, String campus) {
        this.pseudo = pseudo;
        this.vorname = vorname;
        this.nachname = nachname;
        this.studiengang = studiengang;
        this.semester = semester;
        Campus = campus;
    }
}

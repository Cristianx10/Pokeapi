package com.example.pokeapi.model;

import com.google.firebase.Timestamp;

import java.io.Serializable;


public class ItemPokemon implements Serializable {

    private String uid;
    private String name;
    private String image;
    private String date;

    public ItemPokemon(){ }

    public ItemPokemon(String uid, String name, String image, String date) {
        this.uid = uid;
        this.name = name;
        this.image = image;
        this.date = date;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

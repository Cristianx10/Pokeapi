package com.example.pokeapi.model;

import java.io.Serializable;

public class Pokemon implements Serializable {

    private String uid;
    private String name;
    private String tipo;
    private int defensa;
    private int ataque;
    private int velocidad;
    private int vida;
    private String image;

    public Pokemon (){ }

    public Pokemon(String uid, String name, String tipo, int defensa, int ataque, int velocidad, int vida, String image) {
        this.uid = uid;
        this.name = name;
        this.tipo = tipo;
        this.defensa = defensa;
        this.ataque = ataque;
        this.velocidad = velocidad;
        this.vida = vida;
        this.image = image;
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



    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getDefensa() {
        return defensa;
    }

    public void setDefensa(int defensa) {
        this.defensa = defensa;
    }

    public int getAtaque() {
        return ataque;
    }

    public void setAtaque(int ataque) {
        this.ataque = ataque;
    }

    public int getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(int velocidad) {
        this.velocidad = velocidad;
    }

    public int getVida() {
        return vida;
    }

    public void setVida(int vida) {
        this.vida = vida;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

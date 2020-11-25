package com.example.pokeapi.model;

import java.io.Serializable;
import java.util.List;

public class Entrenador implements Serializable {

    private String uid;
    private String name;
    private List<ItemPokemon> pokemones;

    public Entrenador() {}

    public Entrenador(String uid, String name, List<ItemPokemon> pokemones) {
        this.uid = uid;
        this.name = name;
        this.pokemones = pokemones;
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

    public List<ItemPokemon> getPokemones() {
        return pokemones;
    }

    public void setPokemones(List<ItemPokemon> pokemones) {
        this.pokemones = pokemones;
    }
}

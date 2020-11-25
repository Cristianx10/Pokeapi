package com.example.pokeapi.objects;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ItemPokeApiPokemon {

    @SerializedName("sprites")
    private ItemPokeApiPokemonImage sprites;
    @SerializedName("stats")
    private List<ItemPokeApiPokemonHabilidad> habilidades;
    @SerializedName("types")
    private List<ItemPokeApiPokemonType> types;

    public ItemPokeApiPokemon() { }


    public ItemPokeApiPokemon(ItemPokeApiPokemonImage sprites, List<ItemPokeApiPokemonHabilidad> habilidades, List<ItemPokeApiPokemonType> types) {
        this.sprites = sprites;
        this.habilidades = habilidades;
        this.types = types;
    }

    public String getAllTypes(){
        String types = "";
        for (int i = 0; i < this.types.size(); i++){
            ItemPokeApiPokemonType type = this.types.get(i);
            if(i ==0){
                types = type.getType().getName();
            }else{
                types = types + ", " + type.getType().getName();
            }
        }

        return types;
    }

    public int getHabilidadesName(String name){
        int value =0;
        for (int i = 0; i < this.habilidades.size(); i++){
            ItemPokeApiPokemonHabilidad habilidad = this.habilidades.get(i);
            if(habilidad.getStat().getName().equals(name)){
                value = habilidad.getValue();
                i = this.habilidades.size();
            }
        }
        return value;
    }

    public ItemPokeApiPokemonImage getSprites() {
        return sprites;
    }

    public void setSprites(ItemPokeApiPokemonImage sprites) {
        this.sprites = sprites;
    }

    public List<ItemPokeApiPokemonHabilidad> getHabilidades() {
        return habilidades;
    }

    public void setHabilidades(List<ItemPokeApiPokemonHabilidad> habilidades) {
        this.habilidades = habilidades;
    }

    public List<ItemPokeApiPokemonType> getTypes() {
        return types;
    }

    public void setTypes(List<ItemPokeApiPokemonType> types) {
        this.types = types;
    }

    public class ItemPokeApiPokemonType{

        @SerializedName("type")
        private ItemPokeApiPokemonTypeTipo type;

        public ItemPokeApiPokemonTypeTipo getType() {
            return type;
        }

        public class ItemPokeApiPokemonTypeTipo{
            @SerializedName("name")
            private String name;

            public String getName() {
                return name;
            }
        }
    }

    public class ItemPokeApiPokemonHabilidad{
        @SerializedName("base_stat")
        private int value;

        @SerializedName("stat")
        private ItemPokeApiPokemonHabilidadStat stat;

        public int getValue() {
            return value;
        }

        public ItemPokeApiPokemonHabilidadStat getStat() {
            return stat;
        }

        public class ItemPokeApiPokemonHabilidadStat{
            @SerializedName("name")
            private String name;

            public String getName() {
                return name;
            }
        }
    }

    public class ItemPokeApiPokemonImage{
        @SerializedName("front_default")
        private String image;

        public String getImage() {
            return image;
        }
    }


}







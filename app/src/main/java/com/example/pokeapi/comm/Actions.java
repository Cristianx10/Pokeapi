package com.example.pokeapi.comm;

import android.annotation.SuppressLint;
import android.location.Location;
import android.util.Log;


import com.example.pokeapi.model.ItemPokemon;
import com.example.pokeapi.model.Pokemon;
import com.example.pokeapi.objects.ItemPokeApiPokemon;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;


public class Actions {

    private HTTPSWebUtilDomi https;
    private Gson gson;

    public static String URL_PROYECT = "https://pokeapi.co/api/v2/pokemon/";

    //METODO DE SUSCRIPCION AL EVENTO


    public Actions() {
        https = new HTTPSWebUtilDomi();
       // gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        gson = new Gson();
    }



    public void findPokemon(String namePokemon, OnLoadFindPokemon load){
        findPokemonInFirestore(namePokemon, pokemonFirestore -> {
            if(pokemonFirestore != null){
                load.onReadPokemon(pokemonFirestore);
            }else{
                findPokemonInPokeApi(namePokemon, pokemonApi -> {
                    if(pokemonApi != null){
                        load.onReadPokemon(pokemonApi);
                    }else{
                        load.onReadPokemon(null);
                    }
                });
            }
        });
    }

    private void findPokemonInFirestore(String namePokemon, OnLoadFindPokemon load){
        FirebaseFirestore firebaseFirestore =FirebaseFirestore.getInstance();
        firebaseFirestore.collection("pokemones").document(namePokemon).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DocumentSnapshot documentSnapshot = task.getResult();
                if(documentSnapshot.exists()){
                    Pokemon pokemon = documentSnapshot.toObject(Pokemon.class);
                    load.onReadPokemon(pokemon);
                }else{
                    load.onReadPokemon(null);
                }
            }
        });
    }

    private void createPokemonInFirestore(Pokemon pokemon){
        FirebaseFirestore firebaseFirestore =FirebaseFirestore.getInstance();
        firebaseFirestore.collection("pokemones").document(pokemon.getName()).set(pokemon);
    }

    private void findPokemonInPokeApi(String namePokemon, OnLoadFindPokemon load){
        new Thread(()->{

            String response = this.https.GETrequest(URL_PROYECT + namePokemon);

            if(response.equals("null") || response.equals("") ){
                load.onReadPokemon(null);
            }else{

                ItemPokeApiPokemon itemPokeApiPokemon = this.gson.fromJson(response, ItemPokeApiPokemon.class);

                //Log.e(">>>>>>>>>>>>>>>", this.gson.toJson(itemPokeApiPokemon));
                //Log.e(">>>>>>>>>>>>>>>", itemPokeApiPokemon.getSprites().getImage());

                String uid = UUID.randomUUID().toString();
                String name = namePokemon;
                String tipo = itemPokeApiPokemon.getAllTypes();
                int defensa =itemPokeApiPokemon.getHabilidadesName("defense");
                int ataque = itemPokeApiPokemon.getHabilidadesName("attack");
                int velocidad = itemPokeApiPokemon.getHabilidadesName("speed");
                int vida = itemPokeApiPokemon.getHabilidadesName("hp");
                String image = itemPokeApiPokemon.getSprites().getImage();

                Pokemon pokemon = new Pokemon(uid, name, tipo, defensa, ataque,velocidad, vida, image);
                createPokemonInFirestore(pokemon);
                load.onReadPokemon(pokemon);
            }

        }).start();
    }

    public interface OnLoadFindPokemon{
        void onReadPokemon(Pokemon pokemon);
    }


}

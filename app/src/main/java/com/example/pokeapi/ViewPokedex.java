package com.example.pokeapi;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pokeapi.adapter.AdapterList;
import com.example.pokeapi.adapter.AdapterManagerList;
import com.example.pokeapi.comm.Actions;
import com.example.pokeapi.model.Entrenador;
import com.example.pokeapi.model.ItemPokemon;
import com.example.pokeapi.model.Pokemon;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewPokedex extends AppCompatActivity {

    private EditText et_name_pokemon;
    private EditText et_buscar_pokemon;
    private Button btn_atrapar;
    private Button btn_buscar_pokemon;
    private RecyclerView rv_lista_pokemones;

    private AdapterList<ItemPokemon> adapterManager;
    private Entrenador entrenador;


    private Actions actions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokedex);

        this.actions = new Actions();

        if (this.getIntent().getExtras() != null) {
            Entrenador entrenador = (Entrenador) this.getIntent().getSerializableExtra("entrenador");
            this.entrenador = entrenador;
            Toast.makeText(this, "Bienvenido " + entrenador.getName(), Toast.LENGTH_SHORT).show();
        }

        this.et_name_pokemon = findViewById(R.id.et_name_pokemon);
        this.et_buscar_pokemon = findViewById(R.id.et_buscar_pokemon);

        this.btn_atrapar = findViewById(R.id.btn_atrapar);
        this.btn_buscar_pokemon = findViewById(R.id.btn_buscar_pokemon);

        this.rv_lista_pokemones = findViewById(R.id.rv_lista_pokemones);
        this.rv_lista_pokemones.setLayoutManager(new LinearLayoutManager(this));

        this.btn_atrapar.setOnClickListener(this::onCatchPokemon);
        this.btn_buscar_pokemon.setOnClickListener(this::onFindPokemon);

        ViewPokedex viewPokedex = this;

        this.adapterManager = new AdapterList(this.rv_lista_pokemones, this.entrenador.getPokemones(), R.layout.item_pokemon,
                new AdapterManagerList<ItemPokemon>() {

                    private ImageView iv_item_pokemon_view;
                    private TextView tv_item_pokemon_name;
                    private View v;

                    @Override
                    public void onCreateView(View v) {
                        this.v = v;
                        this.tv_item_pokemon_name = v.findViewById(R.id.tv_item_pokemon_name);
                        this.iv_item_pokemon_view = v.findViewById(R.id.iv_item_pokemon_view);

                    }

                    @Override
                    public void onChangeView(ItemPokemon pokemon, int position) {
                        String namePokemon = pokemon.getName();
                        namePokemon = namePokemon.substring(0, 1).toUpperCase() + namePokemon.substring(1);
                        this.tv_item_pokemon_name.setText(namePokemon);
                        Glide.with(this.iv_item_pokemon_view).load(pokemon.getImage()).into(this.iv_item_pokemon_view);

                        this.v.setOnClickListener(view -> {
                            this.onGoToPokemonView(v, pokemon);
                        });
                    }

                    public void onGoToPokemonView(View v, ItemPokemon pokemon) {
                        if (pokemon != null) {
                            Intent intent = new Intent(viewPokedex, ViewPokemon.class);
                            intent.putExtra("pokemon", pokemon);
                            intent.putExtra("entrenador", entrenador);

                            startActivity(intent);
                        }
                    }
                });


    }

    private void onCatchPokemon(View v) {
        String namePokemon = this.et_name_pokemon.getText().toString();
        if(namePokemon.equals("")){

            Toast.makeText(this, "Escriba el nombre de un pokemon", Toast.LENGTH_SHORT).show();

        }else{
            this.actions.findPokemon(namePokemon, pokemon -> {
                if (pokemon != null) {
                    ItemPokemon itemPokemon = new ItemPokemon(pokemon.getUid(), pokemon.getName(), pokemon.getImage());

                    FirebaseFirestore.getInstance().collection("users").document(this.entrenador.getName())
                            .collection("pokemones").add(itemPokemon).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentReference documentReference = task.getResult();
                            String uid = documentReference.getId();

                            itemPokemon.setUid(uid);
                            this.entrenador.getPokemones().add(itemPokemon);
                            this.adapterManager.onAddItem(itemPokemon);
                        }
                    });
                } else {
                    runOnUiThread(()->{
                        Toast.makeText(this, "El pokemon no existe", Toast.LENGTH_SHORT).show();
                    });

                }
            });
            this.et_name_pokemon.setText("");
        }


    }

    private void onFindPokemon(View v) {
        String namePokemon = this.et_buscar_pokemon.getText().toString();
        //  Toast.makeText(this, "Buscando a " + namePokemon, Toast.LENGTH_SHORT).show();
        if(!namePokemon.equals("")){
            FirebaseFirestore.getInstance().collection("users").document(this.entrenador.getName())
                    .collection("pokemones").whereEqualTo("name", namePokemon).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    List<ItemPokemon> itemPokemons = new ArrayList<>();

                    for (DocumentSnapshot doc : value) {
                        ItemPokemon poke = doc.toObject(ItemPokemon.class);
                        poke.setUid(doc.getId());
                        itemPokemons.add(poke);
                    }

                    adapterManager.onUpdateData(itemPokemons);


                }
            });
        }else{
            Toast.makeText(this, "Todos los Pokemones", Toast.LENGTH_SHORT).show();
            adapterManager.onUpdateData(this.entrenador.getPokemones());
        }

    }

}
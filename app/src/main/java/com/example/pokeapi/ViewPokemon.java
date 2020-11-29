package com.example.pokeapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pokeapi.model.Entrenador;
import com.example.pokeapi.model.ItemPokemon;
import com.example.pokeapi.model.Pokemon;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

public class ViewPokemon extends AppCompatActivity {

    private TextView tv_defensa;
    private TextView tv_ataque;
    private TextView tv_velocidad;
    private TextView tv_vida;
    private TextView tv_name_pokemon;
    private TextView tv_tipo_pokemon;

    private Button btn_soltar;

    private ImageView iv_pokemon;
    private Entrenador entrenador;
    private ItemPokemon itemPokemon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon);

        if(this.getIntent().getExtras() != null){
            Entrenador entrenador = (Entrenador) this.getIntent().getSerializableExtra("entrenador");
            ItemPokemon itemPokemon = (ItemPokemon) this.getIntent().getSerializableExtra("pokemon");
            this.entrenador = entrenador;
            this.itemPokemon = itemPokemon;
        }

        this.tv_defensa = findViewById(R.id.tv_defensa);
        this.tv_ataque = findViewById(R.id.tv_ataque);
        this.tv_velocidad = findViewById(R.id.tv_velocidad);
        this.tv_vida = findViewById(R.id.tv_vida);
        this.tv_name_pokemon = findViewById(R.id.tv_name_pokemon);
        this.tv_tipo_pokemon = findViewById(R.id.tv_tipo_pokemon);

        this.iv_pokemon = findViewById(R.id.iv_pokemon);
        this.btn_soltar = findViewById(R.id.btn_soltar);

        this.btn_soltar.setOnClickListener(this::onFreePokemon);

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("pokemones").document(this.itemPokemon.getName()).get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        DocumentSnapshot documentSnapshot = task.getResult();
                       if( documentSnapshot.exists()){
                           Pokemon pokemon = documentSnapshot.toObject(Pokemon.class);
                           String namePokemon = pokemon.getName();
                           namePokemon = namePokemon.substring(0, 1).toUpperCase() + namePokemon.substring(1);
                           this.tv_name_pokemon.setText(namePokemon);
                           this.tv_tipo_pokemon.setText(pokemon.getTipo());

                           this.tv_defensa.setText(pokemon.getDefensa() +"");
                           this.tv_ataque.setText(pokemon.getAtaque() +"");
                           this.tv_velocidad.setText(pokemon.getVelocidad() +"");
                           this.tv_vida.setText(pokemon.getVida() +"");
                           Glide.with(this.iv_pokemon).load(pokemon.getImage()).into(this.iv_pokemon);
                       }
                    }
                });



    }

    private void onFreePokemon(View v){
        for(int i =0 ; i < this.entrenador.getPokemones().size(); i++){
            ItemPokemon itemPokemon = this.entrenador.getPokemones().get(i);
            if(itemPokemon.getUid().equals(this.itemPokemon.getUid())){
                this.entrenador.getPokemones().remove(i);
                i = this.entrenador.getPokemones().size();
            }
        }

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("users").document(this.entrenador.getName())
                .collection("pokemones")
                .document(this.itemPokemon.getUid()).delete()
        .addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Intent intent = new Intent(this, ViewPokedex.class);
                intent.putExtra("entrenador", this.entrenador);
                startActivity(intent);
            }
        });
    }
}
package com.example.pokeapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pokeapi.comm.Actions;
import com.example.pokeapi.model.Entrenador;
import com.example.pokeapi.model.ItemPokemon;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private EditText et_name_entrenador;
    private Button btn_ingresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
        }, 1);

        this.et_name_entrenador = findViewById(R.id.et_name_entrenador);
        this.btn_ingresar = findViewById(R.id.btn_ingresar);

        this.btn_ingresar.setOnClickListener(this::onIngresar);

        new Actions();

    }

    private void onIngresar(View v){
        String nameEntrenador = this.et_name_entrenador.getText().toString().toLowerCase();
        this.getEntrenadorFirestore(nameEntrenador, entrenador -> {
            this.goToActivityEntrenador(entrenador);
        });
    }

    public void getEntrenadorFirestore(String nameEntrenador, OnLoadUser load){

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("users")
                .document(nameEntrenador)
                .get()
                .addOnCompleteListener((task)->{
                    if(task.isSuccessful()){
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()){
                            Entrenador entrenador = document.toObject(Entrenador.class);
                            this.getPokemonesFirestore(entrenador, load);
                        }else{
                            List<ItemPokemon> pokemones = new ArrayList<>();
                            String uid = UUID.randomUUID().toString();
                            Entrenador entrenador = new Entrenador(uid, nameEntrenador, pokemones);

                            firebaseFirestore.collection("users")
                                    .document(nameEntrenador).set(entrenador).addOnCompleteListener((proceso)->{
                                if(proceso.isSuccessful()){
                                    this.getPokemonesFirestore(entrenador, load);
                                }
                            });
                        }


                    }else{
                        Toast.makeText(this, "Se ha producido un Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void getPokemonesFirestore(Entrenador entrenador, OnLoadUser load){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("users")
                .document(entrenador.getName())
                .collection("pokemones")
                .get()
                .addOnCompleteListener((task)->{
                    if(task.isSuccessful()){
                        QuerySnapshot querySnapshot = task.getResult();
                        for (DocumentSnapshot documentSnapshot : querySnapshot){
                            if(documentSnapshot.exists()){
                                ItemPokemon pokemon = documentSnapshot.toObject(ItemPokemon.class);
                                pokemon.setUid(documentSnapshot.getId());
                                entrenador.getPokemones().add(pokemon);
                            }
                        }
                        load.getEntrenador(entrenador);
                    }else{
                        load.getEntrenador(entrenador);
                    }
                });
    }

    public void goToActivityEntrenador(Entrenador entrenador){

        Intent intent = new Intent(this, ViewPokedex.class);
        intent.putExtra("entrenador", entrenador);
        startActivity(intent);
    }

    interface OnLoadUser{
        public void getEntrenador(Entrenador entrenador);
    }
}
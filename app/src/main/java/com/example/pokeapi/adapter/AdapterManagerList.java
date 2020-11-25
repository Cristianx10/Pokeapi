package com.example.pokeapi.adapter;

import android.view.View;

import java.util.ArrayList;

public interface AdapterManagerList<T> {

    void onCreateView(View v);

    void onChangeView(T elemnto, int position);

}

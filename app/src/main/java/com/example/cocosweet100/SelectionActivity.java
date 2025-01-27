package com.example.cocosweet100;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
public class SelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        Button btnFood = findViewById(R.id.btnFood);
        Button btnSweet = findViewById(R.id.btnSweet);
        Button btnFruits = findViewById(R.id.btnFruits);
        Button btnDrinks = findViewById(R.id.btnDrinks);
        Button btnAnimals = findViewById(R.id.btnAnimals);
        Button btnPlace = findViewById(R.id.btnPlace);
        Button btnMusic = findViewById(R.id.btnMusic);
        Button btnFolk = findViewById(R.id.btnFolk);
        Button btnCeleb = findViewById(R.id.btnCeleb);

        // Set up button click listeners
        btnFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGameWithCategory("Food");
            }
        });

        btnSweet .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGameWithCategory("Sweets");
            }
        });

        btnFruits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGameWithCategory("Fruits");
            }
        });

        btnDrinks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGameWithCategory("Drinks");
            }
        });
        btnAnimals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGameWithCategory("Animals");
            }
        });

        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGameWithCategory("Place");
            }
        });

        btnMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGameWithCategory("Hit Song");
            }
        });

        btnFolk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGameWithCategory("Folk Song");
            }
        });
        btnCeleb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGameWithCategory("Celebrity");
            }
        });
    }

    private void startGameWithCategory(String category) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("category", category); // Pass the selected category to MainActivity
        startActivity(intent);
        finish(); // Close this activity
    }
}
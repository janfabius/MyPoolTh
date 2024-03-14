package com.fabius.mypoolth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);

        // Aggiungi un listener per gestire il click sul pulsante
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Crea un Intent per aprire la seconda Activity
                Intent intent = new Intent(MainActivity.this, Activity2.class);
                startActivity(intent);
            }
        });

    }
}
package com.fabius.mypoolth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private Button button;
    private EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        input = findViewById(R.id.editTextNumber);

        // Aggiungi un listener per gestire il click sul pulsante
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ottieni il valore inserito dall'utente
                String inputText = input.getText().toString();

                // Verifica se l'input è un numero intero maggiore di 0
                if (!inputText.isEmpty()) {
                    try {
                        int inputValue = Integer.parseInt(inputText);
                        if (inputValue > 0) {
                            // Crea un Intent per aprire la seconda Activity e passa l'input come parametro
                            Intent intent = new Intent(MainActivity.this, Activity2.class);
                            intent.putExtra("INPUT_VALUE", inputValue);
                            startActivity(intent);
                        } else {
                            // Mostra un messaggio all'utente se l'input non è maggiore di 0
                            Toast.makeText(MainActivity.this, "Inserisci un numero intero maggiore di 0", Toast.LENGTH_SHORT).show();
                        }
                    } catch (NumberFormatException e) {
                        // Mostra un messaggio all'utente se l'input non è un numero intero
                        Toast.makeText(MainActivity.this, "Inserisci un numero intero valido", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Mostra un messaggio all'utente se l'input è vuoto
                    Toast.makeText(MainActivity.this, "Inserisci un valore", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
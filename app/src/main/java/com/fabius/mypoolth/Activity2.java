package com.fabius.mypoolth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Activity2 extends AppCompatActivity {

    private TextView testo1, testo2, testo3, testo_finale;
    private Button button1, button2, button3;

    //Pool executor
    private ExecutorService executorService;

    //Lista dei task
    private List<Future<?>> taskFutures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        // FIND BY ID
        testo1 = findViewById(R.id.textView1);
        testo2 = findViewById(R.id.textView2);
        testo3 = findViewById(R.id.textView3);
        testo_finale = findViewById(R.id.textView_finale);
        button1 = findViewById(R.id.buttonA);
        button2 = findViewById(R.id.buttonB);
        button3 = findViewById(R.id.buttonC);

        // INIZ
        button1.setVisibility(View.INVISIBLE);
        button2.setVisibility(View.INVISIBLE);
        button3.setVisibility(View.VISIBLE);


        // Creazione di un ThreadPoolExecutor con un numero di thread pari a 3
        executorService = Executors.newFixedThreadPool(4);

        // Creazione della lista per monitorare lo stato dei Task
        taskFutures = new ArrayList<>();

        // Avvio dei Task
        startTasks();



        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chiudi l'executor service prima di passare alla nuova activity
                executorService.shutdown();

                // torno indietro
                onBackPressed();
            }
        });

    }

    private void startTasks() {
        // Ottieni il parametro dai dati dell'intent
        Intent intent = getIntent();
        int seconds = intent.getIntExtra("INPUT_VALUE", 2); // DEFAULT_VALUE è il valore predefinito se il parametro non è presente


        // Avvio dei Task e aggiunta dei Future alla lista

        // task con parametri
        taskFutures.add(executorService.submit(new Task1(seconds)));
        taskFutures.add(executorService.submit(new Task2()));
        taskFutures.add(executorService.submit(new Task3()));
        //taskFutures.add(executorService.submit(new Task4(0)));
        // Puoi aggiungere altri Task come desiderato

        // Avvia il Task 4 con il valore predefinito
        Future<?> futureTask4 = executorService.submit(new Task4(0)); // Valore predefinito

        // Avvio dei Task e aggiunta dei Future alla lista
        taskFutures.add(futureTask4);

        // Controllo dello stato di completamento dei Task in un thread separato
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Attendi il completamento dei primi tre Task
                    for (int i = 0; i < 3; i++) {
                        taskFutures.get(i).get();
                    }

                    // Attendi il completamento di entrambi i Task 3 e 4
                    taskFutures.get(2).get(); // Task 3
                    futureTask4.get(); // Task 4

                    // Una volta completati tutti i Task, abilita i pulsanti o altro
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            button1.setVisibility(View.VISIBLE);
                            button2.setVisibility(View.VISIBLE);

                            // Chiudi l'executor service
                            executorService.shutdown();
                        }
                    });
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    // Classe per il Task 1
    private class Task1 implements Runnable {
        private final int seconds; // Il parametro da passare

        // Costruttore per inizializzare il parametro seconds
        public Task1(int seconds) {
            this.seconds = seconds;
        }

        @Override
        public void run() {
            // Simula un'elaborazione lunga
            try {
                Thread.sleep(seconds * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Popola la grafica con il risultato
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    testo1.setText("Task 1 completato di "+seconds+" sec");
                }
            });

        }
    }

    // Classe per il Task 2
    private class Task2 implements Runnable {
        @Override
        public void run() {
            // Simula un'elaborazione lunga
            try {
                Thread.sleep(2000);
                // Mostra un messaggio di "..."
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        testo2.setText("...");
                    }
                });

                Thread.sleep(2000);
                // Mostra un messaggio di "Attenzione..."
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        testo2.setText("Attenzione...");
                    }
                });

                Thread.sleep(1000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Popola la grafica con il risultato
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    testo2.setText("Task 2 completato");
                }
            });


        }
    }



    // Classe per il Task 3
    private class Task3 implements Runnable {
        @Override
        public void run() {
            // Simula un'elaborazione lunga
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            // Ottenere un valore da passare al Task 4
            final int valueForTask4 = 42; // Questo è un valore di esempio, puoi cambiare questo in base alla tua logica

            Log.d("__Task3", "Value for Task4: " + valueForTask4); // Aggiungi questo log per controllare il valore ottenuto

            // Popola la grafica con il risultato
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    testo3.setText("Task 3 completato");
                }
            });

            // Avvia il Task 4 con il valore ottenuto
            taskFutures.add(executorService.submit(new Task4(valueForTask4)));
        }
    }


    // Classe per il Task 4
    private class Task4 implements Runnable {
        private final int valueFromTask3;

        public Task4(int valueFromTask3) {
            this.valueFromTask3 = valueFromTask3;
        }

        @Override
        public void run() {
            // Simula un'elaborazione lunga utilizzando il valore ottenuto dal Task 3
            try {
                Thread.sleep(3000); // Simula un'elaborazione di 2 secondi
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Popola la grafica con il risultato utilizzando il valore ottenuto dal Task 3
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    testo_finale.setText("Task 4 completato con valore da Task 3: " + valueFromTask3);
                }
            });
        }
    }

    // Puoi aggiungere altre classi per altri Task come desiderato

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Chiudi l'executor service quando l'Activity viene distrutta
        executorService.shutdown();
    }
}
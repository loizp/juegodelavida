package com.zambrix.juegodelavida;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private GridView tablero;
    private GridAdapter gridAdapter;
    int numFila=21, numColumna=14;
    private Thread thread;
    private Boolean estadojuego=Boolean.FALSE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tablero=(GridView) findViewById(R.id.gltablero);
        ArrayList<Boolean> itablero= new ArrayList<>();
        for (int i = 0; i < numFila; i++) {
            for(int j=0;j<numColumna;j++) {
                if (((int) Math.round(Math.random())) == 1)
                    itablero.add(Boolean.TRUE);
                else itablero.add(Boolean.FALSE);
            }
        }
        tablero.setNumColumns(numColumna);
        gridAdapter=new GridAdapter(itablero, this);
        tablero.setAdapter(gridAdapter);
        jugar();
        tablero.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!estadojuego){
                    TextView tv=(TextView)tablero.getChildAt(position);
                    if (gridAdapter.items.get(position)) {
                        gridAdapter.items.set(position, Boolean.FALSE);
                        tv.setBackgroundColor(Color.rgb(191,191,191));
                    } else {
                        gridAdapter.items.set(position, Boolean.TRUE);
                        tv.setBackgroundColor(Color.rgb(33,150,243));
                    }
                    gridAdapter.notifyDataSetChanged();
                }
            }
        });

        final ImageButton btnjuego = (ImageButton) findViewById(R.id.btn_juego);
        btnjuego.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (thread.getState()==Thread.State.TIMED_WAITING){
                    estadojuego=Boolean.FALSE;
                    btnjuego.setBackgroundResource(R.drawable.ic_play);
                } else {
                    estadojuego=Boolean.TRUE;
                    jugar();
                    thread.start();
                    btnjuego.setBackgroundResource(R.drawable.ic_pause);
                }
            }
        });
    }

    private int num_vecinos(int posicion){
        ArrayList<Integer> vecinos = new ArrayList<>();
        if ((posicion + 1) % numColumna!=0) {
            vecinos.add(posicion + 1);
            if (posicion < (numFila - 1) * numColumna)
                vecinos.add(posicion + numColumna + 1);
        }
        if ((posicion) % numColumna != 0 && posicion != 0){
            vecinos.add(posicion - 1);
            if (posicion >= numColumna)
                vecinos.add(posicion - numColumna - 1);
        }

        if (posicion >= numColumna){
            vecinos.add(posicion - numColumna);
            if ((posicion + 1) % numColumna!=0)
                vecinos.add(posicion-numColumna+1);
        }

        if (posicion < (numFila - 1) * numColumna){
            vecinos.add(posicion + numColumna);
            if ((posicion) % numColumna != 0 && posicion != 0)
                vecinos.add(posicion+numColumna-1);
        }
        int nvecinos=0;
        for (int i=0; i<vecinos.size(); i++){
            if (gridAdapter.items.get(vecinos.get(i)))
                nvecinos++;
        }
        return nvecinos;
    }

    private void jugar(){
        thread = new Thread(){
            @Override
            public void run() {
                while (estadojuego){
                    try {
                        Thread.sleep(300);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ArrayList<Boolean> it = new ArrayList<>();
                                for (int p=0;p<numFila*numColumna;p++){
                                    TextView tv=(TextView)tablero.getChildAt(p);
                                    it.add(gridAdapter.items.get(p));
                                    if (num_vecinos(p) < 2 || num_vecinos(p) > 3){
                                        it.set(p, Boolean.FALSE);
                                        tv.setBackgroundColor(Color.rgb(191,191,191));
                                    }
                                    if (num_vecinos(p) == 3) {
                                        it.set(p, Boolean.TRUE);
                                        tv.setBackgroundColor(Color.rgb(33,150,243));
                                    }
                                }
                                gridAdapter.items = it;
                                gridAdapter.notifyDataSetChanged();
                            }
                        });
                    } catch (InterruptedException ie){
                        ie.printStackTrace();
                    }
                }
            }
        };
    }
}

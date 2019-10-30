package es.upm.miw.SolitarioCelta;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.RadioButton;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

import es.upm.miw.SolitarioCelta.dialogs.AlertDialogFragment;
import es.upm.miw.SolitarioCelta.dialogs.AlertRestartDialogFragment;
import es.upm.miw.SolitarioCelta.dialogs.AlertRetrieveDialogFragment;
import es.upm.miw.SolitarioCelta.models.Partida;
import es.upm.miw.SolitarioCelta.models.RepositorioPartidas;

public class MainActivity extends AppCompatActivity {

	public SCeltaViewModel miJuego;
    public final String LOG_KEY = "MiW";
    private RepositorioPartidas repositorioPartidas;
    TextView nFichasRestantes;
    public Chronometer chronometer;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        repositorioPartidas = new RepositorioPartidas(this);
        miJuego = ViewModelProviders.of(this).get(SCeltaViewModel.class);
        chronometer = (Chronometer) findViewById(R.id.chronometer);
        nFichasRestantes = findViewById(R.id.fichasRestantes);
        nFichasRestantes.setText(Html.fromHtml("Quedan <b>"+miJuego.numeroFichas()+ "</b> fichas restantes"));
        mostrarTablero();
    }

    /**
     * Se ejecuta al pulsar una ficha
     * Las coordenadas (i, j) se obtienen a partir del nombre del recurso, ya que el botón
     * tiene un identificador en formato pXY, donde X es la fila e Y la columna
     * @param v Vista de la ficha pulsada
     */
    public void fichaPulsada(@NotNull View v) {
        chronometer.start();
        chronometer.setTextColor(Color.RED);
        String resourceName = getResources().getResourceEntryName(v.getId());
        int i = resourceName.charAt(1) - '0';   // fila
        int j = resourceName.charAt(2) - '0';   // columna

        Log.i(LOG_KEY, "fichaPulsada(" + i + ", " + j + ") - " + resourceName);
        miJuego.jugar(i, j);
        Log.i(LOG_KEY, "#fichas=" + miJuego.numeroFichas());
        nFichasRestantes.setText(Html.fromHtml("Quedan <b>"+miJuego.numeroFichas()+ "</b> fichas restantes"));
        mostrarTablero();
        if (miJuego.juegoTerminado()) {
            SharedPreferences sharedPrefs =
                    PreferenceManager.getDefaultSharedPreferences(this);
            String nombreUsuario = sharedPrefs.getString("nombreJugador", "Usuario anónimo");
            chronometer.setTextColor(Color.BLACK);
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.stop();
            repositorioPartidas.insert(new Partida(nombreUsuario, miJuego.numeroFichas()));
            new AlertDialogFragment().show(getFragmentManager(), "ALERT_DIALOG");
        }
    }

    /**
     * Visualiza el tablero
     */
    public void mostrarTablero() {
        RadioButton button;
        String strRId;
        String prefijoIdentificador = getPackageName() + ":id/p"; // formato: package:type/entry
        int idBoton;

        for (int i = 0; i < JuegoCelta.TAMANIO; i++)
            for (int j = 0; j < JuegoCelta.TAMANIO; j++) {
                strRId = prefijoIdentificador + i + j;
                idBoton = getResources().getIdentifier(strRId, null, null);
                if (idBoton != 0) { // existe el recurso identificador del botón
                    button = findViewById(idBoton);
                    button.setChecked(miJuego.obtenerFicha(i, j) == JuegoCelta.FICHA);
                }
            }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.opciones_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.opcAjustes:
                startActivity(new Intent(this, SCeltaPrefs.class));
                return true;
            case R.id.opcAcercaDe:
                startActivity(new Intent(this, AcercaDe.class));
                return true;
            case R.id.opcReiniciarPartida:
                new AlertRestartDialogFragment().show(getFragmentManager(), "REINICIAR_PARTIDA");
                return true;
            case R.id.opcGuardarPartida:
                accionGuardar();
                Snackbar.make(
                        findViewById(android.R.id.content),
                        getString(R.string.txtGuardadoExito),
                        Snackbar.LENGTH_LONG
                ).show();
                return true;
            case R.id.opcRecuperarPartida:
                if(miJuego.isPartidaIniciada()){
                    new AlertRetrieveDialogFragment().show(getFragmentManager(), "RECUPERAR_PARTIDA");
                } else {
                    accionRecuperar();
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    chronometer.setTextColor(Color.BLACK);
                    chronometer.stop();
                }
                return true;
            case R.id.opcMejoresResultados:
                startActivity(new Intent(this, MejoresResultados.class));
                return true;
            default:
                Snackbar.make(
                        findViewById(android.R.id.content),
                        getString(R.string.txtSinImplementar),
                        Snackbar.LENGTH_LONG
                ).show();
        }
        return true;
    }

    /**
     * Devuelve el nombre del fichero
     *
     * @return nombre del fichero
     */
    private String obtenerNombreFichero() {

        return getResources().getString(R.string.nombreFicheroPersistencia);
    }

    public void accionGuardar() {

        try {  // Añadir al fichero
            FileOutputStream fos;

            fos = openFileOutput(obtenerNombreFichero(), Context.MODE_PRIVATE); // Memoria interna
            fos.write(miJuego.serializaTablero().getBytes());
            fos.close();
        } catch (Exception e) {
            Log.e("accionGuardar()", "FILE I/O ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public void accionRecuperar() {
        BufferedReader fin;

        try {
            fin = new BufferedReader(
                    new InputStreamReader(openFileInput(obtenerNombreFichero()))); // Memoria interna
            String linea = fin.readLine();
            while (linea != null) {
                miJuego.deserializaTablero(linea);
                linea = fin.readLine();
            }
            fin.close();
        } catch (Exception e) {
            Log.e("accionRecuperar", "FILE I/O ERROR: " + e.getMessage());
            e.printStackTrace();
        }

        mostrarTablero();
    }

}

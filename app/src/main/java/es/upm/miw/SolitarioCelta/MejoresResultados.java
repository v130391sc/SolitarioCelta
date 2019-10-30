package es.upm.miw.SolitarioCelta;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import es.upm.miw.SolitarioCelta.dialogs.AlertDeleteDialogFragment;
import es.upm.miw.SolitarioCelta.dialogs.AlertRestartDialogFragment;
import es.upm.miw.SolitarioCelta.models.Partida;
import es.upm.miw.SolitarioCelta.models.RepositorioPartidas;

public class MejoresResultados extends Activity {

    private RepositorioPartidas repositorioPartidas;

    List<Partida> listaPartidas;
    ListView lvPuntuaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.best_scores);

        lvPuntuaciones = findViewById(R.id.lvListadoPuntuaciones);
        repositorioPartidas = new RepositorioPartidas(this);
        listaPartidas = repositorioPartidas.getAllOrderByNFichas();
        lvPuntuaciones.setAdapter(new PartidaAdapter(this, listaPartidas, R.layout.layout_listado_partidas));

        ImageButton button = findViewById(R.id.borrarPuntuaciones);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new AlertDeleteDialogFragment().show(getFragmentManager(), "BORRAR_PUNTUACIONES");
            }
        });

    }

    public void borrarContenido() {
        repositorioPartidas.deleteAll();
        listaPartidas = new ArrayList<>();
        lvPuntuaciones.setAdapter(new PartidaAdapter(this, listaPartidas, R.layout.layout_listado_partidas));
    }
}

package es.upm.miw.SolitarioCelta;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import es.upm.miw.SolitarioCelta.models.Partida;

public class PartidaAdapter extends ArrayAdapter {

    private Context _contexto;
    private List<Partida> _partidas;
    private int _resourceId;

    /**
     * Constructor
     *
     * @param contexto      Contexto
     * @param partidas        Datos a representar
     * @param resourceId    Id. recurso layout entidad
     */
    public PartidaAdapter(Context contexto, List<Partida> partidas, int resourceId) {
        super(contexto, resourceId, partidas);
        this._contexto = contexto;
        this._partidas = partidas;
        this._resourceId = resourceId;
        setNotifyOnChange(true);
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // obtener o generar vista
        if (convertView == null) {
            LayoutInflater inflater =
                    (LayoutInflater) _contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(this._resourceId, null);
        }

        Partida partida = _partidas.get(position);
        if (partida != null) {

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            TextView tvId = convertView.findViewById(R.id.tvListadoPartidaId);
            tvId.setText(Long.toString(position+1));

            TextView tvNombreUsuario = convertView.findViewById(R.id.tvListadoPartidaNombreUsuario);
            tvNombreUsuario.setText(partida.getNombreJugador());

            TextView tvFichas = convertView.findViewById(R.id.tvListadoPartidaFichas);
            tvFichas.setText(String.valueOf(partida.getnFichasRestantes()));

            TextView tvFecha = convertView.findViewById(R.id.tvListadoPartidaFecha);
            tvFecha.setText(String.valueOf(df.format(partida.getTimestamp())));
        }

        return convertView;
    }

}

package es.upm.miw.SolitarioCelta.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import es.upm.miw.SolitarioCelta.MainActivity;
import es.upm.miw.SolitarioCelta.MejoresResultados;
import es.upm.miw.SolitarioCelta.R;
import es.upm.miw.SolitarioCelta.models.RepositorioPartidas;

public class AlertDeleteDialogFragment extends DialogFragment {

    private RepositorioPartidas repositorioPartidas;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final MejoresResultados mejoresResultados = (MejoresResultados) getActivity();

        super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(mejoresResultados);
        builder
                .setTitle(R.string.txtDialogoBorrarTitulo)
                .setMessage(R.string.txtDialogoBorrarPregunta)
                .setPositiveButton(
                        getString(R.string.txtDialogoBorrarAfirmativo),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mejoresResultados.borrarContenido();
                            }
                        }
                )
                .setNegativeButton(
                        getString(R.string.txtDialogoBorrarNegativo),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getDialog().cancel();
                            }
                        }
                );

        return builder.create();
    }
}

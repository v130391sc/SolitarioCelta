package es.upm.miw.SolitarioCelta.models;

import android.provider.Telephony;

import java.time.LocalDateTime;
import java.util.Date;

public class Partida {

    private int id;
    private String nombreJugador;
    private Date timestamp;
    private int nFichasRestantes;

    public Partida(){}

    public Partida(String nombreJugador, int nFichasRestantes) {
        this.nombreJugador = nombreJugador;
        this.nFichasRestantes = nFichasRestantes;
        this.timestamp = new Date();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreJugador() {
        return nombreJugador;
    }

    public void setNombreJugador(String nombreJugador) {
        this.nombreJugador = nombreJugador;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getnFichasRestantes() {
        return nFichasRestantes;
    }

    public void setnFichasRestantes(int nFichasRestantes) {
        this.nFichasRestantes = nFichasRestantes;
    }
}

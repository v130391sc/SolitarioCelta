package es.upm.miw.SolitarioCelta;

import android.provider.BaseColumns;

public class PartidaContract {

    private PartidaContract() {}

    public static class PartidaEntry implements BaseColumns {
        public static final String TABLE_NAME = "partidas";

        public static final String COL_NAME_ID = BaseColumns._ID;
        public static final String COL_NAME_PLAYERNAME = "nombre";
        public static final String COL_NAME_GAME_TIMESTAMP = "fecha";
        public static final String COL_NAME_NTILES = "nFichas";
    }
}

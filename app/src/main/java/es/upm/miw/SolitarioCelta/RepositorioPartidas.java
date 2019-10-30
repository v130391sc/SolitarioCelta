package es.upm.miw.SolitarioCelta;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static es.upm.miw.SolitarioCelta.PartidaContract.PartidaEntry;

public class RepositorioPartidas extends SQLiteOpenHelper {

    private static final String DB_NAME = PartidaEntry.TABLE_NAME +".db";

    private static final int DB_VERSION = 1;

    public RepositorioPartidas(@Nullable Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String consultaSQL = "CREATE TABLE " + PartidaEntry.TABLE_NAME + "("
                + PartidaEntry.COL_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PartidaEntry.COL_NAME_PLAYERNAME + " TEXT, "
                + PartidaEntry.COL_NAME_GAME_TIMESTAMP + " TEXT, "
                + PartidaEntry.COL_NAME_NTILES + " INTEGER)";
        sqLiteDatabase.execSQL(consultaSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String consultaSQL = "DROP TABLE IF EXISTS " + PartidaEntry.TABLE_NAME;
        sqLiteDatabase.execSQL(consultaSQL);
        onCreate(sqLiteDatabase);
    }

    public long insert(Partida partida){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.insert(PartidaEntry.TABLE_NAME, null, this.getContentValues(partida));
    }

    public List<Partida> getAll(){
        String consultaSQL = "SELECT * FROM " + PartidaEntry.TABLE_NAME;
        List<Partida> partidas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(consultaSQL, null);

        if(cursor.moveToFirst()){
            while (!cursor.isAfterLast()){
                Partida partida = new Partida();
                partida.setId(cursor.getInt(cursor.getColumnIndex(PartidaEntry.COL_NAME_ID)));
                partida.setNombreJugador(cursor.getString(cursor.getColumnIndex(PartidaEntry.COL_NAME_PLAYERNAME)));
                partida.setTimestamp(new Date(cursor.getString(cursor.getColumnIndex(PartidaEntry.COL_NAME_GAME_TIMESTAMP))));
                partida.setnFichasRestantes(cursor.getInt(cursor.getColumnIndex(PartidaEntry.COL_NAME_NTILES)));
                partidas.add(partida);
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
        return partidas;
    }

    public ContentValues getContentValues(Partida partida) {
        ContentValues contentValues = new ContentValues();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        contentValues.put(PartidaEntry.COL_NAME_PLAYERNAME, partida.getNombreJugador());
        contentValues.put(PartidaEntry.COL_NAME_GAME_TIMESTAMP, df.format(partida.getTimestamp()));
        contentValues.put(PartidaEntry.COL_NAME_NTILES, partida.getnFichasRestantes());
        return contentValues;

    }
}

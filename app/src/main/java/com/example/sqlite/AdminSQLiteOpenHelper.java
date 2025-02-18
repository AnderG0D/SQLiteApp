package com.example.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {

    // Define el nombre de la base de datos y su versión
    private static final String DATABASE_NAME = "db_gramineas";
    private static final int DATABASE_VERSION = 2; // Incrementa la versión para aplicar cambios

    public AdminSQLiteOpenHelper(@Nullable Context context, @Nullable String name,
                                 @Nullable SQLiteDatabase.CursorFactory factory,
                                 int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Crear objetos de la base de datos
        sqLiteDatabase.execSQL("CREATE TABLE gramineas (" +
                "idgraminea INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombrecomun TEXT, " +
                "descripcion TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int version_anterior, int version_nueva) {
        /*sqLiteDatabase.execSQL("create table gramineas" +
                "(idgraminea integer primary key autoincrement, " +
                " nombrecomun text, " +
                "descripcion text)");*/
        // Eliminar la tabla antigua si existe para evitar conflictos
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS gramineas");

        // Volver a crear la tabla con la estructura actualizada
        onCreate(sqLiteDatabase);
    }

    // Metodo para obtener el contenido de la tabla
    public Cursor verDatos() {
        SQLiteDatabase db = this.getReadableDatabase();
        //String query = "select * from gramineas ";
        return db.rawQuery("SELECT * FROM gramineas", null);
        //return cursor1;
    }
}

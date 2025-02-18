package com.example.sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.SweepGradient;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private SQLiteOpenHelper admin;
    private SQLiteDatabase db;
    private Cursor cursor;

    EditText editText_Id, editText_Nombre, editText_Descripcion;

    ImageButton imageButton_RegistroIncial,
            imageButton_RegistroSiguiente,
            imageButton_RegistroAnterior,
            imageButton_RegistroFinal;

    ImageButton imageButton_Alta, imageButton_Baja,
            imageButton_Cambio, imageButton_Buscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText_Id = findViewById(R.id.editText_Id);
        editText_Nombre = findViewById(R.id.editText_Nombre);
        editText_Descripcion = findViewById(R.id.editText_Descripcion);

        imageButton_RegistroIncial =
                findViewById(R.id.imageButton_RegistroInicial);
        imageButton_RegistroAnterior =
                findViewById(R.id.imageButton_RegistroAnterior);
        imageButton_RegistroSiguiente =
                findViewById(R.id.imageButton_RegistroSiguiente);
        imageButton_RegistroFinal =
                findViewById(R.id.imageButton_RegistroFinal);

        imageButton_Alta = findViewById(R.id.imageButton_Alta);
        imageButton_Baja = findViewById(R.id.imageButton_Baja);
        imageButton_Cambio = findViewById(R.id.imageButton_Cambio);
        imageButton_Buscar = findViewById(R.id.imageButton_Buscar);

        // Revisar si hay datos y cargar si es necesario
        cargarDatos();
        // Revisar si el cursor esta o no vacio
        if (!String.valueOf(cursor.getCount()).equals("0")) {
            mostrarRegistro();
        }

        // Codigo para imageButton "Alta o agregar"
        imageButton_Alta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                admin = new AdminSQLiteOpenHelper(getApplicationContext(),
                        "administracion", null, 1);
                db = admin.getWritableDatabase();

                // Recuperar datos de la GUI
                String id = editText_Id.getText().toString();
                String nombre = editText_Nombre.getText().toString();
                String descripcion = editText_Descripcion.getText().toString();

                // Generar o crear un registro Java con estos datos
                ContentValues values = new ContentValues();
                values.put("idgraminea", id);
                values.put("nombrecomun", nombre);
                values.put("descripcion", descripcion);
                // Agregar registro a la tabla
                long cantidad = db.insert("gramineas", null, values);
                db.close();

                if (cantidad == 1) {
                    Toast.makeText(MainActivity.this,
                            "Graminea agregada",
                            Toast.LENGTH_SHORT).show();

                    // Como exite un nuevo registro, volver a cargar datos
                    // y mostrar registro inicial
                    cargarDatos();
                    mostrarRegistro();
                } else {
                    Toast.makeText(MainActivity.this,
                            "",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        imageButton_Baja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                admin = new AdminSQLiteOpenHelper(getApplicationContext(),
                        "administracion", null, 1);
                db = admin.getWritableDatabase();

                // Recuperar datos de la GUI
                String id = editText_Id.getText().toString();

                // Operacion de baja en la tabla
                int cantidad = db.delete("gramineas",
                        "idgraminea = " + id,
                        null);
                db.close();
                if (cantidad == 1) { // Cantidad de registros afectados
                    Toast.makeText(MainActivity.this,
                            "Graminea dada de baja",
                            Toast.LENGTH_SHORT).show();

                    cargarDatos();
                    mostrarRegistro();
                }
                else {
                    Toast.makeText(MainActivity.this,
                            "ERROR! Graminea no eliminada",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        imageButton_Cambio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Operacion update en la tabla
                admin = new AdminSQLiteOpenHelper(getApplicationContext(),
                        "administracion", null, 1);
                db = admin.getWritableDatabase();

                // Recuperar datos de la GUI
                String id = editText_Id.getText().toString();
                String nombre = editText_Nombre.getText().toString();
                String descripcion = editText_Descripcion.getText().toString();

                // Generar o crear un registro Java con estos datos
                ContentValues values = new ContentValues();
                values.put("idgraminea", id);
                values.put("nombrecomun", nombre);
                values.put("descripcion", descripcion);
                // Agregar registro a la tabla
                long cantidad = db.update("gramineas",
                        values,
                        "idgraminea = " + id,
                        null);
                db.close();
                if (cantidad == 1){
                    Toast.makeText(MainActivity.this,
                            "Graminea agregada",
                            Toast.LENGTH_SHORT).show();

                    // Como exite un nuevo registro, volver a cargar datos
                    // y mostrar registro inicial
                    int i = cursor.getPosition();
                    cargarDatos();
                    cursor.moveToPosition(i);
                    mostrarRegistro();
                } else {
                    Toast.makeText(MainActivity.this,
                            "ERROR! Graminea NO modificada",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });

        imageButton_Buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



        /*
        Código para botones de navegación
         */
        imageButton_RegistroIncial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int posicion = 0;
                cursor.moveToPosition(posicion);
                mostrarRegistro();
            }
        });

        imageButton_RegistroSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean siguiente = false;
                siguiente = cursor.moveToNext();
                if (siguiente) {
                    mostrarRegistro();
                }
            }
        });

        imageButton_RegistroAnterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int actual = cursor.getPosition();
                if (actual > 0) {
                    int posicion = actual - 1;
                    cursor.moveToPosition(posicion);
                    mostrarRegistro();
                }
            }
        });

        imageButton_RegistroFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cursor.moveToLast();
                mostrarRegistro();
            }
        });


    }

    private void mostrarRegistro() {
        if (!String.valueOf(cursor.getCount()).equals("0")) {
            editText_Id.setText(cursor.getString(0));
            editText_Nombre.setText(cursor.getString(1));
            editText_Descripcion.setText(cursor.getString(2));
        } else {
            editText_Id.setText("");
            editText_Nombre.setText("");
            editText_Descripcion.setText("");
        }
    }

    private void cargarDatos() {
        admin = new AdminSQLiteOpenHelper(this,
                "administracion", null, 1);
        db = admin.getReadableDatabase();
        cursor = db.rawQuery("select * from gramineas",
                null);
        if (cursor != null && cursor.moveToFirst()) {
            cursor.moveToFirst();
        }
        db.close();
    }
}
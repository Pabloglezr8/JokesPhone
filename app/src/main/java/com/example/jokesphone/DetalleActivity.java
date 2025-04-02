package com.example.jokesphone;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DetalleActivity extends AppCompatActivity {

    private String idBroma;
    private String titulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);

        // Obtenemos datos del intent
        idBroma = getIntent().getStringExtra("id");
        titulo = getIntent().getStringExtra("titulo");

        TextView textTitulo = findViewById(R.id.textTitulo);
        EditText editNombre = findViewById(R.id.editNombre);
        Button btnLlamar = findViewById(R.id.btnLlamar);

        textTitulo.setText(titulo);

        //Leemos el campo del nombre, si esta vació da error, si esta completo envia la llamada
        btnLlamar.setOnClickListener(v -> {
            String nombre = editNombre.getText().toString().trim();
            if (!nombre.isEmpty()) {
                new EnviarLlamadaTask().execute(nombre);
            } else {
                Toast.makeText(this, "Escribe un nombre", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class EnviarLlamadaTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                String nombre = params[0];
                String uid = "3b6b7bbc866a36fb@jokesphone";
                String msg = idBroma + "//" + nombre;

                // Realizamos la llamada
                URL url = new URL("https://master.appha.es/lua/jokesphone/logcat");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);

                // Enviamos los parámetros
                String postParams = "uid=" + uid + "&msg=" + msg;
                OutputStream os = conn.getOutputStream();
                os.write(postParams.getBytes());
                os.flush();
                os.close();

                Log.d("RESULTADO", "Código de respuesta: " + conn.getResponseCode());
                Log.d("RESULTADO", uid + ": " + msg);
                return conn.getResponseCode() == 200;

            } catch (Exception e) {
                Log.e("DETALLE", "Error al enviar: " + e.getMessage());
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast.makeText(DetalleActivity.this, "Llamada registrada", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(DetalleActivity.this, "Error al enviar", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

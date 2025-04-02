package com.example.jokesphone;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jokesphone.modelos.Dialplan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ListadoFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Dialplan> dialplanList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listado, container, false);


//Se carga la vista del listado de dialplans y se ejecuta el metodo FetchDialplansTask
        recyclerView = view.findViewById(R.id.recyclerListado);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        new FetchDialplansTask().execute();
        return view;
    }

    //Metodo que nos conecta con el serividor, envia nuestros datos, nos devuelve un JSON con los objetos y los convierte a string para mostrarlos en el recyclerview
    private class FetchDialplansTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {

                //Nos conectamos al servidor con petición POST
                URL url = new URL("https://master.appha.es/lua/jokesphone/user/get_dialplan.lua");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);

                // Dátos que queremos enviar
                String params = "c=ES&sc=ES&uid=3b6b7bbc866a36fb@jokesphone";

                //Envíamos los datos al servidor
                OutputStream os = connection.getOutputStream();
                os.write(params.getBytes());//envía
                os.flush();//se asegura que se manden
                os.close();//e cierra la conexión de escritura

                //Se lee el JSON
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line;

                //Mientras haya algo q leer lo leo y lo añado.
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                reader.close();// cierro la conexión de lectura

                return result.toString();

            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String json) {
            //leemos el JSON pra separar en objetos de la clase Dialplan y poder mostrarlos
            if (json != null) {
                try {
                    JSONArray jsonArray = new JSONArray(json);
                    //por cada objeto JSON creamos un objeto de la clase Dialplan
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        Dialplan d = new Dialplan();
                        d._id = obj.getString("_id");
                        d.titulo = obj.getString("titulo");
                        d.desc = obj.getString("desc");
                        d.image_url = obj.getString("image_url");
                        dialplanList.add(d);//lo agregamos a nuestr array
                    }

                    // Cada objeto de nuestro array se muestra en el recyclerview gracias al adapter.
                    recyclerView.setAdapter(new DialplanAdapter(dialplanList));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

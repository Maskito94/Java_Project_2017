package mathiasgrosz.newsfeed;


import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SplashScreen extends AppCompatActivity {
 static final int SPLASH_TIME = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        String urlSources = "https://newsapi.org/v2/sources?apiKey=d31f5fa5f03443dd8a1b9e3fde92ec34&language=fr";


//utilisation de volley
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,urlSources, new  Response.Listener<String>() {

            @Override
            public void onResponse(String response) { //choix de la source et chargement
                try {
// conversion en json + initialisation des variables
                    try { // attente pour profiter de l'ecran de chargement
                        Thread.sleep(SPLASH_TIME);
                    } catch (InterruptedException e) {
                    }
                    JSONObject json = new JSONObject(response);
                    JSONArray sources = json.getJSONArray("sources");
                    final List<String> idSources   = new ArrayList<>();
                    final List<String> sourcesName = new ArrayList<>();

//traitement des donnees
                    for(int i = 0; i<sources.length(); i++){
                        JSONObject source = sources.getJSONObject(i);
                        idSources.add(source.getString("id"));
                        sourcesName.add(source.getString("name"));
                    }
                    final String[] id_sources   = new String[idSources.size()];
                    final String[] name_sources = new String[sourcesName.size()];
                    sourcesName.toArray(name_sources);
                    idSources.toArray(id_sources);
                    Bundle extras = getIntent().getExtras();

//recuperation du choix de la source
                    String id = idSources.get(0);
                    int tempCurseur = 0;
                    if (extras != null) {
                        id = getIntent().getStringExtra("source");
                        tempCurseur = getIntent().getIntExtra("numero",0);
                    }
                    final int curseur = tempCurseur;
                    String urlActu = "https://newsapi.org/v2/everything?apiKey=d31f5fa5f03443dd8a1b9e3fde92ec34&language=fr&sources=" + id;

                    RequestQueue queue = Volley.newRequestQueue(SplashScreen.this);
                    StringRequest stringRequest = new StringRequest(Request.Method.GET,urlActu, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String news) { //chargement des news de la source choisie

                            try {
//conversion + initialisation
                                JSONObject jsonNews = new JSONObject(news);
                                JSONArray newsArray = jsonNews.getJSONArray("articles");
                                List<String> titles       = new ArrayList<>();
                                List<String> descriptions = new ArrayList<>();
                                List<String> urlLinks     = new ArrayList<>();
                                List<String> images       = new ArrayList<>();
//traitement
                                for(int j=0; j<newsArray.length(); j++){
                                    JSONObject une_actu = newsArray.getJSONObject(j);
                                    String titre = une_actu.getString("title");
                                    titles.add(titre);
                                    String descr = une_actu.getString("description");
                                    descriptions.add(descr);
                                    String lien = une_actu.getString("url");
                                    urlLinks.add(lien);
                                    String img = une_actu.getString("urlToImage");
                                    images.add(img);
                                }
//lancement de l'activité List
                                String[] titleArray = new String[titles.size()];
                                titles.toArray(titleArray);
                                String[] descArray  = new String[descriptions.size()];
                                descriptions.toArray(descArray);
                                String[] urlArray   = new String[urlLinks.size()];
                                urlLinks.toArray(urlArray);
                                String[] imageArray = new String[images.size()];
                                images.toArray(imageArray);

                                Intent i = new Intent(SplashScreen.this, ListActivity.class);
                                i.putExtra("titre", titleArray);
                                i.putExtra("description", descArray);
                                i.putExtra("url", urlArray);
                                i.putExtra("image", imageArray);
                                i.putExtra("id", id_sources);
                                i.putExtra("name", name_sources);
                                i.putExtra("numero",curseur);

                                startActivity(i);
                                finish();

                            }catch(JSONException e){
                                Log.d("Erreur", "Impossible de convertir en JSON - NEWS");
                            }
                        }
                    }, new Response.ErrorListener(){ // erreur de chargement bis ________________________________active seulement si le lien est mort
                                    @Override
                                    public void onErrorResponse(VolleyError error){Log.d("Erreur", "Impossible de se connecter au serveur des news.");}
                                });
                    queue.add(stringRequest);
                }catch(JSONException e){
                    Log.d("Erreur", "Impossible de convertir en JSON - SOURCES");
                }
            }
        }, new Response.ErrorListener(){ // erreur de chargement + rechargement
            @Override
            public void onErrorResponse(VolleyError error){
                Snackbar msnackbar = Snackbar.make(findViewById(R.id.imageView), "Impossible de se connecter au serveur.", Snackbar.LENGTH_INDEFINITE)
                        .setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {recreate();}
                        });
                msnackbar.show();
            }
        });
        queue.add(stringRequest);
    }
}

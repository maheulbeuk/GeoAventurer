package com.example.tefa.projetmaheu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Tefa on 07/10/2016.
 */

public class PagePrincipale extends AppCompatActivity {

    public static final String ROOT_URL = "http://37.187.104.237:88/";
    final Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_principale);
        Bundle extras = this.getIntent().getExtras();
        int IdUser = extras.getInt("id");
        RefreshInfo(IdUser);

    }


        /*
        String identifiant = extras.getString("identifiant");
        int xp = extras.getInt("xp");
        int quest_fini =extras.getInt("quest_fini");
        Log.e("log_tag", "Id: " + Id +
                ", Identifiant: " + identifiant + ", Xp:" + xp
                + ", Quest_fini:" +quest_fini
        );

        TextView textViewPseudo = (TextView) findViewById(R.id.pseudo);
        textViewPseudo.setText(identifiant);
        TextView textViewXp = (TextView) findViewById(R.id.xp);
        textViewXp.setText("Expérience: "+String.valueOf(xp));
        TextView textViewQFini = (TextView) findViewById(R.id.qFinis);
        textViewQFini.setText("Quêtes finis: "+String.valueOf(quest_fini));
        }
           */

    public void RefreshInfo(int IdUser) {

        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(ROOT_URL) //Setting the Root URL
                .build(); //Finally building the adapter
        String keys = "PagePrincipal";

        //Creating object for our interface
        SelectAPI Select = adapter.create(SelectAPI.class);

        Log.e("log_tag", "Identifiant: " + IdUser);


        Select.PagePrincipal(
                IdUser,
                keys,
                new Callback<Response>() {
                    @Override
                    public void success(Response result, Response response) {
                        //On success we will read the server's output using bufferedreader
                        //Creating a bufferedreader object
                        BufferedReader reader = null;

                        //An string to store output from the server
                        String resultat = "";

                        try {
                            //Initializing buffered reader
                            reader = new BufferedReader(new InputStreamReader(result.getBody().in()));
                            StringBuilder sb = new StringBuilder();
                            String line = null;
                            while ((line = reader.readLine()) != null) {
                                sb.append(line + "\n");
                            }
                            resultat = sb.toString();
                        } catch (Exception e) {
                            Log.e("log_tag", "Erreur dans la conversion du résultat : " + e.toString());
                        }

                        try {
                            JSONArray jArray = new JSONArray(resultat);
                            JSONObject RecupdataUser = jArray.getJSONObject(0);
                            JSONObject RecupTotQuest = jArray.getJSONObject(1);


                            Log.e("log_tag", "Identifiant: " + RecupdataUser.getString("Identifiant") +
                                    ", Email: " + RecupdataUser.getString("Email") + ", Xp: " +
                                    RecupdataUser.getInt("Xp") + ", Quest_fini:" + RecupdataUser.getInt("Quest_fini")
                                    + ", TotQuest:" + RecupTotQuest.getInt("TotQuest")
                            );


                        } catch (JSONException e) {
                            Log.e("log_tag", "Erreur dans le parsing des data : " + e.toString());
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e("log_tag", "Se suis une erreur : ");
                    }
                }
        );

        keys = "ListLevel";

        Select.ListLevel(
                keys,
                new Callback<Response>() {
                    @Override
                    public void success(Response result, Response response) {
                        //On success we will read the server's output using bufferedreader
                        //Creating a bufferedreader object
                        BufferedReader reader = null;

                        //An string to store output from the server
                        String resultat = "";

                        try {
                            //Initializing buffered reader
                            reader = new BufferedReader(new InputStreamReader(result.getBody().in()));
                            StringBuilder sb = new StringBuilder();
                            String line = null;
                            while ((line = reader.readLine()) != null) {
                                sb.append(line + "\n");
                            }
                            resultat = sb.toString();
                        } catch (Exception e) {
                            Log.e("log_tag", "Erreur dans la conversion du résultat : " + e.toString());
                        }

                        try {
                            JSONArray jArray = new JSONArray(resultat);
                            JSONObject RecupLevel = jArray.getJSONObject(0);
                            JSONObject RecupdataLevel = RecupLevel.getJSONObject("Id_Level");
                            JSONObject RecupdataXpLevel = RecupLevel.getJSONObject("Xp_Level");



                            Log.e("log_tag", "Level: " + RecupdataLevel +
                                    ", Xp: " + RecupdataXpLevel
                            );


                        } catch (JSONException e) {
                            Log.e("log_tag", "Erreur dans le parsing des data : " + e.toString());
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e("log_tag", "Se suis une erreur : ");
                    }
                }
        );
    }

}

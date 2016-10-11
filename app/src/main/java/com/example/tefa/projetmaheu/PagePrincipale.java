package com.example.tefa.projetmaheu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.StaticLayout;
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
    public static int niveau[][];
    Constant constant = new Constant();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_principale);
        Bundle extras = this.getIntent().getExtras();
        int IdUser = extras.getInt("id");
        /*Xp = 0;
        QuestEnd=0;
        QuestTot=0;
        QtLevel=0;
        identifiant=null;
        LevelUser=0;
        LevelXpTotal=0;*/
        RefreshInfo(IdUser);
    }

    public void RefreshInfo(int IdUser) {

        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(ROOT_URL) //Setting the Root URL
                .build(); //Finally building the adapter
        String keys = "PagePrincipal";

        //Creating object for our interface
        SelectAPI Select = adapter.create(SelectAPI.class);
        ;

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

                            constant.setIdentifiant(RecupdataUser.getString("Identifiant"));
                            constant.setXp(RecupdataUser.getInt("Xp"));
                            constant.setqFini(RecupdataUser.getInt("Quest_fini"));
                            constant.setTotQuest(RecupTotQuest.getInt("TotQuest"));
                            Log.e("popo",constant.getIdentifiant());
                            TextView textViewPseudo = (TextView) findViewById(R.id.pseudo);
                            textViewPseudo.setText(constant.getIdentifiant());

                            TextView textViewQFini = (TextView) findViewById(R.id.qFinis);
                            textViewQFini.setText(constant.getqFini() + "/" + constant.getTotQuest());

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
                            JSONArray ArrayRecup = new  JSONArray(resultat);
                            niveau = new int[ArrayRecup.length()][2];
                            int qtLevel = ArrayRecup.length();

                            for (int i=0;i<ArrayRecup.length()-1;i++) {
                                JSONObject itemobj = ArrayRecup.getJSONObject(i);
                                Log.e("log_tag", "Level: " + itemobj.getInt("Id_Level") + ", Xp: " + itemobj.getInt("Xp_Level"));
                                for(int j=0;j<=1;j++){
                                    int level = itemobj.getInt("Id_Level");
                                    int xpLevel = itemobj.getInt("Xp_Level");
                                   if (j==0) {
                                       niveau[i][j]=level;
                                   }
                                   else{
                                       niveau[i][j]=xpLevel;
                                   }
                                }
                            }
                            constant.setQtLevel(qtLevel);
                            //selon xp on doit en deduire le Level
                             for (int i=0;i<=qtLevel;i++){
                                 if (niveau[i][1]>constant.getXp()){
                                     constant.setLevel(niveau[i][0]);
                                     constant.setXpLevel(niveau[i][1]);
                                 }
                             }
                            TextView textViewXp = (TextView) findViewById(R.id.xp);
                            textViewXp.setText(constant.getXp() +"/"+ constant.getXpLevel());
                            TextView textViewLevel = (TextView) findViewById(R.id.Level);
                            textViewLevel.setText(constant.getLevel()+ "/" + qtLevel );

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

        // selon xp on doit en deduire le Level
               // for (int i=0;i<=QtLevel;i++){
               //     if (Xp < Niveau[i][1]){ LevelUser= Niveau[i][0]; LevelXpTotal= Niveau[i][1];}
               // }
    }

}

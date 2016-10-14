package com.example.tefa.projetmaheu;

import android.util.Log;
import android.widget.Adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.RestAdapter;

/**
 * Created by Tefa on 13/10/2016.
 */

public class InfoJoueur {

    //variable
    int niveau [][];
    private Constant constant;
    private int id;
    public static final String ROOT_URL = "http://37.187.104.237:88/";

    //acesseur
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Constant getConstant() {
        return constant;
    }

    public void setConstant(Constant constant) {
        this.constant = constant;
    }

    //constructeur
    public InfoJoueur(int pId,Constant pConstant){
        setId(pId);
        setConstant(pConstant);
    }

    //methode
    public void RefreshInfo(int IdUser) {

        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(ROOT_URL) //Setting the Root URL
                .build(); //Finally building the adapter
        String keys = "PagePrincipal";

        //Creating object for our interface
        SelectAPI Select = adapter.create(SelectAPI.class);

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
                            JSONArray ArrayRecup = new JSONArray(resultat);
                            niveau = new int[ArrayRecup.length()][2];
                            int qtLevel = ArrayRecup.length();
                            int i = 0;
                            for (i = 0; i < ArrayRecup.length(); i++) {
                                JSONObject itemobj = ArrayRecup.getJSONObject(i);
                                Log.e("log_tag", "Level: " + itemobj.getInt("Id_Level") + ", Xp: " + itemobj.getInt("Xp_Level"));
                                for (int j = 0; j <= 1; j++) {
                                    int level = itemobj.getInt("Id_Level");
                                    int xpLevel = itemobj.getInt("Xp_Level");
                                    if (j == 0) {
                                        niveau[i][j] = level;
                                    } else {
                                        niveau[i][j] = xpLevel;
                                    }
                                }
                            }
                            constant.setQtLevel(qtLevel);
                            //selon xp on doit en deduire le Level
                            i = 0;
                            for (i = 0; i <= qtLevel - 1; i++) {
                                if (i == 0) {
                                    if (constant.getXp() < niveau[i][1]) {
                                        constant.setXpLevel(niveau[i][1]);
                                        break;
                                    }
                                } else {
                                    if (niveau[i - 1][1] <= constant.getXp() && niveau[i + 1][1] >= constant.getXp()) {
                                        constant.setLevel(niveau[i][0]);
                                        constant.setXpLevel(niveau[i + 1][1]);
                                        break;
                                    } else if (constant.getXp() > niveau[qtLevel - 1][1]) {
                                        constant.setLevel(niveau[qtLevel - 1][0]);
                                        constant.setXpLevel(niveau[qtLevel - 1][1]);
                                        break;
                                    }
                                }

                            }

                        } catch (JSONException e) {
                            Log.e("log_tag", "Erreur dans le parsing des data : " + e.toString());
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e("log_tag", "Se suis une erreur : ");
                    }
                });
    }

}

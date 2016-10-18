package com.example.tefa.projetmaheu;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.example.tefa.projetmaheu.AccueilActivity.ROOT_URL;
import static com.example.tefa.projetmaheu.AccueilActivity.constant;

public class PagePrincipal extends AppCompatActivity {

    //final Context context = this;
    private LocationManager locationManager;
    private LocationListener listener;

    int niveau[][];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_principal);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        Log.e("Log_Geoloc_Constant: ", " Longitude " + constant.getLongitude() + ", Latitude " + constant.getLatitude());

        listener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                constant.setLongitude(location.getLongitude());
                constant.setLatitude(location.getLatitude());
                TextView textViewLongitude = (TextView) findViewById(R.id.longitude);
                textViewLongitude.setText(new Double(constant.getLongitude()).toString());
                TextView textViewLatitude = (TextView) findViewById(R.id.latitude);
                textViewLatitude.setText(new Double(constant.getLatitude()).toString());
                Log.e("Log_Geoloc: ", " Longitude " + location.getLongitude() + ", Latitude " + location.getLatitude());
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };


        Refresh_Location();

        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(ROOT_URL) //Setting the Root URL
                .build(); //Finally building the adapter

        SelectAPI Select = adapter.create(SelectAPI.class);

        Select.PagePrincipal(
                constant.getId(),
                "PagePrincipal",
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
                            constant.setIdentifiant(RecupdataUser.getString("Identifiant"));
                            constant.setXp(RecupdataUser.getInt("Xp"));
                            constant.setqFini(RecupdataUser.getInt("Quest_fini"));
                            constant.setTotQuest(RecupTotQuest.getInt("TotQuest"));
                            TextView textViewPseudo = (TextView) findViewById(R.id.pseudo);
                            textViewPseudo.setText(constant.getIdentifiant());

                            RestAdapter adapter = new RestAdapter.Builder()
                                    .setEndpoint(ROOT_URL) //Setting the Root URL
                                    .build(); //Finally building the adapter
                            SelectAPI Select = adapter.create(SelectAPI.class);

                            Select.ListLevel(
                                    "ListLevel",
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
                                                    //Log.e("log_tag", "Level: " + itemobj.getInt("Id_Level") + ", Xp: " + itemobj.getInt("Xp_Level"));
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
                                                TextView textViewQFini = (TextView) findViewById(R.id.qFinis);
                                                TextView textViewLevel = (TextView) findViewById(R.id.Level);
                                                TextView textViewXp = (TextView) findViewById(R.id.xp);
                                                textViewXp.setText(constant.getXp() + "/" + constant.getXpLevel() + "Xp");
                                                textViewLevel.setText(constant.getLevel() + "/" + constant.getQtLevel() + "lvl");
                                                textViewQFini.setText(constant.getqFini() + "/" + constant.getTotQuest() + "Aventures");

                                            } catch (JSONException e) {
                                                Log.e("log_tag", "Erreur dans le parsing des data : " + e.toString());
                                            }
                                        }

                                        @Override
                                        public void failure(RetrofitError error) {
                                            Log.e("log_tag", "Se suis une erreur : ");
                                        }
                                    });

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                Refresh_Location();
                break;
            default:
                break;
        }
    }

    void Refresh_Location(){
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET}
                        ,10);
            }
            return;
        }
        // this code won't execute IF permissions are not allowed, because in the line above there is return statement.
                locationManager.requestLocationUpdates("gps", 5000, 0, listener);



    }
}

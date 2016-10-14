package com.example.tefa.projetmaheu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AcceuilActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener{

    private EditText LoginText;
    private EditText PasswordText;
    private Button button;
    final Context context = this;
    Constant constant = new Constant();



    public static final String ROOT_URL = "http://37.187.104.237:88/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acceuil);

        //Initialisation des champs de saisie avec text
        LoginText = (EditText) findViewById(R.id.LoginText);
        LoginText.addTextChangedListener(this);
        PasswordText = (EditText) findViewById(R.id.PasswordText);
        PasswordText.addTextChangedListener(this);

        //Initialisation des boutons
        Button button = (Button) findViewById(R.id.connection);
        button.setOnClickListener(this);
        Button btnInscription = (Button) findViewById(R.id.inscription);
        btnInscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inscriptionIntent = new Intent(AcceuilActivity.this, Inscription.class);
                startActivity(inscriptionIntent);
            }
        });

    }

    public void loginUser() {

        //Here we will handle the http request to insert user to mysql db
        //Creating a RestAdapter
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(ROOT_URL) //Setting the Root URL
                .build(); //Finally building the adapter
        String keys = "identification";
        //Creating object for our interface
        SelectAPI api = adapter.create(SelectAPI.class);

        //Defining the method insertuser of our interface
        api.loginUser(

                LoginText.getText().toString().toLowerCase(),
                PasswordText.getText().toString(),
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
                            }
                            catch (Exception e){
                                Log.e("log_tag", "Erreur dans la conversion du résultat : " + e.toString());
                            }

                        try{
                            JSONArray jArray = new JSONArray(resultat);
                            JSONObject json_data = jArray.getJSONObject(0);

                            if (json_data.getString("Validation").equals("nok")){
                                AlertMessage mdpInco = new AlertMessage("Erreur", "Votre Identifiant ou mdp est incorrect", context);
                                mdpInco.test();
                            }else  if (json_data.getString("Validation").equals("0")) {
                                AlertMessage compteNActive = new AlertMessage("Erreur", "Votre compte n'a pas eté activé", context);
                                compteNActive.test();
                            }
                            else{
                                    //Ouverture de la page principale
                                Intent pagePrincip = new Intent(AcceuilActivity.this, PagePrincipale.class);
                                Bundle objetbunble = new Bundle();
                                objetbunble.putInt("id",json_data.getInt("Id"));
                                //constant.setId(json_data.getInt("Id"));
                                //InfoJoueur infoJoueur = new InfoJoueur(json_data.getInt("Id"),constant);
                                //infoJoueur.RefreshInfo(json_data.getInt("Id"));
                                pagePrincip.putExtras(objetbunble);
                                startActivity(pagePrincip);


                           }
                        }catch(JSONException e){
                            Log.e("log_tag", "Erreur dans le parsing des data : " + e.toString());
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        //If any error occured displaying the error as toast
                        Toast.makeText(AcceuilActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        );
    }


    @Override
    public void onClick(View v){

        //Onclick Connection
        Pattern pPattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,}$");
        Matcher pMatcher = pPattern.matcher(PasswordText.getText().toString());

        //Verification des identifiants et password non vide

        if (pMatcher.matches()) {
            loginUser();
        }
        else {
            AlertMessage passIncorrect = new AlertMessage("Erreur",
                    "Le mot de passe doit au moins contenir une Majuscule, une minuscule, un chiffre et doit contenir au moins 6 caractère", context);
            passIncorrect.test();
        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }


    @Override
    public void onTextChanged(CharSequence charSeq, int arg1, int arg2, int arg3) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

}

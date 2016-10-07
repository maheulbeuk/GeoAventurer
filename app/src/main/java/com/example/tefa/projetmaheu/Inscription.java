package com.example.tefa.projetmaheu;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.*;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Tefa on 05/10/2016.
 */

public class Inscription extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    //Declaration des vues
    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextEmail;
    private EditText editTextPassword2;
    private Button buttonRegister;


    //root URL
    public static final String ROOT_URL = "http://37.187.104.237:88/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.inscription);

        //Initialisations des vue
        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextUsername.addTextChangedListener(this);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextPassword.addTextChangedListener(this);
        editTextPassword2 = (EditText) findViewById(R.id.editTextPassword2);
        editTextPassword2.addTextChangedListener(this);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextEmail.addTextChangedListener(this);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);

        //Ajout du listener au bouton
        buttonRegister.setOnClickListener(this);
    }


    private void insertUser(){

        //Here we will handle the http request to insert user to mysql db
        //Creating a RestAdapter
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(ROOT_URL) //Setting the Root URL
                .build(); //Finally building the adapter
        String keys = "inscriptions";
        //Creating object for our interface
        RegisterAPI api = adapter.create(RegisterAPI.class);

        //Defining the method insertuser of our interface
        api.insertUser(

                //Passing the values by getting it from editTexts
                editTextUsername.getText().toString(),
                editTextPassword.getText().toString(),
                editTextEmail.getText().toString(),
                keys,

                //Creating an anonymous callback
                new Callback<Response>() {
                    @Override
                    public void success(Response result, Response response) {
                        //On success we will read the server's output using bufferedreader
                        //Creating a bufferedreader object
                        BufferedReader reader = null;

                        //An string to store output from the server
                        String output = "";

                        try {
                            //Initializing buffered reader
                            reader = new BufferedReader(new InputStreamReader(result.getBody().in()));

                            //Reading the output in the string
                            output = reader.readLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        //Displaying the output as a toast
                        Toast.makeText(Inscription.this, output, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        //If any error occured displaying the error as toast
                        Toast.makeText(Inscription.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    //Overriding onclick method
    @Override
    public void onClick(View v) {

        Pattern mPattern = Pattern.compile("^[a-zA-Z0-9._-]+@[a-z0-9._-]{2,}\\.[a-z]{2,4}$");
        Matcher mMatcher = mPattern.matcher(editTextEmail.getText().toString());
        Pattern pPattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,}$");
        Matcher pMatcher = pPattern.matcher(editTextPassword.getText().toString());
        final Context context = this;

        if (pMatcher.matches()) {
            if (editTextPassword.getText().toString().equals(editTextPassword2.getText().toString())) {

                if (mMatcher.matches()) {
                    //Calling insertUser on button click
                    insertUser();
                } else {
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);

                    // set title
                    alertDialogBuilder.setTitle("Erreur!");

                    // set dialog message
                    alertDialogBuilder
                            .setMessage("Veuillez saisir un email valide!")
                            .setCancelable(true)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }

                            });

                    // create alert dialog
                    android.app.AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();
                }

            } else {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);

                // set title
                alertDialogBuilder.setTitle("Erreur!");

                // set dialog message
                alertDialogBuilder
                        .setMessage("Le mot de passe est différent, veuillez confirmez correctement le mot de passe !")
                        .setCancelable(true)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }

                        });

                // create alert dialog
                android.app.AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        }
        else {
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);

            // Choix du titre du message d'erreur
            alertDialogBuilder.setTitle("Erreur!");

            // Choix du message d'erreur
            alertDialogBuilder
                    .setMessage("Le mot de passe doit au moins contenir une Majuscule, une minuscule, un chiffre et doit contenir au moins 6 caractère")
                    .setCancelable(true)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }

                    });

            // creation de alert dialogue
            android.app.AlertDialog alertDialog = alertDialogBuilder.create();

            // affichage
            alertDialog.show();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
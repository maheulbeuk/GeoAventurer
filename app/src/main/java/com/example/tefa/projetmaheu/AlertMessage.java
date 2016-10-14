package com.example.tefa.projetmaheu;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

/**
 * Created by Tefa on 07/10/2016.
 */

public class AlertMessage {
    //Défininition des variables de classes
    private String title;
    private String message;
    private Context context;

    //Accesseurs et Mutateurs
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    //Constructeurs
    public AlertMessage(String pTitle, String pMessage, Context pContext) {

        setTitle(pTitle);
        setMessage(pMessage);
        setContext(pContext);
    }

    public void test() {
        Log.i(this.title, this.message);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

        // Choix du titre du message d'erreur
        alertDialogBuilder.setTitle(this.title);

        // Choix du message d'erreur
        alertDialogBuilder
                .setMessage(this.message)
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

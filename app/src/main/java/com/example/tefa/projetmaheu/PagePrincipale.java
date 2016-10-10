package com.example.tefa.projetmaheu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by Tefa on 07/10/2016.
 */

public class PagePrincipale extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_principale);
        Bundle extras = new Bundle();
        int Id = extras.getInt("id");
        String Identifiant = extras.getString("Identifiant");
        int Xp = extras.getInt("xp");
        int Quest_fini =extras.getInt("quest_fini");
        Log.e("log_tag", "Id: " + Id +
                ", Identifiant: " + Identifiant + ", Xp:" + Xp
                + ", Quest_fini:" +Quest_fini
        );


        }

}

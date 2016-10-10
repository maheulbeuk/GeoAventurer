package com.example.tefa.projetmaheu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by Tefa on 07/10/2016.
 */

public class PagePrincipale extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_principale);
        Bundle extras  = this.getIntent().getExtras();

        int Id = extras.getInt("id");
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

}

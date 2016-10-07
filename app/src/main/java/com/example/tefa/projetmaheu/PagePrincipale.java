package com.example.tefa.projetmaheu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Tefa on 07/10/2016.
 */

public class PagePrincipale extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_principale);

        final Intent intent;
        String login = intent.getStringExtra(AcceuilActivity.identifiants);

        }
    }
}

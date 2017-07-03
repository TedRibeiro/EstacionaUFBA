package com.matc89.estacionaufba.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.matc89.estacionaufba.R;
import com.matc89.estacionaufba.meta.EstacionaUFBAConfigurations;

/**
 * Created by tedri on 01/07/2017.
 */

public class LandingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Verificando se existe um usuário logado
        SharedPreferences sharedPreferences = getSharedPreferences(EstacionaUFBAConfigurations.CONFIGURATION, 0);
        Long loggedUser = sharedPreferences.getLong(EstacionaUFBAConfigurations.CONFIGURATION_LOGGED_USER, 0);
        if (loggedUser > 0) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        //Abrindo activity e colocando em tela cheia
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_landing);
    }


}

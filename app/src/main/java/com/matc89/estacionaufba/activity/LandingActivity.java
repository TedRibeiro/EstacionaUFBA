package com.matc89.estacionaufba.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaCodec;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.matc89.estacionaufba.R;
import com.matc89.estacionaufba.db.DatabaseHandler;
import com.matc89.estacionaufba.db.vo.User;
import com.matc89.estacionaufba.fragment.RegisterFragment;
import com.matc89.estacionaufba.meta.EstacionaUFBAConfigurations;
import com.matc89.estacionaufba.meta.EstacionaUFBAFunctions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public void onClick(View v) {
        DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
        databaseHandler.open();

        switch (v.getId()) {
            //Botão de efetuar login
            case R.id.button_login_login:

                //Capturando componentes
                EditText editTextLoginEmail = (EditText) findViewById(R.id.editText_login_email);
                EditText editTextLoginPassword = (EditText) findViewById(R.id.editText_login_password);

                //Capturando valores dos componentes
                String loginEmail = editTextLoginEmail.getText().toString().trim();
                String loginPassword = editTextLoginPassword.getText().toString().trim();

                // TODO Validar campos (máximo de caracteres)
                //Validando campos vazios
                if (loginEmail.trim().length() == 0) {
                    editTextLoginEmail.requestFocus();
                    editTextLoginEmail.setError(getApplicationContext().getString(R.string.required_field));
                } else if (loginPassword.trim().length() == 0) {
                    editTextLoginPassword.requestFocus();
                    editTextLoginPassword.setError(getApplicationContext().getString(R.string.required_field));
                } else {
                    //Verificando usuário existente
                    // TODO Tratamento das strings para evitar SQL injection
                    User loginUser = databaseHandler.getUserDAO().fetchUserBy(loginEmail, loginPassword);
                    if (loginUser != null) {
                        //Avisando que o login obteve sucesso
                        Toast.makeText(this, getApplicationContext().getString(R.string.login_successful), Toast
                                .LENGTH_SHORT).show();

                        //Colocando nas configurações o usuário recém logado
                        SharedPreferences sharedPreferences = getSharedPreferences(EstacionaUFBAConfigurations.CONFIGURATION,
                                0);
                        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
                        sharedPreferencesEditor.putLong(EstacionaUFBAConfigurations.CONFIGURATION_LOGGED_USER, loginUser
                                .getId());
                        sharedPreferencesEditor.commit();

                        //Abrindo o app de fato
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, getApplicationContext().getString(R.string.login_error), Toast
                                .LENGTH_SHORT).show();
                    }
                }
                break;

            //Botão de cadastre-se agora
            case R.id.button_login_registernow:
                RegisterFragment registerFragment = new RegisterFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_landing, registerFragment);
                transaction.addToBackStack(null); //Se não colocar isso, ele não volta para a activity que chamou
                // essa fragment!
                transaction.commit();
                break;

            //Botão de efetuar cadastro
            case R.id.button_register_register:
                //Capturando componentes
                EditText editTextRegisterName = (EditText) findViewById(R.id.editText_register_name);
                EditText editTextRegisterEmail = (EditText) findViewById(R.id.editText_register_email);
                EditText editTextRegisterPassword = (EditText) findViewById(R.id.editText_register_password);
                EditText editTextRegisterPlacaCarro = (EditText) findViewById(R.id.editText_register_placa_carro);

                Pattern emailPattern = Pattern.compile("([A-Za-z0-9.\\-_])+@([A-Za-z0-9-])+(\\.([a-zA-Z])+)+");

                //Capturando valores dos componentes
                String registerName = editTextRegisterName.getText().toString().trim();
                String registerEmail = editTextRegisterEmail.getText().toString().trim();
                String registerPassword = editTextRegisterPassword.getText().toString().trim();
                String registerBirthday = editTextRegisterPlacaCarro.getText().toString().trim();

                // TODO Validar campos (e-mail válido, campos com tamanho até o máximo, data de nascimento a partir
                // de 18 anos...)
                //Validando campos vazios

                boolean error = false;

                if (registerName.length() == 0) {
                    editTextRegisterName.requestFocus();
                    editTextRegisterName.setError(getApplicationContext().getString(R.string.required_field));
                    error = true;
                }
                if (registerEmail.length() == 0) {
                    editTextRegisterEmail.requestFocus();
                    editTextRegisterEmail.setError(getApplicationContext().getString(R.string.required_field));
                    error = true;
                } else if (databaseHandler.getUserDAO().fetchUserByEmail(registerEmail) != null) {
                    editTextRegisterEmail.requestFocus();
                    editTextRegisterEmail.setError(getApplicationContext().getString(R.string.existe_usuario));
                    error = true;
                } else if(!emailPattern.matcher(registerEmail).matches()){
                    editTextRegisterEmail.requestFocus();
                    editTextRegisterEmail.setError("E-mail inválido");
                    error = true;
                }
                if (registerPassword.length() == 0) {
                    editTextRegisterPassword.requestFocus();
                    editTextRegisterPassword.setError(getApplicationContext().getString(R.string.required_field));
                    error = true;
                }
                if (registerBirthday.length() == 0) {
                    editTextRegisterPlacaCarro.requestFocus();
                    editTextRegisterPlacaCarro.setError(getApplicationContext().getString(R.string.required_field));
                    error = true;
                }

                if(error){
                    break;
                }

                //Cadastrando usuário
                // TODO Tratamento das strings para evitar SQL injection
                User user = new User(registerName, registerEmail, registerPassword, registerBirthday, 1,
                        EstacionaUFBAFunctions.getCurrentDateTime(), null);
                if (databaseHandler.getUserDAO().create(user)) {
                    //Avisando que o cadastro obteve sucesso
                    Toast.makeText(this, getApplicationContext().getString(R.string.user_register_successful),
                            Toast.LENGTH_SHORT).show();

                    //Colocando nas configurações o usuário recém cadastrado
                    SharedPreferences sharedPreferences = getSharedPreferences(EstacionaUFBAConfigurations.CONFIGURATION,
                            0);
                    SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
                    sharedPreferencesEditor.putLong(EstacionaUFBAConfigurations.CONFIGURATION_LOGGED_USER, user.getId());
                    sharedPreferencesEditor.commit();

                    //Abrindo o app de fato
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, getApplicationContext().getString(R.string.user_register_error), Toast
                            .LENGTH_SHORT).show();
                }
                break;
        }
        databaseHandler.close();
    }

/*    @Override
    public void onDestroy() {
        // TODO Melhorar encerramento do aplicativo. Desta forma o usuário vê por um instante a tela do Koope.
        System.exit(1);
    }*/
}

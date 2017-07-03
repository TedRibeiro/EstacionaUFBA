package com.matc89.estacionaufba.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.matc89.estacionaufba.R;
import com.matc89.estacionaufba.db.DatabaseHandler;
import com.matc89.estacionaufba.db.vo.Ocorrencia;
import com.matc89.estacionaufba.db.vo.User;
import com.matc89.estacionaufba.fragment.ListaOcorrenciasFragment;
import com.matc89.estacionaufba.fragment.MinhaContaFragment;
import com.matc89.estacionaufba.fragment.NovaOcorrenciaFragment;
import com.matc89.estacionaufba.fragment.NovaOcorrenciaFragment;
import com.matc89.estacionaufba.interfaces.IUserSchema;
import com.matc89.estacionaufba.interfaces.OnOcorrenciaInteractionListener;
import com.matc89.estacionaufba.meta.EstacionaUFBAConfigurations;

/**
 * Created by tedri on 01/07/2017.
 */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        OnOcorrenciaInteractionListener {

    private Toolbar toolbar = null;
    private NavigationView navigationView = null;
    private boolean resetFragmentOnBackPressed = false;

    //VOs
    private User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Abrindo activity e banco de dados
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //carrega o framgment inicial sem permitir voltar
        carregaFragmentInicial();
        //carregaFragmentInicial();

        //Instanciando toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Instanciando menu
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string
                .navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Verificando se existe um usuário logado
        SharedPreferences sharedPreferences = getSharedPreferences(EstacionaUFBAConfigurations.CONFIGURATION, 0);
        Long loggedUser = sharedPreferences.getLong(EstacionaUFBAConfigurations.CONFIGURATION_LOGGED_USER, 0);
        user = DatabaseHandler.userDao.fetchById(loggedUser);
        View headerView = navigationView.getHeaderView(0);
        TextView textView_menu_name = (TextView) headerView.findViewById(R.id.textView_menu_name);
        textView_menu_name.setText(user.getNome());
        TextView textView_menu_email = (TextView) headerView.findViewById(R.id.textView_menu_email);
        textView_menu_email.setText(user.getEmail());
    }

    public void onClick(View v) {
        switch (v.getId()) {
            //Botão de atualizar minha conta
            case R.id.button_minha_conta_update:
                //Capturando componentes
                EditText editTextMinhaContaNome = (EditText) findViewById(R.id.editText_minha_conta_nome);
                EditText editTextMinhaContaSenha = (EditText) findViewById(R.id.editText_minha_conta_senha);
                EditText editTextMinhaContaEmail = (EditText) findViewById(R.id.editText_minha_conta_email);
                EditText editTextMinhaContaPlacaCarro = (EditText) findViewById(R.id.editText_minha_conta_placa_carro);

                String minhaContaNome = editTextMinhaContaNome.getText().toString().trim();
                String minhaContaEmail = editTextMinhaContaEmail.getText().toString().trim();
                String minhaContaSenha = editTextMinhaContaSenha.getText().toString().trim();
                String minhaContaPlacaCarro = editTextMinhaContaPlacaCarro.getText().toString().trim();

                // TODO Validar campos (máximo de caracteres)
                //Validando campos vazios
                if (minhaContaNome.length() == 0) {
                    editTextMinhaContaNome.requestFocus();
                    editTextMinhaContaNome.setError(getApplicationContext().getString(R.string.campo_obrigatorio));
                } else if (minhaContaEmail.length() == 0) {
                    editTextMinhaContaEmail.requestFocus();
                    editTextMinhaContaEmail.setError(getApplicationContext().getString(R.string.campo_obrigatorio));
                } else if (!minhaContaEmail.equals(user.getEmail()) && DatabaseHandler.userDao.fetchUserByEmail
                        (minhaContaEmail) != null) {
                    editTextMinhaContaEmail.requestFocus();
                    editTextMinhaContaEmail.setError(getApplicationContext().getString(R.string.existe_usuario));
                } else {
                    //Avisando que a atualização obteve sucesso
                    Toast.makeText(this, getApplicationContext().getString(R.string.atualizacao_sucedida), Toast
                            .LENGTH_SHORT).show();

                    //Atualizando as informações na VO do usuário logado
                    user.setNome(minhaContaNome);
                    user.setEmail(minhaContaEmail);
                    user.setPlacaCarro(minhaContaPlacaCarro);

                    //Alterando a senha se tiver sido digitada
                    if (minhaContaSenha.length() > 0) {
                        user.setPassword(minhaContaSenha);
                    }

                    //Atualizando de fato o usuário
                    DatabaseHandler.userDao.update(user);

                    //Alterando as informações do usuário logado no menu
                    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                    View headerView = navigationView.getHeaderView(0);
                    TextView textView_menu_name = (TextView) headerView.findViewById(R.id.textView_menu_name);
                    textView_menu_name.setText(user.getNome());
                    TextView textView_menu_email = (TextView) headerView.findViewById(R.id.textView_menu_email);
                    textView_menu_email.setText(user.getEmail());

                    //"Pressionando" o botão de voltar
                    super.onBackPressed();
                }
                break;

            //Botão de fazer logout
            case R.id.button_minha_conta_logout:
                //Tirando o usuário logado das configurações
                SharedPreferences sharedPreferences = getSharedPreferences(EstacionaUFBAConfigurations.CONFIGURATION, 0);
                SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
                sharedPreferencesEditor.putLong(EstacionaUFBAConfigurations.CONFIGURATION_LOGGED_USER, 0);
                sharedPreferencesEditor.apply();

                //Voltando para a tela de login
                Intent intent = new Intent(this, LandingActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (resetFragmentOnBackPressed) {
            resetFragmentOnBackPressed = false;
            int count = getSupportFragmentManager().getBackStackEntryCount();
            for (int i = 0; i < count; ++i) {
                getSupportFragmentManager().popBackStack();
            }
            carregaFragmentInicial();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //Esquema de transição de fragments no menu
        Fragment currentFragment = null;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        int id = item.getItemId();
        if (id == R.id.item_todas_ocorrencias) {
            currentFragment = ListaOcorrenciasFragment.newInstance();
        } else if (id == R.id.item_nova_ocorrencia) {
            currentFragment = NovaOcorrenciaFragment.newInstance(user.getId());
        } else if (id == R.id.item_minhas_ocorrencias) {
            currentFragment = ListaOcorrenciasFragment.newInstance(user.getId());
        } else if (id == R.id.item_minha_conta) {
            Bundle args = new Bundle();
            currentFragment = new MinhaContaFragment();
            args.putString(IUserSchema.COLUMN_NOME, user.getNome());
            args.putString(IUserSchema.COLUMN_EMAIL, user.getEmail());
            args.putString(IUserSchema.COLUMN_PLACA_CARRO, user.getPlacaCarro());
            currentFragment.setArguments(args);
        }
        transaction.replace(R.id.fragment_container, currentFragment);
        transaction.addToBackStack(null);
        transaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void mostrarOcorrencia(Ocorrencia ocorrencia) {

    }

    @Override
    public void editarOcorrencia(Ocorrencia ocorrencia) {

    }

    @Override
    public void deletarOcorrencia(Ocorrencia ocorrencia) {

    }

    @Override
    public void atualizarOcorrencia(Ocorrencia ocorrencia) {

    }

    public void replaceFragmentTo(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    private void carregaFragmentInicial() {
        ListaOcorrenciasFragment fragment = ListaOcorrenciasFragment.newInstance();
        Bundle args = new Bundle();

        args.putBoolean("dummy", true);
        fragment.setArguments(args);
        replaceFragmentTo(fragment, false);
    }
}

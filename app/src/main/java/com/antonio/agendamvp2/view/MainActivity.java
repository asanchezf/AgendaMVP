package com.antonio.agendamvp2.view;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.antonio.agendamvp2.R;
import com.antonio.agendamvp2.adaptadores.AdaptadorRecyclerViewSearch;
import com.antonio.agendamvp2.model.Contactos;
import com.antonio.agendamvp2.presenter.MainPresenter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainMvpView,AdaptadorRecyclerViewSearch.OnItemClickListener, android.widget.SearchView.OnQueryTextListener, SearchView.OnQueryTextListener, MenuItemCompat.OnActionExpandListener{


    //CONSTANTES PARA EL MODO FORMULARIO Y PARA LOS TIPOS DE LLAMADA.============================
    public static final String C_MODO = "modo";
    public static final int C_VISUALIZAR = 551;
    public static final int C_CREAR = 552;
    public static final int C_EDITAR = 553;
    public static final int C_ELIMINAR = 554;
    private static final int SOLICITUD_ACCESS_READ_CONTACTS = 1;//Para control de permisos en Android M o superior e importar contactos
    private static final int SOLICITUD_ACCESS_CALL_PHONE = 2;//Para control de permisos en Android M o superior y poder realizar llamadas
    //FIN CONSTANTES==============================================================================

    private RecyclerView lista;

    //private AdaptadorRecyclerView3 adaptador;

    private FloatingActionButton btnFab;
    private CollapsingToolbarLayout ctlLayout;

    /*private SQLControlador dbConnection;//CONTIENE LAS CONEXIONES A BBDD (CREADA EN DBHELPER.CLASS) Y LOS M�TODOS INSERT, UPDATE, DELETE, BUSCAR....
    ArrayList<Contactos> contactos;
    private adaptadores.AdaptadorRecyclerViewSearch adaptadorBuscador;
*/
    private static long back_pressed;//Contador para cerrar la app al pulsar dos veces seguidas el btón de cerrar. Se gestiona en el evento onBackPressed

    private static int index = -1;
    private static int top = -1;
    private LinearLayoutManager llmanager;
    private SearchView searchView;
    private int id_Contacto_Llamada = 0;//Para llamar a los contactos de la agenda.

    private ProgressBar barraProgreso;
    private Handler manejador = new Handler();//Manejador del hilo

    private MainPresenter presenter;
    private List<Contactos> contactosview;
    private AdaptadorRecyclerViewSearch adaptadorBuscador;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //App bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("Mi Aplicación");
        // Configuración del RecyclerView-----------------------------
        lista = findViewById(R.id.lstLista);
        lista.setLayoutManager(new LinearLayoutManager(this));

        lista.setItemAnimator(new DefaultItemAnimator());//Animación por defecto....

        ctlLayout = (CollapsingToolbarLayout) findViewById(R.id.ctlLayout);
        ctlLayout.setTitle("Mi Agenda");

        presenter=new MainPresenter(this);

    }

    @Override
    public Context dameContexto() {
        return this;
    }

    @Override
    public void devuelveMessage(int stringId) {

    }

    @Override
    public void devuelveDatos(List<Contactos> contactosPresenter) {

        contactosview=contactosPresenter;
        adaptadorBuscador = new AdaptadorRecyclerViewSearch((ArrayList<Contactos>) contactosview, this, dameContexto());//Implementa el adapatador: pasamos ahora tres parámetros....
        lista.setAdapter(adaptadorBuscador);
        adaptadorBuscador.notifyDataSetChanged();
    }

    @Override
    public void importarContactos() {


        presenter.importarContactosPresenter(dameContexto(),(ArrayList<Contactos>) contactosview);
        adaptadorBuscador.notifyDataSetChanged();





     /*   ImportarContactos importarContactos=new ImportarContactos(dameContexto(),(ArrayList<Contactos>) contactosview);
        importarContactos.importar();
        presenter.consultar();*/

    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return false;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onClick(RecyclerView.ViewHolder holder, int idPromocion, View v) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        //SE IMPLEMENTA EL MENÚ BUSCAR. Se añaden a la clase dos interfaces y se implmenta sus métodos más abajo...


        // MenuItem searchItem = menu.findItem(R.id.action_search);
        MenuItem searchItem = menu.findItem(R.id.buscar);
        //SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint(getResources().getString(R.string.buscar_en_searchview));

        //Personalizamos con color y tamaño de letra
        SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setHintTextColor(getResources().getColor(android.R.color.white));
        searchAutoComplete.setTextSize(16);


        //searchView.setSubmitButtonEnabled(true);


        searchView.setOnQueryTextListener(this);


        // LISTENER PARA LA APERTURA Y CIERRE DEL WIDGET
        //MenuItemCompat.setOnActionExpandListener(searchItem, this);
        //FIN IMPLEMENTACION DEL MENU BUSCAR

        /*searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adaptadorBuscador.getFilter().filter(newText);
                return false;
            }
        });*/


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_traer_contactos) {
            //MIGRAR DATOS DE LA AGENDA DE ANDROID=========================
           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //permisosPorAplicacion(id, 1);
            } else
            {

                //ABRE NUEVA ACTIVITY CON TAMAÑO ALERTDIALOG--FUNCIONA CORRECTAMENTE
                  /*  Intent i = new Intent(this, ImportarContactos.class);
                    startActivity(i);*/

                importarContactos();
            }

            return true;
        }

        if (id == R.id.menu_borrar_todos) {

            //borrarTodos();

            return true;
        }

        if (id == R.id.menu_borrar_algunos) {

            /*Intent intent = new Intent(MainActivity.this, BorrarUsuarios.class);
            intent.putExtra(C_MODO, C_ELIMINAR);
            startActivityForResult(intent, C_ELIMINAR);*/


            return true;
        }


        return super.onOptionsItemSelected(item);
    }


}

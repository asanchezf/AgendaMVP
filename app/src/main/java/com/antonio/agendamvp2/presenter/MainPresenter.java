package com.antonio.agendamvp2.presenter;

import android.content.Context;

import com.antonio.agendamvp2.model.Contactos;
import com.antonio.agendamvp2.model.MainMvpModel;
import com.antonio.agendamvp2.model.MvpModel;
import com.antonio.agendamvp2.view.MainMvpView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;




public class MainPresenter implements Presenter<MainMvpView> {
    private MainMvpView vista;
    private ArrayList<Contactos> contactos;
    private MvpModel modelo;

    public MainPresenter(MainMvpView vista) {
        this.vista = vista;
        consultar();
        modelo=new MainMvpModel(vista.dameContexto());
        //modelo.BuscarTodos();
    }

    @Override
    public void consultar() {
        //ES EL PRIMER MÉTODO LLAMADO QUE ACCEDE A LA BB.DD DONDE SE ENCUENTRAN LOS REGISTROS.
        //SI LA BB.DD NO EXISTE SE CREARÁ. SI YA EXISTE LA DEVUELVE SEGÚN EL MODO EN QUE LLAMEMOS: EXCRITURA O LECTURA.
        //AL INSTALAR LA APP ES AQUÍ DONDE REALMENTE SE CREA PQ LA CLASE DBhelper QUE ES LA ENCARGADA DE CREAR LA BB.DD
        //SE INSTANCIA DESDE LA CLASE SQLcontrolador DISTINGUIENDO SI LLAMA A ONCREATE O A ONUPGRADE.. PARA GESTIONAR LAS
        //VERSIONES DE LA bb.dd.

        contactos=new ArrayList<Contactos>();


        modelo = new MainMvpModel(vista.dameContexto());
        try {
            modelo.abrirBaseDeDatos(1);//Modo lectura
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }// Lectura. Solo para ver


        contactos = modelo.BuscarTodos();// llamamos a BuscarTodos() que devuelve un arraylist de contactos...+
        modelo.cerrar();

        vista.devuelveDatos(contactos);



    }

    @Override
    public void adjuntaVista(MainMvpView view) {

    }

    @Override
    public void separaVista() {

    }



    @Override
    public List<Contactos> traeContactos() {


        contactos=new ArrayList<Contactos>();


        modelo = new MainMvpModel(vista.dameContexto());
        try {
            modelo.abrirBaseDeDatos(1);//Modo lectura
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }// Lectura. Solo para ver


        contactos = modelo.BuscarTodos();// llamamos a BuscarTodos() que devuelve un arraylist de contactos...+



    /*    adaptadorBuscador = new adaptadores.AdaptadorRecyclerViewSearch(contactos, this, this);//Implementa el adapatador: pasamos ahora tres parámetros....
        lista.setAdapter(adaptadorBuscador);
        adaptadorBuscador.notifyDataSetChanged();*/

        //modelo.cerrar();

        return contactos;

    }

    @Override
    public void importarContactosPresenter(Context context, ArrayList<Contactos> contactos) {
        /*ImportarContactos importarContactos=new ImportarContactos(context,(ArrayList<Contactos>) contactos);
        importarContactos.importar();*/
        modelo.importarContactosModelo(context,contactos);
        //vista.devuelveDatos(contactos);
    }
}

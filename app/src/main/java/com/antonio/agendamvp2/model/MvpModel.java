package com.antonio.agendamvp2.model;

import android.content.Context;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Usuario on 24/01/2018.
 */

public interface MvpModel {

    MainMvpModel abrirBaseDeDatos(int opcion) throws SQLException;
    void cerrar();
    ArrayList <Contactos>BuscarTodos();
    void importarContactosModelo(Context context, ArrayList<Contactos> contactos);

}

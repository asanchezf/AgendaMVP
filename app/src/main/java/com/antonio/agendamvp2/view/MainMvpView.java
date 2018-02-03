package com.antonio.agendamvp2.view;

import com.antonio.agendamvp2.model.Contactos;

import java.util.List;



/**
 * Created by Usuario on 24/01/2018.
 */

public interface MainMvpView extends MvpView {

    void devuelveMessage(int stringId);
    void devuelveDatos(List<Contactos> contactos);
    void importarContactos();

}

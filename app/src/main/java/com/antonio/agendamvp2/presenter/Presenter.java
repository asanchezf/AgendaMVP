package com.antonio.agendamvp2.presenter;

import android.content.Context;

import com.antonio.agendamvp2.model.Contactos;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Usuario on 24/01/2018.
 */

public interface Presenter<V> {

        void adjuntaVista(V view);
        void separaVista();

        void consultar();
        List<Contactos> traeContactos();
        void importarContactosPresenter(Context context, ArrayList<Contactos> contactos);


}

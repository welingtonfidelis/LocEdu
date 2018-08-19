package com.example.welington.locedu.Controller;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

/**
 * Created by welington on 15/08/18.
 */

public class Util {

    public static int  posicaoSpinner(String nome){
        switch (nome){
            case "Minicurso":
                return 0;

            case "Palestra":
                return 1;

            case "Oficina":
                return  2;

        }
        return 0;
    }
}

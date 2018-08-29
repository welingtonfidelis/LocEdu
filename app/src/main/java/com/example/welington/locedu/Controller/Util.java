package com.example.welington.locedu.Controller;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.widget.Toast;

import com.example.welington.locedu.R;

/**
 * Created by welington on 15/08/18.
 */

public class Util {

    public static int  posicaoTipoEvento(String nome){
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

    public static int posicaoBloco(String nome){
        switch (nome){
            case "A":
                return 0;

            case "B":
                return 1;

            case "C":
                return 2;

            case "D":
                return 3;

             case "E":
                return 4;
        }
        return 0;
    }

    public static void exibeAlerta(final String  chave, final String tabela, final Activity context) {
        final Boolean resposta;
        AlertDialog alerta;
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            vibrator.vibrate(VibrationEffect.createOneShot(500,VibrationEffect.DEFAULT_AMPLITUDE));
        }
        else{
            vibrator.vibrate(500);
        }
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //define o titulo
        builder.setTitle("ALERTA");
        //define a mensagem
        builder.setMessage("Deletar informação?");
        //define um botão como positivo
        builder.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                ReferencesHelper.getDatabaseReference().child(tabela).child(chave).removeValue();
                Toast.makeText(context, "Informação deletada.", Toast.LENGTH_SHORT).show();
                context.finish();
            }
        });
        //define um botão como negativo.
        builder.setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                return;
            }
        });
        //cria o AlertDialog
        alerta = builder.create();
        //Exibe
        alerta.show();
    }

}

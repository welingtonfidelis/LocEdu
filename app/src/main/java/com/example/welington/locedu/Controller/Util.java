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

    public static class SimpleDialog extends DialogFragment implements DialogInterface.OnClickListener {

        private static final
        String EXTRA_ID      = "id";
        private static final
        String EXTRA_MESSAGE = "message";
        private static final
        String EXTRA_TITLE   = "title";
        private static final
        String EXTRA_BUTTONS = "buttons";
        private static final
        String DIALOG_TAG    = "SimpleDialog";

        private int dialogId;

        public static SimpleDialog newDialog(int id,
                                             String title, String message, int[] buttonTexts){
            // Usando o Bundle para salvar o estado
            Bundle bundle  = new Bundle();
            bundle.putInt(EXTRA_ID, id);
            bundle.putString(EXTRA_TITLE, title);
            bundle.putString(EXTRA_MESSAGE, message);
            bundle.putIntArray(EXTRA_BUTTONS, buttonTexts);

            SimpleDialog dialog = new SimpleDialog();
            dialog.setArguments(bundle);
            return dialog;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            String title = getArguments()
                    .getString(EXTRA_TITLE);
            String message = getArguments()
                    .getString(EXTRA_MESSAGE);
            int[] buttons = getArguments()
                    .getIntArray(EXTRA_BUTTONS);

            AlertDialog.Builder alertDialogBuilder =
                    new AlertDialog.Builder(getActivity());
            alertDialogBuilder.setTitle(title);
            alertDialogBuilder.setMessage(message);

            switch (buttons.length) {
                case 3:
                    alertDialogBuilder.setNeutralButton(
                            buttons[2], this);

                case 2:
                    alertDialogBuilder.setNegativeButton(
                            buttons[1], this);

                case 1:
                    alertDialogBuilder.setPositiveButton(
                            buttons[0], this);
            }
            return alertDialogBuilder.create();
        }

        @Override
        public void onClick(
                DialogInterface dialog, int which) {
            // Sua Activity deve implementar essa interface
            ((FragmentDialogInterface)getActivity())
                    .onClick(dialogId, which);
        }

        public void openDialog(
                FragmentManager supportFragmentManager) {

            if (supportFragmentManager.findFragmentByTag(
                    DIALOG_TAG) == null){

                show(supportFragmentManager, DIALOG_TAG);
            }
        }
        // Interface que erá chamada ao clicar no bot"ao
        public interface FragmentDialogInterface {
            void onClick(int id, int which);
        }
    }
}

package com.msa.timetracker;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class PopupDialogFragment extends DialogFragment {
    String dialogMessage;
    Context applicationContext;

    public PopupDialogFragment(String dialogMessage, Context applicationContext) {
        this.dialogMessage = dialogMessage;
        this.applicationContext = applicationContext;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(dialogMessage)
                .setPositiveButton("OK!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        showToast("Great...");
                    }
                })
                .setNegativeButton("Not OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        showToast(" :( ");
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    private void showToast(String toastMessage) {
        Toast.makeText(applicationContext, toastMessage, Toast.LENGTH_SHORT).show();
    }
}
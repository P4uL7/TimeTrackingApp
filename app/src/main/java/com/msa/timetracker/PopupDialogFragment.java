package com.msa.timetracker;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

public class PopupDialogFragment extends DialogFragment {
    private String dialogMessage;
    private Context applicationContext;
    private String negativeButton, positiveButton;

    PopupDialogFragment(String dialogMessage, Context applicationContext, String negativeButton, String positiveButton) {
        this.dialogMessage = dialogMessage;
        this.applicationContext = applicationContext;
        this.negativeButton = negativeButton;
        this.positiveButton = positiveButton;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        builder.setMessage(dialogMessage)
                .setPositiveButton(positiveButton, (dialog, id) -> {
                    showToast("Great...");

                    if (dialogMessage.equals("Are you sure you wish to exit?")) {
                        System.exit(-1);
                    }
                })
                .setNegativeButton(negativeButton, (dialog, id) -> showToast(" :( "));
        // Create the AlertDialog object and return it
        return builder.create();
    }

    private void showToast(String toastMessage) {
        Toast.makeText(applicationContext, toastMessage, Toast.LENGTH_SHORT).show();
    }
}
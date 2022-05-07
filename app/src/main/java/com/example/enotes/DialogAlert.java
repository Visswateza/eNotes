package com.example.enotes;

import android.annotation.SuppressLint;
        import android.app.AlertDialog;
        import android.app.Dialog;
        import android.content.DialogInterface;
        import android.os.Bundle;

        import androidx.appcompat.app.AppCompatDialogFragment;

public class DialogAlert extends AppCompatDialogFragment {

    public String message = "OOPS Something was wrong!";
    public String title = "Alert!";

    DialogAlert(String title, String message){
        this.message = message;
        this.title = title;
    }

    @SuppressLint("ResourceType")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(this.title)
                .setMessage(this.message)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        return builder.create();
    }
}

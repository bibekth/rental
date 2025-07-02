package com.example.rental;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class LoadingAlert {
    private AlertDialog alertDialog;
    private TextView textView;

    public LoadingAlert(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_layout, null);
        builder.setView(view);

        textView = view.findViewById(R.id.textView); // Ensure this ID matches your dialog_layout.xml

        alertDialog = builder.create();
    }

    public void setMessage(String message) {
        if (textView != null) {
            textView.setText(message);
        }
    }

    public void show() {
        if (alertDialog != null && !alertDialog.isShowing()) {
            alertDialog.show();
        }
    }

    public void dismiss() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }
}

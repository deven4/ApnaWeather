package com.example.theweathermate.Utils;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.theweathermate.R;

public class AlertDialogBox extends DialogFragment {

    String message;
    dialogListener mListener;

    public AlertDialogBox(String message, dialogListener mListener) {
        this.message = message;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_box);

        TextView textView = dialog.findViewById(R.id.textView8);
        textView.setText(message);

        dialog.findViewById(R.id.okBtn).setOnClickListener(v -> {
            mListener.okBtn();
            dialog.dismiss();
        });
        return dialog;
    }

    public interface dialogListener {
        void okBtn();
    }
}

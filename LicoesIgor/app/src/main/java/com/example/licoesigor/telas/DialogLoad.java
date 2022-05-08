package com.example.licoesigor.telas;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.licoesigor.R;

public class DialogLoad extends Dialog {

    Context context;

    public DialogLoad(@NonNull Context context) {
        super(context);

        this.context = context;
        carrega("Carregando...");
    }

    public DialogLoad(@NonNull Context context, String texto) {
        super(context);

        this.context = context;
        carrega(texto);
    }

    TextView lblTexto;

    private void carrega(String texto)
    {
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_load);
        setCancelable(true);

        ImageView img = findViewById(R.id.img);
        Glide.with(context).asGif().load(R.drawable.load).into(img);

        TextView lblTexto = findViewById(R.id.lblTexto);
        lblTexto.setText(texto);
    }

    public void atualizaTexto(String texto)
    {
        if (lblTexto != null)
        {
            lblTexto.setText(texto);
        }
    }
}

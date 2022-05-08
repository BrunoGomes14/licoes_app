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

public class DialogErro extends Dialog {


    public DialogErro(@NonNull Context context, String mensagem) {
        super(context);

        carrega(mensagem);
    }

    private void carrega(String texto)
    {
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_erro);
        setCancelable(true);

        ImageView img = findViewById(R.id.img);
        Glide.with(getContext()).asGif().load(R.drawable.error).into(img);

        TextView lblTexto = findViewById(R.id.lblTexto);
        lblTexto.setText(texto);
    }
}

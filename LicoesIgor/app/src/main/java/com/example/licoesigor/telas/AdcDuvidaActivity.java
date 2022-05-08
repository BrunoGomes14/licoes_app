package com.example.licoesigor.telas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.licoesigor.R;
import com.example.licoesigor.model.modelImagem;

public class AdcDuvidaActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView imgDuvida;
    EditText txtDuvida;
    Button btnSalvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adc_duvida);

        carregaControles();
        carregaDados();
    }

    public void carregaControles()
    {
        imgDuvida = findViewById(R.id.imgDuvida);
        txtDuvida = findViewById(R.id.txtDuvida);
        btnSalvar = findViewById(R.id.btnSalvar);

        btnSalvar.setOnClickListener(this);
    }

    byte[] byteImgs;

    public void carregaDados()
    {
        Intent intent = getIntent();
        //Bitmap btm = (Bitmap) intent.getParcelableExtra("imgDuvida");

        Bundle bData = intent.getExtras();

        byteImgs = bData.getByteArray("imgDuvida");
        Bitmap btm = BitmapFactory.decodeByteArray(byteImgs, 0, byteImgs.length);

        imgDuvida.setImageBitmap(btm);
        txtDuvida.setText(bData.getString("sDuvida", ""));
    }

    @Override
    public void onClick(View v)
    {
        if (btnSalvar.getId() == v.getId())
        {

            modelImagem img = new modelImagem();
            img.isDuvida = 1;
            img.ImgBt = byteImgs;

            Intent intent = new Intent();
            intent.putExtra("mDuvida", img);

            setResult(RESULT_OK, intent);
            finish();
        }
    }
}

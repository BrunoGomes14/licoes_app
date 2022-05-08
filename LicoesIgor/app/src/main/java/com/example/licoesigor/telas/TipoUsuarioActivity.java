package com.example.licoesigor.telas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.licoesigor.R;
import com.example.licoesigor.model.ErrorModel;
import com.example.licoesigor.model.modelUsuario;
import com.example.licoesigor.servidor.EnviaDados;
import com.example.licoesigor.servidor.responseCallback;
import com.example.licoesigor.tipoUsuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class TipoUsuarioActivity extends AppCompatActivity implements View.OnClickListener {

    EditText txtNome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipo_usuario);

        findViewById(R.id.btnAluno).setOnClickListener(this);
        findViewById(R.id.btnAdm).setOnClickListener(this);
        txtNome = findViewById(R.id.txtNome);
    }

    @Override
    public void onClick(View v) {

        final int tp_user;

        if (txtNome.getText().toString().trim().isEmpty())
            return;


        if (v.getId() == R.id.btnAdm){
            tp_user = tipoUsuario.TIPO_ADM;
        } else {
            tp_user = tipoUsuario.TIPO_ALUNO;
        }

        DialogLoad load = new DialogLoad(this, "Enviando dados\nao servidor....");
        load.show();

        responseCallback callback = new responseCallback() {
            @Override
            public void responseSucess(Object response) {
                load.dismiss();
                tipoUsuario.insereTipoUsuario(TipoUsuarioActivity.this, tp_user);
                Intent intent = new Intent(TipoUsuarioActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void responseError(Object response) {
                ErrorModel error = (ErrorModel)response;
                DialogErro dlgErro = new DialogErro(TipoUsuarioActivity.this, error.getMensagemErro());
                dlgErro.show();
                load.dismiss();
            }
        };

        FirebaseInstanceId instance = FirebaseInstanceId.getInstance();

        instance.getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {

                modelUsuario usuario = new modelUsuario();
                usuario.dsChaveFirebase = task.getResult().getToken();
                usuario.dsTipoUsuario = tp_user;
                usuario.nmUsuario = txtNome.getText().toString().trim();

                EnviaDados api = new EnviaDados();
                api.enviaUsuario(usuario, callback);

            }
        });


    }
}

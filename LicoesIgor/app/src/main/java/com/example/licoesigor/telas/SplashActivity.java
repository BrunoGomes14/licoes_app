package com.example.licoesigor.telas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.licoesigor.R;
import com.example.licoesigor.model.modelUsuario;
import com.example.licoesigor.servidor.EnviaDados;
import com.example.licoesigor.servidor.responseCallback;
import com.example.licoesigor.tipoUsuario;
import com.example.licoesigor.ultil.ServicoHistorico;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class SplashActivity extends AppCompatActivity {

    private ServicoHistorico servicoHistorico = new ServicoHistorico();
    Intent intentServico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //tipoUsuario.insereTipoUsuario(this, tipoUsuario.TIPO_ALUNO);
        intentServico = new Intent(this, servicoHistorico.getClass());
    }

    @Override
    protected void onResume() {
        super.onResume();

        abreActivity();
    }

    public void abreActivity()
    {
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

                responseCallback callback = new responseCallback() {
                    @Override
                    public void responseSucess(Object response) {
                        Class activityAbrir;

                        if (tipoUsuario.possuiUsuario(SplashActivity.this)) {
                            activityAbrir = MainActivity.class;
                        } else {
                            activityAbrir = TipoUsuarioActivity.class;
                        }

                        if (!servicoRodando(servicoHistorico.getClass()))
                        {
                            // inicia o servico de histórico
                            intentServico.setAction("LIGA");
                            //startService(intentServico);
                        }

                        Intent intent = new Intent(SplashActivity.this, activityAbrir);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void responseError(Object response) {
                        DialogErro dlg = new DialogErro(SplashActivity.this, "Comunicação com servidor indisponível");
                        dlg.show();

                        dlg.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                finish();
                            }
                        });
                    }
                };

                EnviaDados dados = new EnviaDados();
                dados.getInicial(callback);
            }

        }, 3000);
    }

    private boolean servicoRodando(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}

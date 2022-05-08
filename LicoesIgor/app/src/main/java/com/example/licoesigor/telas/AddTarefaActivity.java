package com.example.licoesigor.telas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.example.licoesigor.R;
import com.example.licoesigor.model.ErrorModel;
import com.example.licoesigor.model.modelLicao;
import com.example.licoesigor.servidor.EnviaDados;
import com.example.licoesigor.servidor.responseCallback;
import com.example.licoesigor.tipoUsuario;

import javax.xml.transform.Result;

public class AddTarefaActivity extends AppCompatActivity {

    EditText txtTitulo;
    EditText txtDetalhes;
    EditText txtDataEntrega;
    TextView lblTipo;
    ActionBar actionbar;
    Toolbar toolbar;

    int idTarefa = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tarefa);

        carregaControles();
        montaToolbar();
    }

    public void carregaControles()
    {
        idTarefa = getIntent().getExtras().getInt("id", 0);

        toolbar = findViewById(R.id.toolbar);
        txtTitulo = findViewById(R.id.txtTitulo);
        txtDetalhes = findViewById(R.id.txtDetalhes);
        txtDataEntrega = findViewById(R.id.txtDtEntrga);
        lblTipo = findViewById(R.id.lnlTipo);

        if (idTarefa > 0)
        {
            lblTipo.setText("Altere");

            modelLicao licaoRecebida = (modelLicao) getIntent().getSerializableExtra("obj");
            txtTitulo.setText(licaoRecebida.nmTarefa);
            txtDetalhes.setText(licaoRecebida.dsTarefa);

            String[] dataArray =  licaoRecebida.dtEntrega.split("-");
            String dataformatada = dataArray[2].substring(0, 2) + "/" + dataArray[1] + "/" + dataArray[0];

            txtDataEntrega.setText(dataformatada);
        }
    }

    public void montaToolbar()
    {
        setSupportActionBar(toolbar);
        actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(false);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_right);
        toolbar.setTitleTextColor(getColor(R.color.Branco));
        toolbar.setTitle("");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mnu_tarefa, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (!tipoUsuario.isAluno(this))
        {
            MenuItem itemMenu;

            itemMenu = menu.getItem(0);
            itemMenu.setVisible(false);

            itemMenu = menu.getItem(1);
            itemMenu.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

            menu.getItem(2).setVisible(true);
        }
        else
        {
            menu.getItem(0).setVisible(false);
            menu.getItem(1).setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
                break;
            case R.id.ConcluirTarefa:
                enviaDados();
                break;
        }
        return true;
    }

    public void enviaDados()
    {
        DialogLoad load = new DialogLoad(this, "Enviando tarefa\nao servidor...");
        load.show();

        if (txtTitulo.getText().toString().trim().isEmpty()
        || txtDetalhes.getText().toString().trim().isEmpty()
        || txtDataEntrega.getText().toString().trim().isEmpty())
        {
            return;
        }

        modelLicao licao = new modelLicao();
        licao.idTarefa = idTarefa;
        licao.nmTarefa = txtTitulo.getText().toString().trim();
        licao.dsTarefa = txtDetalhes.getText().toString().trim();
        licao.dtEntrega = txtDataEntrega.getText().toString().trim();
        licao.bEntregou = false;

        responseCallback callback = new responseCallback() {
            @Override
            public void responseSucess(Object response) {
                load.dismiss();
                Intent intent = new Intent();
                intent.putExtra("obj", intent);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void responseError(Object response) {
                load.dismiss();
                ErrorModel error = (ErrorModel)response;
                DialogErro dlgErro = new DialogErro(AddTarefaActivity.this, error.getMensagemErro());
                dlgErro.show();
            }
        };

        EnviaDados api = new EnviaDados();
        api.adicionaTarefa(licao, callback);
    }


}

package com.example.licoesigor.telas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.licoesigor.R;
import com.example.licoesigor.adapter.AdapterLicoes;
import com.example.licoesigor.model.ErrorModel;
import com.example.licoesigor.model.guardaLicaoModel;
import com.example.licoesigor.model.modelLicao;
import com.example.licoesigor.servidor.EnviaDados;
import com.example.licoesigor.servidor.responseCallback;
import com.example.licoesigor.tipoUsuario;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView rcvLicoes;
    Toolbar toolbar;
    ActionBar actionbar;
    SwipeRefreshLayout swpAtualiza;
    private final int CADASTRAR_TAREFA = 1;
    private final int ENTREGOU_TAREFA = 2;
    private final int HORARIOS = 3;

    boolean abriuActivity = false;
    boolean abriuPelaGaleria = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            recuperaControles();
            montaToolbar();
            carregaRecycle(false);
        }
        catch (Exception err) {
            String a = err.getMessage();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(!abriuActivity) {
            finish();
        } else {
            abriuActivity = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mnu_principal, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        boolean bTipoAluno = tipoUsuario.isAluno(this);

        if (bTipoAluno)
            menu.getItem(0).setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId)
        {
            case android.R.id.home:
                if (abriuPelaGaleria)
                    limpaExtra();
                    abriuPelaGaleria = false;
                break;
            case R.id.AddTarefa:
                if (!tipoUsuario.isAluno(this))
                {
                    Intent intent = new Intent(this, AddTarefaActivity.class);
                    intent.putExtra("id", 0);
                    abriuActivity = true;
                    startActivityForResult(intent, CADASTRAR_TAREFA);
                }
                break;
            case R.id.itemHorarios:
                Intent intenta = new Intent(this, MateriasSemanaActivity.class);
                abriuActivity = true;
                startActivityForResult(intenta, HORARIOS);
        }

        return true;
    }

    public void recuperaControles()
    {
        rcvLicoes = findViewById(R.id.rcvLicoes);
        toolbar = findViewById(R.id.toolbar);
        swpAtualiza = findViewById(R.id.swpAtualiza);
        swpAtualiza.setColorSchemeColors(getColor(R.color.colorPrimary));
        swpAtualiza.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                carregaRecycle(true);
            }
        });
    }

    public void montaToolbar()
    {
        setSupportActionBar(toolbar);
        actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(false);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_knowledge);
        toolbar.setTitleTextColor(getColor(R.color.Branco));
        toolbar.setTitle("Tarefas");
    }

    ArrayList<modelLicao> list;
    ArrayList<byte[]> imgsAdd = new ArrayList<>();

    private static final long  MEGABYTE = 1024L * 1024L;

    public void carregaRecycle(boolean isRefresh)
    {
//        Resources res = getResources();
//        Drawable drawable = getDrawable(R.drawable.testeimg);
//        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//        byte[] bitMapData = stream.toByteArray();
//
//        modelImagem mdi = new modelImagem();
//        mdi.IsDuvida = 1;
//        mdi.Img = bitMapData;
//
//        modelImagem mdi2 = new modelImagem();
//        mdi2.IsDuvida = 1;
//        mdi2.Img = bitMapData;
//
//        modelImagem mdi3 = new modelImagem();
//        mdi3.IsDuvida = 0;
//        mdi3.Img = bitMapData;
//
//        modelImagem mdi4 = new modelImagem();
//        mdi4.IsDuvida = 0;
//        mdi4.Img = bitMapData;
//
//        modelLicao licao1 = new modelLicao();
//        licao1.qtdDiaVencer = 0;
//        licao1.bEntregou = true;
//        licao1.dtEntrega = "27/10";
//        licao1.nmTarefa = "Lição inglês";
//        licao1.dsTarefa = "tarefa";
//        licao1.imgs = new ArrayList<>();
//        licao1.imgs.add(mdi);
//        licao1.imgs.add(mdi2);
//        licao1.imgs.add(mdi3);
//        licao1.imgs.add(mdi4);
//
//        modelLicao licao2 = new modelLicao();
//        licao2.qtdDiaVencer = 1;
//        licao2.bEntregou = true;
//        licao2.dsTarefa = "tarefa";
//        licao2.dtEntrega = "30/10";
//        licao2.nmTarefa = "Lição português";
//        licao2.imgs = new ArrayList<>();
//
//        modelLicao licao3 = new modelLicao();
//        licao3.qtdDiaVencer = 2;
//        licao3.bEntregou = false;
//        licao3.dsTarefa = "tarefa";
//        licao3.dtEntrega = "30/10";
//        licao3.nmTarefa = "Lição matemática";
//        licao3.imgs = new ArrayList<>();
//
//        modelLicao licao4 = new modelLicao();
//        licao4.qtdDiaVencer = 2;
//        licao4.bEntregou = true;
//        licao4.dsTarefa = "tarefa";
//        licao4.dtEntrega = "30/10";
//        licao4.nmTarefa = "Lição geografia";
//        licao4.imgs = new ArrayList<>();
//
//        list = new ArrayList<>();
//        list.add(licao1);
//        list.add(licao2);
//        list.add(licao3);
//        list.add(licao4);

        DialogLoad load = new DialogLoad(this, "Obtendo tarefas...");

        if (!isRefresh)
            load.show();

        responseCallback callback = new responseCallback() {
            @Override
            public void responseSucess(Object response) {

                load.dismiss();
                list = (ArrayList<modelLicao>)response;

                verificaExtras();

                AdapterLicoes adapter = new AdapterLicoes(MainActivity.this, list, AdapterLicoes.todas, imgsAdd.size() > 1);
                rcvLicoes.setAdapter(adapter);

                LinearLayoutManager manager = new LinearLayoutManager(MainActivity.this);
                rcvLicoes.setLayoutManager(manager);

                swpAtualiza.setRefreshing(false);
            }

            @Override
            public void responseError(Object response) {
                ErrorModel error = (ErrorModel)response;
                DialogErro dlgErro = new DialogErro(MainActivity.this, error.getMensagemErro());
                dlgErro.show();
                load.dismiss();

                swpAtualiza.setRefreshing(false);
            }
        };

        EnviaDados api = new EnviaDados();
        api.buscaTarefas(callback);

    }

    public void verificaExtras()
    {
        rcvLicoes.setEnabled(false);

        if (Intent.ACTION_SEND.equals(getIntent().getAction()) && getIntent().getType() != null)
        {
            Uri imageUri = (Uri) getIntent().getParcelableExtra(Intent.EXTRA_STREAM);
            byte[] bt = convertImageToByte(imageUri);
            imgsAdd.add(bt);
        }
        else if (Intent.ACTION_SEND_MULTIPLE.equals(getIntent().getAction()) && getIntent().getType() != null)
        {
            ArrayList<Uri> imageUris = getIntent().getParcelableArrayListExtra(Intent.EXTRA_STREAM);
            if (imageUris != null) {

                for (Uri img: imageUris)
                {
                    byte[] btImg = convertImageToByte(img);
                    imgsAdd.add(btImg);
                }
            }
        }

        if (imgsAdd.size() > 0)
        {
            abriuPelaGaleria = true;
            String sFrase = imgsAdd.size() > 1 ? "as imagens" : "a imagem";

            actionbar.setHomeAsUpIndicator(R.drawable.ic_close);
            toolbar.setTitle("Para qual lição é " + sFrase);
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setDisplayShowHomeEnabled(true);
        }

        rcvLicoes.setEnabled(true);
    }

    public void limpaExtra()
    {
        imgsAdd.clear();
        getIntent().getExtras().clear();
        AdapterLicoes adapter = (AdapterLicoes)rcvLicoes.getAdapter();
        adapter.bEscondeDuvida = false;
        adapter.notifyDataSetChanged();

        actionbar.setHomeAsUpIndicator(R.drawable.ic_knowledge);
        actionbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Tarefas");
    }

    @Override
    public void onClick(View v) {

        int idView = v.getId();
        Intent intentGeral;

        if (idView == R.id.lnlPrincipal || idView == R.id.lnlDuvida || idView == R.id.lnlConcluir)
        {
            int posicao = (int)v.getTag();

            String tpEntrada = "";
            modelLicao licaoIntent = list.get(posicao);
            intentGeral = new Intent(MainActivity.this, LicaoActivity.class);
            guardaLicaoModel.licaoTransf = licaoIntent;
            //LicaoActivity.licao = licaoIntent;
            //intentGeral.putExtra("licao", licaoIntent);


            switch (idView)
            {
                case R.id.lnlDuvida:
                    tpEntrada = "isDuvida";
                    break;
                case R.id.lnlConcluir:
                    tpEntrada = "isLicao";
                    break;
                default:
                    break;
            }

            if (imgsAdd.size() > 0)
            {
                tpEntrada += "|add";
                intentGeral.putExtra("imgs", imgsAdd);
            }

            abriuActivity = true;
            intentGeral.putExtra("tpEntrada", tpEntrada);
            startActivityForResult(intentGeral, ENTREGOU_TAREFA);

            if (imgsAdd.size() > 0)
                limpaExtra();
        }
        else if (idView == R.id.lnlAlterar)
        {
            modelLicao licaoSelecionada = list.get((int)v.getTag());

            modelLicao licao = new modelLicao();
            licao.idTarefa = 5;
            licao.dtEntrega = licaoSelecionada.dtEntrega;
            licao.nmTarefa = licaoSelecionada.nmTarefa;
            licao.dsTarefa = licaoSelecionada.dsTarefa;

            Intent intent = new Intent(this, AddTarefaActivity.class);
            intent.putExtra("id", licao.idTarefa);
            intent.putExtra("obj", licao);
            abriuActivity = true;

            try{
                startActivityForResult(intent, CADASTRAR_TAREFA);
            }
            catch (Exception err)
            {
                String teste = err.getMessage();
            }
        }
    }

    public byte[] convertImageToByte(Uri uri){
        byte[] data = null;
        try {
            ContentResolver cr = getBaseContext().getContentResolver();
            InputStream inputStream = cr.openInputStream(uri);
            BitmapFactory.Options opc = new BitmapFactory.Options();
            opc.inSampleSize = 8;
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, opc);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            data = baos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return data;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == CADASTRAR_TAREFA || requestCode == ENTREGOU_TAREFA) && resultCode == RESULT_OK){
            carregaRecycle(false);
        }
    }
}

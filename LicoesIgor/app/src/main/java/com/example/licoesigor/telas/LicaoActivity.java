package com.example.licoesigor.telas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.licoesigor.R;
import com.example.licoesigor.adapter.AdapterDuvida;
import com.example.licoesigor.adapter.AdapterLicao;
import com.example.licoesigor.model.ErrorModel;
import com.example.licoesigor.model.guardaLicaoModel;
import com.example.licoesigor.model.modelImagem;
import com.example.licoesigor.model.modelLicao;
import com.example.licoesigor.servidor.EnviaDados;
import com.example.licoesigor.servidor.responseCallback;
import com.example.licoesigor.tipoUsuario;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import android.util.Base64;

public class LicaoActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView rcvDuvidas;
    RecyclerView rcvLicao;
    TextView lblTitulo;
    TextView lblDescricao;
    ImageView imgAddDuvida;
    ImageView imgAddLicao;
    Toolbar toolbar;
    public static modelLicao licao;

    Menu menu = null;

    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int RESULT_DUVIDA = 3;
    private static final int RESULT_LOAD_IMAGE_DUVIDA = 1;
    private static final int RESULT_LOAD_IMAGE_LICAO = 2;
    private static final int RESULT_ALTERACAO = 2;

    private final int CONVERTE_STRING_BYTE = 1;
    private final int CONVERTE_BYTE_STRING = 2;


    AdapterLicao licaoAdapterLicao;
    AdapterDuvida licaoAdapterDuvida;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_licao);


        try{

            carregaControles();
            carregaTela();
            montaToolbar();
            carregaTipoEntrada();
        }
        catch (Exception err)
        {
            Toast.makeText(this, err.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void montaToolbar()
    {
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(false);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_right);
        toolbar.setTitleTextColor(getColor(R.color.Branco));
        toolbar.setTitle("Detalhes da tarefa");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mnu_tarefa, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        this.menu = menu;
        boolean bTipoAluno = tipoUsuario.isAluno(this);;
        MenuItem item;

        if (bTipoAluno)
        {
            item = menu.getItem(0);
            item.setVisible(false);

            item = menu.getItem(1);
            item.setVisible(false);

            item = menu.getItem(2);
            item.setVisible(!licao.bEntregou);
        }
        else
        {
            item = menu.getItem(0);
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

            item = menu.getItem(1);
            item.setVisible(false);
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
            case R.id.AltTarefa:
                Intent intent = new Intent(this, AddTarefaActivity.class);
                intent.putExtra("id", licao.idTarefa);
                intent.putExtra("obj", licao);
                startActivityForResult(intent, 1);
                break;
        }

        return true;
    }

    public void carregaTipoEntrada()
    {
        Bundle bData = getIntent().getExtras();
        String tpEntrada = bData.getString("tpEntrada", "");

        switch (tpEntrada)
        {
            case "isDuvida":
                abrirGaleria(RESULT_LOAD_IMAGE_DUVIDA);
                break;
            case "isLicao":
                abrirGaleria(RESULT_LOAD_IMAGE_LICAO);
                break;
            case "isDuvida|add":
                adicionaImagens(RESULT_LOAD_IMAGE_DUVIDA);
                break;
            case "isLicao|add":
                adicionaImagens(RESULT_LOAD_IMAGE_LICAO);
                break;
        }
    }

    private void adicionaImagens(int aondeAddImg) {
        ArrayList<byte[]> imgs = (ArrayList<byte[]>) getIntent().getExtras().getSerializable("imgs");

        if (aondeAddImg == RESULT_LOAD_IMAGE_DUVIDA) {
            Intent intent = new Intent(this, AdcDuvidaActivity.class);
            intent.putExtra("imgDuvida", imgs.get(0));
            intent.putExtra("sDuvida", "");
            startActivityForResult(intent, RESULT_DUVIDA);
        }
        else{
            AdapterLicao adapter = (AdapterLicao)rcvLicao.getAdapter();

            for (byte[] img: imgs )
            {
                modelImagem imgMod = new modelImagem();
                imgMod.isDuvida = 0;
                imgMod.ImgBt = img;

                adapter.imagens.add(0, imgMod);
                adapter.notifyItemInserted(0);
                rcvLicao.smoothScrollToPosition(0);
            }
        }
    }

    public void carregaControles()
    {
        rcvDuvidas = findViewById(R.id.rcvDuvidas);
        rcvLicao = findViewById(R.id.rcvLicao);
        lblTitulo = findViewById(R.id.lblTitulo);
        lblDescricao = findViewById(R.id.lblDescricao);
        imgAddDuvida = findViewById(R.id.imgAddDuvida);
        imgAddLicao = findViewById(R.id.imgAddLicao);
        toolbar = findViewById(R.id.toolbar);

        imgAddLicao.setOnClickListener(this);
        imgAddDuvida.setOnClickListener(this);
    }

    public void carregaTela() throws Exception
    {

        //licao = (modelLicao) getIntent().getExtras().getSerializable("licao");
        licao = guardaLicaoModel.licaoTransf;
        licao = trataImgs(licao, CONVERTE_STRING_BYTE);

        String[] dataArray =  licao.dtEntrega.split("-");
        String dataformatada = dataArray[2].substring(0, 2) + "/" + dataArray[1] + "/" + dataArray[0];

        String fraseDesc = "Para: " + dataformatada;
        fraseDesc += "\n\n" + licao.dsTarefa;

        lblTitulo.setText(licao.nmTarefa);
        lblDescricao.setText(fraseDesc);

        licaoAdapterDuvida = new AdapterDuvida(this, licao.imgs, AdapterLicao.tipoDuvida, licao.bEntregou);
        licaoAdapterLicao = new AdapterLicao(this, licao.imgs, AdapterLicao.tipoLicao, licao.bEntregou);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager manager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        rcvDuvidas.setLayoutManager(manager);
        rcvDuvidas.setAdapter(licaoAdapterDuvida);

        rcvLicao.setLayoutManager(manager2);
        rcvLicao.setAdapter(licaoAdapterLicao);

        imgAddDuvida.setVisibility(licao.bEntregou ? View.GONE : View.VISIBLE);
        imgAddLicao.setVisibility(licao.bEntregou ? View.GONE : View.VISIBLE);

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == imgAddLicao.getId() || v.getId() == imgAddDuvida.getId())
        {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, MY_CAMERA_PERMISSION_CODE);
            }
            else
            {
                abrirGaleria(v.getId() == imgAddLicao.getId() ? RESULT_LOAD_IMAGE_LICAO : RESULT_LOAD_IMAGE_DUVIDA);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK)
        {
            if (requestCode == RESULT_LOAD_IMAGE_LICAO)
            {
                Uri imageUri = null;

                if (data.getClipData() != null)
                {
                    int count = data.getClipData().getItemCount();

                    for (int i = 0; i < count; i++)
                    {
                        imageUri = data.getClipData().getItemAt(i).getUri();
                        AdapterLicao adapter = (AdapterLicao)rcvLicao.getAdapter();

                        modelImagem img = new modelImagem();
                        img.isDuvida = 0;
                        img.ImgBt = convertImageToByte(imageUri);

                        adapter.imagens.add(0, img);
                        adapter.notifyItemInserted(0);
                        rcvLicao.smoothScrollToPosition(0);
                    }

                }
                else if (data.getData() != null) {

                    byte[] bImg = buscaImagem(data);

                    modelImagem img = new modelImagem();
                    img.isDuvida = 0;
                    img.ImgBt = bImg;

                    AdapterLicao adapter = (AdapterLicao)rcvLicao.getAdapter();
                    adapter.imagens.add(0, img);
                    adapter.notifyItemInserted(0);
                    rcvLicao.smoothScrollToPosition(0);
                }
            }
            else if (requestCode == RESULT_LOAD_IMAGE_DUVIDA)
            {
                Intent intent = new Intent(this, AdcDuvidaActivity.class);
                intent.putExtra("imgDuvida", buscaImagem(data));
                intent.putExtra("sDuvida", "");
                startActivityForResult(intent, RESULT_DUVIDA);
            }
            else if (requestCode == RESULT_DUVIDA)
            {
                modelImagem img = (modelImagem) data.getExtras().get("mDuvida");
                AdapterDuvida duvida = (AdapterDuvida)rcvDuvidas.getAdapter();
                duvida.imagens.add(0, img);
                duvida.notifyItemInserted(0);
                rcvDuvidas.smoothScrollToPosition(0);
            }
            else if (resultCode == RESULT_ALTERACAO){
                modelLicao licao = (modelLicao) data.getSerializableExtra("obj");

                String[] dataArray =  licao.dtEntrega.split("-");
                String dataformatada = dataArray[2].substring(0, 2) + "/" + dataArray[1] + "/" + dataArray[0];

                String fraseDesc = "Para: " + dataformatada;
                fraseDesc += "\n\n" + licao.dsTarefa;

                lblTitulo.setText(licao.nmTarefa);
                lblDescricao.setText(fraseDesc);
            }
        }
    }

    public byte[] convertImageToByte(Uri uri){
        byte[] data = null;
        try {
            ContentResolver cr = getBaseContext().getContentResolver();
            InputStream inputStream = cr.openInputStream(uri);
            BitmapFactory.Options opc = new BitmapFactory.Options();
            opc.inSampleSize = 5;
            opc.inJustDecodeBounds = false;
            opc.inPurgeable = true;
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, opc);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, baos);
            data = baos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return data;
    }

    public byte[] buscaImagem(Intent data)
    {
        Uri pickedImage = data.getData();

        String[] filePath = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
        cursor.moveToFirst();
        String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inSampleSize = 8;
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        cursor.close();
        return byteArray;
    }

    public void abrirGaleria(int constTipo) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");

        if (constTipo != RESULT_LOAD_IMAGE_DUVIDA)
          photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);

        startActivityForResult(photoPickerIntent, constTipo);
    }


    modelLicao licaoEnvio = new modelLicao();

    public void enviaDados()
    {
        DialogLoad load = new DialogLoad(this, "Tratando imagens...");
        load.show();

        licaoEnvio.idTarefa = licao.idTarefa;
        licaoEnvio.dsTarefa = licao.dsTarefa;
        licaoEnvio.imgs = new ArrayList<>();

        AdapterDuvida duvidas = (AdapterDuvida)rcvDuvidas.getAdapter();
        licaoEnvio.imgs.addAll(duvidas.imagens);

        AdapterLicao licaoAd = (AdapterLicao)rcvLicao.getAdapter();
        licaoEnvio.imgs.addAll(licaoAd.imagens);

        if (duvidas.imagens.size() == 0 && licaoAd.imagens.size() == 0){
            load.dismiss();
            return;
        }

        responseCallback callback = new responseCallback() {
            @Override
            public void responseSucess(Object response) {
                load.dismiss();
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void responseError(Object response) {
                ErrorModel error = (ErrorModel)response;
                DialogErro dlgErro = new DialogErro(LicaoActivity.this, error.getMensagemErro());
                dlgErro.show();
                load.dismiss();
            }
        };

        new Thread(new Runnable() {
            public void run() {
                licaoEnvio = trataImgs(licaoEnvio, CONVERTE_BYTE_STRING);

                load.atualizaTexto("Enviando tarefa\nao servidor");

                EnviaDados api = new EnviaDados();
                api.entregaTarefa(licaoEnvio, callback);
            }
        }).start();

    }

    public modelLicao trataImgs(modelLicao licao, int tp_conversao)
    {

        if (tp_conversao == CONVERTE_BYTE_STRING)
        {
            for (modelImagem img: licao.imgs) {
                String encrypted = Base64.encodeToString(img.ImgBt, Base64.DEFAULT);
                img.img = encrypted.replace("\n", "");
            }

            return licao;
        }
        else
        {
            for (modelImagem img: licao.imgs) {
                img.ImgBt = Base64.decode(img.img, Base64.DEFAULT);
            }

            return licao;
        }
    }
}

package com.example.licoesigor.telas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.licoesigor.R;
import com.example.licoesigor.adapter.AdapterHorarioMateria;
import com.example.licoesigor.model.ErrorModel;
import com.example.licoesigor.model.modelHorariosAula;
import com.example.licoesigor.servidor.EnviaDados;
import com.example.licoesigor.servidor.responseCallback;
import com.example.licoesigor.tipoUsuario;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Calendar;

public class MateriasSemanaActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener
{
    RecyclerView rcvMaterias = null;
    Toolbar toolbar = null;
    SwipeRefreshLayout swpAtualiza = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materias_semana);

        iniciaControles();
        montaToolbar();
        carregaDados();
    }

    public void iniciaControles()
    {
        rcvMaterias = findViewById(R.id.rcvMaterias);
        toolbar = findViewById(R.id.toolbar);
        swpAtualiza = findViewById(R.id.swpAtualiza);
        swpAtualiza.setOnRefreshListener(this);
        swpAtualiza.setColorSchemeColors(getColor(R.color.colorPrimary));
    }

    public void montaToolbar()
    {
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(false);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_right);
        toolbar.setTitleTextColor(getColor(R.color.Branco));
        toolbar.setTitle("Horários");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;
        }

        return true;
    }

    public void carregaDados()
    {
        EnviaDados enviaDados = new EnviaDados();
        DialogLoad load = new DialogLoad(this, "Obtendo horários...");

        load.show();

        responseCallback callback = new responseCallback() {
            @Override
            public void responseSucess(Object response) {
                swpAtualiza.setRefreshing(false);
                load.dismiss();

                ArrayList<modelHorariosAula> arrHorariosAulas = (ArrayList<modelHorariosAula>) response;

                LinearLayoutManager lnlManager = new LinearLayoutManager(MateriasSemanaActivity.this, RecyclerView.VERTICAL, false);
                AdapterHorarioMateria adapterHorarioMateria = new AdapterHorarioMateria(MateriasSemanaActivity.this, arrHorariosAulas);
                rcvMaterias.setLayoutManager(lnlManager);
                rcvMaterias.setAdapter(adapterHorarioMateria);

                for (modelHorariosAula horariosAula : arrHorariosAulas)
                {
                    if (horariosAula.isDia())
                    {
                        RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(MateriasSemanaActivity.this) {
                            @Override protected int getVerticalSnapPreference() {
                                return LinearSmoothScroller.SNAP_TO_START;
                            }
                        };

                        smoothScroller.setTargetPosition(arrHorariosAulas.indexOf(horariosAula));

                        lnlManager.startSmoothScroll(smoothScroller);
                        break;
                    }
                }
            }

            @Override
            public void responseError(Object response) {
                load.dismiss();
                swpAtualiza.setRefreshing(false);
                ErrorModel error = (ErrorModel)response;
                DialogErro dlgErro = new DialogErro(MateriasSemanaActivity.this, error.getMensagemErro());
                dlgErro.show();
            }
        };

        enviaDados.listaHorarios(callback);
    }

    @Override
    public void onRefresh() {
        carregaDados();
    }
}
package com.example.licoesigor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.licoesigor.R;
import com.example.licoesigor.model.modelHorariosAula;

import java.util.ArrayList;

public class AdapterHorarioMateria extends RecyclerView.Adapter<AdapterHorarioMateria.HolderHorarioAula>
{
    private Context context;
    private ArrayList<modelHorariosAula> arrHorariosAula = null;

    public AdapterHorarioMateria(Context contextParam, ArrayList<modelHorariosAula> arrHorariosAulaParam)
    {
        context =  contextParam;
        arrHorariosAula = arrHorariosAulaParam;
    }

    @NonNull
    @Override
    public AdapterHorarioMateria.HolderHorarioAula onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_horario_dia, parent, false);
        HolderHorarioAula holder = new HolderHorarioAula(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterHorarioMateria.HolderHorarioAula holder, int position)
    {
        modelHorariosAula horariosAula = arrHorariosAula.get(position);

        holder.lblDiaSemana.setText(horariosAula.sDia);

        AdapterMaterias adapterMaterias = new AdapterMaterias(context, horariosAula.arrMaterias);
        LinearLayoutManager lnlManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);

        holder.rcvMaterias.setLayoutManager(lnlManager);
        holder.rcvMaterias.setAdapter(adapterMaterias);

        if (!horariosAula.isDia())
        {
            holder.rtlPricipal.setBackgroundResource(0);
        }
    }

    @Override
    public int getItemCount() {
        return arrHorariosAula.size();
    }

    class HolderHorarioAula extends RecyclerView.ViewHolder
    {
        private final TextView lblDiaSemana;
        private final RecyclerView rcvMaterias;
        private final RelativeLayout rtlPricipal;

        public HolderHorarioAula(@NonNull View itemView)
        {
            super(itemView);

            rtlPricipal = itemView.findViewById(R.id.rtlPricipal);
            lblDiaSemana = itemView.findViewById(R.id.lblDiaSemana);
            rcvMaterias = itemView.findViewById(R.id.rcvMaterias);
        }
    }
}

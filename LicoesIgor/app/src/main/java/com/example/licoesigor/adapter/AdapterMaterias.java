package com.example.licoesigor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.licoesigor.R;
import com.example.licoesigor.model.modelMateria;
import java.util.ArrayList;

public class AdapterMaterias extends RecyclerView.Adapter<AdapterMaterias.HolderMateria>
{
    private Context context;
    private ArrayList<modelMateria> arrMaterias = null;

    public AdapterMaterias(Context contextParam, ArrayList<modelMateria> arrMateriasParam)
    {
        context = contextParam;
        arrMaterias = arrMateriasParam;
    }

    @NonNull
    @Override
    public AdapterMaterias.HolderMateria onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.item_materia, parent, false);
        HolderMateria holder = new HolderMateria(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterMaterias.HolderMateria holder, int position)
    {
        modelMateria materia = arrMaterias.get(position);

        holder.lnlMarcacao.setVisibility(materia.bPossuiLicao ? View.VISIBLE : View.GONE);
        holder.lblMateria.setText(materia.sMateria);
    }

    @Override
    public int getItemCount() {
        return arrMaterias.size();
    }

    class HolderMateria extends RecyclerView.ViewHolder
    {
        private final TextView lblMateria;
        private final LinearLayout lnlMarcacao;

        public HolderMateria(@NonNull View itemView)
        {
            super(itemView);

            lblMateria = itemView.findViewById(R.id.lblMateria);
            lnlMarcacao = itemView.findViewById(R.id.lnlMarcacao);
        }
    }
}

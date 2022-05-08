package com.example.licoesigor.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.licoesigor.R;
import com.example.licoesigor.model.modelLicao;
import com.example.licoesigor.telas.MainActivity;
import com.example.licoesigor.tipoUsuario;

import java.util.ArrayList;

public class AdapterLicoes extends RecyclerView.Adapter<AdapterLicoes.HolderLicoes>
{
    ArrayList<modelLicao> listLicoes = new ArrayList<>();
    Context context;
    MainActivity activity;
    int filtro;
    public boolean bEscondeDuvida;

    public static final int todas = 0;
    public static final int concluidas = 1;
    public static final int pendentes = 2;

    public AdapterLicoes(Context context, ArrayList<modelLicao> listLicoes, int filtro, boolean bEscondeDuvida)
    {
        this.listLicoes.addAll(listLicoes);
        this.context = context;
        this.filtro = filtro;
        this.bEscondeDuvida = bEscondeDuvida;
        activity = (MainActivity)context;
    }

    @NonNull
    @Override
    public HolderLicoes onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_lista, parent, false);
        HolderLicoes holder = new HolderLicoes(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HolderLicoes holder, int position) {
        modelLicao licao = listLicoes.get(position);

        int idImg = licao.qtdDiaVencer >= 2 ?
                R.drawable.exclamation :
                licao.qtdDiaVencer == 1 ? R.drawable.exclamation_atention : R.drawable.exclamation_red;

        // carrega glide
        Glide.with(context).load(idImg).into(holder.imgStatus);

        holder.lblNomeLicao.setText(licao.nmTarefa);

        if (licao.bEntregou)
        {
            holder.lblStatus.setText("Lição entregue");
            holder.lblStatus.setTextColor(Color.parseColor("#9AE29F"));
            // colocar cor verde acima

            holder.lnlBotoes.setVisibility(View.GONE);
            Glide.with(context).load(R.drawable.finished).into(holder.imgStatus);
        }
        else if (licao.qtdDiaVencer <= 0 && !licao.bEntregou)
        {
            holder.lblStatus.setTextColor(Color.parseColor("#EC4242"));
            // colocar cor vermelha acima
        }
        else if (licao.qtdDiaVencer == 1)
        {
            // adicionar cor amarela abaixo
            holder.lblStatus.setTextColor(Color.parseColor("#F0F063"));
        }
        else if (licao.qtdDiaVencer >= 2)
        {
            // adicionar cor padrao abaixo
            holder.lblStatus.setTextColor(Color.parseColor("#B4B4B4"));
        }

        String[] dataArray =  licao.dtEntrega.split("-");
        String dataformatada = dataArray[2].substring(0, 2) + "/" + dataArray[1] + "/" + dataArray[0];

        holder.lblStatus.setText("Para: " + dataformatada);

        int iPosicao = listLicoes.indexOf(licao);

        holder.lnlPrincipal.setTag(iPosicao);
        holder.lnlPrincipal.setOnClickListener(activity);

        holder.lnlDuvida.setOnClickListener(activity);
        holder.lnlDuvida.setTag(iPosicao);
        holder.lnlDuvida.setVisibility(bEscondeDuvida ? View.GONE : View.VISIBLE);

        holder.lnlConcluir.setOnClickListener(activity);
        holder.lnlConcluir.setTag(iPosicao);

        holder.lnlAlterar.setTag(iPosicao);
        holder.lnlAlterar.setOnClickListener(activity);

        if (!tipoUsuario.isAluno(context))
        {
            holder.lnlDuvida.setVisibility(View.GONE);
            holder.lnlConcluir.setVisibility(View.GONE);
            holder.lnlAlterar.setVisibility(View.VISIBLE);
            holder.lnlBotoes.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.lnlAlterar.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return listLicoes.size();
    }

    class HolderLicoes extends RecyclerView.ViewHolder{

        ImageView imgStatus;
        TextView lblNomeLicao;
        TextView lblStatus;
        LinearLayout lnlBotoes;
        LinearLayout lnlDuvida;
        LinearLayout lnlConcluir;
        LinearLayout lnlAlterar;
        LinearLayout lnlPrincipal;

        public HolderLicoes(@NonNull View itemView) {
            super(itemView);

            imgStatus = itemView.findViewById(R.id.imgStatus);
            lblNomeLicao = itemView.findViewById(R.id.lblNomeLicao);
            lblStatus = itemView.findViewById(R.id.lblStatus);
            lnlBotoes = itemView.findViewById(R.id.lnlBotoes);
            lnlDuvida = itemView.findViewById(R.id.lnlDuvida);
            lnlConcluir = itemView.findViewById(R.id.lnlConcluir);
            lnlPrincipal = itemView.findViewById(R.id.lnlPrincipal);
            lnlAlterar = itemView.findViewById(R.id.lnlAlterar);
        }
    }
}





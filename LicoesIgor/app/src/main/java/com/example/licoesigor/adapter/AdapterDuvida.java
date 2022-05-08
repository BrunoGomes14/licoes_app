package com.example.licoesigor.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.licoesigor.ImagemActivity;
import com.example.licoesigor.R;
import com.example.licoesigor.RoundedCornersTransformation;
import com.example.licoesigor.model.modelImagem;

import java.util.ArrayList;

public class AdapterDuvida extends RecyclerView.Adapter<AdapterDuvida.HolderDuvida>
{
    Context context;
    public static ArrayList<modelImagem> imagens = new ArrayList<>();

    public static int sCorner = 15;
    public static int sMargin = 7;
    public static int sBorder = 17;
    public static String sColor = "#BBBBBB";

    public static final int tipoDuvida = 1;
    public static final int tipoLicao = 2;

    int tipo = 0;
    int iPosicaoDuvida = 0;
    boolean bEntregou;

    public AdapterDuvida(Context context, ArrayList<modelImagem> imagens, int iTipo, boolean bEntregou)
    {
        this.context = context;

        this.imagens.clear();
        for (modelImagem img: imagens)
        {
            if (img.isDuvida == 1)
            {
                this.imagens.add(img);
            }
        }

        this.bEntregou = bEntregou;
        tipo = iTipo;
    }

    @NonNull
    @Override
    public HolderDuvida onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_imagem, parent, false);
        HolderDuvida holder = new HolderDuvida(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HolderDuvida holder, int position) {
        modelImagem itemImg =  imagens.get(position);

//        Glide.with(context).load(itemImg.Img)
//                .apply(RequestOptions.bitmapTransform(
//                        new RoundedCornersTransformation(this, sCorner, sMargin))).into(holder.img);

//        if (tipo == tipoDuvida && itemImg.IsDuvida == 0)
//        {
//            holder.rnlPrincipal.setVisibility(View.GONE);
//            return;
//        }

            // amarelo
            sColor = "#FDFB79";

            //String url = itemImg.IsDuvida == 1 ? "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcTLV4hWP_HyxSJKL_5Wm6pv65oTzopXF4VO-w&usqp=CAU"
            //       : "https://cdn.shopify.com/s/files/1/0001/9202/0527/products/quadros-democrart-gustavo-jacob-ondas-paisagem-de-baixa-luz-beleza-incontavel-galeria-de-arte-obras-de-arte_7ba14f0d-e0e9-4dc2-adc0-2121e3fa734e_1200x1200.jpg?v=1528209171";

            // Rounded corners with border
            Glide.with(context).load(itemImg.ImgBt)
                    .apply(RequestOptions.bitmapTransform(
                            new RoundedCornersTransformation(context, sCorner, sMargin, sColor, sBorder)))
                    .into(holder.img);

            holder.rnlPrincipal.setTag(imagens.indexOf(itemImg));
            holder.rnlPrincipal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    modelImagem img = imagens.get((int) v.getTag());

                    Intent tela = new Intent(context, ImagemActivity.class);
                    tela.putExtra("img", img.ImgBt);
                    context.startActivity(tela);
                }
            });
            holder.imgFechar.setTag(position);
            holder.imgFechar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    imagens.remove(position);

                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, getItemCount());
                }
            });

            holder.imgFechar.setVisibility(bEntregou ? View.GONE : View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return imagens.size();
    }

    class HolderDuvida extends RecyclerView.ViewHolder {

        public RelativeLayout rnlPrincipal;
        public ImageView img;
        public ImageView imgFechar;

        public HolderDuvida(@NonNull View itemView) {
            super(itemView);

            rnlPrincipal = itemView.findViewById(R.id.rnlPrincipal);
            img = itemView.findViewById(R.id.img);
            imgFechar = itemView.findViewById(R.id.imgFechar);

        }
    }
}

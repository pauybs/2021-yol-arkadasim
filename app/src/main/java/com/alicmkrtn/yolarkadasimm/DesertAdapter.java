package com.alicmkrtn.yolarkadasimm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class DesertAdapter extends RecyclerView.Adapter<DesertAdapter.ProductViewHolder> {
    Context mCtx;
    List<Dessert> productList;
    public DesertAdapter(Context mCtx, List<Dessert> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }
    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(mCtx).inflate(R.layout.custom_listwiew,
                parent, false);
        ProductViewHolder productViewHolder = new ProductViewHolder(view);
        return productViewHolder;
    }
    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Dessert product = productList.get(position);
        holder.txtad.setText(product.getad());
        holder.txtkalkis.setText(product.getkalkis());
        holder.txtvaris.setText(product.getvaris());
        holder.txtaciklama.setText(product.getaciklama());
        holder.txtfiyat.setText(product.getfiyat()+"₺");
        holder.txttarih.setText(product.gettarih());
        holder.txtid.setText(product.getid());
        Picasso.get().load(product.geturl()).into(holder.imageView);
        holder.setIsRecyclable(false);
    }
    @Override
    public int getItemCount() {
        return productList.size();
    }
    class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView txtad,txtkalkis,txtvaris,txtaciklama,txtfiyat,txttarih,txtid;
        public ProductViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgprofilresmi);
            txtad = itemView.findViewById(R.id.txtadsoyad);
            txtkalkis = itemView.findViewById(R.id.txtkalkis);
            txtvaris = itemView.findViewById(R.id.txtvaris);
            txtaciklama = itemView.findViewById(R.id.txtaciklama);
            txtfiyat = itemView.findViewById(R.id.txtfiyat);
            txttarih = itemView.findViewById(R.id.txttarih);
            txtid = itemView.findViewById(R.id.txtid);
        }
    }
}
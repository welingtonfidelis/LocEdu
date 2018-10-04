package com.example.welington.locedu.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.welington.locedu.Helper.ReferencesHelper;
import com.example.welington.locedu.Model.Setor;
import com.example.welington.locedu.R;
import com.example.welington.locedu.View.ListaLocal;
import com.example.welington.locedu.View.NovoSetor;
import com.google.gson.Gson;

import java.util.List;

public class SetorAdapterGrid extends RecyclerView.Adapter<SetorAdapterGrid.ViewHolder> {

    private Context context;
    private List<Setor> setores;
    private LayoutInflater mInflater;
    private String[] paletaCores = {"#cd298a", "#a9ab08", "#2f7967","#b10e0e", "#8947d8", "#b1500e","#0a9637", "#e6385d", "#4731fc","#4731fc", "#9812db", "#0a9637","#9a4677", "#ff6600", "#0a9637","#c8f737", "#c8f737", "#c8f737"};
   // private ItemClickListener itemClickListener;

    public SetorAdapterGrid(Context context, List<Setor> setores){
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.setores = setores;
    }


    @NonNull
    @Override
    public SetorAdapterGrid.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_setor_grid, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SetorAdapterGrid.ViewHolder holder, final int position) {
        holder.nomeSetor.setText(setores.get(position).getNomeSetor());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TESTE", setores.get(position).getNomeSetor());
                Gson gson = new Gson();
                Intent it = new Intent(context, ListaLocal.class);
                it.putExtra("SETOR", gson.toJson(setores.get(position)));
                context.startActivity(it);
            }
        });

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(ReferencesHelper.getFirebaseAuth().getCurrentUser()!=null){
                    Gson gson = new Gson();
                    Intent it = new Intent(context, NovoSetor.class);
                    it.putExtra("SETOR", gson.toJson(setores.get(position)));
                    context.startActivity(it);
                }
                return true;
            }
        });

        //setando cor do gridview
        holder.cardView.setBackgroundColor(Color.parseColor(paletaCores[position]));
    }

    @Override
    public int getItemCount() {
        return setores.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder /*implements View.OnClickListener*/{
        TextView nomeSetor;
        CardView cardView;

        ViewHolder(View itemView){
            super(itemView);
            nomeSetor = itemView.findViewById(R.id.info_text);
            cardView = itemView.findViewById(R.id.card_view);
        }

       /* @Override
        public void onClick(View v) {
            if (itemClickListener != null){

                itemClickListener.onItemClick(v, getAdapterPosition());
            }
        }*/
    }

    public Setor getItem(int id) {
        return setores.get(id);
    }

    // allows clicks events to be caught
 /*   public void setClickListener(ItemClickListener itemClickListener) {
        //this.itemClickListener = itemClickListener;
    }*/

    // parent activity will implement this method to respond to click events
   /* public interface ItemClickListener {
        void onItemClick(View view, int position);
    }*/
}

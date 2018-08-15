package com.example.welington.locedu.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.welington.locedu.Controller.ReferencesHelper;
import com.example.welington.locedu.Model.Setor;
import com.example.welington.locedu.R;
import com.example.welington.locedu.View.AlterarSetor;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by welington on 09/08/18.
 */

public class SetorAdapter extends RecyclerView.Adapter<SetorAdapter.ViewHolder> {

    private Context context;
    private List<Setor> setores;

    public SetorAdapter(Context context, List<Setor> setores) {
        this.context = context;
        this.setores = setores;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_setor, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Setor setor = setores.get(position);

        holder.adapterSetorNome.setText(setor.getNomeSetor());
        holder.adapterSetorBloco.setText(setor.getBloco());

        holder.adapterSetorCard.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(ReferencesHelper.getFirebaseAuth().getCurrentUser() != null) { //Verificando se adm está logado para dar opçao de alterar/deletar Setor
                    System.out.println("usuario logado");
                    Gson gson = new Gson();
                    Intent it = new Intent(context, AlterarSetor.class);
                    it.putExtra("SETOR", gson.toJson(setor));
                    context.startActivity(it);
                    //Toast.makeText(context, "Item :" + setor.getNomeSetor(), Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return setores.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        protected TextView adapterSetorNome;
        protected TextView adapterSetorBloco;
        protected CardView adapterSetorCard;

        public ViewHolder(final View itemView) {
            super(itemView);

            adapterSetorNome = itemView.findViewById(R.id.adapter_setor_nome);
            adapterSetorBloco = itemView.findViewById(R.id.adapter_setor_bloco);
            adapterSetorCard = itemView.findViewById(R.id.adapter_setor_card);
        }
    }

}

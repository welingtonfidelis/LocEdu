package com.example.welington.locedu.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.welington.locedu.Controller.ReferencesHelper;
import com.example.welington.locedu.Model.Setor;
import com.example.welington.locedu.R;
import com.example.welington.locedu.View.AlterarSetor;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by welington on 16/08/18.
 */

public class SetorAdapter2 extends BaseAdapter {

    private Context context;
    private List<Setor> setores;

    public SetorAdapter2(Context context, List<Setor> setores) {
        this.context = context;
        this.setores = setores;
    }

   // @Override
    public SetorAdapter2.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_setor_2, parent, false);
        return new ViewHolder(itemView);
    }

   // @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Setor setor = setores.get(position);

        holder.adapterSetorNome.setText(setor.getNomeSetor());
        holder.adapterSetorBloco.setText(setor.getBloco());

        holder.adapterSetorCard.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (ReferencesHelper.getFirebaseAuth().getCurrentUser() != null) { //Verificando se adm está logado para dar opçao de alterar/deletar Setor
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

    protected class ViewHolder extends RecyclerView.ViewHolder {

        protected TextView adapterSetorNome;
        protected TextView adapterSetorBloco;
        protected CardView adapterSetorCard;

        public ViewHolder(final View itemView) {
            super(itemView);

            adapterSetorNome = itemView.findViewById(R.id.tvSetor);
            adapterSetorBloco = itemView.findViewById(R.id.tvBloco);
            adapterSetorCard = itemView.findViewById(R.id.cvSet);
        }
    }

    @Override
    public int getCount() {
        return setores.size();
    }

    @Override
    public Object getItem(int position) {
        return setores.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return null;
    }
}
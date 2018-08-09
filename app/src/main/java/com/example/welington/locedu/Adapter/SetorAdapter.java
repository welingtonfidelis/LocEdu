package com.example.welington.locedu.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.welington.locedu.Model.Setor;
import com.example.welington.locedu.R;

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
        holder.adapterSetorResponsavel.setText(setor.getNomeResponsavel());
        holder.adapterSetorCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Item :" + setor.getNomeSetor(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return setores.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        protected TextView adapterSetorNome;
        protected TextView adapterSetorResponsavel;
        protected CardView adapterSetorCard;

        public ViewHolder(final View itemView) {
            super(itemView);

            adapterSetorNome = itemView.findViewById(R.id.adapter_setor_nome);
            adapterSetorResponsavel = itemView.findViewById(R.id.adapter_setor_responsavel);
            adapterSetorCard = itemView.findViewById(R.id.adapter_setor_card);
        }
    }

}

package com.example.welington.locedu.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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

/**
 * Created by welington on 09/08/18.
 */

public class SetorAdapterList extends RecyclerView.Adapter<SetorAdapterList.ViewHolder> {

    private Context context;
    private List<Setor> setores;

    public SetorAdapterList(Context context, List<Setor> setores) {
        this.context = context;
        this.setores = setores;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_setor_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Setor setor = setores.get(position);

        holder.adapterSetorNome.setText(setor.getNomeSetor());

        holder.adapterSetorCard.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(ReferencesHelper.getFirebaseAuth().getCurrentUser() != null) { //Verificando se adm está logado para dar opçao de alterar/deletar Setor
                    Gson gson = new Gson();
                    Intent it = new Intent(context, NovoSetor.class);
                    it.putExtra("SETOR", gson.toJson(setor));
                    context.startActivity(it);
                    //Toast.makeText(context, "Item :" + setor.getNomeSetor(), Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });

        holder.adapterSetorCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                Intent it = new Intent(context, ListaLocal.class);
                it.putExtra("SETOR", gson.toJson(setor));
                context.startActivity(it);
            }
        });
    }

    @Override
    public int getItemCount() {
        return setores.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        protected TextView adapterSetorNome;
        protected TextView adapterResposavel;
        protected TextView adapterSetorBloco;
        protected CardView adapterSetorCard;

        public ViewHolder(final View itemView) {
            super(itemView);

            adapterSetorNome = itemView.findViewById(R.id.adapter_setor_nome);
            adapterResposavel = itemView.findViewById(R.id.adapter_setor_responsavel);
            adapterSetorBloco = itemView.findViewById(R.id.adapter_setor_bloco);
            adapterSetorCard = itemView.findViewById(R.id.adapter_setor_card);
        }
    }

}

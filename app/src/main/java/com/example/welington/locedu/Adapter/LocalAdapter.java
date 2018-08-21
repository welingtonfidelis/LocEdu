package com.example.welington.locedu.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.welington.locedu.Controller.ReferencesHelper;
import com.example.welington.locedu.Model.Local;
import com.example.welington.locedu.R;
import com.example.welington.locedu.View.AlterarLocal;
import com.example.welington.locedu.View.ListaEvento;
import com.example.welington.locedu.View.PopUpInfoLocal;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by welington on 15/08/18.
 */

public class LocalAdapter  extends RecyclerView.Adapter<LocalAdapter.ViewHolder>{

    private Context context;
    private List<Local> locais;

    public LocalAdapter(Context context, List<Local> locais) {
        this.context = context;
        this.locais = locais;
    }

    protected class ViewHolder extends RecyclerView.ViewHolder{
        protected TextView adapterLocalNome;
        protected TextView adapterLocalResponsavel;
        protected CardView adapterLocalCard;
        protected TextView adapterQntEvento;
        protected ImageView adapterLocalInformacao;

        public ViewHolder(final View itemView){
            super(itemView);

            adapterLocalNome = itemView.findViewById(R.id.adapter_local_nome);
            adapterLocalResponsavel = itemView.findViewById(R.id.adapter_loca_responsavel);
            adapterLocalCard = itemView.findViewById(R.id.adapter_local_card);
            adapterQntEvento = itemView.findViewById(R.id.tvQntdEvento);
            adapterLocalInformacao = itemView.findViewById(R.id.imgv_informacao);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_local, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Local local = locais.get(position);

        ValueEventListener m = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                holder.adapterQntEvento.setText(String.valueOf(dataSnapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        ReferencesHelper.getDatabaseReference().child("Evento").orderByChild("localKey").equalTo(local.getKey()).addValueEventListener(m);

        holder.adapterLocalNome.setText(local.getNomeLocal());
        holder.adapterLocalResponsavel.setText(local.getNomeResponsavel());

        holder.adapterLocalCard.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                if(ReferencesHelper.getFirebaseAuth().getCurrentUser() != null){
                    Gson gson = new Gson();
                    Intent it = new Intent(context, AlterarLocal.class);
                    it.putExtra("LOCAL", gson.toJson(local));
                    context.startActivity(it);
                }
                //Toast.makeText(context, "Item :" + local.getLatitude() + " " +local.getLongitude(), Toast.LENGTH_LONG).show();
                return true;
            }
        });

        holder.adapterLocalCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson g = new Gson();
                Intent it = new Intent(context, ListaEvento.class);
                it.putExtra("LOCAL", g.toJson(local));
                context.startActivity(it);
            }
        });

        holder.adapterLocalInformacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson g = new Gson();
                Intent it = new Intent(context, PopUpInfoLocal.class);
                it.putExtra("LOCAL", g.toJson(local));
                context.startActivity(it);
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.e("Quantidade na LOCAIS", String.valueOf(locais.size()) );
        return locais.size();
    }

}

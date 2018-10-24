package br.com.welingtonfidelis.locedu.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import br.com.welingtonfidelis.locedu.Helper.ReferencesHelper;
import br.com.welingtonfidelis.locedu.Helper.Util;
import br.com.welingtonfidelis.locedu.Model.Local;
import br.com.welingtonfidelis.locedu.R;
import br.com.welingtonfidelis.locedu.View.ListaEvento;
import br.com.welingtonfidelis.locedu.View.NovoLocal;
import br.com.welingtonfidelis.locedu.View.PopUpListaMenu;
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

    protected class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView adapterLocalNome;
        protected CardView adapterLocalCard;
        protected TextView adapterQntEvento;
        protected FloatingActionButton abrirMenu;

        public ViewHolder(final View itemView){
            super(itemView);

            adapterLocalNome = itemView.findViewById(R.id.adapter_local_nome);
            adapterLocalCard = itemView.findViewById(R.id.adapter_local_card);
            adapterQntEvento = itemView.findViewById(R.id.tvQntdEvento);
            abrirMenu = itemView.findViewById(R.id.fb_menu);
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

        /*Util.contaEventos(local);
        holder.adapterQntEvento.setText(String.valueOf(local.getQntEvento()));*/

        ValueEventListener m = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                local.setQntEvento((int) dataSnapshot.getChildrenCount());
                holder.adapterQntEvento.setText(String.valueOf(local.getQntEvento()));
                Log.e("CONTADOR", local.getQntEvento()+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        ReferencesHelper.getDatabaseReference().child("Evento").orderByChild("localKey").equalTo(local.getKey()).addValueEventListener(m);


        holder.adapterLocalNome.setText(local.getNomeLocal());
        //holder.adapterLocalResponsavel.setText(local.getNomeResponsavel());

        holder.adapterLocalCard.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                if(ReferencesHelper.getFirebaseAuth().getCurrentUser() != null){
                    Gson gson = new Gson();
                    Intent it = new Intent(context, NovoLocal.class);
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
                if(local.getQntEvento()<=0 && ReferencesHelper.getFirebaseAuth().getCurrentUser() == null){
                    Toast.makeText(context, "Não há eventos disponíveis neste local.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Gson g = new Gson();
                    Intent it = new Intent(context, ListaEvento.class);
                    it.putExtra("LOCAL", g.toJson(local));
                    context.startActivity(it);
                }
            }
        });

        holder.abrirMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson gson = new Gson();
                Intent it = new Intent(context, PopUpListaMenu.class);

                it.putExtra("LOCAL", gson.toJson(local));
                it.putExtra("TIPOCHAMADA", false);
                context.startActivity(it);
            }
        });

        if(position %2 != 0){
            holder.adapterLocalCard.setBackgroundColor( context.getResources().getColor(R.color.card_impar));
        }
        else{
            holder.adapterLocalCard.setBackgroundColor( context.getResources().getColor(R.color.card_par));
        }
    }

    @Override
    public int getItemCount() {
        Log.e("Quantidade na LOCAIS", String.valueOf(locais.size()) );
        return locais.size();
    }
}

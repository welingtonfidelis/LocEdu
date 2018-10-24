package br.com.welingtonfidelis.locedu.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.welingtonfidelis.locedu.Helper.ReferencesHelper;
import br.com.welingtonfidelis.locedu.Model.Local;
import br.com.welingtonfidelis.locedu.Model.Setor;
import br.com.welingtonfidelis.locedu.R;
import br.com.welingtonfidelis.locedu.View.ListaLocal;
import br.com.welingtonfidelis.locedu.View.NovoSetor;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.List;

public class SetorAdapterGrid extends RecyclerView.Adapter<SetorAdapterGrid.ViewHolder> {

    private Context context;
    private List<Setor> setores;
    private LayoutInflater mInflater;

    private TextView eventos;
    /*private String[] paletaCores = {"#cd298a", "#a9ab08", "#2f7967","#b10e0e", "#8947d8", "#b1500e",
            "#0a9637", "#e6385d", "#4731fc","#4731fc", "#00ff0e", "#9812db", "#0a9637",
            "#9a4677", "#cfcce8", "#000000","#000000"}; */

    private int contador;

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
        //Log.e("CONTADOR", contaEventos(setores.get(position).getKey())+"");
        //contaEventos(setores.get(position).getKey());

        if (contador>0){
            Log.e("CONTADOREVENTO", setores.get(position).getNomeSetor() + " "  + contador);
        }

        //holder.qntEventos.setText(contador);

        holder.nomeSetor.setText(setores.get(position).getNomeSetor());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.e("TESTE", setores.get(position).getNomeSetor());
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

        //setando cores ligadas ao mapa do gridview
        //holder.cardView.setBackgroundColor(Color.parseColor(paletaCores[position]));


    }

    @Override
    public int getItemCount() {
        return setores.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder /*implements View.OnClickListener*/{
        TextView nomeSetor, qntEventos;
        CardView cardView;

        ViewHolder(View itemView){
            super(itemView);
            nomeSetor = itemView.findViewById(R.id.info_text);
            cardView = itemView.findViewById(R.id.card_view);
            eventos = itemView.findViewById(R.id.tv_numero_eventos);
            qntEventos = itemView.findViewById(R.id.tv_numero_eventos);
        }
    }

    public Setor getItem(int id) {
        return setores.get(id);
    }

    private void contaEventos(String key) {
        contador = 0;
        ValueEventListener localEventListener = new ValueEventListener() {
            @Override
            public void onDataChange( com.google.firebase.database.DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                        final Local loc = postSnapshot.getValue(Local.class);
                        loc.setKey(postSnapshot.getKey());

                        ValueEventListener m = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                contador =+ ((int) dataSnapshot.getChildrenCount());
                                //holder.adapterQntEvento.setText(String.valueOf(local.getQntEvento()));
                                //Log.e("CONTADOR", loc.getNomeLocal() + " " + contador +"");
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        };
                        ReferencesHelper.getDatabaseReference().child("Evento").orderByChild("localKey").equalTo(loc.getKey()).addValueEventListener(m);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        ReferencesHelper.getDatabaseReference().child("Local").orderByChild("keySetor").equalTo(key).addValueEventListener(localEventListener);
    }
}

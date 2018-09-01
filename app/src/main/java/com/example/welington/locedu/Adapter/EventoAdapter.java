package com.example.welington.locedu.Adapter;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.welington.locedu.Controller.ReferencesHelper;
import com.example.welington.locedu.Model.Evento;
import com.example.welington.locedu.Model.Local;
import com.example.welington.locedu.R;
import com.example.welington.locedu.View.AlterarEvento;
import com.example.welington.locedu.View.Mapa;
import com.example.welington.locedu.View.PopUpInfoEvento;
import com.google.gson.Gson;

import java.util.Date;
import java.util.List;

public class EventoAdapter extends RecyclerView.Adapter<EventoAdapter.ViewHolder>{
    private Context context;
    private Local local;
    private List<Evento> eventos;

    public EventoAdapter(Context context, List<Evento> eventos, Local local) {
        this.context = context;
        this.eventos = eventos;
        this.local = local;
    }

    protected class ViewHolder extends RecyclerView.ViewHolder{
        protected TextView adapterEventoTipo;
        protected TextView adapterEventoNome;
        protected TextView adapterEventoResponsavel;
        protected CardView adapterEventoCard;
        protected TextView adapterEventoData;
        protected ImageButton adapterInformacao, adapterRota;

        public ViewHolder(final View itemView){
            super(itemView);
            adapterEventoTipo = itemView.findViewById(R.id.adapter_evento_tipo);
            adapterEventoNome = itemView.findViewById(R.id.adapter_evento_nome);
            adapterEventoResponsavel = itemView.findViewById(R.id.adapter_evento_responsavel);
            adapterEventoCard = itemView.findViewById(R.id.adapter_evento_card);
            adapterEventoData = itemView.findViewById(R.id.adapter_evento_data);
            adapterInformacao = itemView.findViewById(R.id.img_button_info);
            adapterRota = itemView.findViewById(R.id.img_button_rota);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_evento, parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Evento evento = eventos.get(position);

        holder.adapterEventoTipo.setText(evento.getTipo());
        holder.adapterEventoNome.setText(evento.getNomeEvento());
        holder.adapterEventoResponsavel.setText(evento.getResponsavel());
        holder.adapterEventoData.setText(evento.getData().toString());

        holder.adapterEventoCard.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                if(ReferencesHelper.getFirebaseAuth().getCurrentUser() != null){
                    Gson gson = new Gson();
                    Intent it = new Intent(context, AlterarEvento.class);
                    it.putExtra("EVENTO", gson.toJson(evento));
                    context.startActivity(it);
                    //Toast.makeText(context, "Item :" + evento.getNomeEvento() + " " +evento.getData(), Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });

        holder.adapterInformacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                Intent it = new Intent(context, PopUpInfoEvento.class);
                it.putExtra("EVENTO", gson.toJson(evento));
                context.startActivity(it);
            }
        });

        holder.adapterRota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                Intent it = new Intent(context, Mapa.class);
                it.putExtra("LOCAL", gson.toJson(local));
                context.startActivity(it);
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventos.size();
    }
}

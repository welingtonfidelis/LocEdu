package com.example.welington.locedu.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.welington.locedu.Model.Setor;

import java.util.List;

/**
 * Created by welington on 16/08/18.
 */

public class SetorAdapter2 extends BaseAdapter{

    private Context context;
    private List<Setor> setores;

    public SetorAdapter2(Context context, List<Setor> setores) {
        this.context = context;
        this.setores = setores;
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

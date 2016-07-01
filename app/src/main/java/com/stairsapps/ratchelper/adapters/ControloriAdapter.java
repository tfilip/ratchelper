package com.stairsapps.ratchelper.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stairsapps.ratchelper.R;
import com.stairsapps.ratchelper.models.Controlor;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by filip on 6/28/2016.
 */
public class ControloriAdapter extends RecyclerView.Adapter<ControloriAdapter.ViewHolder> {

    private List<Controlor> mContolori;
    private Context mContext;

    public ControloriAdapter(List<Controlor> mContolori, Context mContext) {
        this.mContolori = mContolori;
        this.mContext = mContext;
    }

    public Context getmContext() {
        return mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View controlorView = inflater.inflate(R.layout.item_controlor,parent,false);

        ViewHolder viewHolder = new ViewHolder(controlorView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Controlor controlor = mContolori.get(position);

        TextView liniaTV = holder.liniaTV;
        liniaTV.setText("Linia "+controlor.getLine());

        TextView dataTV = holder.timpTV;
        dataTV.setText(controlor.getData());

        TextView controlTV = holder.liberTV;
        if(controlor.isLiber()){
            controlTV.setText("CONTROL");
        }
        else {
            controlTV.setText("LIBER");
        }

        TextView numarTV = holder.numarTV;
        if(numarTV != null || !numarTV.equals("")){
            numarTV.setText(controlor.getNumar());
        }

    }

    @Override
    public int getItemCount() {
        return mContolori.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView liniaTV;
        public TextView timpTV;
        public TextView liberTV;
        public TextView numarTV;

        public ViewHolder(View itemView) {
            super(itemView);

            liniaTV = ButterKnife.findById(itemView, R.id.linia);
            timpTV = ButterKnife.findById(itemView,R.id.data);
            liberTV = ButterKnife.findById(itemView,R.id.control);
            numarTV = ButterKnife.findById(itemView,R.id.placuta);

        }
    }

}

package com.gh.sammie.ghanawater.ViewHolder;


import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gh.sammie.ghanawater.Interface.ItemClickListener;
import com.gh.sammie.ghanawater.R;

public class HistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txt_action_performed, txt_name, history_time_ago, txt_email, txt_price;
    private ItemClickListener itemClickListener;

    public HistoryViewHolder(View itemView) {
        super(itemView);
        txt_action_performed = itemView.findViewById(R.id.txt_action_performed);
        txt_name = itemView.findViewById(R.id.txt_name);
        txt_price = itemView.findViewById(R.id.txt_price);
        history_time_ago = itemView.findViewById(R.id.history_time_ago);
        txt_email = itemView.findViewById(R.id.txt_email);
        itemView.setOnClickListener(this);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false); // true to enable long click

    }
}

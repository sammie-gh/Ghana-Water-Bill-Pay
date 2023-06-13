package com.gh.sammie.ghanawater.ViewHolder;


import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gh.sammie.ghanawater.Interface.ItemClickListener;
import com.gh.sammie.ghanawater.R;

public class NotificationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtTitle, txtmessage, timeAgo;
    private ItemClickListener itemClickListener;

    public NotificationViewHolder(View itemView) {
        super(itemView);
        txtTitle = itemView.findViewById(R.id.users_title);
        txtmessage = itemView.findViewById(R.id.user_message);
        timeAgo = itemView.findViewById(R.id.users_time_ago);
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

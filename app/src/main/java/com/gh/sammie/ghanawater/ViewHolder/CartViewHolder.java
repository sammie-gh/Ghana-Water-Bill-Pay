package com.gh.sammie.ghanawater.ViewHolder;


import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gh.sammie.ghanawater.Interface.ItemClickListener;
import com.gh.sammie.ghanawater.R;

/**
 * Created by Sammie on 2/19/2018.
 */

public class CartViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener
        ,View.OnCreateContextMenuListener{
    public TextView txt_cart_name,txt_price,txt_discount;

    public ImageView cartImage;

    public RelativeLayout view_background;
    public LinearLayout view_forebackground;

    private ItemClickListener itemClickListener;

    public void setTxt_cart_name(TextView txt_cart_name) {
        this.txt_cart_name = txt_cart_name;
    }

    public CartViewHolder(View itemView) {
        super(itemView);

        txt_cart_name = (TextView)itemView.findViewById(R.id.cart_item_name);
        txt_price = (TextView)itemView.findViewById(R.id.cart_item_Price);
        txt_discount = (TextView)itemView.findViewById(R.id.cart_discount);
        cartImage =  itemView.findViewById(R.id.cart_image);
        view_background = itemView.findViewById(R.id.view_Background);
        view_forebackground = itemView.findViewById(R.id.view_foreground);

        itemView.setOnCreateContextMenuListener(this);


        itemView.setOnCreateContextMenuListener(this);

    }


    @Override
    public void onClick(View view) {

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

    }
}
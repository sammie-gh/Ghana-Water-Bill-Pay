//package com.gh.sammie.lessonplan.ViewHolder;
//
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.gh.sammie.lessonplan.CartBasicTerm1;
//
//import com.gh.sammie.lessonplan.R;
//import com.squareup.picasso.Picasso;
//
//import java.text.NumberFormat;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Locale;
//
///**
// * Created by A.Richard on 12/09/2017.
// */
//
//
//
//public class CartAdapter extends RecyclerView.Adapter<CartViewHolder>{
//
//    private List<Order> listData = new ArrayList<>();
//    private CartBasicTerm1 cart;
//
//    public CartAdapter(List<Order> listData, CartBasicTerm1 cart) {
//        this.listData = listData;
//        this.cart = cart;
//    }
//
//    @NonNull
//    @Override
//    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        LayoutInflater inflater = LayoutInflater.from(cart);
//        View itemView = inflater.inflate(R.layout.cart_layout,parent,false);
//        return new CartViewHolder(itemView);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull CartViewHolder holder, final int position) {
//
//        Picasso.get()
//                .load(listData.get(position).getImage())
//                .resize(150,150)  //100 width first
//                .into(holder.cartImage);
//
//
////        TextDrawable drawable   = TextDrawable.builder()
////                .buildRound(""+listData.get(position).getQuantity(), Color.RED);
////        holder.img_cart_count.setImageDrawable(drawable);
//
//
//
//        Locale locale = new Locale("en","GH");//US
//        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
//
//        double price = (Double.parseDouble(listData.get(position).getPrice()))*(Double.parseDouble(listData.get(position).getQuantity()));
//        holder.txt_price.setText(fmt.format(price));
//
//
//        holder.txt_cart_name.setText(listData.get(position).getProductName());
//
////        //discount
////        double discount = (Double.parseDouble(listData.get(position).getDiscount()))*(Double.parseDouble(listData.get(position).getQuantity()));
////        holder.txt_discount.setText("Discount : "+ discount);
//
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return listData.size();
//    }
//
//
//
//    public Order getItem(int position)
//    {
//        return listData.get(position);
//    }
//
//    public void removeItem(int position)
//    {
//        listData.remove(position);
//        notifyItemRemoved(position);
//    }
//
//
//
//    public void restoreItem(Order item, int position)
//    {
//        listData.add(position,item);
//        notifyItemInserted(position);
//    }
//}

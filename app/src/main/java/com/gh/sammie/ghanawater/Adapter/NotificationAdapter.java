package com.gh.sammie.ghanawater.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gh.sammie.ghanawater.R;
import com.gh.sammie.ghanawater.model.NotificationsModel;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
    private DatabaseReference mDatabaseRef;
    private Context mContext;
    private ArrayList<NotificationsModel> mArrayList;

    public NotificationAdapter(Context mContext, ArrayList<NotificationsModel> mArrayList) {
        this.mContext = mContext;
        this.mArrayList = mArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_notification_layout, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        NotificationsModel model = mArrayList.get(position);

        holder.txtTitle.setText(model.getTitle());
        holder.txtmessage.setText(model.getMessage());

//        holder.userPhoneNumber.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                intentCall(model.getUserPhoneNumber());
//            }
//        });

        holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                return true;
            }
        });
    }

//    private void removeNotificationData(User model, int position) {
////        String currentUserID = "";
////        FirebaseAuth auth = FirebaseAuth.getInstance();
////        FirebaseUser mFirebaseUser = auth.getCurrentUser();
////        if(mFirebaseUser != null) {
////            currentUserID = mFirebaseUser.getUid(); //Do what you need to do with the id
////        }
//
////        mDatabaseRef = FirebaseDatabase.getInstance().getReference(ConstantKey.NOTIFICATION_POST_NODE).child(currentUserID);
////        Log.d("NotificationAdapter", "removePostAd: " + mDatabaseRef);
////        //mDatabaseRef.removeValue();
////        mDatabaseRef.removeValue(new DatabaseReference.CompletionListener() {
////            @Override
////            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
////                if (databaseError == null) {
////                    mArrayList.remove(position);
////                    notifyItemRemoved(position);
////                    notifyItemRangeChanged(position, mArrayList.size());
////                } else {
////                    Toast.makeText(mContext, "Error ", Toast.LENGTH_SHORT).show();
////
////                }
////            }
////        });
//        new FirebaseRepository().removeNotification(new FirebaseRepository.RemovePostAdCallback() {
//            @Override
//            public void onCallback(String result) {
//                if (result.equals("success")) {
//                    mArrayList.remove(position);
//                    notifyItemRemoved(position);
//                    notifyItemRangeChanged(position, mArrayList.size());
//                }
//            }
//        }, model);
//    }

    @Override
    public int getItemCount() {
        if (mArrayList == null) {
            return 0;
        } else {
            return mArrayList.size();
        }
//        return mArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout layout;
        CircleImageView userImageUrl;
        TextView txtTitle, txtmessage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = (RelativeLayout) itemView.findViewById(R.id.notification_item_id);
            txtTitle = itemView.findViewById(R.id.users_title);
            txtmessage = itemView.findViewById(R.id.user_message);

        }
    }

    //====================================================| Call
//    private void intentCall(String number) {
//        Intent dial = new Intent(Intent.ACTION_DIAL);
//        try {
//            dial.setData(Uri.parse("tel:" + number));
//            mContext.startActivity(dial);
//        } catch (ActivityNotFoundException e) {
//            Utility.alertDialog(mContext, mContext.getString(R.string.msg_no_dialer));
//        }
//    }

}

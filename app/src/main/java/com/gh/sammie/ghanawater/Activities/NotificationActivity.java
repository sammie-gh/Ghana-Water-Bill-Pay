package com.gh.sammie.ghanawater.Activities;

import static com.gh.sammie.ghanawater.Common.Common.isConnectedToInternet;
import static com.gh.sammie.ghanawater.services.MyFirebaseMessagingService.NOTIFICATION_DATABASE;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.gh.sammie.ghanawater.Common.Common;
import com.gh.sammie.ghanawater.Interface.ItemClickListener;
import com.gh.sammie.ghanawater.R;
import com.gh.sammie.ghanawater.ViewHolder.NotificationViewHolder;
import com.gh.sammie.ghanawater.model.NotificationsModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class NotificationActivity extends AppCompatActivity {
    private static final String TAG = "NotificationActivity";
    private DatabaseReference notificationsDb;
    private FirebaseRecyclerAdapter<NotificationsModel, NotificationViewHolder> adapter;
    private RecyclerView recyclerView;
    private Context mContext;
    private SwipeRefreshLayout mRefreshLayout;
    private SweetAlertDialog pDialog;
    FirebaseAuth mAuth;
    private List<NotificationsModel> notificationsModelList = new ArrayList<>();
    private int mCurrentPage = 1;
    private static final int TOTAL_ITEMS_TO_LOAD = 4;
    int contextMenuIndexClicked = -1;
    private String nameTitle;
    private LottieAnimationView animationView;
    private ImageView imageBackground;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_notification, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_clear) {
            clearNotifications(nameTitle);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        }
        mContext = NotificationActivity.this;

        // init database
        initDatabase();
        //hooks
        initView();

        if (isConnectedToInternet((this))) {
            loadNotifications();
//            loadMenu();
        } else {
            loadNotifications();
            Toast.makeText(mContext, "Oops your are offline", Toast.LENGTH_SHORT).show();
//            errorSweetDialog();
        }

        //layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setReverseLayout(true);
        mLinearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLinearLayoutManager);

        /*End of oncreate*/
    }

    private void initDatabase() {
        //init Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        String uid = Objects.requireNonNull(mAuth.getCurrentUser().getUid());

//        String name = Objects.requireNonNull(mAuth.getCurrentUser().getDisplayName());
//        notificationsDb = database.getReference(NOTIFICATION_DATABASE).child(uid).child(name);
        notificationsDb = database.getReference(NOTIFICATION_DATABASE).child(uid);
        notificationsDb.keepSynced(true);
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.main_app_bar);
        toolbar.setTitleTextAppearance(this, R.style.ToolFont);
        toolbar.setTitle("My Alerts");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setElevation(7.0f);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = findViewById(R.id.notification_recycler_view);
        animationView = findViewById(R.id.animationView);
        imageBackground = findViewById(R.id.imageBackground);
    }

    private void loadNotifications() {
        //create query by category
//        Query query = notificationsDb.child(name).orderByChild("title");

//        PagedList.Config config = new PagedList.Config.Builder()
//                .setEnablePlaceholders(false)
//                .setPrefetchDistance(4)
//                .setPageSize(1)
//                .build();

//        DatabasePagingOptions<NotificationsModel> options = new DatabasePagingOptions.Builder<NotificationsModel>()
//                .setLifecycleOwner(this)
//                .setQuery(notificationsDb, config, NotificationsModel.class)
//                .build();
//        Query notificationQuery = notificationsDb.limitToLast(mCurrentPage * TOTAL_ITEMS_TO_LOAD);
        FirebaseRecyclerOptions<NotificationsModel> options = new FirebaseRecyclerOptions.Builder<NotificationsModel>()
                .setQuery(notificationsDb, NotificationsModel.class) //notificationsDb
                .build();

        adapter = new FirebaseRecyclerAdapter<NotificationsModel, NotificationViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull NotificationViewHolder viewHolder, int position, @NonNull NotificationsModel model) {
                viewHolder.txtTitle.setText(model.getTitle());
                viewHolder.txtmessage.setText(model.getMessage());
                viewHolder.timeAgo.setText(Common.getTimeAgo(model.getTimeStampValue()));

                nameTitle = model.getTitle();

                //Log tests
                Log.d("Time", "Time: " + Common.getTimeAgo(model.getTimeStampValue()));
                Log.d("value", "Value: " + model.getMessage() + model.getTitle() + model.getTimeStampValue());
                Log.d("nameTitle", "nameTitles: " + nameTitle);

                viewHolder.txtmessage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listOptions(position, nameTitle);
                    }
                });
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        listOptions(position, nameTitle);
                    }
                });
            }

            //            @Override
//            protected void onLoadingStateChanged(@NonNull LoadingState state) {
//                switch (state) {
//                    case LOADING_INITIAL:
//
//                        // The initial load has begun
//                        // ...
//                    case LOADING_MORE:
//                        // The adapter has started to load an additional page
//                        //
//                        // Do your loading animation
//                        mRefreshLayout.setRefreshing(true);
//                    case LOADED:
//                        // The previous load (either initial or additional) completed
//                        // ...
//                        mRefreshLayout.setRefreshing(true);
//                        if (pDialog != null) {
//                            pDialog.dismiss();
//                        }
//                    case FINISHED:
//                        //Reached end of Data set
//
//                        recyclerView.smoothScrollToPosition(0);
//                        mRefreshLayout.setRefreshing(false);
//                        break;
//
//                    case ERROR:
//                        // The previous load (either initial or additional) failed. Call
//                        // the retry() method in order to retry the load operation.
//                        // ...
//                        retry();
//                }
//
//
//            }


            @NonNull
            @Override
            public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recycler_item_notification_layout, parent, false);
                return new NotificationViewHolder(itemView);

            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                Log.d(TAG, "itemCount" + getItemCount());
                if (getItemCount() == 0) {
                    Log.d(TAG, "onDataChanged: Empty view");
                    recyclerView.setVisibility(View.GONE);
                    animationView.setVisibility(View.VISIBLE);
                    imageBackground.setVisibility(View.GONE);
                    Toast.makeText(NotificationActivity.this, "you currently have no new notifications", Toast.LENGTH_SHORT).show();
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    animationView.setVisibility(View.GONE);
                    imageBackground.setVisibility(View.VISIBLE);
                }

                if (pDialog != null) {
                    pDialog.dismiss();
                }
            }

        };

        adapter.startListening();
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);


    }


    private void listOptions(int position, String title) {

        //Alert here
        CharSequence[] options = new CharSequence[]
                {"delete selected", "clear all Alerts",
                };

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setCancelable(true);

        builder.setItems(options, (dialogInterface, i) -> {
            //Click Event for each item.
            if (i == 0) {
                Log.d("Delete", "listOptions: " + position);
//                notificationsDb.removeValue(position)
                adapter.getRef(position).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(mContext, "item deleted successful ", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(mContext, "Failed to delete", Toast.LENGTH_SHORT).show();
                    }
                });
                adapter.onDataChanged();
                adapter.notifyDataSetChanged();

                return;
            }
            if (i == 1) {
                clearNotifications(title);

            }
        });

        builder.show();
    }

    private void clearNotifications(String title) {
        if (!TextUtils.isEmpty(title)) {
            new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Clear all Notifications ?")
                    .setContentText("This action can not be undone")
                    .setConfirmText("Clear")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            notificationsDb.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        //if notify all does'nt work use complete listener
                                        adapter.onDataChanged();
                                        adapter.notifyDataSetChanged();
                                        Toast.makeText(mContext, "All Notifications Cleared", Toast.LENGTH_SHORT).show();
                                        sDialog.dismissWithAnimation();
                                    } else
                                        Toast.makeText(mContext, "Failed to Clear Notifications", Toast.LENGTH_SHORT).show();
                                    sDialog.dismissWithAnimation();
                                }
                            });


                        }
                    })
                    .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
//                            finish();
                        }
                    })
                    .show();
        } else
            Toast.makeText(mContext, "Boss please no item is available to be cleared", Toast.LENGTH_SHORT).show();

    }

    private void errorSweetDialog() {
        SweetAlertDialog.DARK_STYLE = true;
        new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText("Something went wrong! \n Please check Your connection")
                .show();
        pDialog.dismiss();

    }

    private void showDialog() {
        pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(true);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        showDialog();
        if (adapter != null)
            adapter.startListening();

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        // fix click back view
        if (adapter != null)
            adapter.startListening();
    }


    @Override
    public void onStop() {
        super.onStop();

//        if (adapter != null)
//            adapter.stopListening(); adapter.stopListening();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (pDialog != null)
            if (pDialog.isShowing())
                pDialog.dismiss();
        pDialog = null;
    }

}

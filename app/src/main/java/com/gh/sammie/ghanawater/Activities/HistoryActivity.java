package com.gh.sammie.ghanawater.Activities;

import static com.gh.sammie.ghanawater.Common.Common.isConnectedToInternet;
import static com.gh.sammie.ghanawater.services.MyFirebaseMessagingService.HISTORY_DATABASE;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.gh.sammie.ghanawater.ViewHolder.HistoryViewHolder;
import com.gh.sammie.ghanawater.model.History;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.MessageFormat;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class HistoryActivity extends AppCompatActivity {

    private static final String TAG = "HistoryActivity";
    private DatabaseReference HistoryDB;
    private FirebaseRecyclerAdapter<History, HistoryViewHolder> adapter;
    private RecyclerView recyclerView;
    private Context mContext;
    private SwipeRefreshLayout mRefreshLayout;
    private SweetAlertDialog pDialog;
    private LinearLayoutManager mLinearLayoutManager;
    FirebaseAuth mAuth;

    private int mCurrentPage = 1;
    private static final int TOTAL_ITEMS_TO_LOAD = 4;
    int contextMenuIndexClicked = -1;
    private String nameTitle;
    private LottieAnimationView animationView;


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.home_notification, menu);
//
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.action_clear) {
////            clearNotifications(nameTitle);
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        }
        mContext = HistoryActivity.this;

        //hooks
        initView();

        // init database
        initDatabase();


        if (isConnectedToInternet((this))) {
            loadHistoryInfo();
//            loadMenu();
        } else {
            loadHistoryInfo();
            Toast.makeText(mContext, "Oops your are offline", Toast.LENGTH_SHORT).show();
//            errorSweetDialog();
        }


    }


    private void initDatabase() {
        //init Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        String uid = Objects.requireNonNull(mAuth.getCurrentUser().getUid());

//        String name = Objects.requireNonNull(mAuth.getCurrentUser().getDisplayName());
//        notificationsDb = database.getReference(NOTIFICATION_DATABASE).child(uid).child(name);
        HistoryDB = database.getReference(HISTORY_DATABASE).child(uid);


        HistoryDB.keepSynced(true);
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.main_app_bar);
        toolbar.setTitleTextAppearance(this, R.style.ToolFont);
        toolbar.setTitle("User History");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setElevation(7.0f);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = findViewById(R.id.history_recycler_view);
        animationView = findViewById(R.id.animationView);

        recyclerView.setHasFixedSize(true);
        mLinearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        mLinearLayoutManager.setReverseLayout(true);
        mLinearLayoutManager.setStackFromEnd(true);
    }

    private void loadHistoryInfo() {
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

        Query query = HistoryDB.orderByChild("timeStamp");
        FirebaseRecyclerOptions<History> options = new FirebaseRecyclerOptions.Builder<History>()
                .setQuery(query, History.class) //notificationsDb
                .build();

        adapter = new FirebaseRecyclerAdapter<History, HistoryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull HistoryViewHolder viewHolder, int position, @NonNull History model) {
                viewHolder.txt_name.setText(model.getDesc());
                viewHolder.txt_email.setText(model.getEmail());
                viewHolder.txt_action_performed.setText("Task: " + model.getActionPerformed());
//                viewHolder.txt_price.setText(MessageFormat.format("₵:{0}", model.getPrice()));
                viewHolder.txt_price.setText(MessageFormat.format("GHS {0}", model.getPrice()));
                viewHolder.history_time_ago.setText(Common.getTimeAgo(model.getTimeStamp()));

                nameTitle = model.getId();

//                Log tests
//                Log.d("Time", "Time: " + Common.getTimeAgo(model.getTimeStamp()));
//                Log.d("value", "Value: " + model.getMessage() + model.getTitle() + model.getTimeStampValue());
                Log.d("TIMESTAMP", "getTimeStamp: " + model.getTimeStamp());

                viewHolder.txt_email.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(HistoryActivity.this, "Action disabled by Admin", Toast.LENGTH_SHORT).show();
//                        listOptions(position, nameTitle);
                    }
                });
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
//                        listOptions(position, nameTitle);
                        Toast.makeText(HistoryActivity.this, "Action disabled by Admin", Toast.LENGTH_SHORT).show();
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
//            }


            @NonNull
            @Override
            public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recycler_item_history_layout, parent, false);
                return new HistoryViewHolder(itemView);

            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                Log.d(TAG, "itemCount" + getItemCount());

                if (getItemCount() == 0) {
                    Log.d(TAG, "onDataChanged: Empty view");
                    recyclerView.setVisibility(View.GONE);
                    animationView.setVisibility(View.VISIBLE);

                    Toast.makeText(HistoryActivity.this, "You have no history", Toast.LENGTH_SHORT).show();
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    animationView.setVisibility(View.GONE);

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
                            HistoryDB.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
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
        showDialog();
        if (adapter != null)
            adapter.startListening();

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        // fix click back view

    }

    @Override
    public void onStop() {
        super.onStop();
//        if (adapter != null)
//            adapter.stopListening();

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
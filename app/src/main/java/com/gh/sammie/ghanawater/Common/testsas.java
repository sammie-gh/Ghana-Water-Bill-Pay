//package com.gh.sammie.orderit;
//
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.Bitmap;
//import android.graphics.Color;
//import android.graphics.Typeface;
//import android.graphics.drawable.Drawable;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.design.widget.BottomNavigationView;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.Toolbar;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Toast;
//
//import com.amulyakhare.textdrawable.TextDrawable;
//import com.facebook.CallbackManager;
//import com.facebook.share.model.SharePhoto;
//import com.facebook.share.model.SharePhotoContent;
//import com.facebook.share.widget.ShareDialog;
//import com.firebase.ui.database.FirebaseRecyclerAdapter;
//import com.firebase.ui.database.FirebaseRecyclerOptions;
//import com.gh.sammie.orderit.Common.Common;
//import com.gh.sammie.orderit.Database.Database;
//import com.gh.sammie.orderit.Interface.ItemClickListener;
//import com.gh.sammie.orderit.Model.Favorites;
//import com.gh.sammie.orderit.Model.Food;
//import com.gh.sammie.orderit.Model.Order;
//import com.gh.sammie.orderit.Utils.BottomNavigationViewHelper;
//import com.gh.sammie.orderit.ViewHolder.FoodViewHolder;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.Query;
//import com.google.firebase.database.ValueEventListener;
//import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
//import com.mancj.materialsearchbar.MaterialSearchBar;
//import com.squareup.picasso.Picasso;
//import com.squareup.picasso.Target;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import es.dmoral.toasty.Toasty;
//
//public class SearchActivity extends AppCompatActivity {
//    private static final String TAG = "SearchActivity";
//
//    private Toolbar mToolbar;
//
//    RecyclerView recyclerView;
//    RecyclerView.LayoutManager layoutManager;
//
//    FirebaseRecyclerAdapter<Food,FoodViewHolder> adapter;
//
//    FirebaseDatabase database;
//    DatabaseReference foodlist;
//
//
//    //facebookshare
//    CallbackManager callbackManager;
//    ShareDialog shareDialog;
//
//    //fav
//    Database localDB;
//
//    String categoryId = "";
//    SwipeRefreshLayout mResfreshLayout;
//
//    //Search Functionality
//    FirebaseRecyclerAdapter<Food,FoodViewHolder> searchAdapter;
//    List<String> suggestion = new ArrayList<>();
//    MaterialSearchBar materialSearchBar;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_search);
//
//        //Toolbar
//        mToolbar = (Toolbar) findViewById(R.id.main_app_bar);
//        setSupportActionBar(mToolbar);
//
//
//        //bottomview  annd all activity listener
//        setupBottomNavigationView();
//
//        //Get Intent here
//        if (getIntent() != null)
//            categoryId = getIntent().getStringExtra("FoodId");//CategoryId
//
//        mResfreshLayout = findViewById(R.id.swipe_layout);
//        mResfreshLayout.setColorSchemeResources(R.color.colorPrimary,
//                android.R.color.holo_green_light,
//                android.R.color.white,
//                android.R.color.holo_blue_dark,
//                android.R.color.holo_orange_dark);
//
//
//
//        //init Firebase
//        database = FirebaseDatabase.getInstance();
//        database =  FirebaseDatabase.getInstance();
//        foodlist = database.getReference("Foods");
//
//        recyclerView = (RecyclerView) findViewById(R.id.recycler_search);
//        recyclerView.setHasFixedSize(true);
//        layoutManager  = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//
////        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(recyclerView.getContext(),
////                R.anim.fade_in);
////        recyclerView.setLayoutAnimation(controller);
//
//
//        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottomNavViewBar);
//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.nav_menu:
//                        Intent intent1 = new Intent(SearchActivity.this, Home.class);//ACTIVITY_NUM = 0
//                        startActivity(intent1);
//                        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
//                        break;
//
//                    case R.id.ic_cart:
//                        Intent intent2 = new Intent(SearchActivity.this, Cart.class);//ACTIVITY_NUM = 1
//                        startActivity(intent2);
//                        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
//                        break;
//
//
//                    case R.id.nav_favorite:
//                        Intent favourite = new Intent(SearchActivity.this, FavoritesActivity.class);//ACTIVITY_NUM = 1
//                        startActivity(favourite);
//                        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
//                        break;
//
//
//                    case R.id.nav_orders:
//                        Intent intent4 = new Intent(SearchActivity.this, OrderStatus.class);//ACTIVITY_NUM = 2
//                        startActivity(intent4);
//                        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
//                        break;
//
////
//                }
//
//
//                return false;
//            }
//        });
//
//
//        localDB = new Database(this);
//        //Search
//        materialSearchBar = (MaterialSearchBar) findViewById(R.id.search_bar);
//        materialSearchBar.setHint("Enter food name to Search");
//
//        loadSuggest();
//
//        materialSearchBar.setLastSuggestions(suggestion);
//        materialSearchBar.setCardViewElevation(10);
//
//        mResfreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                //Load food if internet or network
//                if (Common.isConnectedToInternet(getBaseContext()))
//
//                    loadAllFoods();
//
//                else{
//                    Toasty.warning(SearchActivity.this, "Please check your connection", Toast.LENGTH_LONG).show();
//                    return;
//                }
//            }
//        });
//
//        mResfreshLayout.post(new Runnable() {
//            @Override
//            public void run() {
//                if (Common.isConnectedToInternet(getBaseContext()))
//
//                    loadAllFoods();
//
//                else{
//                    Toasty.warning(SearchActivity.this, "Please check your connection", Toast.LENGTH_LONG).show();
//                    return;
//                }
//            }
//        });
//
//
//
//        materialSearchBar.addTextChangeListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                //when user type their text, we will change sugesst list
//                List<String> suggest = new ArrayList<String>();
//                for (String search:suggestion)
//                {
//                    if (search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
//                        suggest.add(search);
//
//                }
//                materialSearchBar.setLastSuggestions(suggest);
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
//
//        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
//            @Override
//            public void onSearchStateChanged(boolean enabled) {
//                //when search is closed
//                // restore original adapter
//
//                if (!enabled)
//                    recyclerView.setAdapter(adapter);
//
//            }
//
//            @Override
//            public void onSearchConfirmed(CharSequence text) {
//                //when search finish
//                //show of search adapter
//
//                startSearch(text);
//
//            }
//
//            @Override
//            public void onButtonClicked(int buttonCode) {
//
//
//            }
//        });
//
//
//    }
//
//    private void startSearch(CharSequence text)  {
//
//        //create query by name
//        Query searchByName = foodlist.orderByChild("name").equalTo(text.toString());
//
//        //create options with query
//        FirebaseRecyclerOptions<Food> foodOptions = new FirebaseRecyclerOptions.Builder<Food>()
//                .setQuery(searchByName,Food.class)
//                .build();
//
//        searchAdapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(foodOptions) {
//            @Override
//            protected void onBindViewHolder(@NonNull FoodViewHolder viewHolder, int position, @NonNull Food model) {
//                viewHolder.food_name.setText(model.getName());
//                Picasso.with(getBaseContext()).load(model.getImage())
//                        .into(viewHolder.food_image);
//
//                final Food local = model;
//
//                viewHolder.setItemClickListener(new ItemClickListener() {
//
//                    @Override
//                    public void onClick(View view, int position, boolean isLongClick) {
//                        //start new Activity
//                        Intent fooddetail  = new Intent(SearchActivity.this,FoodDetail.class);
//                        fooddetail.putExtra("FoodId",searchAdapter.getRef(position).getKey());// send food Id to new new act...
//                        startActivity(fooddetail);
//
//
//                    }
//                });
//            }
//
//            @Override
//            public FoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//                View itemView = LayoutInflater.from(parent.getContext())
//                        .inflate(R.layout.food_item,parent,false);
//                return  new FoodViewHolder(itemView);
//            }
//        };
//        searchAdapter.startListening();
//        recyclerView.setAdapter(searchAdapter); //set adpter for Recyc... is search result
//
//    }
//
//    //load suggest//load suggest
//    private void loadSuggest() {
//
//        foodlist.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
//
//                    Food item = postSnapshot.getValue(Food.class);
//                    suggestion.add(item.getName()); //add name of food to suggest list
//                }
//                materialSearchBar.setLastSuggestions(suggestion);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        });
//    }
//
//
//    private void loadAllFoods() {
//
//        //create query by category id
//        Query searchByName = foodlist;
//
//        //create options with query
//        FirebaseRecyclerOptions<Food> foodOptions = new FirebaseRecyclerOptions.Builder<Food>()
//                .setQuery(searchByName,Food.class)
//                .build();
//
//        adapter = new FirebaseRecyclerAdapter<Food,FoodViewHolder>(foodOptions) {
//            @Override
//            protected void onBindViewHolder(@NonNull final FoodViewHolder viewHolder, final int position, @NonNull final Food model) {
//
//                Typeface face  = Typeface.createFromAsset(getAssets(),"fonts/ABeeZee-Regular.ttf");
//                viewHolder.food_name.setTypeface(face);
//                viewHolder.food_name.setText(model.getName());
//
//                //add favourite
//                if (localDB.isFavourites(adapter.getRef(position).getKey(),Common.currentUser.getPhone()))
//                    viewHolder.fav_image.setImageResource(R.drawable.ic_favorite_black_24dp);
//
//                //click to change statelistener
//                viewHolder.fav_image.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        Favorites favorites = new Favorites();
//                        favorites.setFoodId(adapter.getRef(position).getKey());
//                        favorites.setFoodName(model.getName());
//                        favorites.setFoodDescription(model.getDescription());
//                        favorites.setFoodDiscount(model.getDiscount());
//                        favorites.setFoodImage(model.getImage());
//                        favorites.setFoodMenuId(model.getMenuId());
//                        favorites.setUserPhone(Common.currentUser.getPhone());
//                        favorites.setFoodPrice(model.getPrice());
//
//
//                        if (!localDB.isFavourites(adapter.getRef(position).getKey(),Common.currentUser.getPhone()))
//                        {
//                            localDB.addToFavourites(favorites);
//                            viewHolder.fav_image.setImageResource(R.drawable.ic_favorite_black_24dp);
//                            Toasty.success(SearchActivity.this, ""+ model.getName()+"Was added to Favorite list", Toast.LENGTH_SHORT).show();
//
//                        }else
//                        {
//                            localDB.removeFromFavourites(adapter.getRef(position).getKey(),Common.currentUser.getPhone());
//                            viewHolder.fav_image.setImageResource(R.drawable.ic_favorite_border_black_24dp);
//                            Toasty.success(SearchActivity.this, ""+ model.getName()+"Was removed from Favorite list", Toast.LENGTH_SHORT).show();
//
//                        }
//                    }
//                });
//
//
//
//                //quck cart
//
//                viewHolder.quickCart.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        boolean isExist = new Database(getBaseContext()).checkFoodExist(adapter.getRef(position).getKey(),Common.currentUser.getPhone());
//
//                        if (!isExist) {
//                            new Database(getBaseContext()).addToCart(new Order(
//                                    Common.currentUser.getPhone(),
//                                    adapter.getRef(position).getKey(),
//                                    model.getName(),
//                                    "1",
//                                    model.getPrice(),
//                                    model.getDiscount(),
//                                    model.getImage()
//                            ));
//
//                        }
//                        else
//
//                        {
//                            new Database(getBaseContext()).IncreaseCart(Common.currentUser.getPhone(), adapter.getRef(position).getKey());
//                        }
//                        Toasty.success(SearchActivity.this, "Added to cart ", Toast.LENGTH_SHORT).show();
//
//                    }
//                });
//
//
//                //click to share
//                viewHolder.share_image.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        //checkFacebook
//                        checkFacebook();
//
//                        Picasso.with(getApplicationContext())
//                                .load(model.getImage())
//                                .into(target);
//                    }
//                });
//                Picasso.with(getBaseContext()).load(model.getImage())
//                        .into(viewHolder.food_image);
//
//                TextDrawable drawable   = TextDrawable.builder()
//
//                        .beginConfig()
//                        .textColor(Color.BLACK)
//                        .useFont(Typeface.DEFAULT)
//                        .fontSize(30) /* size in px */
//                        .bold()
//                        .toUpperCase()
//                        .endConfig()
//                        .buildRect("₵ "+ model.getPrice(),Color.WHITE ); // radius in px
//
//                viewHolder.price.setImageDrawable(drawable);
//
//                TextDrawable time   = TextDrawable.builder()
//                        .beginConfig()
//                        .textColor(Color.WHITE)
//                        .useFont(Typeface.DEFAULT)
//                        .fontSize(30) /* size in px */
//                        .bold()
//                        .toUpperCase()
//                        .endConfig()
//                        .buildRect("45 min",Color.RED); // radius in px
//
//                viewHolder.time.setImageDrawable(time);
//
//                final Food local = model;
//                viewHolder.setItemClickListener(new ItemClickListener() {
//
//                    @Override
//                    public void onClick(View view, int position, boolean isLongClick) {
//                        //start new Activity
//                        Intent fooddetail  = new Intent(SearchActivity.this,FoodDetail.class);
//                        fooddetail.putExtra("FoodId",adapter.getRef(position).getKey());// send food Id to new new act...
//                        startActivity(fooddetail);
//
//                    }
//                });
//            }
//
//
//            @Override
//            public FoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//
//                View itemView = LayoutInflater.from(parent.getContext())
//                        .inflate(R.layout.food_item,parent,false);
//                return  new FoodViewHolder(itemView);
//            }
//        };
//
//        adapter.startListening();
//        Log.d(TAG, ""+ adapter.getItemCount());
//
////        //Animation
////        recyclerView.getAdapter().notifyDataSetChanged();
////        recyclerView.scheduleLayoutAnimation();
//
//        adapter.startListening();
//        recyclerView.setAdapter(adapter);
//        mResfreshLayout.setRefreshing(false); //end ring
//    }
//
//
//
//}

package com.gh.sammie.ghanawater.Activities;

import androidx.appcompat.app.AppCompatActivity;

public class CartBasicTerm1 extends AppCompatActivity {
//    public static final String mypreference = "mypref";
//    public static final String saveId = "savedkey";
//    private static final String ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm";
//    private static final int MY_PERMISSIONS_REQUEST_READ = 1;
//    public TextView txtTotalPrice;
//    public TextView txtDiscount;
//    public TextView txtFee;
//    private CartAdapter adapter;
//    private RelativeLayout rootLayout;
//    private RecyclerView recyclerView;
//    private Button btnPlace;
//    private String bookId;
//    private FirebaseAuth mAuth;
//    private String user_phone;
//    private String user_name;
//    private String bookName;
//    private SharedPreferences sharedpreferences;
//    private FirebaseDatabase database;
//    //    public static final String Email = "emailKey";
//    private DatabaseReference books;
//    private DatabaseReference requests;
//    private String price;
//    private SharedPreferences shPref;
//    private SharedPreferences.Editor editor;
//    private List<Order> cart = new ArrayList<>();
//    private Toolbar mToolbar;
//    private Context mContext = CartBasicTerm1.this;
//
//    private static String getRandomString(final int sizeOfRandomString) {
//        final Random random = new Random();
//        final StringBuilder sb = new StringBuilder(sizeOfRandomString);
//        for (int i = 0; i < sizeOfRandomString; ++i)
//            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
//        return sb.toString();
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_cart);
//
//        //runtime permission
//        if (ContextCompat.checkSelfPermission(CartBasicTerm1.this, Manifest.permission.READ_PHONE_STATE)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            if (ActivityCompat.shouldShowRequestPermissionRationale(CartBasicTerm1.this,
//                    Manifest.permission.READ_CONTACTS)) {
//
//            } else {
//
//                ActivityCompat.requestPermissions(CartBasicTerm1.this,
//                        new String[]{Manifest.permission.READ_PHONE_STATE},
//                        MY_PERMISSIONS_REQUEST_READ);
//
//            }
//        } else {
//
//        }
//
//        mToolbar = findViewById(R.id.main_app_bar);
//
//        setSupportActionBar(mToolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        mAuth = FirebaseAuth.getInstance();
//
//        //Firebase
//        database = FirebaseDatabase.getInstance();
//        books = database.getReference(BASIS_SCHOOL_TERM_1_DATABASE);
//        requests = database.getReference("Request");
//
//        //init
//        recyclerView = findViewById(R.id.listCart);
//        recyclerView.setHasFixedSize(true);
//
//
//        //swipe todelete;
//        ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerItemTouchhelper(0, ItemTouchHelper.LEFT, this);
//        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);
//
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setStackFromEnd(true);
//        linearLayoutManager.setReverseLayout(true);
//
//        recyclerView.setLayoutManager(linearLayoutManager);
//
//
//        txtTotalPrice = findViewById(R.id.total);
//        txtFee = findViewById(R.id.txt_fee);
//        txtDiscount = findViewById(R.id.txt_discount);
//        btnPlace = findViewById(R.id.btnPlaceOrder);
//        rootLayout = findViewById(R.id.rootLayout);
//
//
//        if (getIntent() != null) {
//            bookId = getIntent().getStringExtra("cart");
//            user_phone = getIntent().getStringExtra("number");
//            user_name = getIntent().getStringExtra("name");
//            bookName = getIntent().getStringExtra("description");
//
//        }
//        //sharedpreference
//        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
//
//        btnPlace.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                AlertDialog.Builder builder;
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
//                    builder = new AlertDialog.Builder(mContext, android.R.style.Theme_Material_Dialog_Alert);
//                else
//                    builder = new AlertDialog.Builder(mContext);
//
//                builder = new AlertDialog.Builder(mContext)
//                        .setTitle("Proceed with payment !!!")
//                        .setMessage("Are you sure on Action ?")
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                                if (cart.size() > 0) {
//
//                                    paymentFunction();
//
//                                } else
//
//                                    Toast.makeText(mContext, "Your CartBasicTerm1 is Empty \" + (\"\\ud83d\\ude20\")", Toast.LENGTH_SHORT).show();
//
//                            }
//                        })
//                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                                dialog.dismiss();
//                            }
//                        });
//
//                builder.show();
//
//
//            }
//        });
//        loadBook();
//    }
//
//    @Override
//    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
//        if (viewHolder instanceof CartViewHolder) {
//            String name = ((CartAdapter) recyclerView.getAdapter()).getItem(viewHolder.getAdapterPosition()).getProductName();
//
//            final Order deleteItem = ((CartAdapter) recyclerView.getAdapter()).getItem(viewHolder.getAdapterPosition());
//
//            final int deleteIndex = viewHolder.getAdapterPosition();
//
//
//            adapter.removeItem(deleteIndex);
//
//            new Database(getBaseContext()).removeFromCart(deleteItem.getProductId(), bookId);
//
//            //update total
//            double total = 0;
//            List<Order> orders = new Database(getBaseContext()).getCarts(bookId);
//            for (Order item : orders)
//                total += (Double.parseDouble(item.getPrice())) * (Double.parseDouble(item.getQuantity()));
//
//
//            Locale locale = new Locale("en", "GH");
//            NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
//
//            txtTotalPrice.setText(fmt.format(total));
//
//            //.Make Snackbar
//            Snackbar snackbar = Snackbar.make(rootLayout, name + "Removed from CartBasicTerm1!", Snackbar.LENGTH_LONG);
//            snackbar.setAction("UNDO", new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    adapter.restoreItem(deleteItem, deleteIndex);
//                    new Database(getBaseContext()).addToCart(deleteItem);
//
//                    //update total
//                    double total = 0;
//                    List<Order> orders = new Database(getBaseContext()).getCarts(bookId);
//                    for (Order item : orders)
//                        total += (Double.parseDouble(item.getPrice())) * (Double.parseDouble(item.getQuantity()));
//
//
//                    Locale locale = new Locale("en", "GH");
//                    NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
//
//                    txtTotalPrice.setText(fmt.format(total));
//
//                }
//            });
//
//            snackbar.setActionTextColor(Color.WHITE);
//            snackbar.show();
//
//        }
//    }
//
//    @Override
//    public void onPointerCaptureChanged(boolean hasCapture) {
//
//    }
//
//    private void paymentFunction() {
//
//        Toast.makeText(mContext, "please wait ... ", Toast.LENGTH_SHORT).show();
//
//        String formatAmount = price
//                .replace("GH₵", "")
//                .replace(",", "");
//
//        String orderId = getRandomString(5);
//
//
//        //send request  to database
//
//        Request request = new Request(
//                "null",
//                user_name,
//                txtTotalPrice.getText().toString(),
//                String.valueOf(System.currentTimeMillis()),
//                orderId,
//                "Not verified ",
//                cart
//        );
//
//        //submit to Firebase
//        String order_number = user_name;
//        requests.child(order_number)
//                .setValue(request);
//
//
//        new RavePayManager(this)
//                .setAmount(Double.parseDouble(formatAmount))
//                .setCountry("GH")
//                .setCurrency("GHS")
//                .setEmail(mAuth.getCurrentUser().getEmail())
//                .setfName(user_name)
//                .setlName(user_name)
//                .setNarration(bookName)
//                .setPublicKey(PublicKey)
//                .setEncryptionKey(EncryptionKey)
//                .setTxRef(orderId)
//                .acceptAccountPayments(true)
//                .acceptCardPayments(true)
//                .acceptGHMobileMoneyPayments(true)
//                .acceptBankTransferPayments(true)
//                .onStagingEnv(false)
//                .shouldDisplayFee(false)
//                .showStagingLabel(true)
//                .allowSaveCardFeature(true)
////                .setMeta(List < Meta >)
////                .withTheme(R.style.AppTheme)
//                .isPreAuth(true)
////                .setSubAccounts(List < SubAccount >)
//                .initialize();
//
////        Pay(this, bookName, Double.parseDouble(formatAmount), bookName, user_name,mAuth.getCurrentUser().getEmail(),orderId, formatNnumber, SLYDEPAY_REQUEST_CODE);
////                     Toasty.info(mContext, ""+orderId, Toast.LENGTH_LONG).show();
//
//    }
//
//    private void loadBook() {
//        cart = new Database(this).getCarts(bookId);
//
////        try {
////
////            cart = new Database(this).getCarts(bookId);
////        } catch (NullPointerException e) {
////            Log.e("NullCaught", "Try getting phone/bookid failed");
////        }
//
//        adapter = new CartAdapter(cart, this);
//        adapter.notifyDataSetChanged(); //handles delete referesh
//        recyclerView.setAdapter(adapter);
//
//        //Calculate total price
//        int total = 0;
//        for (Order order : cart)
//            total += (Double.parseDouble(order.getPrice())) * (Double.parseDouble(order.getQuantity())) -
//                     (Double.parseDouble(order.getQuantity()));
//
//
//        Locale locale = new Locale("en", "GH");
//        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
//
//        txtTotalPrice.setText(fmt.format(total));
//
//        //Calculate total discount
////        int discount = 0;
////        for (Order order : cart)
////            discount += (Double.parseDouble(order.getDiscount())) * (Double.parseDouble(order.getQuantity()));
//
////        txtDiscount.setText(fmt.format(discount));
//
//        //Calculate current  discount
//        int fee = 0;
//        for (Order order : cart)
//            fee += (Double.parseDouble(order.getPrice())) * (Double.parseDouble(order.getQuantity()));
//
//        for (Order order : cart)
//            mToolbar.setTitle(order.getProductName());//change
//
//        txtFee.setText(fmt.format(fee));
//
//
//        price = fmt.format(fee);
//
//
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case MY_PERMISSIONS_REQUEST_READ: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // permission was granted, yay! Do the
//                    // contacts-related task you need to do.
//                } else {
//                    // permission denied, boo! Disable the
//                    // functionality that depends on this permission.
//                }
//                return;
//            }
//
//            // other 'case' lines to check for other
//            // permissions this app might request.
//        }
//    }
//
//    private void deleteCart(int position) {
//
//        cart.remove(position);
//        //delete from sql
//
//        new Database(this).cleanCart(bookId);
//
//        for (Order item : cart)
//            new Database(this).addToCart(item);
//
//        loadBook();
//
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == RaveConstants.RAVE_REQUEST_CODE && data != null) {
//            String message = data.getStringExtra("response");
//            if (resultCode == RavePayActivity.RESULT_SUCCESS) {
//
//                Toast.makeText(mContext, "SUCCESS  " + message, Toast.LENGTH_SHORT).show();
//
//                //change access
//                books.child(bookId).child("Access")
//                        .child(mAuth.getCurrentUser().getUid())
//                        .setValue("allow")
//                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//
//                                Toast.makeText(mContext, "Doe !!!", Toast.LENGTH_SHORT).show();
//                            }
//
//                        }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//
//                        Toast.makeText(mContext, "error", Toast.LENGTH_SHORT).show();
//
//                    }
//                });
//
//                //
//                //delete cart
////                deleteCart(0);
//                finish();
//
//            } else if (resultCode == RavePayActivity.RESULT_ERROR) {
//
//                Toast.makeText(mContext, "ERROR" + message, Toast.LENGTH_SHORT).show();
//            } else if (resultCode == RavePayActivity.RESULT_CANCELLED) {
//
//                Toast.makeText(mContext, "CANCELLED " + message, Toast.LENGTH_SHORT).show();
//            }
//        } else {
//            super.onActivityResult(requestCode, resultCode, data);
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
////        deleteCart(0);
//        Toast.makeText(mContext, "CartBasicTerm1 items Emptied", Toast.LENGTH_SHORT).show();
//    }
}

package com.apps.norris.paywithslydepay.core;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.apps.norris.paywithslydepay.R;
import com.apps.norris.paywithslydepay.fragments.ConfirmPaymentFragment;
import com.apps.norris.paywithslydepay.fragments.ConfirmingTransactionFragment;
import com.apps.norris.paywithslydepay.fragments.LoadingFragment;
import com.apps.norris.paywithslydepay.fragments.MobileMoneyFragment;
import com.apps.norris.paywithslydepay.fragments.PaymentChoiceFragment;
import com.apps.norris.paywithslydepay.fragments.TransactionResponseFragment;
import com.apps.norris.paywithslydepay.models.AdminTimer;
import com.apps.norris.paywithslydepay.offline.PrefsManager;
import com.apps.norris.paywithslydepay.utils.Constants;
import com.apps.norris.paywithslydepay.utils.PaymentUtils;
import com.apps.norris.paywithslydepay.views.CustomTextView;
import com.apps.norris.paywithslydepay.views.CustomViewPager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class PayWithSlydepay extends AppCompatActivity {
    public CustomTextView next;
    private ImageView close;
    public CustomViewPager viewpager;
    private int count = 0;
    private String token = "";
    private String paymentCode = "";
    public String paymentOption = "";
    public String transactionResponse = "";
    private final int CHROME_CUSTOM_TAB_REQUEST_CODE = 100;
    private PrefsManager prefsManager;
    private static int RESULT_CODE;
    private static final String TAG = "Payment Selection";
    private boolean successful;
    static String getCustomerName;
    static double getAmount;
    static String getDescription;
    static String itemCode;
    static String getEmail;
    public String getPhoneNumber;
    static String getItemName;
    private final int MOBILE_MONEY_PAYMENT = 1;
    private final int CONFIRM_PAYMENT = 3;
    private final int LOADING = 2;
    private final int CHECKING_INVOICE = 4;
    private final int TRANSACTION_SUCCESS = 6;
    private final int TRANSACTION_FAILED = 8;
    private String timer;
    private Handler handler = new Handler();
    private boolean doubleBackToExitPressedOnce = false;

    public static void Pay(Activity context, String itemName, double amount, String description, String customerName, String customerEmail, String orderCode, String phoneNumber, int setRequestCode) {
        Bundle bundle = new Bundle();
        bundle.putDouble(Constants.AMOUNT, amount);
        bundle.putString(Constants.DESCRIPTION, description);
        bundle.putString(Constants.CUSTOMER_NAME, customerName);
        bundle.putString(Constants.CUSTOMER_EMAIL, customerEmail);
        bundle.putString(Constants.CUSTOMER_PHONE_NUMBER, phoneNumber);
        bundle.putString(Constants.ORDER_CODE, orderCode);
        bundle.putString(Constants.ITEM_NAME, itemName);
        bundle.putInt(Constants.REQUEST_CODE, setRequestCode);

        start(context, bundle);
    }

    public static void start(Activity context, Bundle bundle) {
        Intent intent = new Intent(context, PayWithSlydepay.class);
        intent.putExtras(bundle);
        context.startActivityForResult(intent, bundle.getInt(Constants.REQUEST_CODE));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_with_slydepay);
        initView();
        setupTabs();
        initListeners();
    }

    private void initView() {
        // Get a reference to your user
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference(getString(R.string.admin));

        // Attach a listener to read the data at your profile reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                /*retrieve milliseconbds from databse */
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    AdminTimer countDown = dataSnapshot.getValue(AdminTimer.class);
                    timer = countDown.getTimer();
                    Log.d("TIMER", "onDataChange: " + timer);
                }

                //without loop
                AdminTimer countDown = dataSnapshot.getValue(AdminTimer.class);

                if (countDown != null) {
                    if (!TextUtils.isEmpty(timer)) {
                        timer = countDown.getTimer();
                    } else timer = "51000"; //51000

                }
                Log.d("TIMER", "onDataChange: " + timer);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
//                timer = "50000";
                timer = "51000";
            }
        });


        next = (CustomTextView) findViewById(R.id.next);
        next.setTextColor(getResources().getColor(android.R.color.white));
        viewpager = (CustomViewPager) findViewById(R.id.viewpager);
        close = (ImageView) findViewById(R.id.close);

        prefsManager = new PrefsManager(PayWithSlydepay.this);

        Intent intent = getIntent();
        getDescription = intent.getStringExtra(Constants.DESCRIPTION);
        getCustomerName = intent.getStringExtra(Constants.CUSTOMER_NAME);
        getEmail = intent.getStringExtra(Constants.CUSTOMER_EMAIL);
        itemCode = intent.getStringExtra(Constants.ORDER_CODE);
        getPhoneNumber = intent.getStringExtra(Constants.CUSTOMER_PHONE_NUMBER);
        getItemName = intent.getStringExtra(Constants.ITEM_NAME);
        getAmount = intent.getDoubleExtra(Constants.AMOUNT, 0);

        if (TextUtils.isEmpty(getPhoneNumber))
            getPhoneNumber = "233";

        if (TextUtils.isEmpty(itemCode))
            itemCode = PaymentUtils.generateOrderCode();

    }

    private void initListeners() {
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getTextViewValue().equalsIgnoreCase("DONE"))
                    setResults(RESULT_CODE);
                else if (getTextViewValue().equalsIgnoreCase("CLICK HERE TO CONFIRM")) {
                    //enable after countdown reaches 0
                    next.setEnabled(true);
                    viewpager.setCurrentItem(CHECKING_INVOICE);
                    new CheckInvoiceStatus().execute();
                } else if (getTextViewValue().equalsIgnoreCase("MAKE PURCHASE")) {
                    viewpager.setCurrentItem(LOADING);
                    hideKeyboard();
                    new CreateInvoice().execute();
                } else if (getTextViewValue().equalsIgnoreCase("RETRY PAYMENT VERIFICATION")) {
                    next.setEnabled(true);
                    viewpager.setCurrentItem(CONFIRM_PAYMENT);

                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doExit();
            }
        });
    }

    private String getTextViewValue() {
        return next.getText().toString();
    }

    private void setupTabs() {
        paymentTabAdapter adapter = new paymentTabAdapter(getSupportFragmentManager());
        viewpager.setAdapter(adapter);
        viewpager.setOffscreenPageLimit(5);
        viewpager.setPagingEnabled(false); //false
        viewpager.setOffscreenPageLimit(0);


        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == MOBILE_MONEY_PAYMENT) {
                    if (next.getVisibility() == View.GONE)
                        next.setVisibility(View.VISIBLE);

                    next.setText("MAKE PURCHASE");

                    next.setEnabled(getPhoneNumber.length() == 12);

                } else if (position == LOADING) {
                    next.setVisibility(View.GONE);
                } else if (position == CHECKING_INVOICE) {
                    next.setVisibility(View.GONE);
                }
                /*Retry verification*/
                else if (position == TRANSACTION_FAILED) {
                    if (next.getVisibility() == View.GONE)
                        next.setVisibility(View.VISIBLE);
                    next.setText("RETRY PAYMENT VERIFICATION");
                } else if (position == CONFIRM_PAYMENT) {
                    if (next.getVisibility() == View.GONE)
                        next.setVisibility(View.VISIBLE);
                    next.setText("CONFIRM");
                    ConfirmButtonDelay();

                } else if (position == TRANSACTION_SUCCESS) {
                    if (next.getVisibility() == View.GONE)
                        next.setVisibility(View.VISIBLE);
                    next.setText("DONE");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void ConfirmButtonDelay() {
        next.setEnabled(false);
        getTimerCount();
        next.postDelayed(new Runnable() {
            @Override
            public void run() {
                next.setEnabled(true);
            }
        }, Long.parseLong(timer));

    }

    private void getTimerCount() {
        new CountDownTimer(Long.parseLong(timer), 1000) {

            public void onTick(long duration) {
                long Mmin = (duration / 1000) / 60;
                long Ssec = (duration / 1000) % 60;

                if (Ssec < 10) {
                    next.setText("Verifying please wait for the countdown " + Mmin + ":0" + Ssec + " remaining");


                } else
                    next.setText("Verifying please wait for the countdown " + Mmin + ":" + Ssec + " remaining");

//                next.setText(MessageFormat.format("please wait : {0} seconds remaining", duration / 1000));

                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                next.setText("CLICK HERE TO CONFIRM");
                next.setEnabled(true);
            }

        }.start();
    }

    public void startTransaction() {
        if (paymentOption.equalsIgnoreCase(Constants.VISA)) {
            new CreateInvoice().execute();
            viewpager.setCurrentItem(LOADING);
            Log.d(TAG, "startTransaction: Visa plus vf cash");
        } else {
            viewpager.setCurrentItem(MOBILE_MONEY_PAYMENT);

        }
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    private class paymentTabAdapter extends FragmentPagerAdapter {

        paymentTabAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "";
        }

        @Override
        public int getCount() {
            return 7;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            if (position == LOADING) {
                String msg;
                if (paymentOption.equalsIgnoreCase(Constants.VISA))
                    msg = getResources().getString(R.string.visa_loading_message);
                else if
                (paymentOption.equalsIgnoreCase(Constants.VODAFONE_CASH))
                    msg = getResources().getString(R.string.mm_loading_message);
                else if
                (paymentOption.equalsIgnoreCase(Constants.MTN_MOBILE_MONEY))
                    msg = getResources().getString(R.string.mm_loading_message);
                else
                    msg = getResources().getString(R.string.mm_other_loading_message);
                return LoadingFragment.newInstance(msg);


            } else if (position == MOBILE_MONEY_PAYMENT)
                return MobileMoneyFragment.newInstance();

            else if (position == CONFIRM_PAYMENT) {
                String msg, title;
                title = "CONFIRM PAYMENT";
                if (paymentOption.equalsIgnoreCase(Constants.VISA))
                    msg = getResources().getString(R.string.visa_confirm_message);
                else if (paymentOption.equalsIgnoreCase(Constants.MTN_MOBILE_MONEY))
                    msg = getResources().getString(R.string.mtn_confirm_message);
                else msg = getResources().getString(R.string.mm_confirm_message);
                return ConfirmPaymentFragment.newInstance(title, msg);
            } else if (position == CHECKING_INVOICE)
                return ConfirmingTransactionFragment.newInstance("Confirming Transaction..."); //string message pass in Fragments arg...
            else if (position == TRANSACTION_SUCCESS)
                /*Success or error messgae fragment here*/
                return TransactionResponseFragment.newInstance(successful, transactionResponse);
            else if (position == TRANSACTION_FAILED)
                return TransactionResponseFragment.newInstance(successful, transactionResponse);

            else
                return PaymentChoiceFragment.newInstance();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHROME_CUSTOM_TAB_REQUEST_CODE) {
            viewpager.setCurrentItem(CONFIRM_PAYMENT);
        }
    }

    public void setResults(int resultCodeHere) {
        Intent intent = new Intent();
        //intent.putExtra(PayWithUiUtils.ORDER_ID,orderId);
        setResult(resultCodeHere, intent);
        finish();
    }

    private class CreateInvoice extends AsyncTask<String, String, String> {
        private int success;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override

        protected String doInBackground(String... strings) {
            URL httpbinEndpoint;
            try {

                httpbinEndpoint = new URL("https://app.slydepay.com/api/merchant/invoice/create");
                HttpsURLConnection myConnection = (HttpsURLConnection) httpbinEndpoint.openConnection();
                myConnection.setRequestProperty("Content-Type", "application/json");

                myConnection.setRequestMethod("POST");

                JSONObject invoice = new JSONObject();
                invoice.put("emailOrMobileNumber", prefsManager.getMerchantEmailOrPhoneNumber());
                invoice.put("merchantKey", prefsManager.getMerchantKey());
                invoice.put("amount", getAmount);
                invoice.put("description", getDescription);
                invoice.put("orderCode", PaymentUtils.generateOrderCode());
                invoice.put("sendInvoice", true);
                invoice.put("payOption", paymentOption);
                invoice.put("customerName", getCustomerName);
                invoice.put("customerEmail", getEmail);
                if (paymentOption.equalsIgnoreCase(Constants.VISA))
                    invoice.put("customerEmail", getEmail);
                else {
                    invoice.put("customerMobileNumber", getPhoneNumber);
                }

                String myData = invoice.toString();

                // Enable Writing
                myConnection.setDoOutput(true);

                // Write the data
                myConnection.getOutputStream().write(myData.getBytes());

                success = 1;
                InputStream responseBody = myConnection.getInputStream();

                BufferedReader bR = new BufferedReader(new InputStreamReader(responseBody));
                String line;

                StringBuilder responseStrBuilder = new StringBuilder();
                while ((line = bR.readLine()) != null) {

                    responseStrBuilder.append(line);
                }
                responseBody.close();

                JSONObject result = new JSONObject(responseStrBuilder.toString());

                if (result.getBoolean("success")) {
                    success = 1;
                    JSONObject result1 = new JSONObject(result.getString("result"));
                    token = result1.getString("payToken");
                    paymentCode = result1.getString("orderCode");

                } else {
                    success = 0;
                }

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (success == 1) {
                if (paymentOption.equalsIgnoreCase(Constants.VISA)) {
                    startChromeTab();
                } else viewpager.setCurrentItem(CONFIRM_PAYMENT);
            } else {
                RESULT_CODE = RESULT_CANCELED;
            }
        }
    }

    private void startChromeTab() {
        String url = "https://app.slydepay.com/paylive/detailsnew.aspx?pay_token=" + token;
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(getResources().getColor(R.color.slydeGreen));
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.intent.setData(Uri.parse(url));
        startActivityForResult(customTabsIntent.intent, CHROME_CUSTOM_TAB_REQUEST_CODE);
        Log.d(TAG, "startChromeTab: start Browser");
    }

    //check payment invoice and confirm transaction
    public class CheckInvoiceStatus extends AsyncTask<String, String, String> {
        private int success;
        private String status = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            URL httpbinEndpoint;
            try {

                httpbinEndpoint = new URL("https://app.slydepay.com/api/merchant/invoice/checkstatus");
                HttpsURLConnection myConnection = (HttpsURLConnection) httpbinEndpoint.openConnection();
                myConnection.setRequestProperty("Content-Type", "application/json");
                myConnection.setRequestMethod("POST");
                JSONObject invoice = new JSONObject();
                invoice.put("emailOrMobileNumber", prefsManager.getMerchantEmailOrPhoneNumber());
                invoice.put("merchantKey", prefsManager.getMerchantKey());
                invoice.put("payToken", token);
                invoice.put("orderCode", paymentCode);
                invoice.put("confirmTransaction", true);

                String myData = invoice.toString();

                // Enable writing
                myConnection.setDoOutput(true);

                // Write the data
                myConnection.getOutputStream().write(myData.getBytes());

                success = 1;
                InputStream responseBody = myConnection.getInputStream();

                BufferedReader bR = new BufferedReader(new InputStreamReader(responseBody));
                String line;

                StringBuilder responseStrBuilder = new StringBuilder();
                while ((line = bR.readLine()) != null) {

                    responseStrBuilder.append(line);
                }
                responseBody.close();

                JSONObject result = new JSONObject(responseStrBuilder.toString());
                Log.v("SammieTest", "payment result " + result.toString());

                if (result.getBoolean("success")) {
                    success = 1;
                    status = result.getString("result");
                    return result.getString("errorMessage");
                } else {
                    success = 0;
                    return result.getString("errorMessage");
                }

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if (success == 1) {
                // if confirm payment button is true get value success message
                if (status.equalsIgnoreCase(Constants.CONFIRMED)) {
                    successful = true;
                    RESULT_CODE = RESULT_OK;
                    transactionResponse = String.format(getResources().getString(R.string.successful_purchase), getItemName, getAmount);
                    viewpager.setCurrentItem(TRANSACTION_SUCCESS);

                } else {
                    if (count != 7) {
                        new CheckInvoiceStatus().execute();
                        count++;
                    } else {

                        successful = false;
//                        transactionResponse = "Payment Verication Failed";
                        /*     enable retry button here*/
//                        Runnable runnable = new Runnable() {
//                            public void run() {
//                                while (true) {
//                                    new CheckInvoiceStatus().execute();
//                                    handler.postDelayed(this, 1000);
//                                }
////
//                            }
//
//                        };
//                        handler.postDelayed(runnable, 1000);

                        //                        MyRunnablePayService myRunnablePayService = new MyRunnablePayService();
//                        Thread thread = new Thread(myRunnablePayService);
//                        thread.start();
//
//                        try {
//                            Thread.sleep(3L * 1000L);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        myRunnablePayService.doStop();

                        RESULT_CODE = RESULT_CANCELED;
                        viewpager.setCurrentItem(TRANSACTION_FAILED);
                        next.setText("RETRY PAYMENT VERIFICATION");
                        timer = "4000";

//                        viewpager.setCurrentItem(TRANSACTION_RESPONSE);

                    }

                }
            } else setResults(RESULT_CANCELED);
        }
    }

    @Override
    public void onBackPressed() {
        doExit();
//        if (doubleBackToExitPressedOnce) {
//            super.onBackPressed();
////            RESULT_CODE = RESULT_CANCELED;
//            return;
//
//        }
//
//        this.doubleBackToExitPressedOnce = true;
//        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
//
//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                doubleBackToExitPressedOnce=false;
//            }
//        }, 2000);
    }

    /**
     * Exit the app if user select exit.
     */
    private void doExit() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                PayWithSlydepay.this);

        alertDialog.setPositiveButton("Exit", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                setResults(RESULT_FIRST_USER);
                finish();
            }
        });

        alertDialog.setNegativeButton("Stay", null);

        alertDialog.setMessage("Do you want to exit payment page ?");
        alertDialog.setTitle("Cancel Payment !");
        alertDialog.show();
    }

//    public class MyRunnablePayService implements Runnable {
//
//        private boolean doStop = false;
//
//        public synchronized void doStop() {
//            this.doStop = true;
//        }
//
//        private synchronized boolean keepRunning() {
//            return this.doStop == false;
//        }
//
//        @Override
//        public void run() {
//            while (keepRunning()) {
//                // keep doing what this thread should do.
//                System.out.println("Running");
//
//                try {
//                    Thread.sleep(3L * 1000L);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }
//    }

}

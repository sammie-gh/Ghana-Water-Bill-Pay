package com.gh.sammie.ghanawater.Activities;

import static com.gh.sammie.ghanawater.Common.Common.EncryptionKey;
import static com.gh.sammie.ghanawater.Common.Common.PublicKey;
import static com.gh.sammie.ghanawater.Common.Common.getRandomString;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.flutterwave.raveandroid.RavePayActivity;
import com.flutterwave.raveandroid.RaveUiManager;
import com.flutterwave.raveandroid.rave_java_commons.RaveConstants;
import com.gh.sammie.ghanawater.Common.Common;
import com.gh.sammie.ghanawater.R;
import com.gh.sammie.ghanawater.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BillActivity extends AppCompatActivity {
    TextView txt_banner_balance, meterId, customer_name;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private User user_info;
    private TextView btn_Pay;
    private DatabaseReference history, mUserDatabase;

    String bill_desc, meter_id, meter_bill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


        history = database.getReference("History").child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
        //views
        txt_banner_balance = findViewById(R.id.txt_banner_balance);
        meterId = findViewById(R.id.meterId);
        btn_Pay = findViewById(R.id.btn_Pay);
        customer_name = findViewById(R.id.customer_name);


        /*set clicks*/
        btn_Pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ravePayBill();
            }
        });
        userInformationDatabase();
    }

    private void ravePayBill() {

        new RaveUiManager(BillActivity.this).setAmount(5)
                .setCurrency("GHS")
                .setEmail(mAuth.getCurrentUser().getEmail())
                .setfName(mAuth.getCurrentUser().getDisplayName() + " buying " + " " + mAuth.getCurrentUser().getUid())
                .setNarration(mAuth.getCurrentUser().getDisplayName() + " buying " + mAuth.getCurrentUser().getUid())
                .setPublicKey(PublicKey)
                .setEncryptionKey(EncryptionKey)
                .setTxRef(getRandomString(7))
                .acceptGHMobileMoneyPayments(true)
                .acceptAccountPayments(true)
                .acceptCardPayments(true)
                .acceptMpesaPayments(true)
                .acceptAchPayments(true)
                .acceptUgMobileMoneyPayments(true)
                .acceptZmMobileMoneyPayments(true)
                .acceptRwfMobileMoneyPayments(true)
                .acceptSaBankPayments(true)
                .acceptUkPayments(true)
                .acceptBankTransferPayments(true)
                .acceptUssdPayments(true)
                .acceptBarterPayments(true)
                .allowSaveCardFeature(true)
                .acceptBankTransferPayments(true)
                .onStagingEnv(false)
                .shouldDisplayFee(false)
                .showStagingLabel(true)
                .allowSaveCardFeature(true)
//                .setMeta(List < Meta >)
//                .withTheme(R.style.AppTheme)
                .isPreAuth(true)
//                .setSubAccounts(List < SubAccount >)
                .initialize();


    }

    private void userInformationDatabase() {
        mUserDatabase = database.getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        mUserDatabase.keepSynced(true);
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user_info = dataSnapshot.getValue(User.class);

                if (user_info != null) {
                    txt_banner_balance.setText("GHS " + user_info.getBillTotal());
                    meterId.setText("Meter ID #" + user_info.getMeter());
                    customer_name.setText(user_info.getName());
                    bill_desc = user_info.getName();
                    meter_id = user_info.getMeter();
                    meter_bill = user_info.getBillTotal();

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void SaveToHistory(String payment_failed) {

        String key = mUserDatabase.push().getKey();
        String val = Common.getRandomString(10);
//        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//        String timestamp = ServerValue.TIMESTAMP.toString();

        if (key != null) {
            Map<String, Object> payment_details = new HashMap<String, Object>();
            payment_details.put("status", "Bill Paid");
            payment_details.put("desc", "Bill Paid By " + bill_desc);
            payment_details.put("price", meter_bill);
            payment_details.put("id", meter_id);
            payment_details.put("timeStamp", ServerValue.TIMESTAMP);
            payment_details.put("actionPerformed", payment_failed);
            payment_details.put("email", Objects.requireNonNull(mAuth.getCurrentUser()).getEmail());
            history.child(val).setValue(payment_details)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
//                        successPaymentSweetDialog("Payment verified successfully !");
                            Log.d("TAG", "onComplete: History added successfully");

                        }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(BillActivity.this, "error" + e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });

//            books.child(bookId).child("DownloadList")
//                    .child(key)
//                    .setValue("File downloaded by " + mAuth.getCurrentUser().getEmail() +
//                            " On:" + timestamp)
//                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//                            Log.d("BulkBookDetail", "onSuccess: Record added");
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Log.d("BulkBookDetail", "onSuccess: Record adding Failed");
//                }
//            });
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RaveConstants.RAVE_REQUEST_CODE && data != null) {
            String message = data.getStringExtra("response");
            if (resultCode == RavePayActivity.RESULT_SUCCESS) {

                SaveToHistory("Payment success");
                Toast.makeText(this, "SUCCESS " + message, Toast.LENGTH_LONG).show();
            } else if (resultCode == RavePayActivity.RESULT_ERROR) {
                Toast.makeText(this, "ERROR " + message, Toast.LENGTH_LONG).show();
            } else if (resultCode == RavePayActivity.RESULT_CANCELLED) {
                Toast.makeText(this, "CANCELLED " + message, Toast.LENGTH_LONG).show();
                SaveToHistory("Payment failed");

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


}
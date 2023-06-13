package com.gh.sammie.ghanawater.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.gh.sammie.ghanawater.R;
import com.gh.sammie.ghanawater.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DigitalWalletHomePage extends AppCompatActivity {
    private String userName, userEmail, meterNumber;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private User user_info;
    TextView txt_meter_id, txt_name;
    EditText edt_total_bill;
    LinearLayout btn_account, btn_bill, btn_history, btn_alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_digital_wallet_home_page);

        //Firebase
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        //views
        txt_meter_id = findViewById(R.id.txt_meter_id);
        txt_name = findViewById(R.id.txt_name);
        btn_account = findViewById(R.id.btn_account);
        btn_bill = findViewById(R.id.btn_bill);
        btn_history = findViewById(R.id.btn_history);
        btn_alert = findViewById(R.id.btn_alert);
        edt_total_bill = findViewById(R.id.edt_total_bill);


        userInformationDatabase();

        //setClicks
        btn_bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DigitalWalletHomePage.this, BillActivity.class);
                startActivity(intent);
            }
        });

        btn_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DigitalWalletHomePage.this, AcountWallet.class);
                startActivity(intent);
            }
        });
        btn_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DigitalWalletHomePage.this, HistoryActivity.class);
                startActivity(intent);
            }
        });

        btn_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DigitalWalletHomePage.this, HistoryActivity.class);
                startActivity(intent);
            }
        });


    }

    private void userInformationDatabase() {
        DatabaseReference mUserDatabase = database.getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        mUserDatabase.keepSynced(true);
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user_info = dataSnapshot.getValue(User.class);

                if (user_info != null) {
                    txt_meter_id.setText("Meter ID #" + user_info.getMeter());
                    txt_name.setText(user_info.getName());
                    edt_total_bill.setText("Total Bill GHS" + " " + user_info.getBillTotal());

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
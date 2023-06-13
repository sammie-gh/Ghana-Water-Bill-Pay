package com.gh.sammie.ghanawater.Activities;

import android.os.Bundle;
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

public class AcountWallet extends AppCompatActivity {
    TextView txt_banner_meter_id, meterId, customer_name;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;

    private User user_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acount_wallet);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        //views
        txt_banner_meter_id = findViewById(R.id.txt_banner_meter_id);
        meterId = findViewById(R.id.meterId);
        customer_name = findViewById(R.id.customer_name);

        userInformationDatabase();
    }

    private void userInformationDatabase() {
        DatabaseReference mUserDatabase = database.getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        mUserDatabase.keepSynced(true);
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user_info = dataSnapshot.getValue(User.class);

                if (user_info != null) {
                    txt_banner_meter_id.setText("Meter ID #" + user_info.getMeter());
                    meterId.setText("Meter ID #" + user_info.getMeter());
                    customer_name.setText(user_info.getName());


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
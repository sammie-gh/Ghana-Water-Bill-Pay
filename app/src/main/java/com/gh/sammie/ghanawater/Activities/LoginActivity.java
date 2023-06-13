package com.gh.sammie.ghanawater.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.gh.sammie.ghanawater.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginActivity extends AppCompatActivity {
    private final static int RC_SIGN_IN = 2;

    private TextInputEditText email, password;
    private FirebaseAuth mAuth;
    private CardView login;
    private ProgressDialog pdialog;

    private ProgressDialog mProgressDialog;
    private static final String TAG = "GoogleActivity";
    private DatabaseReference mDatabase;
    Uri personPhoto;
    private AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);


        mAuth = FirebaseAuth.getInstance();


        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signinWithmail();
            }
        });


    }


    private void showProgressDialog() {

        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("loading...");
            mProgressDialog.setTitle("Please wait");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setIndeterminate(true);

        }

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("loading...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();


    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void signinWithmail() {
        String useremail = email.getText().toString();
        String userpassword = password.getText().toString();

        if (TextUtils.isEmpty(useremail)) {
            email.setError("Please enter your email");
        } else if (!useremail.contains("@") || !useremail.contains(".")) {
            email.setError("Please enter a valid Email");
        } else if (TextUtils.isEmpty(userpassword)) {
            password.setError("Please enter your password");
        } else {
            pdialog = new ProgressDialog(LoginActivity.this, R.style.MyAlertDialogStyle);
            pdialog.setMessage("Please wait...");
            pdialog.setIndeterminate(true);
            pdialog.setCancelable(false);
            pdialog.setCanceledOnTouchOutside(false);
            pdialog.show();

            mAuth.signInWithEmailAndPassword(useremail, userpassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                moveToDashBoard();
                                pdialog.dismiss();
                            } else {
                                String message = task.getException().toString();
                                if (message.contains("password is invalid")) {
                                    pdialog.dismiss();
                                    responseWarningSweetDialog("Sorry!!!", "Email or Password is Incorrect please check and try again");
//                                    Toast.makeText(LoginActivity.this, "Email or Password is Incorrect", Toast.LENGTH_LONG).show();
                                } else if (message.contains("There is no user")) {
                                    pdialog.dismiss();
//                                    Toast.makeText(LoginActivity.this, "Account doesn't exists", Toast.LENGTH_LONG).show();
                                    responseErrorSweetDialog("Sorry!!!", "Account doesn't exists please check well and try again");
                                } else {
                                    pdialog.dismiss();
                                    responseWarningSweetDialog("\uD83E\uDD37\u200D♂️Oops\uD83E\uDD37\u200D♂", "Unable to login kindly try again");
//                                    Toast.makeText(LoginActivity.this, "Unable to Login !", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
        }
    }





    private void saveUserDetails(String name, String url) {

        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = current_user.getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("image", personPhoto.toString());
        user.put("url", url);
        user.put("password", "googleAuth");


        mDatabase.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {

                    Toast.makeText(LoginActivity.this, "Database creation success", Toast.LENGTH_SHORT).show();
                    moveToDashBoard();

                } else
                    Toast.makeText(getApplicationContext(), "Something went wrong :(", Toast.LENGTH_LONG).show();

            }
        });

    }

    private void moveToDashBoard() {
        Intent intent = new Intent(LoginActivity.this, DigitalWalletHomePage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void responseErrorSweetDialog(String txtTitle, String message) {
        Log.d("SAMMIE", "ResponseDialogPaymentSweetDialog: " + message);
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(txtTitle)
                .setContentText(message)
                .show();
    }

    private void responseWarningSweetDialog(String txtTitle, String message) {
        Log.d("SAMMIE", "ResponseDialogPaymentSweetDialog: " + message);
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(txtTitle)
                .setContentText(message)
                .show();
    }

    private void buttonsState(boolean option) {
        login.setEnabled(option);


    }

    public void backMTHD(View view) {
        finish();
    }
}

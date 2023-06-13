package com.gh.sammie.ghanawater.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.gh.sammie.ghanawater.Common.Common;
import com.gh.sammie.ghanawater.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class RegisterActivity extends AppCompatActivity {
    public static final int PICK_IMAGE = 1;
    private final static int RC_SIGN_IN = 2;
    private static final String TAG = "REGISTER";
    private CardView signup;
    private GoogleSignInClient mGoogleSignInClient;
    private ImageView avatar;
    private TextInputEditText fullname, email, password, edtPhone, edtMeterID;
    private Uri mainImageUri;
    private FirebaseAuth mAuth;
    private ProgressDialog pdialog;
    private DatabaseReference mDatabase;
    private String photoLink;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registry);

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        fullname = findViewById(R.id.edtName);
        email = findViewById(R.id.edtEmail);
        password = findViewById(R.id.edtPassword);
        edtMeterID = findViewById(R.id.edtMeterID);
        edtPhone = findViewById(R.id.edtPhone);
        signup = findViewById(R.id.signupryt);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String name = fullname.getText().toString();
                final String mail = email.getText().toString();
                final String pwd = password.getText().toString();
                final String strphone = edtPhone.getText().toString();
                final String strMeterId = edtMeterID.getText().toString();

                String newPass = Common.getEncryptedString(pwd);

                Log.d(TAG, "onClick: " + newPass);

                if (checkFields(name, mail, pwd, strphone, strMeterId)) {
                    pdialog = new ProgressDialog(RegisterActivity.this, R.style.MyAlertDialogStyle);
                    pdialog.setMessage("Signing up...");
                    pdialog.setIndeterminate(true);
                    pdialog.setCanceledOnTouchOutside(false);
                    pdialog.setCancelable(false);
                    pdialog.show();

                    //check Meter ID
                    DatabaseReference root = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference users = root.child("Meters");
                    users.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (snapshot.child(strMeterId).exists()) {
                                // run some code
                                createuser(mail, newPass, name, strphone, strMeterId);
                            } else {
                                pdialog.dismiss();
                                responseErrorSweetDialog("Meter Number " ,"Meter number does not exist contact Admin");
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }
            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);

            } catch (ApiException e) {


                Log.d(TAG, "onActivityResult: " + e.getMessage());
                responseErrorSweetDialog("🤷‍♂️Oops🤷‍♂", "Something went wrong please try again");
//                Toast.makeText(RegisterActivity.this, "Something went wrong :(", Toast.LENGTH_LONG).show();
            }
        }
    }


    private void saveUserDetails(String name, String url, String password, String phone, String meter_id) {

        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = current_user.getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("image", "image");
        user.put("url", url);
        user.put("phone", phone);
        user.put("meter", meter_id);
        user.put("password", password);
        user.put("billTotal", "0");


        mDatabase.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {

                    Toast.makeText(RegisterActivity.this, "Database creation success", Toast.LENGTH_SHORT).show();
                    moveToDashBoard();

                } else
                    Toast.makeText(getApplicationContext(), "Something went wrong :(", Toast.LENGTH_LONG).show();

            }
        });

    }


    private void createuser(final String email, final String password, final String name, String phone_number, String meter_id) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            signIn(email, password, name, phone_number, meter_id);
                        } else {
                            if (task.getException().toString().contains("already in use")) {
                                pdialog.dismiss();
                                responseWarningSweetDialog("Sorry !", "User already exists, please sign in your correct credentials");
//                                Toast.makeText(RegisterActivity.this, "User already exists, please sign in !", Toast.LENGTH_LONG).show();
                            } else {
                                pdialog.dismiss();
//                                Toast.makeText(RegistryActivity.this, "" + password, Toast.LENGTH_SHORT).show();
                                responseErrorSweetDialog("Sorry!", "Something wrong happened please try again later or use Easy signUp with google");
//                                Toast.makeText(RegisterActivity.this, "Something wrong happened please try again later or use Easy signUp with google", Toast.LENGTH_LONG).show();
                            }

                        }
                    }
                });

    }

    private void signIn(final String email, final String password, final String name, String phone, String meter_id) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    saveUserDetails(name, email, password, phone, meter_id);
                } else {
                    pdialog.dismiss();
                    responseErrorSweetDialog("🤷‍♂️Oops🤷‍♂", "Something went wrong please try again");
//                    Toast.makeText(RegisterActivity.this, "Something went wrong :(", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean checkFields(String name, String mail, String pwd, String strphone, String meterId) {

        if (TextUtils.isEmpty(name)) {
            fullname.setError("Name can't be empty");
        }
        if (TextUtils.isEmpty(mail)) {
            email.setError("Email can't be empty");
        }
        if (TextUtils.isEmpty(pwd)) {
            password.setError("Please enter a password");
        }

        if (TextUtils.isEmpty(strphone)) {
            edtPhone.setError("Please enter phone");
        }
        if (TextUtils.isEmpty(meterId)) {
            edtMeterID.setError("Please enter Meter ID");
        }

        if (name.length() < 6) {
            fullname.setError("Name must be of at least 6 characters");
        } else if (!mail.contains("@") || !mail.contains(".")) {
            email.setError("Please enter a valid email");
        } else if (pwd.length() < 6) {
            password.setError("Password must be of at least 6 characters");
        }else {
            return true;
        }

//        else if (!cnfpwd.matches(pwd)) {
//            password.setError("Passwords don't match !");
//            cnfpassword.setError("Passwords don't match !");
//        } else {
//            return true;
//        }

        return false;
    }



    private void moveToDashBoard() {
        Intent mainIntent = new Intent(RegisterActivity.this, DigitalWalletHomePage.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
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


    public void backMTHDs(View view) {
        finish();
    }
}

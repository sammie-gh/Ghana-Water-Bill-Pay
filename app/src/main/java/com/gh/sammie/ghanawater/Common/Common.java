package com.gh.sammie.ghanawater.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Common {
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;
    //high school
    public static final String HIGH_SCHOOL_CLASSES = "HighSchoolClasses";
    public static final String HIGH_SCHOOL_TERM_1_DATABASE = "HighSchoolTerm1Book";
    public static final String HIGH_SCHOOL_TERM_2_DATABASE = "HighSchoolTerm2Book";
    public static final String HIGH_SCHOOL_TERM_3_DATABASE = "HighSchoolTerm3Book";
    public static final String downloadDirectory = "Lesson plans";
    public static ArrayList<File> fileList = new ArrayList<File>();
    public static final String DELETE = "Delete";
    public static final String PDF = "V0VTRkFZT0xTQU1NSUU3Nw==";
    public static final String BASIS_SCHOOL_TERM_1_DATABASE = "LessonNotesBasicTerm1Book";
    public static final String BASIS_SCHOOL_TERM_2_DATABASE = "LessonNotesBasicTerm2Book";
    public static final String BASIS_SCHOOL_TERM_3_DATABASE = "LessonNotesBasicTerm3Book";
    public static final String BULK_DATABASE = "BulkDatabase";
    public static final String FREEBIES_DATABASE = "Freebies";
    //   public static String  merchantEmail = "inspirationkairos@gmail.com";
    //    public static String  merchantKey = "1543858049193";
    public static final String NOTIFICATIONS = "notifications_enabled";
    public static final String PREFS = "prefs";
    private static final String ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm";
    public static String CATEGORY_SELECTED = "My Favorite books";
 

    public static boolean isConnectedToInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }

        return false;
    }

    public static String getRandomString(final int sizeOfRandomString) {
        final Random random = new Random();
        final StringBuilder sb = new StringBuilder(sizeOfRandomString);
        for (int i = 0; i < sizeOfRandomString; ++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

    private static String priceWithDecimal(Double price) {
        DecimalFormat formatter = new DecimalFormat("###,###,###.00");
        return formatter.format(price);
    }

    public static String priceWithoutDecimal(Double price) {
        DecimalFormat formatter = new DecimalFormat("###,###,###.##");
        return formatter.format(price);
    }

    public static class CopyTo {
        public static void copyFiles(File sourceLocation, File targetLocation)
                throws IOException {

            if (sourceLocation.isFile()) {
                Log.d("sourceLocation", "Exist: ");
                if (!targetLocation.exists()) {
                    targetLocation.mkdir();
                }
                InputStream inStream = null;
                OutputStream outStream = null;
                try {

                    File afile = new File("C:\\folderA\\Afile.txt");
                    File bfile = new File("C:\\folderB\\Afile.txt");

                    inStream = new FileInputStream(afile);
                    outStream = new FileOutputStream(bfile);

                    byte[] buffer = new byte[1024];

                    int length;
                    //copy the file content in bytes
                    while ((length = inStream.read(buffer)) > 0) {
                        outStream.write(buffer, 0, length);
                    }

                    inStream.close();
                    outStream.close();

                    //delete the original file
                    afile.delete();

                    System.out.println("File is copied successful!");

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else Log.d("sourceLocation", "Does not exist: ");
        }
    }

    public static String getEncryptedString(String value) {
        try {
            byte[] key = new byte[16];
            byte[] input = new byte[16];
            return Base64.encodeToString(encrypt(value.getBytes("UTF-8"), key, input), Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    private static byte[] encrypt(byte[] data, byte[] key, byte[] ivs) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
            byte[] finalIvs = new byte[16];
            int len = ivs.length > 16 ? 16 : ivs.length;
            System.arraycopy(ivs, 0, finalIvs, 0, len);
            IvParameterSpec ivps = new IvParameterSpec(finalIvs);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivps);
            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void loadPdfSize(String pdfUrl, String pdfTitle, TextView sizeTv) {
        String TAG = "PDF_SIZE_TAG";

        try {
            StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(pdfUrl);
            ref.getMetadata()
                    .addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                        @Override
                        public void onSuccess(StorageMetadata storageMetadata) {
                            //get size in bytes
                            double bytes = storageMetadata.getSizeBytes();
                            Log.d(TAG, "onSuccess: " + pdfTitle + " " + bytes);
                            //convert bytes to KB ,Mb
                            double kb = bytes / 1024;
                            double mb = kb / 1024;
                            if (mb >= 1) {
                                sizeTv.setText("Doc size: " + String.format("%.2f", mb) + " MB");
                            } else if (kb >= 1) {
                                sizeTv.setText("Doc size: " + String.format("%.2f", kb) + " KB");
                            } else {
                                sizeTv.setText("Doc size: " + String.format("%.2f", kb) + " bytes");
                            }


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure: " + e.getMessage());

                }
            });

        } catch (IllegalArgumentException e) {
            Log.e(TAG, "loadPdfSize: " + e.getMessage());
        }

        //load metadata from storage


    }

//    public static void updateToken(Context context, String token) {
//        DatabaseReference TokenStore;
//        FirebaseDatabase database;
//
//        FirebaseAuth s = FirebaseAuth.getInstance();
//
//        if (s.getCurrentUser() != null) {
//            MyToken myToken = new MyToken();
//            myToken.setToken(token);
//            myToken.setTokenType(TOKEN_TYPE.CLIENT);
//            myToken.setUid(s.getUid());
//
//
//            database = FirebaseDatabase.getInstance();
//            TokenStore = database.getReference("TokenStore");
//
//            TokenStore.child("Tokens").child("DownloadList")
//                    .child(Objects.requireNonNull(mAuth.getCurrentUser()).getDisplayName())
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
//
//            FirebaseDatabase.getInstance()
//                    .collection("Tokens")
//                    .document(s.getUid()) //to change to use member login change to userphone but only update token if user update profile in homeactivty or make uppdate to change profile
//                    .set(myToken)
//                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//
//                        }
//                    });
//
//        }
//    }

    public static String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }

        // TODO: localize
        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }

    public static final String IAPG = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhhO7pZwAV82rGuuowXkBC7FvKyxCIWX2qLhk6vnyKgja46AsO4YOivGblIRKP5fZyXLRjKlQvqFhRnrpQ9LP8RFTtt4d0C807ScpOwyVpVOPXAfmoDhqBpDKsO9lMOOHcKDY+ky57viFde8d7lqO8Bs8ZTtvccXpeiE1EdW+NkaC7ReIwIbrLFnjiUcUuCY4Cnp0JDxyTsr2GBSkjP+GpbejnF+H2Ws0B9hCEbOhD84I6gwwHyqRDSlCsaFuskPrSp4jRZOerRNpLQqZD3jbvXux84XJTuyEp+LpbPoOusKsUYkwfNDOvYnRAM/p0PGoTEsEBD8RWOelCdmAIqDJ8QIDAQAB";

    public static String priceToString(Double price) {
        String toShow = priceWithoutDecimal(price);
        if (toShow.indexOf(".") > 0) {
            return priceWithDecimal(price);
        } else {
            return priceWithoutDecimal(price);
        }
    }


    public static class randomStringGenerator {
        public void main(String[] args) {
            System.out.println(generateString());
        }

        public String generateString() {
            String uuid = UUID.randomUUID().toString();
            return "uuid = " + uuid;
        }
    }


    public static class LinearLayoutManagerWrapper extends LinearLayoutManager {

        public LinearLayoutManagerWrapper(Context context) {
            super(context);
        }

        public LinearLayoutManagerWrapper(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        public LinearLayoutManagerWrapper(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        @Override
        public boolean supportsPredictiveItemAnimations() {
            return false;
        }
    }

}

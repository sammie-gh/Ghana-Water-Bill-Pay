//package com.gh.sammie.lessonplan.Payment;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.app.Application;
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//import android.widget.Toast;
//
//import com.gh.sammie.lessonplan.Application.LessonPlanApp;
//import com.gh.sammie.lessonplan.model.APIResponse;
//import com.gh.sammie.lessonplan.R;
//import com.gh.sammie.lessonplan.network.APIUtils;
//import com.gh.sammie.lessonplan.network.ApiInterface;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class WebViewActivity extends AppCompatActivity {
//    private WebView web;
//    private String url, orderCode, payToken;
//    private ApiInterface apiClient;
//   private ProgressDialog dialog;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_web_view);
//
//        url = getIntent().getStringExtra("url");
//        orderCode = getIntent().getStringExtra("orderCode");
//        payToken = getIntent().getStringExtra("payToken");
//        apiClient = APIUtils.getAPIService();
//        setContentView(R.layout.activity_web_view);
//        web = (WebView) findViewById(R.id.web);
//        web.getSettings().setJavaScriptEnabled(true);
//        web.loadUrl(url);
//
//        web.setWebViewClient(new WebViewClient() {
//            @Override
//            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                super.onPageStarted(view, url, favicon);
//
//                if (url.contains(LessonPlanApp.CALLBACK_URL)) {
//                    //close the webview
//                    view.destroy();
//                    //create a progress dialog and show to the user
//                    dialog = new ProgressDialog(WebViewActivity.this);
//                    dialog.setMessage("Confirming Transaction, Please Wait");
//                    dialog.show();
//                    //create a HashMap of the parameters required for the check status call
//
//                    Map<String, Object> map;
//                    map = new HashMap();
//                    map.put("emailOrMobileNumber", LessonPlanApp.EMAIL_OR_MOBILE_NUMBER);
//                    map.put("merchantKey", LessonPlanApp.MERCHANT_KEY);
//                    map.put("orderCode", orderCode);
//                    map.put("payToken", payToken);
//                    map.put("confirmTransaction", true);
//
//
//                    //Then we call the check status function in our apiClient and override the
//                    apiClient.checkStatus(map).enqueue(new Callback<APIResponse>() {
//                        @Override
//                        public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
//                            dialog.dismiss();
//                            if (response.body().getSuccess()) {
//                                String result = response.body().getResult().toString();
//                                if (result.equals("CONFIRMED")) {
//                                    navigateToSuccessPage();
//
//                                } else if (result.equals("PENDING")) {
//
//                                } else if (result.equals("CANCELLED")) {
//
//                                } else if (result.equals("DISPUTED")) {
//
//                                }
//                            } else {
//                                if (response.body().getErrorMessage() != null) {
//                                    dialog.dismiss();
//                                    Toast.makeText(WebViewActivity.this, response.body().getErrorMessage(), Toast.LENGTH_LONG).show();
//                                }
//                            }
//
//
//                        }
//
//
//                        @Override
//                        public void onFailure(Call<APIResponse> call, Throwable t) {
//                            dialog.dismiss();
//                            Toast.makeText(WebViewActivity.this, "Failed please contact your administrator", Toast.LENGTH_LONG).show();
//                        }
//                    });
//                }
//            }
//
//        });
//
//    }
//
//    private void navigateToSuccessPage(){
////        Intent intent = new Intent(WebViewActivity.this, SuccessActivity.class);
////        startActivity(intent);
////        finish();
//    }
//
//}

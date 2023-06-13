//package com.gh.sammie.lessonplan.network;
//
//import com.gh.sammie.lessonplan.model.APIResponse;
//
//import java.util.Map;
//
//import retrofit2.Call;
//import retrofit2.http.Body;
//import retrofit2.http.Headers;
//import retrofit2.http.POST;
//
//
///**
// * Created by Tonte on 6/15/17.
// */
//
//public interface ApiInterface {
//    @Headers({
//            "Accept: application/json",
//            "Content-Type:application/json"
//    })
//
//    @POST("invoice/create")
//    Call<APIResponse> createInvoice(@Body Map<String, Object> options);
//    @POST ("invoice/checkstatus")
//    Call<APIResponse> checkStatus(@Body Map<String, Object> options);
//
//
//}

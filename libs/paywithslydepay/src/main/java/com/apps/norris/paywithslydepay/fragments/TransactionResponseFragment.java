package com.apps.norris.paywithslydepay.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.apps.norris.paywithslydepay.R;
import com.apps.norris.paywithslydepay.core.PayWithSlydepay;
import com.apps.norris.paywithslydepay.views.CustomTextView;
import com.apps.norris.paywithslydepay.views.CustomViewPager;

public class TransactionResponseFragment extends Fragment {
    private static final String ARG_MESSAGE = "message";
    private static final String ARG_SUCCESS = "success";
    private boolean success;
    private String message;
    public CustomViewPager viewpager;


    public TransactionResponseFragment() {

        // Required empty public constructor
    }

    public static TransactionResponseFragment newInstance(boolean success, String message) {
        Bundle args = new Bundle();
        args.putString(ARG_MESSAGE, message);
        args.putBoolean(ARG_SUCCESS, success);
        TransactionResponseFragment fragment = new TransactionResponseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            success = getArguments().getBoolean(ARG_SUCCESS);
            message = getArguments().getString(ARG_MESSAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transaction_response, container, false);

        CustomTextView titleTV = view.findViewById(R.id.title);
        CustomTextView messageTV = view.findViewById(R.id.message);
        ImageView status = view.findViewById(R.id.status);
        Button btnStatus = view.findViewById(R.id.btn_verify);


        String title;
        if (success) {
            title = "Success";
            status.setImageResource(R.drawable.success);
        } else {
//            btnStatus.setVisibility(View.VISIBLE);
//            btnStatus.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    Toast.makeText(getContext(), "Retrying", Toast.LENGTH_SHORT).show();
//                }
//            });
            title = "If you are debited and still unable to download your notes, contact Admin.\n Please click on the below Button to proceed";
            status.setImageResource(R.drawable.ic_caution);
        }

        titleTV.setText(title);
        messageTV.setText(message);

        return view;

    }

}

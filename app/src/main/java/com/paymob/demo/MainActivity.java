package com.paymob.demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.paymob.acceptsdk.IntentConstants;
import com.paymob.acceptsdk.PayActivity;
import com.paymob.acceptsdk.PayActivityIntentKeys;
import com.paymob.acceptsdk.PayResponseKeys;
import com.paymob.acceptsdk.SaveCardResponseKeys;
import com.paymob.acceptsdk.ToastMaker;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button button1;
    Button button2;
    Button button3;

    // Arbitrary number and used only in this activity. Change it as you wish.
    static final int ACCEPT_PAYMENT_REQUEST = 10;

    // Replace this with your actual payment key
    final String paymentKey = "ZXlKaGJHY2lPaUpJVXpVeE1pSXNJblI1Y0NJNklrcFhWQ0o5LmV5SmhiVzkxYm5SZlkyVnVkSE1pT2pFd01Dd2lkWE5sY2w5cFpDSTZNVEU1TENKamRYSnlaVzVqZVNJNklrVkhVQ0lzSW1sdWRHVm5jbUYwYVc5dVgybGtJam94TWpFc0ltOXlaR1Z5WDJsa0lqb3lOall3TXpOOS5SVHd6RmlZWGlEQkhfRTFqUzhlZ0ZrR1VsSlpxM3p5LWxoQW1BMW5LbU82Wm9iODRMc3FWSnNLQjEtdGRxbUpVQXgxSnVhNUZaN0tPNjV2T2pSMzVMQQ==";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button1 = findViewById(R.id.Button1);
        button1.setOnClickListener(this);
        button2 = findViewById(R.id.Button2);
        button2.setOnClickListener(this);
        button3 = findViewById(R.id.Button3);
        button3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Button1:
                startPayActivityNoToken(true);
                break;
            case R.id.Button2:
                startPayActivityNoToken(false);
                break;
            case R.id.Button3:
                startPayActivityToken();
                break;
        }
    }

    private void startPayActivityNoToken(Boolean showSaveCard) {
        Intent pay_intent = new Intent(this, PayActivity.class);

        putNormalExtras(pay_intent);
        pay_intent.putExtra(PayActivityIntentKeys.SAVE_CARD_DEFAULT, true);
        pay_intent.putExtra(PayActivityIntentKeys.SHOW_ALERTS, showSaveCard);
        pay_intent.putExtra(PayActivityIntentKeys.SHOW_SAVE_CARD, showSaveCard);
        pay_intent.putExtra(PayActivityIntentKeys.THEME_COLOR, 0x8033B5E5);

        startActivityForResult(pay_intent, ACCEPT_PAYMENT_REQUEST);
    }

    private void startPayActivityToken() {
        Intent pay_intent = new Intent(this, PayActivity.class);

        putNormalExtras(pay_intent);
        // replace this with your actual card token
        pay_intent.putExtra(PayActivityIntentKeys.TOKEN, "token");
        pay_intent.putExtra(PayActivityIntentKeys.MASKED_PAN_NUMBER, "xxxx-xxxx-xxxx-1234");
        pay_intent.putExtra(PayActivityIntentKeys.SAVE_CARD_DEFAULT, false);
        pay_intent.putExtra(PayActivityIntentKeys.SHOW_ALERTS, true);
        pay_intent.putExtra(PayActivityIntentKeys.SHOW_SAVE_CARD, false);
        startActivityForResult(pay_intent, ACCEPT_PAYMENT_REQUEST);
    }

    private void putNormalExtras(Intent intent) {
        // Pass the correct values for the billing data keys
        intent.putExtra(PayActivityIntentKeys.FIRST_NAME, "firsy_name");
        intent.putExtra(PayActivityIntentKeys.LAST_NAME, "last_name");
        intent.putExtra(PayActivityIntentKeys.BUILDING, "1");
        intent.putExtra(PayActivityIntentKeys.FLOOR, "1");
        intent.putExtra(PayActivityIntentKeys.APARTMENT, "1");
        intent.putExtra(PayActivityIntentKeys.CITY, "cairo");
        intent.putExtra(PayActivityIntentKeys.STATE, "new_cairo");
        intent.putExtra(PayActivityIntentKeys.COUNTRY, "egypt");
        intent.putExtra(PayActivityIntentKeys.EMAIL, "email@gmail.com");
        intent.putExtra(PayActivityIntentKeys.PHONE_NUMBER, "2345678");
        intent.putExtra(PayActivityIntentKeys.POSTAL_CODE, "3456");

        intent.putExtra(PayActivityIntentKeys.PAYMENT_KEY, paymentKey);

        intent.putExtra(PayActivityIntentKeys.THREE_D_SECURE_ACTIVITY_TITLE, "Verification");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bundle extras = data.getExtras();

        if (requestCode == ACCEPT_PAYMENT_REQUEST) {

            if (resultCode == IntentConstants.USER_CANCELED) {
                // User canceled and did no payment request was fired
                ToastMaker.displayShortToast(this, "User canceled!!");
            } else if (resultCode == IntentConstants.MISSING_ARGUMENT) {
                // You forgot to pass an important key-value pair in the intent's extras
                ToastMaker.displayShortToast(this, "Missing Argument == " + extras.getString(IntentConstants.MISSING_ARGUMENT_VALUE));
            } else if (resultCode == IntentConstants.TRANSACTION_ERROR) {
                // An error occurred while handling an API's response
                ToastMaker.displayShortToast(this, "Reason == " + extras.getString(IntentConstants.TRANSACTION_ERROR_REASON));
            } else if (resultCode == IntentConstants.TRANSACTION_REJECTED) {
                // User attempted to pay but their transaction was rejected

                // Use the static keys declared in PayResponseKeys to extract the fields you want
                ToastMaker.displayShortToast(this, extras.getString(PayResponseKeys.DATA_MESSAGE));
            } else if (resultCode == IntentConstants.TRANSACTION_REJECTED_PARSING_ISSUE) {
                // User attempted to pay but their transaction was rejected. An error occured while reading the returned JSON
                ToastMaker.displayShortToast(this, extras.getString(IntentConstants.RAW_PAY_RESPONSE));
            } else if (resultCode == IntentConstants.TRANSACTION_SUCCESSFUL) {
                // User finished their payment successfully

                // Use the static keys declared in PayResponseKeys to extract the fields you want
                ToastMaker.displayShortToast(this, extras.getString(PayResponseKeys.DATA_MESSAGE));
            } else if (resultCode == IntentConstants.TRANSACTION_SUCCESSFUL_PARSING_ISSUE) {
                // User finished their payment successfully. An error occured while reading the returned JSON.
                ToastMaker.displayShortToast(this, "TRANSACTION_SUCCESSFUL - Parsing Issue");

               // ToastMaker.displayShortToast(this, extras.getString(IntentConstants.RAW_PAY_RESPONSE));
            } else if (resultCode == IntentConstants.TRANSACTION_SUCCESSFUL_CARD_SAVED) {
                // User finished their payment successfully and card was saved.

                // Use the static keys declared in PayResponseKeys to extract the fields you want
                // Use the static keys declared in SaveCardResponseKeys to extract the fields you want
                ToastMaker.displayShortToast(this, "Token == " + extras.getString(SaveCardResponseKeys.TOKEN));
            } else if (resultCode == IntentConstants.USER_CANCELED_3D_SECURE_VERIFICATION) {
                ToastMaker.displayShortToast(this, "User canceled 3-d scure verification!!");

                // Note that a payment process was attempted. You can extract the original returned values
                // Use the static keys declared in PayResponseKeys to extract the fields you want
                ToastMaker.displayShortToast(this, extras.getString(PayResponseKeys.PENDING));
            } else if (resultCode == IntentConstants.USER_CANCELED_3D_SECURE_VERIFICATION_PARSING_ISSUE) {
                ToastMaker.displayShortToast(this, "User canceled 3-d scure verification - Parsing Issue!!");

                // Note that a payment process was attempted.
                // User finished their payment successfully. An error occured while reading the returned JSON.
                ToastMaker.displayShortToast(this, extras.getString(IntentConstants.RAW_PAY_RESPONSE));
            }
        }
    }
}

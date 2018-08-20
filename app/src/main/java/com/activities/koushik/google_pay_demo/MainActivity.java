package com.activities.koushik.google_pay_demo;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.CardRequirements;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentMethodToken;
import com.google.android.gms.wallet.PaymentMethodTokenizationParameters;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.TransactionInfo;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private PaymentsClient mPaymentsClient;
    // Arbitrarily-picked result code.
    private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 991;
    private static final String TAG = MainActivity.class.getSimpleName();
    private Button vBtnGooglePay;
    private TextView vTVGoogleStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vTVGoogleStatus = findViewById(R.id.tv_status);
        vBtnGooglePay = findViewById(R.id.btn_gateway_demo);

        mPaymentsClient =
                Wallet.getPaymentsClient(
                        this,
                        new Wallet.WalletOptions.Builder()
                                .setEnvironment(WalletConstants.ENVIRONMENT_TEST)
                                .build());

        vBtnGooglePay.setVisibility(View.GONE);
        vTVGoogleStatus.setVisibility(View.VISIBLE);
        vTVGoogleStatus.setText(getText(R.string.googlepay_status_checking));
        isReadyToPay();

        vBtnGooglePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vBtnGooglePay.setClickable(false);

                PaymentDataRequest request = createPaymentDataRequest();
                if (request != null) {
                    AutoResolveHelper.resolveTask(
                            mPaymentsClient.loadPaymentData(request),
                            MainActivity.this,
                            // LOAD_PAYMENT_DATA_REQUEST_CODE is a constant value
                            // you define.
                            LOAD_PAYMENT_DATA_REQUEST_CODE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case LOAD_PAYMENT_DATA_REQUEST_CODE:
                switch (resultCode) {
                    case RESULT_OK:
                        PaymentData paymentData = PaymentData.getFromIntent(data);
                        String token = paymentData.getPaymentMethodToken().getToken();
                        Log.d(TAG, token);
                        handlePaymentSuccess(paymentData);
                        break;
                    case RESULT_CANCELED:
                        // Nothing to here normally - the user simply cancelled without selecting a
                        // payment method.
                        break;
                    case AutoResolveHelper.RESULT_ERROR:
                        Status status = AutoResolveHelper.getStatusFromIntent(data);
                        // Log the status for debugging.
                        // Generally, there is no need to show an error to
                        // the user as the Google Pay API will do that.
                        Log.d(TAG, status.toString());
                        handleError(status.getStatusCode());
                        break;
                    default:
                        // Do nothing.
                }

                // Re-enables the Pay with Google button.
                vBtnGooglePay.setClickable(true);
                break;
            default:
                // Do nothing.
        }
    }

    private void handleError(int statusCode) {
        // At this stage, the user has already seen a popup informing them an error occurred.
        // Normally, only logging is required.
        // statusCode will hold the value of any constant from CommonStatusCode or one of the
        // WalletConstants.ERROR_CODE_* constants.
        Log.w("loadPaymentData failed", String.format("Error code: %d", statusCode));
    }

    private void handlePaymentSuccess(PaymentData paymentData) {
        // PaymentMethodToken contains the payment information, as well as any additional
        // requested information, such as billing and shipping address.
        //
        // Refer to your processor's documentation on how to proceed from here.
        PaymentMethodToken token = paymentData.getPaymentMethodToken();

        // getPaymentMethodToken will only return null if PaymentMethodTokenizationParameters was
        // not set in the PaymentRequest.
        if (token != null) {
            // If the gateway is set to example, no payment information is returned - instead, the
            // token will only consist of "examplePaymentMethodToken".
            if (token.getToken().equals("examplePaymentMethodToken")) {
                AlertDialog alertDialog = new AlertDialog.Builder(this)
                        .setTitle("Warning")
                        .setMessage("Gateway name set to \"example\" - please modify " +
                                "Constants.java and replace it with your own gateway.")
                        .setPositiveButton("OK", null)
                        .create();
                alertDialog.show();
            }

            String billingName = paymentData.getCardInfo().getBillingAddress().getName();
            Toast.makeText(this, getString(R.string.payments_show_name, billingName), Toast.LENGTH_LONG).show();

            // Use token.getToken() to get the token string.
            Log.d("PaymentData", "PaymentMethodToken received");
        }
    }

    private void isReadyToPay() {
        IsReadyToPayRequest request =
                IsReadyToPayRequest.newBuilder()
                        .addAllowedPaymentMethod(WalletConstants.PAYMENT_METHOD_CARD)
                        .addAllowedPaymentMethod(WalletConstants.PAYMENT_METHOD_TOKENIZED_CARD)
                        .build();
        Task<Boolean> task = mPaymentsClient.isReadyToPay(request);
        task.addOnCompleteListener(
                new OnCompleteListener<Boolean>() {
                    public void onComplete(Task<Boolean> task) {
                        try {
                            boolean result = task.getResult(ApiException.class);
                            setGooglePayAvailable(result);
                        } catch (ApiException exception) {
                            Log.w("isReadyToPay failed", exception);
                        }
                    }
                });
    }

    private void setGooglePayAvailable(boolean available) {
        // If isReadyToPay returned true, show the button and hide the "checking" text. Otherwise,
        // notify the user that Pay with Google is not available.
        // Please adjust to fit in with your current user flow. You are not required to explicitly
        // let the user know if isReadyToPay returns false.
        if (available) {
            vTVGoogleStatus.setVisibility(View.GONE);
            vBtnGooglePay.setVisibility(View.VISIBLE);
        } else {
            vTVGoogleStatus.setText(R.string.googlepay_status_unavailable);
        }
    }

    private PaymentDataRequest createPaymentDataRequest() {
        PaymentDataRequest.Builder request =
                PaymentDataRequest.newBuilder()
                        .setTransactionInfo(
                                // Supported countries for shipping (use ISO 3166-1 alpha-2 country codes).
                                // Relevant only when requesting a shipping address.
                                TransactionInfo.newBuilder()
                                        .setTotalPriceStatus(WalletConstants.TOTAL_PRICE_STATUS_FINAL)
                                        .setTotalPrice("10.00")
                                        .setCurrencyCode("USD")
                                        .build())
                        .addAllowedPaymentMethod(WalletConstants.PAYMENT_METHOD_CARD)
                        .addAllowedPaymentMethod(WalletConstants.PAYMENT_METHOD_TOKENIZED_CARD)
                        .setCardRequirements(
                                CardRequirements.newBuilder()
                                        .addAllowedCardNetworks(
                                                Arrays.asList(
                                                        WalletConstants.CARD_NETWORK_AMEX,
                                                        WalletConstants.CARD_NETWORK_DISCOVER,
                                                        WalletConstants.CARD_NETWORK_VISA,
                                                        WalletConstants.CARD_NETWORK_MASTERCARD))
                                        .build());

        // Custom parameters required by the processor / gateway.
        // In many cases, your processor / gateway will only require a gatewayMerchantId.
        // Please refer to your processor's documentation for more information. The number of parameters
        // required and their names vary depending on the processor.
        PaymentMethodTokenizationParameters params =
                PaymentMethodTokenizationParameters.newBuilder()
                        .setPaymentMethodTokenizationType(
                                WalletConstants.PAYMENT_METHOD_TOKENIZATION_TYPE_PAYMENT_GATEWAY)
                        .addParameter("gateway", "example")
                        .addParameter("gatewayMerchantId", "exampleGatewayMerchantId")
                        .build();

        request.setPaymentMethodTokenizationParameters(params);
        return request.build();
    }
}

package com.example.llm_project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PurchaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        Button btnBasic = findViewById(R.id.btnBasic);
        Button btnPro = findViewById(R.id.btnPro);
        Button btnPremium = findViewById(R.id.btnPremium);

        btnBasic.setOnClickListener(v -> launchGooglePay("Basic", "1.99"));
        btnPro.setOnClickListener(v -> launchGooglePay("Pro", "4.99"));
        btnPremium.setOnClickListener(v -> launchGooglePay("Premium", "9.99"));
    }

    private void launchGooglePay(String plan, String price) {
        PaymentsClient paymentsClient = Wallet.getPaymentsClient(this,
                new Wallet.WalletOptions.Builder()
                        .setEnvironment(WalletConstants.ENVIRONMENT_TEST)
                        .build());

        try {
            JSONObject paymentRequest = new JSONObject()
                    .put("apiVersion", 2)
                    .put("apiVersionMinor", 0)
                    .put("allowedPaymentMethods", new JSONArray()
                            .put(new JSONObject()
                                    .put("type", "CARD")
                                    .put("parameters", new JSONObject()
                                            .put("allowedAuthMethods", new JSONArray().put("PAN_ONLY").put("CRYPTOGRAM_3DS"))
                                            .put("allowedCardNetworks", new JSONArray().put("VISA").put("MASTERCARD")))
                                    .put("tokenizationSpecification", new JSONObject()
                                            .put("type", "PAYMENT_GATEWAY")
                                            .put("parameters", new JSONObject()
                                                    .put("gateway", "example")
                                                    .put("gatewayMerchantId", "exampleMerchantId")))))
                    .put("transactionInfo", new JSONObject()
                            .put("totalPriceStatus", "FINAL")
                            .put("totalPrice", price)
                            .put("currencyCode", "USD"))
                    .put("merchantInfo", new JSONObject()
                            .put("merchantName", "Quiz App"));

            PaymentDataRequest request = PaymentDataRequest.fromJson(paymentRequest.toString());
            AutoResolveHelper.resolveTask(paymentsClient.loadPaymentData(request), this, 999);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 999) {
            if (resultCode == RESULT_OK) {
                // TODO: payment successful — show a success message
                Toast.makeText(this,"Payment successful", Toast.LENGTH_SHORT).show();
            } else {
                // TODO: payment cancelled or failed — show a message
                Toast.makeText(this,"Payment failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
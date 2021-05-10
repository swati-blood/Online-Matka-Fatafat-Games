package in.games.jollygames.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.shreyaspatil.EasyUpiPayment.EasyUpiPayment;
import com.shreyaspatil.EasyUpiPayment.listener.PaymentStatusListener;
import com.shreyaspatil.EasyUpiPayment.model.PaymentApp;
import com.shreyaspatil.EasyUpiPayment.model.TransactionDetails;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import in.games.jollygames.Common.Common;
import in.games.jollygames.Interfaces.OnConfigData;
import in.games.jollygames.Interfaces.OnGetWallet;
import in.games.jollygames.Model.ConfigModel;
import in.games.jollygames.Model.WalletObjects;
import in.games.jollygames.R;
import in.games.jollygames.databinding.ActivityRequestFundsBinding;
import in.games.jollygames.utils.LoadingBar;
import in.games.jollygames.utils.Session_management;

import static in.games.jollygames.Config.BaseUrl.URL_INSERT_REQUEST;
import static in.games.jollygames.Config.Constants.KEY_ID;
import static in.games.jollygames.Config.Constants.KEY_WALLET;

public class RequestFundsActivity extends AppCompatActivity implements View.OnClickListener, PaymentStatusListener {
    Session_management session_management ;
    LoadingBar loadingBar ;
    Common common ;
    ActivityRequestFundsBinding binding ;
    Activity ctx = RequestFundsActivity.this;
    private EasyUpiPayment mEasyUpiPayment;
    int min_amt = 0;
    boolean upi_flag = false;
    String upi = "", upi_name = "", upi_desc = "", upi_type = "", transactionId = "", upi_status = "";
    String TAG = RequestFundsActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRequestFundsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViews();
    }
    void initViews() {
        common = new Common(ctx);
        loadingBar = new LoadingBar(ctx);
        session_management = new Session_management(ctx);
        binding.btnSubmit.setOnClickListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Funds Request");
        common.getWalletAmount(new OnGetWallet() {
            @Override
            public void onGetWallet(WalletObjects walletModel) {
                binding.tvWallet.setText(walletModel.getWallet_points());

            }
        });
        common.getConfigData(new OnConfigData() {
            @Override
            public void onGetConfigData(ConfigModel model) {

                min_amt = Integer.parseInt(common.checkNullNumber(model.getMin_amount().toString()));
                upi = common.checkNullString(model.getUpi()).toString();
                upi_name = common.checkNullString(model.getUpi_name());
                upi_desc = common.checkNullString(model.getUpi_desc());
                upi_type = common.checkNullString(model.getUpi_type());
                upi_status = common.checkNullString(model.getUpi_status());
//                upi_status = "0";


            }
        });
        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String point = binding.etPoints.getText().toString();
                if (point.isEmpty()) {
                    binding.etPoints.setError("Enter Points ");
                } else {
                    int points = Integer.parseInt(common.checkNullNumber(point));
                    if (points < min_amt) {
                        common.showToast("Minimum Request Amount is " + min_amt);
                    } else {

                            transactionId = "TXN" + System.currentTimeMillis();
                            String payeeVpa = upi;
                            String payeeName = upi_name;
                            String transactionRefId = transactionId;
                            String description = upi_desc;
                            String amount = point + ".00";
                            if (upi_status.equals("0")) {
                                addRequest(session_management.getUserDetails().get(KEY_ID), point, "pending", transactionRefId);

                            } else {
                                payViaUpi(transactionId, payeeVpa, payeeName, transactionRefId, description, amount);

                            }

                    }
                }
            }


        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_submit:
                break;
        }
    }

    public void addRequest(String user_id, String points, String status, String txn_id) {
        loadingBar.show();
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("points", points);
        params.put("request_status", status);
        params.put("type", "Add");
        params.put("trans_id", txn_id);
        params.put("details", "");
        params.put("method", "");
//        params.put("txn_id", txn_id);
        params.put("wallet", session_management.getUserDetails().get(KEY_WALLET));
        common.postRequest(URL_INSERT_REQUEST, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String resp) {
                Log.e(TAG, "onResponse: "+resp.toString() );
                loadingBar.dismiss();
                try {
                    JSONObject resonse = new JSONObject(resp);
                    if (resonse.getBoolean("responce")) {
                        common.showToast(resonse.getString("message"));
                        finish();
                    } else {
                        common.showToast(resonse.getString("error"));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                common.showVolleyError(error);
            }
        });
    }

    private void payViaUpi(String transactionId, String payeeVpa, String payeeName, String transactionRefId, String description, String amount) {
        // START PAYMENT INITIALIZATION
        upi_flag = true;
        mEasyUpiPayment = new EasyUpiPayment.Builder()
                .with(this)
                .setPayeeVpa(payeeVpa)
                .setPayeeName(payeeName)
                .setTransactionId(transactionId)
                .setTransactionRefId(transactionRefId)
                .setDescription(description)
                .setAmount(amount)
                .build();

        // Register Listener for Events
        mEasyUpiPayment.setPaymentStatusListener(this);


        switch (upi_type) {
            case "None":
                mEasyUpiPayment.setDefaultPaymentApp(PaymentApp.NONE);
                break;
            case "AMAZON_PAY":
                mEasyUpiPayment.setDefaultPaymentApp(PaymentApp.AMAZON_PAY);
                break;
            case "BHIM_UPI":
                mEasyUpiPayment.setDefaultPaymentApp(PaymentApp.BHIM_UPI);
                break;
            case "GOOGLE_PAY":
                mEasyUpiPayment.setDefaultPaymentApp(PaymentApp.GOOGLE_PAY);
                break;
            case "PHONE_PE":
                mEasyUpiPayment.setDefaultPaymentApp(PaymentApp.PHONE_PE);
                break;
            case "PAYTM":
                mEasyUpiPayment.setDefaultPaymentApp(PaymentApp.PAYTM);
                break;
        }

//        mEasyUpiPayment.setDefaultPaymentApp(PaymentApp.NONE);

        // Check if app exists or not
        if (mEasyUpiPayment.isDefaultAppExist()) {
            onAppNotFound();
            return;
        }
        // END INITIALIZATION

        // START PAYMENT
        mEasyUpiPayment.startPayment();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (upi_flag) {
            mEasyUpiPayment.detachListener();
        }
    }

    @Override
    public void onTransactionCompleted(TransactionDetails transactionDetails) {
        Log.e("transactionDetails", "" + transactionDetails);
        if (transactionDetails.getStatus().equalsIgnoreCase("success")) {
//            String user_id= Prevalent.currentOnlineuser.getId();
            String user_id = session_management.getUserDetails().get(KEY_ID);
            addRequest(user_id, transactionDetails.getAmount().toString(), "approved", transactionDetails.getTransactionId().toString());

        } else {
            common.showToast("Payment Failed.");
        }

    }

    @Override
    public void onTransactionSuccess() {

    }

    @Override
    public void onTransactionSubmitted() {

    }

    @Override
    public void onTransactionFailed() {
        common.showToast("Failed");
    }

    @Override
    public void onTransactionCancelled() {
        common.showToast("Cancelled");
    }

    @Override
    public void onAppNotFound() {
        common.showToast("App Not Found");
    }
}
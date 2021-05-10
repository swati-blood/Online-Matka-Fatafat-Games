package in.games.jollygames.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.games.jollygames.AppController;
import in.games.jollygames.Common.Common;
import in.games.jollygames.R;
import in.games.jollygames.utils.ConnectivityReceiver;
import in.games.jollygames.utils.CustomJsonRequest;
import in.games.jollygames.utils.LoadingBar;
import in.games.jollygames.utils.Session_management;

import static in.games.jollygames.Config.BaseUrl.URL_REGISTER;
import static in.games.jollygames.Config.Constants.KEY_ACCOUNNO;
import static in.games.jollygames.Config.Constants.KEY_BANK_NAME;
import static in.games.jollygames.Config.Constants.KEY_HOLDER;
import static in.games.jollygames.Config.Constants.KEY_ID;
import static in.games.jollygames.Config.Constants.KEY_IFSC;

public class BankDetailActivity extends AppCompatActivity implements View.OnClickListener {
    LoadingBar loadingBar ;
    Common common ;
    Session_management session_management ;
    Activity ctx = BankDetailActivity.this;
 in.games.jollygames.databinding.ActivityBankDetailsBinding binding ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = in.games.jollygames.databinding.ActivityBankDetailsBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        initViews();
    }
    void initViews()
    {
        common = new Common(ctx);
        loadingBar = new LoadingBar(ctx);
        session_management = new Session_management(ctx);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Bank Details");
        binding.etName.setText(common.checkNullString(session_management.getUserDetails().get(KEY_HOLDER)));
        binding.etAccNumber.setText(common.checkNullString(session_management.getUserDetails().get(KEY_ACCOUNNO)));
        binding.etIfsc.setText(common.checkNullString(session_management.getUserDetails().get(KEY_IFSC)));
        binding.etBankName.setText(common.checkNullString(session_management.getUserDetails().get(KEY_BANK_NAME)));
        binding.btnSubmit.setOnClickListener(this);


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
                validate();
                break;
        }
    }

    void validate()
    {
        String name  = binding.etName.getText().toString();
        String accno  = binding.etAccNumber.getText().toString();
        String ifsc = binding.etIfsc.getText().toString();
        String bank = binding.etBankName.getText().toString();
        if (name.isEmpty())
        {
            binding.etName.setError("Required");
        }
        else if (accno.isEmpty())
        {
            binding.etAccNumber.setError("Required");
        }
        else if (ifsc.isEmpty())
        {
            binding.etIfsc.setError("Required");
        }
        else if (bank.isEmpty())
        {
            binding.etBankName.setError("Required");
        }
        else
        {
            if (ConnectivityReceiver.isConnected()) {
                updateBankDetails(session_management.getUserDetails().get(KEY_ID), bank, accno, ifsc, name);
            }
            else
            {
                common.showToast("No Internet Connection");
            }
        }
    }

    private void updateBankDetails(String user_id,final String bankname, final String  accno, final String ifsc, final String hod_name) {
        loadingBar.show();
        Map<String,String> params=new HashMap<>();
        params.put("key","4");
        params.put("accountno",accno);
        params.put("bankname",bankname);
        params.put("ifsc",ifsc);
        params.put("accountholder",hod_name);
        params.put("user_id",user_id);
        Log.e("bank",params.toString());

        CustomJsonRequest customJsonRequest=new CustomJsonRequest(Request.Method.POST, URL_REGISTER, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loadingBar.dismiss();
                try {
                    Log.e("bank",response.toString());
                    boolean resp=response.getBoolean("responce");
                    if(resp)
                    {
                        common.showToast(""+response.getString("message"));
                        session_management.updateAccSection(accno,bankname,ifsc,hod_name);
                        finish();


                    }
                    else
                    {
                        common.showToast(""+response.getString("error"));
                    }

                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                    common.showToast(ex.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                String msg=common.VolleyErrorMessage(error);
                if(!msg.isEmpty())
                {
                    common.showToast(msg);
                }
            }
        });
        AppController.getInstance().addToRequestQueue(customJsonRequest,"json_bank");

    }
}
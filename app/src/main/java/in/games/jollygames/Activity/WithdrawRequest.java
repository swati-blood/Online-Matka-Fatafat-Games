package in.games.jollygames.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.games.jollygames.Common.Common;
import in.games.jollygames.Interfaces.OnConfigData;
import in.games.jollygames.Interfaces.OnGetWallet;
import in.games.jollygames.Model.ConfigModel;
import in.games.jollygames.Model.TimeSlots;
import in.games.jollygames.Model.WalletObjects;
import in.games.jollygames.databinding.ActivityWithdrawRequestBinding;

import in.games.jollygames.utils.LoadingBar;
import in.games.jollygames.utils.Session_management;

import static in.games.jollygames.Config.BaseUrl.URL_INSERT_REQUEST;
import static in.games.jollygames.Config.BaseUrl.URL_TIME_SLOTS;
import static in.games.jollygames.Config.Constants.KEY_ACCOUNNO;
import static in.games.jollygames.Config.Constants.KEY_BANK_NAME;
import static in.games.jollygames.Config.Constants.KEY_HOLDER;
import static in.games.jollygames.Config.Constants.KEY_ID;
import static in.games.jollygames.Config.Constants.KEY_IFSC;
import static in.games.jollygames.Config.Constants.KEY_PAYTM;
import static in.games.jollygames.Config.Constants.KEY_PHONEPAY;
import static in.games.jollygames.Config.Constants.KEY_TEZ;
import static in.games.jollygames.Config.Constants.KEY_WALLET;

public class WithdrawRequest extends AppCompatActivity {
    ActivityWithdrawRequestBinding binding ;
    ArrayList<String> o_list;
    Activity ctx = WithdrawRequest.this;
    Session_management session_management ;
    LoadingBar loadingBar ;
    Common common ;
    ArrayList<TimeSlots> timeList;
    int wMinAmt=0 ,wallet_amt=0;
    String wSunday="",wSaturday="" ,wType="",details="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWithdrawRequestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViews();
    }

    void initViews()
    {
        common = new Common(ctx);
        loadingBar = new LoadingBar(ctx);
        session_management = new Session_management(ctx);
        o_list = new ArrayList<>();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Withdraw Request");
        o_list.clear();
        o_list.add("Select");


        if (!common.checkNull(session_management.getUserDetails().get(KEY_ACCOUNNO)))
        {
        o_list.add("Bank");
        }
       if (!common.checkNull(session_management.getUserDetails().get(KEY_PAYTM)))
        {
        o_list.add("Paytm");
        }
        if (!common.checkNull(session_management.getUserDetails().get(KEY_PHONEPAY)))
        {
        o_list.add("PhonePe");
        }
      if (!common.checkNull(session_management.getUserDetails().get(KEY_TEZ)))
        {
        o_list.add("Google Pay");
        }

        ArrayAdapter adapter = new ArrayAdapter(ctx,
                android.R.layout.simple_spinner_dropdown_item,
               o_list);
        binding.spinMehtods.setAdapter(adapter);
        binding.spinMehtods.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (binding.spinMehtods.getSelectedItem().toString().toLowerCase())
                {
                    case "paytm":
                        wType="Paytm";
                        details = session_management.getUserDetails().get(KEY_PAYTM);
                        break;
                    case "phonepe":
                        wType="Phonepe";
                        details = session_management.getUserDetails().get(KEY_PHONEPAY);
                        break;
                    case "google pay":
                        wType="Google Pay";
                        details = session_management.getUserDetails().get(KEY_TEZ);
                        break;
                    case "bank":
                        wType = "Bank";
                        details = "Account Holder Name - "+ session_management.getUserDetails().get(KEY_HOLDER) +"\n" +"Account Number - "
                                + session_management.getUserDetails().get(KEY_ACCOUNNO) +"\n"
                                +"Ifsc Code - "+  session_management.getUserDetails().get(KEY_IFSC);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        common.getConfigData(new OnConfigData() {
            @Override
            public void onGetConfigData(ConfigModel model) {
                binding.tvWithdrawMsg.setText(model.getWithdraw_text());
                Log.e("timeslots",model.getTime_slot());
                try {
                    JSONArray arr = new JSONArray(model.getTime_slot());
                    for (int i = 0 ; i<arr.length();i++)
                    {
                        TimeSlots timeSlots = new TimeSlots();
                        timeSlots.setStart_time(arr.getString(Integer.parseInt("start_time")));
                        timeSlots.setEnd_time(arr.getString(Integer.parseInt("end_time")));
                        timeSlots.setId(arr.getString(Integer.parseInt("id")));
                        timeSlots.setStatus(arr.getString(Integer.parseInt("status")));
                        timeList.add(timeSlots);
                    }
                    wMinAmt = Integer.parseInt(model.getW_amount());
                    wSaturday = model.getW_saturday();
                    wSunday = model.getW_sunday();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        common.getWalletAmount(new OnGetWallet() {
            @Override
            public void onGetWallet(WalletObjects walletModel) {
                binding.tvWallet.setText(walletModel.getWallet_points());

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
                    if (points < wMinAmt) {
                        common.showToast("Minimum Withdraw Amount is " + wMinAmt);
                    } else {
                        if (points > Integer.parseInt(session_management.getUserDetails().get(KEY_WALLET))) {
                            common.showToast("Your requested amount exceeded");
                        } else {
                            if (wType.isEmpty()) {
                                common.showToast("Select Any One Withdraw Type");
                            } else {
                                addWithdrawRequest(point,wType,details);
                            }
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


    public void addWithdrawRequest(String points,String wType,String details){
        loadingBar.show();
        HashMap<String,String> params=new HashMap<>();
        params.put("user_id",session_management.getUserDetails().get(KEY_ID));
        params.put("points",points);
        params.put("details",details);
        params.put("method",wType);
        params.put("trans_id","WTXN" + System.currentTimeMillis());
        params.put("request_status", "pending");
        params.put("type", "Withdrawal");

        common.postRequest(URL_INSERT_REQUEST, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String resp) {
                Log.e("witdrara", "onResponse: "+resp.toString() );
                loadingBar.dismiss();
                try {
                    JSONObject response=new JSONObject(resp);
                    if(response.getBoolean("responce")){
                        common.showToast(""+response.getString("message"));
                        finish();
                    }else{
                        common.showToast(""+response.getString("error"));
                    }

                }catch (Exception ex){
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

}
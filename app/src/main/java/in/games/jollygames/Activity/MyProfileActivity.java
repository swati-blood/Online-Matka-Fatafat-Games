package in.games.jollygames.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.games.jollygames.Adapter.GameRateAdapter;
import in.games.jollygames.AppController;
import in.games.jollygames.Common.Common;
import in.games.jollygames.Model.GameRateModel;
import in.games.jollygames.R;
import in.games.jollygames.databinding.ActivityGameRatesBinding;
import in.games.jollygames.databinding.ActivityMyProfileBinding;
import in.games.jollygames.utils.ConnectivityReceiver;
import in.games.jollygames.utils.CustomJsonRequest;
import in.games.jollygames.utils.LoadingBar;
import in.games.jollygames.utils.Session_management;

import static in.games.jollygames.Config.BaseUrl.URL_REGISTER;
import static in.games.jollygames.Config.Constants.KEY_EMAIL;
import static in.games.jollygames.Config.Constants.KEY_ID;
import static in.games.jollygames.Config.Constants.KEY_MOBILE;
import static in.games.jollygames.Config.Constants.KEY_NAME;

public class MyProfileActivity extends AppCompatActivity {
    ActivityMyProfileBinding binding ;
    Activity ctx = MyProfileActivity.this;
    LoadingBar loadingBar ;
    Common common ;
    Session_management session_management ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViews();
    }
    void  initViews()
    {
        common = new Common(ctx);
        loadingBar = new LoadingBar(ctx);
        session_management = new Session_management(ctx);
        getSupportActionBar().setTitle("My Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.etEmail.setText(session_management.getUserDetails().get(KEY_EMAIL));
        binding.etName.setText(session_management.getUserDetails().get(KEY_NAME));
        binding.etMobile.setText(session_management.getUserDetails().get(KEY_MOBILE));
        binding.etEmail.setText(session_management.getUserDetails().get(KEY_EMAIL));
        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ConnectivityReceiver.isConnected())
                {
                    updateProfile(session_management.getUserDetails().get(KEY_ID),binding.etMobile.getText().toString(),
                            binding.etEmail.getText().toString().trim(),binding.etName.getText().toString().trim());
                }
                else
                {
                    common.showToast("No Internet Connection");
                }

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        return true;
    }

    private void updateProfile(String user_id,  final String mobile, final String email, final String name)
    {
        loadingBar.show();
        Map<String,String> params=new HashMap<>();
        params.put("key","5");
        params.put("user_id",user_id);
        params.put("mobile",mobile);
        params.put("email",email);
        params.put("name",name);
        Log.e("profile",params.toString());

        CustomJsonRequest customJsonRequest=new CustomJsonRequest(Request.Method.POST,URL_REGISTER, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loadingBar.dismiss();
                try {
                    Log.e("profile",response.toString());
                    boolean resp=response.getBoolean("responce");
                    if(resp)
                    {

                        session_management.updateEmailSection(email,mobile,name);
                        common.showToast(""+response.getString("message"));

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
        AppController.getInstance().addToRequestQueue(customJsonRequest,"json_profile");

    }

}
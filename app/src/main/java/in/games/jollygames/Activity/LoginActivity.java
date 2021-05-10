package in.games.jollygames.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.games.jollygames.Common.Common;
import in.games.jollygames.R;
import in.games.jollygames.databinding.ActivityLoginBinding;
import in.games.jollygames.utils.ConnectivityReceiver;
import in.games.jollygames.utils.LoadingBar;
import in.games.jollygames.utils.Session_management;
import in.games.jollygames.utils.ToastMsg;

import static in.games.jollygames.Config.BaseUrl.URL_LOGIN;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
ActivityLoginBinding binding ;
Activity ctx = LoginActivity.this;
LoadingBar loadingBar ;
ToastMsg toastMsg ;
Session_management session_management ;
Common common ;
String TAG = LoginActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViews();


    }

    void initViews()
    {
        common = new Common(ctx);
        toastMsg = new ToastMsg(ctx);
        session_management = new Session_management(ctx);
        loadingBar = new LoadingBar(ctx);
        binding.relGo.setOnClickListener(this);
        binding.tvRegister.setOnClickListener(this);
        binding.tvPass.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.rel_go:
                validate();
                break;
            case R.id.tv_register:
                startActivity(new Intent(ctx,RegisterActivity.class));
                break;

                case R.id.tv_pass:
                startActivity(new Intent(ctx,SendOtpActivity.class));
                break;
        }
    }

   void validate()
    {
        String m = binding.etMobile.getText().toString();
        String p = binding.etPass.getText().toString();
        if (m.isEmpty())
        {
            binding.etMobile.setError("Mobile Number is Required");

        }
        else if (p.isEmpty())
        {
            binding.etPass.setError("Password is Required");
        }
        else
        {
            if (ConnectivityReceiver.isConnected())
            {
                makeLogin(m,p);
            }
        }
    }


    public void makeLogin(String mobile ,String pas)
    {
        loadingBar.show();
       HashMap<String, String> params = new HashMap<String, String>();
        params.put("mobileno",mobile);
        params.put("password",pas);
        common.postRequest(URL_LOGIN, params, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                Log.e("login",response.toString());
                try {
                    JSONObject object = new JSONObject(String.valueOf(response));
                    if (object.getBoolean("responce"))
                    {
                        JSONObject jsonObject = object.getJSONObject("data");
                        String id=common.checkNullString(jsonObject.getString("id").toString());
                        String name=common.checkNullString(jsonObject.getString("name").toString());
                        String username=common.checkNullString(jsonObject.getString("username").toString());
                        String mobile=common.checkNullString(jsonObject.getString("mobileno").toString());
                        String email=common.checkNullString(jsonObject.getString("email").toString());
                        String address=common.checkNullString(jsonObject.getString("address").toString());
                        String city=common.checkNullString(jsonObject.getString("city").toString());
                        String pincode=common.checkNullString(jsonObject.getString("pincode").toString());
                        String accno=common.checkNullString(jsonObject.getString("accountno").toString());
                        String bank=common.checkNullString(jsonObject.getString("bank_name").toString());
                        String ifsc=common.checkNullString(jsonObject.getString("ifsc_code").toString());
                        String holder=common.checkNullString(jsonObject.getString("account_holder_name").toString());
                        String paytm=common.checkNullString(jsonObject.getString("paytm_no").toString());
                        String tez=common.checkNullString(jsonObject.getString("tez_no").toString());
                        String phonepay=common.checkNullString(jsonObject.getString("phonepay_no").toString());
                        String wallet=common.checkNullString(jsonObject.getString("wallet").toString());
                        String dob=common.checkNullString(jsonObject.getString("dob").toString());
                        String gender=common.checkNullString(jsonObject.getString("gender").toString());
                        String p = jsonObject.getString("password");
                        if (pas.equals(p)) {
                            Log.e(TAG, "onResponse: "+id+"\n"+bank+"\n "+ifsc+"\n"+accno+"\n"+holder );
                            session_management.createLoginSession(id,name,username,mobile,email,address
                                    ,city,pincode,accno,bank,ifsc,holder,paytm,tez,phonepay,dob,wallet,gender);
//                                Log.e(TAG, "onResponse: "+session_management.getUserDetails());
                            Intent intent = new Intent(ctx,MainActivity.class);
                            intent.putExtra("username", jsonObject.getString("username").toString());
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {

                            toastMsg.toastIconError("Password is not correct ");
                        }

                    }
                    else
                    {
                        toastMsg.toastIconError(object.getString("error"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                loadingBar.dismiss();

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
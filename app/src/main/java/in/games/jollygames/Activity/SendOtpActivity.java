package in.games.jollygames.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import in.games.jollygames.Common.Common;
import in.games.jollygames.R;
import in.games.jollygames.databinding.ActivitySendOtpBinding;
import in.games.jollygames.utils.ConnectivityReceiver;
import in.games.jollygames.utils.LoadingBar;
import in.games.jollygames.utils.Session_management;
import in.games.jollygames.utils.ToastMsg;

import static in.games.jollygames.Config.BaseUrl.URL_GENERATE_OTP;
import static in.games.jollygames.Config.BaseUrl.URL_VERIFICATION;

public class SendOtpActivity extends AppCompatActivity implements View.OnClickListener {
    ActivitySendOtpBinding binding ;
    Activity ctx = SendOtpActivity.this;
    LoadingBar loadingBar ;
    ToastMsg toastMsg ;
    Session_management session_management ;
    Common common ;
    String TAG = SendOtpActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySendOtpBinding.inflate(getLayoutInflater());
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
        binding.tvLogin.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.rel_go:
                validate();
                break;
            case R.id.tv_login:
                startActivity(new Intent(ctx,LoginActivity.class));
                break;
        }


    }

    void validate()
    {
        String m = binding.etMobile.getText().toString();

        if (m.isEmpty())
        {
            binding.etMobile.setError("Mobile Number is Required");

        }
        else
        {
            if (ConnectivityReceiver.isConnected())
            {
                sendOtp(m,URL_GENERATE_OTP);
            }
        }
    }

    void sendOtp(String m ,String url)
    {   loadingBar.show();
        String otp =common.getRandomKey(6);

        HashMap<String, String> params=new HashMap<>();
        params.put("mobile",m);
        params.put("otp",otp);
        common.postRequest(url, params, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject object = new JSONObject(response.toString());
                    String res=object.getString("status");
                    if(res.equalsIgnoreCase("success"))
                    {
                       Intent i = new Intent(ctx,VerifyOtpActivity.class);
                       i.putExtra("mobile",m);
                       i.putExtra("otp",otp);
                       i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                       startActivity(i);

                    }
                    else
                    {
                        common.showToast(object.getString("message"));
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
package in.games.jollygames.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.HashMap;

import in.games.jollygames.Common.Common;
import in.games.jollygames.R;
import in.games.jollygames.databinding.ActivityRegisterBinding;
import in.games.jollygames.utils.LoadingBar;
import in.games.jollygames.utils.Session_management;
import in.games.jollygames.utils.ToastMsg;

import static in.games.jollygames.Config.BaseUrl.URL_REGISTER;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
ActivityRegisterBinding binding;
    Activity ctx = RegisterActivity.this;
    LoadingBar loadingBar ;
    ToastMsg toastMsg ;
    Session_management session_management ;
    Common common ;
    String TAG = RegisterActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
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
            case R.id.tv_login:
                startActivity(new Intent(ctx,LoginActivity.class));
                break;
            case R.id.rel_go:
                validate();
                break;
        }
    }


    void validate()
    {
//        String name = binding.etName.getText().toString();
        String uname = binding.etUsername.getText().toString();
        String mobile = binding.etMobile.getText().toString();
        String email = binding.etEmail.getText().toString();
        String pass = binding.etPass.getText().toString();
        String cpass = binding.etCPass.getText().toString();

        if (uname.isEmpty())
        {
            binding.etUsername.setError("Username is Required");
        }
//        else if (name.isEmpty())
//        {
//            binding.etName.setError("Name is Required");
//        }
        else if( mobile.isEmpty())
        {
            binding.etMobile.setError("Mobile Number is Required");
        }
        else if(email.isEmpty())
        {
            binding.etEmail.setError("Email Address is Required");
        }
        else if (pass.isEmpty())
        {
            binding.etPass.setError("Password is Required");
        }
        else if (cpass.isEmpty())
        {
            binding.etCPass.setError("Confirm Password is Required");
        }
        else
        {
            if (pass.equals(cpass))
            {
               registerUser(uname,mobile,email,pass);

            }
            else
            {
                common.showToast("No Internet Connection");
            }
        }
    }

    void registerUser(String name ,String mobile ,String email ,String pass)
    {
        loadingBar.show();
        HashMap<String, String> params=new HashMap<>();
        params.put("key","1");
        params.put("username",name);
        params.put("email",email);
        params.put("mobile",mobile);
        params.put("password",pass);
        Log.e("r_params",params.toString());
        common.postRequest(URL_REGISTER, params, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                Log.e("register",response.toString());
                try {
                    JSONObject object = new JSONObject(response.toString());
                    boolean resp=object.getBoolean("responce");
                    if(resp)
                    {
                        common.showToast(""+object.getString("message").toString());
                        Intent intent=new Intent(ctx, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }
                    else
                    {
                        common.showToast(object.getString("error").toString());
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
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
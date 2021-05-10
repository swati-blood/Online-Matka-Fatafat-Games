package in.games.jollygames.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.games.jollygames.Common.Common;
import in.games.jollygames.Interfaces.OnConfigData;
import in.games.jollygames.Model.ConfigModel;
import in.games.jollygames.Model.IndexResponse;
import in.games.jollygames.R;
import in.games.jollygames.utils.Session_management;

import static in.games.jollygames.Config.BaseUrl.URL_INDEX;
import static in.games.jollygames.Config.Constants.REQUEST_TIMEOUT_ERR;

public class SplashActivity extends AppCompatActivity {
    Session_management session_management ;
    Common common ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        common = new Common(this);
        session_management = new Session_management(this);
        common.getConfigData(new OnConfigData() {
            @Override
            public void onGetConfigData(ConfigModel model) {
                session_management.updateItem(REQUEST_TIMEOUT_ERR,model.getError_msg());
                if (session_management.isLoggedIn())
                { Intent i = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(i);
                    finish();
                }
                else {
                    Intent i = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(i);
                    finish();
                }


            }
        });
//        Thread background = new Thread() {
//            public void run() {
//                try {
//                    // Thread will sleep for 5 seconds
//                    sleep(5*1000);
//
//                    // After 5 seconds redirect to another intent
//
//
//                } catch (Exception e) {
//                }
//            }
//        };
//        // start thread
//        background.start();
    }

    public void getConfigData(){
        HashMap<String,String> params=new HashMap<>();
        common.postRequest(URL_INDEX, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String resp) {
                Log.e("Common", "onResponse: "+resp.toString());
                try{
                    JSONArray object = new JSONArray(resp);


                    ArrayList<IndexResponse> list=new ArrayList<>();
                    Gson gson =new Gson();
                    Type typeList=new TypeToken<List<IndexResponse>>(){}.getType();
                    list=gson.fromJson(object.toString(),typeList);
                String error_msg = list.get(0).getErrorMsg();
                session_management.updateItem(REQUEST_TIMEOUT_ERR,error_msg);
                    if (session_management.isLoggedIn())
                    { Intent i = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                    else {
                        Intent i = new Intent(getBaseContext(), LoginActivity.class);
                        startActivity(i);
                        finish();
                    }

                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                common.showVolleyError(error);
            }
        });
    }


}
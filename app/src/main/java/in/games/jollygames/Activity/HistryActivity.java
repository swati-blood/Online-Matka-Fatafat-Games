package in.games.jollygames.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.games.jollygames.Adapter.AllHistoryAdapter;
import in.games.jollygames.AppController;
import in.games.jollygames.Common.Common;
import in.games.jollygames.Model.HistryModel;
import in.games.jollygames.databinding.ActivityHistryBinding;
import in.games.jollygames.utils.ConnectivityReceiver;
import in.games.jollygames.utils.CustomJsonRequest;
import in.games.jollygames.utils.LoadingBar;
import in.games.jollygames.utils.Session_management;

import static in.games.jollygames.Config.BaseUrl.URL_GET_HISTORY;
import static in.games.jollygames.Config.Constants.KEY_ID;

public class HistryActivity extends AppCompatActivity {

    ActivityHistryBinding binding ;
    ArrayList<HistryModel> b_list ,w_list ,list;
    Activity ctx = HistryActivity.this;
    Session_management session_management ;
    LoadingBar loadingBar ;
    Common common ;
    String type;
    AllHistoryAdapter historyAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHistryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViews();
    }
    void initViews() {
        common = new Common(ctx);
        loadingBar = new LoadingBar(ctx);
        session_management = new Session_management(ctx);
        b_list = new ArrayList<>();
        w_list = new ArrayList<>();
        list = new ArrayList<>();
        type = getIntent().getStringExtra("type");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (type.equalsIgnoreCase("bid"))
        {
        getSupportActionBar().setTitle("Bid History");
        }
        else
        {
            getSupportActionBar().setTitle("Win History");
        }
        binding.rvHistry.setLayoutManager(new LinearLayoutManager(ctx));
        if(ConnectivityReceiver.isConnected())
        {
            getBidData(session_management.getUserDetails().get(KEY_ID));
        }
        else
        {
            common.showToast("No Internet Connection");
        }


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }


    private void getBidData(String user_id) {
       loadingBar.show();
       b_list.clear();
       w_list.clear();
      list.clear();
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id",user_id);
        CustomJsonRequest jsonObjReq = new CustomJsonRequest(Request.Method.POST,
                URL_GET_HISTORY, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.e("histry", response.toString());
               loadingBar.dismiss();
                try {
                    boolean res = response.getBoolean("responce");
                    if (res)
                    {
                        JSONArray data = response.getJSONArray("data");

                        for (int i =0 ;i <data.length();i++)
                        {
                            HistryModel model = new HistryModel();
                            JSONObject obj = data.getJSONObject(i);
                            model.setId(obj.getString("id"));
                            model.setMatka_id(obj.getString("matka_id"));
                            model.setGame_id(obj.getString("game_id"));
                            model.setUser_id(obj.getString("user_id"));
                            model.setPoints(obj.getString("points"));
                            model.setDigits(obj.getString("digits"));
                            model.setDate(obj.getString("date"));
                            model.setTime(obj.getString("time"));
                            model.setBet_type(obj.getString("bet_type"));
                            model.setStatus(obj.getString("status"));
                            model.setName(obj.getString("name"));
                            list.add(model);
                        }
                        Log.d("all_list", String.valueOf(list.size()));



                        for (int i = 0 ; i <list.size() ;i++)
                        {
                            if (list.get(i).getStatus().equalsIgnoreCase("win")||list.get(i).getStatus().equalsIgnoreCase("won"))
                            {
                                w_list.add(list.get(i));

                            }
                            else
                            {
                                b_list.add(list.get(i));
                            }
                        }
                      if (type.equalsIgnoreCase("bid"))
                      {
                          historyAdapter = new AllHistoryAdapter(ctx,b_list);
                      }
                      else
                      {
                          historyAdapter = new AllHistoryAdapter(ctx,w_list);
                      }
                      binding.rvHistry.setAdapter(historyAdapter);
                      historyAdapter.notifyDataSetChanged();

                    }
                    else
                    {
                       common.showToast(""+response.get("Error").toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
               loadingBar.dismiss();
           common.VolleyErrorMessage(error);
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq,"histry");

    }

}
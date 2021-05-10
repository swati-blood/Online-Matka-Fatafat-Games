package in.games.jollygames.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import in.games.jollygames.AppController;
import in.games.jollygames.Common.Common;
import in.games.jollygames.R;
import in.games.jollygames.databinding.ActivityHowToPlayBinding;
import in.games.jollygames.utils.ConnectivityReceiver;
import in.games.jollygames.utils.CustomVolleyJsonArrayRequest;
import in.games.jollygames.utils.LoadingBar;

import static in.games.jollygames.Config.BaseUrl.URL_PLAY;

public class HowtoPlayActivity extends AppCompatActivity {
    ActivityHowToPlayBinding binding ;
    LoadingBar loadingBar ;
    Common common ;
    Activity ctx = HowtoPlayActivity.this;
    String slink = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHowToPlayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loadingBar = new LoadingBar(ctx);
        common = new Common(ctx);
        getSupportActionBar().setTitle("How To Play");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (ConnectivityReceiver.isConnected())
        {
            getHowToPlayData();
        }
        else
        {
            common.showToast("No Internet Connection");
        }
        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(slink));
                startActivity(intent);
            }
        });
    }

    private void getHowToPlayData() {
       loadingBar.show();
        String json_request_tag="json_how_request";
        HashMap<String, String> params=new HashMap<String, String>();

        CustomVolleyJsonArrayRequest customVolleyJsonArrayRequest=new CustomVolleyJsonArrayRequest(Request.Method.GET, URL_PLAY, params, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try
                {
                    Log.e("fjnngjn",response.toString());
                    JSONObject jsonObject=response.getJSONObject(0);
                    String data=jsonObject.getString("data");
                    String link=jsonObject.getString("link");
                   binding.tvMsg.setText(data);

                 slink=link;


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
             common.VolleyErrorMessage(error);
            }
        });

        AppController.getInstance().addToRequestQueue(customVolleyJsonArrayRequest,json_request_tag);


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}
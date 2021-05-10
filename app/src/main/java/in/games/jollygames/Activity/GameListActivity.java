package in.games.jollygames.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.games.jollygames.Adapter.GameAdapter;
import in.games.jollygames.AppController;
import in.games.jollygames.Common.Common;
import in.games.jollygames.Model.ApiGameModel;
import in.games.jollygames.Model.GameModel;
import in.games.jollygames.R;
import in.games.jollygames.databinding.ActivityGameListBinding;
import in.games.jollygames.utils.ConnectivityReceiver;
import in.games.jollygames.utils.CustomVolleyJsonArrayRequest;
import in.games.jollygames.utils.LoadingBar;
import in.games.jollygames.utils.RecyclerTouchListener;

import static in.games.jollygames.Config.BaseUrl.URL_MATKA_GAMES;

public class GameListActivity extends AppCompatActivity {
    ActivityGameListBinding binding ;
    ArrayList<GameModel>game_list;
    ArrayList<ApiGameModel> tempList;
    Activity ctx = GameListActivity.this;
    GameAdapter gameAdapter ;
    LoadingBar loadingBar;
    Common common ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGameListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setTitle(""+getIntent().getStringExtra("matkaName"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loadingBar = new LoadingBar(ctx);
        common = new Common(ctx);
        tempList = new ArrayList<>();
        game_list = new ArrayList<>();
        game_list.add(new GameModel("2", "Single Digits ", R.drawable.singledigit, "1"));
        game_list.add(new GameModel("3", "Jodi Digits ", R.drawable.jodidigit, "1"));
        game_list.add(new GameModel("7", "Single Pana", R.drawable.singlepana, "1"));
        game_list.add(new GameModel("8", "Double Pana", R.drawable.doublepana, "1"));
        game_list.add(new GameModel("9", "Triple Pana", R.drawable.tripplepana, "1"));
        game_list.add(new GameModel("12", "Half Sangam Digits", R.drawable.halfsangam, "2"));
        game_list.add(new GameModel("13", "Full Sangam Digits", R.drawable.fullsangam, "3"));

        if (ConnectivityReceiver.isConnected())
        {
            getGames();
        }
        else
        {
            common.showToast("No Internet Connection");
        }

        binding.rvGames.addOnItemTouchListener(new RecyclerTouchListener(ctx, binding.rvGames, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent i = null;
                ApiGameModel model = tempList.get(position);
                switch (model.getGame_name().toLowerCase())
                {
                    case "single_digit":
                    case "jodi_digits":
                    case "single_pana":
                    case "double_pana":
                    case "triple_pana":
                        i = new Intent(ctx, DigitPanaActivity.class);
                        break;
                    case "half_sangam":
                            i = new Intent(ctx, HalfSangamActivity.class);
                        break;
                    case"full_sangam":
                            i = new Intent(ctx, FullSangamActivity.class);
                            break;

                }
               



                if(i!=null) {

                    i.putExtra("game_id", model.getGame_id());
                    i.putExtra("game_name", model.getName());
                    i.putExtra("m_id", getIntent().getStringExtra("m_id"));
                    i.putExtra("matkaName", getIntent().getStringExtra("matkaName"));
                    i.putExtra("start_time", getIntent().getStringExtra("start_time"));
                    i.putExtra("end_time", getIntent().getStringExtra("end_time"));

                    startActivity(i);

                }
            }


            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));



    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        return true;
    }


    public void getGames() {
        tempList.clear ( );
        loadingBar.show ( );
        HashMap<String, String> params = new HashMap<> ( );
        CustomVolleyJsonArrayRequest arrayRequest = new CustomVolleyJsonArrayRequest(Request.Method.POST, URL_MATKA_GAMES, params, new Response.Listener<JSONArray> ( ) {
            @Override
            public void onResponse(JSONArray response) {
                loadingBar.dismiss ( );

                try {
                    Gson gson = new Gson ( );
                    Type listType = new TypeToken<List<ApiGameModel>>( ) {
                    }.getType ( );
                    tempList = gson.fromJson (response.toString ( ), listType);
                    gameAdapter = new GameAdapter(ctx,tempList);
                    binding.rvGames.setLayoutManager(new GridLayoutManager(ctx,2));
                    binding.rvGames.setAdapter(gameAdapter);




                } catch (Exception ex) {

                    ex.printStackTrace ( );
                }
            }
        }, new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss ( );
                common.showVolleyError (error);

            }
        });
        AppController.getInstance ( ).addToRequestQueue (arrayRequest);
    }
}
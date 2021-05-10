package in.games.jollygames.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.games.jollygames.Adapter.NewMatkaAdpater;
import in.games.jollygames.AppController;
import in.games.jollygames.Common.Common;
import in.games.jollygames.Interfaces.OnConfigData;
import in.games.jollygames.Interfaces.OnGetWallet;
import in.games.jollygames.Model.ConfigModel;
import in.games.jollygames.Model.MatkasObjects;
import in.games.jollygames.Model.SliderModel;
import in.games.jollygames.Model.WalletObjects;
import in.games.jollygames.R;
import in.games.jollygames.databinding.FragmentHomeBinding;
import in.games.jollygames.utils.ConnectivityReceiver;
import in.games.jollygames.utils.CustomJsonRequest;
import in.games.jollygames.utils.LoadingBar;
import in.games.jollygames.utils.Session_management;
import in.games.jollygames.utils.ToastMsg;

import static in.games.jollygames.Config.BaseUrl.URL_GET_SLIDER;
import static in.games.jollygames.Config.BaseUrl.URL_Matka;
import static in.games.jollygames.Config.BaseUrl.URL_SLIDER_IMG;


public class HomeFragment extends Fragment implements View.OnClickListener {
    FragmentHomeBinding binding ;
    LoadingBar loadingBar ;
    ToastMsg toastMsg ;
    Session_management session_management ;
    Common common ;
String whatsapp_no ="",call_no ="";
NewMatkaAdpater matkaAdpater ;
ArrayList<MatkasObjects>matkaList ;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater,container,false);
        initViews();
        return binding.getRoot();

    }
    void initViews() {
        common = new Common(getActivity());
        toastMsg = new ToastMsg(getActivity());
        session_management = new Session_management(getActivity());
        loadingBar = new LoadingBar(getActivity());
        binding.linCall.setOnClickListener(this);
        binding.linWhtsapp.setOnClickListener(this);
        binding.rvMatka.setLayoutManager(new LinearLayoutManager(getActivity()));
        matkaList = new ArrayList<>();
        binding.swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (ConnectivityReceiver.isConnected()) {
                    getMatkaData();
                } else {
                    common.showToast("No Internet Connection");
                }
            }
        });

        common.getConfigData(new OnConfigData() {
            @Override
            public void onGetConfigData(ConfigModel model) {
                whatsapp_no = model.getWhatsapp();
                call_no = model.getCall_no();
                binding.tvCall.setText(call_no);
                binding.tvWhatsaap.setText(whatsapp_no);
                int v = Integer.parseInt(model.getVersion());

                if (checkVersion(v)) {
                    if (ConnectivityReceiver.isConnected()) {
                        getMatkaData();
                        getSliderRequest();
                    } else {
                        common.showToast("No Internet Connection");
                    }
                    common.getWalletAmount(new OnGetWallet() {
                        @Override
                        public void onGetWallet(WalletObjects walletModel) {
                            binding.tvWallet.setText(walletModel.getWallet_points());
                        }
                    });

                } else {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
                    builder.setTitle("Version Information");
                    builder.setMessage(model.getMessage());
                    builder.setCancelable(false);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            String url = null;
                            try {
                                url = model.getApp_link();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(url));
                            startActivity(intent);

                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            getActivity().finishAffinity();
                        }
                    });
                    androidx.appcompat.app.AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        });
        binding.getRoot().setFocusableInTouchMode(true);
        binding.getRoot().requestFocus();
        binding.getRoot().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                    builder.setTitle("Confirmation");
                    builder.setMessage("Are you sure want to exit?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();

                            getActivity().finishAffinity();


                        }
                    })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                    final AlertDialog dialog=builder.create();
                    dialog.setOnShowListener( new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface arg0) {
                            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorAccent));
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAccent));
                        }
                    });
                    dialog.show();
                    return true;
                }
                return false;
            }
        });
    }

    public void getMatkaData()
    {
        loadingBar.show();
        if (binding.swipe.isRefreshing())
        {
           binding.swipe.setRefreshing(false);
        }
        HashMap<String,String> params=new HashMap<>();
        common.postRequest(URL_Matka, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String res) {
                matkaList.clear();
                try
                {
                    JSONArray response=new JSONArray(res);
                    for(int i=0; i<response.length();i++)
                    {

                        JSONObject jsonObject=response.getJSONObject(i);

                        MatkasObjects matkasObjects=new MatkasObjects();
                        matkasObjects.setId(jsonObject.getString("id"));
                        matkasObjects.setName(jsonObject.getString("name"));
                        matkasObjects.setStart_time(jsonObject.getString("start_time"));
                        matkasObjects.setEnd_time(jsonObject.getString("end_time"));
                        matkasObjects.setStarting_num(jsonObject.getString("starting_num"));
                        matkasObjects.setNumber(jsonObject.getString("number"));
                        matkasObjects.setEnd_num(jsonObject.getString("end_num"));
                        matkasObjects.setBid_start_time(jsonObject.getString("bid_start_time"));
                        matkasObjects.setBid_end_time(jsonObject.getString("bid_end_time"));
                        matkasObjects.setCreated_at(jsonObject.getString("created_at"));
                        matkasObjects.setUpdated_at(jsonObject.getString("updated_at"));
                        matkasObjects.setSat_start_time(jsonObject.getString("sat_start_time"));
                        matkasObjects.setSat_end_time(jsonObject.getString("sat_end_time"));
                        matkasObjects.setStatus(jsonObject.getString("status"));
//                        matkasObjects.setColor_code(jsonObject.getString("bg_color"));
                        matkaList.add(matkasObjects);





                    }
                }
                catch (Exception ex)
                {

                    ex.printStackTrace();
                    Toast.makeText(getActivity(),"Error :"+ex.getMessage(),Toast.LENGTH_LONG).show();

                    return;
                }


                if(matkaList.size()>0){
                    matkaAdpater=new NewMatkaAdpater(getActivity(),matkaList);
                    binding.rvMatka.setAdapter(matkaAdpater);
                    matkaAdpater.notifyDataSetChanged();
                }
                loadingBar.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                String msg=common.VolleyErrorMessage(error);
                if(!msg.isEmpty())
                {
                    common.showToast(""+msg);
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.lin_call:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_DIAL); // Action for what intent called for
                intent.setData(Uri.parse("tel: " + call_no)); // Data with intent respective action on intent
                startActivity(intent);
                break;
            case R.id.lin_whtsapp:
                common.whatsapp(whatsapp_no,"Hi ! Admin");

        }

    }


    public boolean checkVersion(int v){
        int vs=1;
        try {
            PackageInfo packageInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(),0);
            vs= packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if(v==vs)
            return true;
        else
            return false;
    }

    private void getSliderRequest() {

        HashMap<String, String> params = new HashMap<>();


      CustomJsonRequest req = new CustomJsonRequest(Request.Method.POST, URL_GET_SLIDER, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("sliders", params.toString() + "\n" + response.toString());
                        try {
//                            String slder_type  = response.getString("slider_type");
                            if (response.has("data")) {
                                JSONArray slider_arr = response.getJSONArray("data");
                                Gson gson =new Gson();
                                Type typeList=new TypeToken<List<SliderModel>>(){}.getType();
                                ArrayList<SliderModel>list=gson.fromJson(slider_arr.toString(),typeList);
                                Picasso.with(getActivity()).load(URL_SLIDER_IMG+list.get(0).getImage()).fit().into(binding.ivHomeSlider, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        binding.progressBar.setVisibility(View.GONE);
                                        binding.ivHomeSlider.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onError() {

                                    }
                                });

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            toastMsg.toastIconError(e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
           common.showVolleyError(error);
            }
        });
        AppController.getInstance().addToRequestQueue(req);

    }
}
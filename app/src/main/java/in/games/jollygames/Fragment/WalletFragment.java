package in.games.jollygames.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
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

import in.games.jollygames.Activity.RequestFundsActivity;
import in.games.jollygames.Activity.WithdrawMethodsActivity;
import in.games.jollygames.Activity.WithdrawRequest;
import in.games.jollygames.Adapter.MenuAdapter;
import in.games.jollygames.Adapter.TransactionHistoryAdapter;
import in.games.jollygames.AppController;
import in.games.jollygames.Common.Common;
import in.games.jollygames.Interfaces.OnConfigData;
import in.games.jollygames.Interfaces.OnGetWallet;
import in.games.jollygames.Model.AddWithdrawModel;
import in.games.jollygames.Model.ConfigModel;
import in.games.jollygames.Model.MenuModel;
import in.games.jollygames.Model.WalletObjects;
import in.games.jollygames.R;
import in.games.jollygames.databinding.FragmentWalletBinding;
import in.games.jollygames.utils.ConnectivityReceiver;
import in.games.jollygames.utils.CustomJsonRequest;
import in.games.jollygames.utils.LoadingBar;
import in.games.jollygames.utils.RecyclerTouchListener;
import in.games.jollygames.utils.Session_management;

import static in.games.jollygames.Config.BaseUrl.Url_req_history;
import static in.games.jollygames.Config.Constants.KEY_ID;


public class WalletFragment extends Fragment {
    FragmentWalletBinding binding ;
    Session_management session_management ;
    Common common ;
    LoadingBar loadingBar ;
    ArrayList<MenuModel> list ;
    ArrayList<AddWithdrawModel> wlist ;
    MenuAdapter menuAdapter;
    TransactionHistoryAdapter transactionHistoryAdapter ;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

       binding= FragmentWalletBinding.inflate(inflater, container, false);
        initViews();
        return binding.getRoot();
    }
    void  initViews()
    {
        session_management = new Session_management(getActivity());
       loadingBar= new LoadingBar(getActivity());
        common = new Common(getActivity());
        list = new ArrayList<>();

        wlist = new ArrayList<>();
        common.getConfigData(new OnConfigData() {
            @Override
            public void onGetConfigData(ConfigModel model) {
                binding.tvWithdrawMsg.setText(model.getWithdraw_text());
            }
        });
        common.getWalletAmount(new OnGetWallet() {
            @Override
            public void onGetWallet(WalletObjects walletModel) {
                binding.tvWallet.setText(walletModel.getWallet_points());
            }
        });
        binding.rvMenu.setLayoutManager(new GridLayoutManager(getActivity(),2));
        binding.rvTrans.setLayoutManager(new LinearLayoutManager(getActivity()));
        menuItem();
        if (ConnectivityReceiver.isConnected())
        {
//            getTranssactionHistoryData(session_management.getUserDetails().get(KEY_ID));
            getRequestData(session_management.getUserDetails().get(KEY_ID));
        }
        binding.rvMenu.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), binding.rvMenu, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent i = null;

                switch (position)
                {
                    case 0:
                        i = new Intent(getActivity(), RequestFundsActivity.class);
                        break;
                    case 1:
                        i = new Intent(getActivity(), WithdrawRequest.class);
                        break;
                    case 2:
                        i = new Intent(getActivity(), WithdrawMethodsActivity.class);

                        break;
                    case 3:

                        break;
                   }
                if(i!=null) {
                    startActivity(i);

                }
            }


            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
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
    private void menuItem() {
        list.add(new MenuModel(R.drawable.icons8_add_96px,"TOP UP"));
        list.add(new MenuModel(R.drawable.icons8_minus_96px,"WITHDRAW"));
        list.add(new MenuModel(R.drawable.icons8_bank_96px,"BANKS"));
        list.add(new MenuModel(R.drawable.icons8_data_transfer_96px,"TRANSFER"));

        menuAdapter =new MenuAdapter(getActivity(),list,"wallet");
        binding.rvMenu.setAdapter(menuAdapter);

    }


    private void getRequestData(final String user_id) {

        loadingBar.show();
        String json_tag="json_req";
        final HashMap<String,String> params=new HashMap<String,String>();
        params.put("user_id",user_id);

        wlist.clear();
        CustomJsonRequest customJsonRequest=new CustomJsonRequest(Request.Method.POST, Url_req_history, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("f_histry", response.toString());
                loadingBar.dismiss();
                try {
                    boolean res = response.getBoolean("responce");
                    if (res)
                    {
                        JSONArray data = response.getJSONArray("data");
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<AddWithdrawModel>>() {
                        }.getType();
                        wlist = gson.fromJson(data.toString(), listType);
                        Log.d("w_list", String.valueOf(wlist.size()));

                        if (list.size()>0) {
                           transactionHistoryAdapter= new TransactionHistoryAdapter(getActivity(),wlist);
                            binding.rvTrans.setAdapter(transactionHistoryAdapter);
                            transactionHistoryAdapter.notifyDataSetChanged();
                        }
                        else
                        {
                            common.showToast("No History Available");

                        }


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
        AppController.getInstance().addToRequestQueue(customJsonRequest,json_tag);
    }

}
package in.games.jollygames.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;


import java.util.ArrayList;

import in.games.jollygames.Activity.GameRateActivity;
import in.games.jollygames.Activity.HistryActivity;
import in.games.jollygames.Activity.HowtoPlayActivity;
import in.games.jollygames.Activity.MyProfileActivity;
import in.games.jollygames.Adapter.MenuAdapter;
import in.games.jollygames.BuildConfig;
import in.games.jollygames.Model.MenuModel;
import in.games.jollygames.R;
import in.games.jollygames.databinding.FragmentSettingsBinding;
import in.games.jollygames.utils.RecyclerTouchListener;
import in.games.jollygames.utils.Session_management;

import static in.games.jollygames.Config.Constants.KEY_MOBILE;
import static in.games.jollygames.Config.Constants.KEY_NAME;


public class SettingsFragment extends Fragment {
    FragmentSettingsBinding binding ;
    Session_management session_management ;
    ArrayList<MenuModel> list ;
    MenuAdapter menuAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSettingsBinding.inflate(getLayoutInflater());
        initViews();
        return binding.getRoot();
    }


    void initViews()
    {
        session_management = new Session_management(getActivity());
        binding.tvMobile.setText(session_management.getUserDetails().get(KEY_MOBILE));
        binding.tvName.setText(session_management.getUserDetails().get(KEY_NAME));
        binding.rvMenu.setLayoutManager(new LinearLayoutManager(getActivity()));
        list = new ArrayList<>();
        otherData();
        binding.rvMenu.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), binding.rvMenu, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent i = null;

                switch (position)
                {
                    case 0:
                        i = new Intent(getActivity(), MyProfileActivity.class);
                        break;
                    case 1:
                        i = new Intent(getActivity(), HistryActivity.class);
                        i.putExtra("type","bid");
                        break;
                        case 2:
                        i = new Intent(getActivity(), HistryActivity.class);
                        i.putExtra("type","win");
                        break;
                        case 3:
                        i = new Intent(getActivity(), GameRateActivity.class);
                        break;
                        case 4:
                        i = new Intent(getActivity(), HowtoPlayActivity.class);
                        break;
                        case 5:
                            Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.putExtra(Intent.EXTRA_TEXT,
                                    "Hey check out my app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
                            sendIntent.setType("text/plain");
                            startActivity(sendIntent);
                        break;
                    case 6:
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getResources().getString(R.string.app_name))));
                        break;
                    case 7:

                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());

                        builder.setTitle("LOGOUT ??").setMessage("Do you wish to logout ?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        session_management.logoutSession();

                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });


                        androidx.appcompat.app.AlertDialog dialog=builder.create();
                        dialog.setOnShowListener( new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface arg0) {
                                dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorAccent));
                                dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAccent));
                            }
                        });
                        dialog.show();
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
                    androidx.appcompat.app.AlertDialog.Builder builder=new androidx.appcompat.app.AlertDialog.Builder(getActivity());
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
                    final androidx.appcompat.app.AlertDialog dialog=builder.create();
                    dialog.setOnShowListener( new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface arg0) {
                            dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorAccent));
                            dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAccent));
                        }
                    });
                    dialog.show();
                    return true;
                }
                return false;
            }
        });



    }
    private void otherData() {
     list.add(new MenuModel(R.drawable.icons8_profile_240px,"My Profile"));
     list.add(new MenuModel(R.drawable.icons8_auction_128px,"Bid History"));
     list.add(new MenuModel(R.drawable.icons8_order_history_128px,"Win History"));
     list.add(new MenuModel(R.drawable.icons8_money_transfer_240px,"Game Rates"));
     list.add(new MenuModel(R.drawable.icons8_video_playlist_128px,"How To Play"));
     list.add(new MenuModel(R.drawable.icons8_share_240px,"Share with friends"));
     list.add(new MenuModel(R.drawable.icons8_rating_100px,"Rate Us"));
     list.add(new MenuModel(R.drawable.logout_img,"Sign Out"));
        menuAdapter =new MenuAdapter(getActivity(),list,"setting");
       binding.rvMenu.setAdapter(menuAdapter);

    }


}
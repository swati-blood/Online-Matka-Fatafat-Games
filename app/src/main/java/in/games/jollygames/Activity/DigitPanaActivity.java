package in.games.jollygames.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import in.games.jollygames.Adapter.TableRecyclerAdapter;
import in.games.jollygames.Common.Common;
import in.games.jollygames.Interfaces.OnConfigData;
import in.games.jollygames.Interfaces.OnGetMatka;
import in.games.jollygames.Interfaces.OnGetWallet;
import in.games.jollygames.Model.ConfigModel;
import in.games.jollygames.Model.MatkasObjects;
import in.games.jollygames.Model.TableModel;
import in.games.jollygames.Model.WalletObjects;
import in.games.jollygames.R;
import in.games.jollygames.databinding.ActivityDigitPanaBinding;
import in.games.jollygames.utils.LoadingBar;
import in.games.jollygames.utils.Session_management;

import static in.games.jollygames.utils.InputData.doublePanna;
import static in.games.jollygames.utils.InputData.jodiDigits;
import static in.games.jollygames.utils.InputData.singleDigit;
import static in.games.jollygames.utils.InputData.singlePaana;
import static in.games.jollygames.utils.InputData.triplePanna;

public class DigitPanaActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityDigitPanaBinding binding ;
    Activity ctx = DigitPanaActivity.this;
    Session_management session_management;
    LoadingBar loadingBar;
    Common common;
    int betType =9 ,min_bid_amt =0;
    String matka_name ="" ,game_name="",game_id="",m_id="",end_time="",start_time="",bet_type="";
    List<TableModel> list;
    List<String> digitList;
    TableRecyclerAdapter tableAdaper;
    MatkasObjects item;
    BroadcastReceiver mMessageReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDigitPanaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViews();
    }
    void initViews() {
        list = new ArrayList<>();
        digitList = new ArrayList<>();
        common = new Common(ctx);
        loadingBar = new LoadingBar(ctx);
        session_management = new Session_management(ctx);
        matka_name = getIntent().getStringExtra("matkaName");
        game_id=getIntent().getStringExtra("game_id");
        game_name=getIntent().getStringExtra("game_name");
        m_id=getIntent().getStringExtra("m_id");
        end_time = getIntent().getStringExtra("end_time");
        start_time= getIntent().getStringExtra("start_time");

        binding.tvTitle.setText(game_name);
        binding.ivBack.setOnClickListener(this);
        binding.btnSubmit.setOnClickListener(this);
        binding.btnSave.setOnClickListener(this);
        binding.tvDate.setText(common.getCurrDateDay());
        binding.listView.setLayoutManager(new LinearLayoutManager(ctx));
        tableAdaper=new TableRecyclerAdapter(list, ctx);
        binding.listView.setAdapter(tableAdaper);
        common.getWalletAmount(new OnGetWallet() {
            @Override
            public void onGetWallet(WalletObjects walletModel) {
                binding.tvWallet.setText(walletModel.getWallet_points());

            }
        });
        common.getConfigData(new OnConfigData() {
            @Override
            public void onGetConfigData(ConfigModel model) {
                min_bid_amt = Integer.parseInt(model.getMin_bet_amt());
            }
        });
        common.getMatkaAndWallet(m_id, new OnGetMatka() {
            @Override
            public void onGetMatka(MatkasObjects model) {
                item=model;
                String sTime=common.getStartEndTime(model)[0].toString();
                String eTime=common.getStartEndTime(model)[1].toString();
                betType=common.getBetType(common.getASandC(sTime,eTime));
                Log.e("betType",betType +"--"+bet_type);

                if (betType==1)
                {
                    binding.rbClose.setChecked(true);
                    binding.rbOpen.setEnabled(false);
                }
                else if (betType== 0)
                {
                    binding.rbOpen.setChecked(true);
//                    binding.rbClose.setEnabled(false);
                }

            }
        });

        switch (game_id)
        {
            case "2":
                digitList = Arrays.asList(singleDigit);
               binding.etDigit.setFilters( new InputFilter[] { new InputFilter.LengthFilter(1) } );
                break;
            case "3":
                digitList = Arrays.asList(jodiDigits);
                binding.rbClose.setVisibility(View.GONE);
                binding.rbOpen.setVisibility(View.GONE);
                binding.tvDigit.setText("Jodi Digit");
                bet_type ="Open";
                binding.etDigit.setFilters( new InputFilter[] { new InputFilter.LengthFilter(2) } );
                break;
            case "7":
                digitList = Arrays.asList(singlePaana);
               binding.etDigit.setFilters( new InputFilter[] { new InputFilter.LengthFilter(3) } );
                break;
            case "8":
                digitList = Arrays.asList(doublePanna);
                binding.etDigit.setFilters( new InputFilter[] { new InputFilter.LengthFilter(3) } );
                break;
            case "9":
                digitList = Arrays.asList(triplePanna);
                binding.etDigit.setFilters( new InputFilter[] { new InputFilter.LengthFilter(3) } );
                break;

        }
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(ctx,android.R.layout.simple_list_item_1,digitList);
        binding.etDigit.setAdapter(adapter);
        binding.etDigit.setThreshold(1);

        binding.rbClose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                {
                    binding.rbOpen.setChecked(false);
                    bet_type ="close";
                }
            }
        });
        binding.rbOpen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                {
                    binding.rbClose.setChecked(false);
                    bet_type="open";
                }
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        return true;
    }
    @Override
    protected void onStart() {
        super.onStart();
//
    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_submit:

                common.setBidsDialog(list,m_id,common.getCurrDate(),game_id,
                        binding.tvWallet.getText().toString(),matka_name,loadingBar,binding.btnSubmit,start_time,end_time);

                break;
            case R.id.btn_save:
                validate();

                break;
            case R.id.iv_back:
                finish();
                break;

        }
    }

    void validate()
    {
        String points = binding.etPoints.getText().toString();
        String digit = binding.etDigit.getText().toString();


        if (bet_type.isEmpty())
        {
            common.showToast("Select Bet Type");

        }

        else if (digit.isEmpty())
        {
            binding.etDigit.setError("Enter Digit");
        }
        else if (!digitList.contains(digit))
        {
            binding.etDigit.setError("Invalid Digit");
        }

        else if (points.isEmpty())
        {
            binding.etPoints.setError("Enter Points");
        }
        else if (Integer.parseInt(points)<min_bid_amt)
        {
            common.showToast("Minimum bid ammount is "+ min_bid_amt);
        }
        else if (Integer.parseInt(points)>Integer.parseInt(binding.tvWallet.getText().toString()))
        {
            common.showToast("You dont have sufficient points ");
        }
        else
        {
            if (game_id.equalsIgnoreCase("3") && bet_type.equalsIgnoreCase("close") && betType==1 )
            {
                common.showToast("Bidding is closed for today !");
            }
            else {

                list.add(new TableModel(digit,points,bet_type));
                binding.etDigit.setText("");
                binding.etDigit.clearListSelection();
                binding.etPoints.setText("");
                Log.e("list","---"+list.size()+"---"+list.toString());
                tableAdaper.notifyDataSetChanged();
                if (list.size()>0) {

                    binding.btnSubmit.setVisibility(View.VISIBLE);
                }
                else
                {
                    binding.btnSubmit.setVisibility(View.GONE);
                }
            }

        }

    }


}
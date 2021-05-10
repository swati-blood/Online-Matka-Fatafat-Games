package in.games.jollygames.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import in.games.jollygames.Adapter.WithdrawMethodAdapter;
import in.games.jollygames.Common.Common;

import in.games.jollygames.Model.MenuModel;
import in.games.jollygames.R;

import in.games.jollygames.databinding.ActivityWithdrawMethodsBinding;

import in.games.jollygames.utils.LoadingBar;
import in.games.jollygames.utils.RecyclerTouchListener;

public class WithdrawMethodsActivity extends AppCompatActivity {
    ActivityWithdrawMethodsBinding binding ;
    ArrayList<MenuModel> list;
    Activity ctx = WithdrawMethodsActivity.this;
   WithdrawMethodAdapter withdrawMethodAdapter;
    LoadingBar loadingBar ;
    Common common ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWithdrawMethodsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViews();

    }
    void initViews()
    {
        common = new Common(ctx);
        loadingBar = new LoadingBar(ctx);
      list = new ArrayList<>();
        binding.rvMenu.setLayoutManager(new LinearLayoutManager(ctx));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Withdraw Methods");
        menuItem();
        binding.rvMenu.addOnItemTouchListener(new RecyclerTouchListener(ctx, binding.rvMenu, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent i = null;

                switch (position)
                {
                    case 0:
                        i = new Intent(ctx, BankDetailActivity.class);
                        break;
                    case 1:
                        case 2:
                        case 3:
                        i = new Intent(ctx, UpiDetailsActivity.class);
                        i.putExtra("title",list.get(position).getName());
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


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
    private void menuItem() {
        list.add(new MenuModel(R.drawable.icons8_bank_96px,"Bank Details"));
        list.add(new MenuModel(R.drawable.paytm,"PayTm"));
        list.add(new MenuModel(R.drawable.phonepe,"PhonePe"));
        list.add(new MenuModel(R.drawable.gpay,"Google Pay"));

       withdrawMethodAdapter =new WithdrawMethodAdapter(ctx,list);
        binding.rvMenu.setAdapter(withdrawMethodAdapter);

    }

}
package in.games.jollygames.Activity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import in.games.jollygames.Fragment.HomeFragment;
import in.games.jollygames.Fragment.SettingsFragment;
import in.games.jollygames.Fragment.WalletFragment;
import in.games.jollygames.R;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
     navView  = findViewById(R.id.nav_view);
     navView.setOnNavigationItemSelectedListener(this);
    getSupportFragmentManager().beginTransaction().add(R.id.main_frame,new HomeFragment()).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fm = null ;
        switch (menuItem.getItemId())
        {
            case R.id.nav_home:
                fm = new HomeFragment();
                break;
            case R.id.nav_wallet:
                fm=new WalletFragment() ;
                break;
            case R.id.nav_setting:
                fm = new SettingsFragment();
                break;

        }
        if (fm!=null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,fm).addToBackStack(null).commit();
        }
        return true;
    }
}
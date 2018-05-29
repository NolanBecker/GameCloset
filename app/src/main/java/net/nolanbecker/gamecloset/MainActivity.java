package net.nolanbecker.gamecloset;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.mxn.soul.flowingdrawer_core.FlowingDrawer;

import java.util.ArrayList;
import java.util.List;

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;

public class MainActivity extends AppCompatActivity {

    public static Fragment currentFragment = new GamesFragment();
    public static FragmentManager fm;

    WaveSwipeRefreshLayout swipeRefresh;
    public static FlowingDrawer drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawerLayout);
        fm = getSupportFragmentManager();

        loadFragment();

        setupMenu();

    }

    private void loadFragment() {
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        // replace the FrameLayout with the new Fragment
        fragmentTransaction.replace(R.id.mainFrameLayout, currentFragment);
        fragmentTransaction.commit();
    }

    private void setupMenu() {
        MenuListFragment mMenuFragment = (MenuListFragment) fm.findFragmentById(R.id.id_container_menu);
        if (mMenuFragment == null) {
            mMenuFragment = new MenuListFragment();
            fm.beginTransaction().add(R.id.id_container_menu, mMenuFragment).commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isMenuVisible()) {
            drawerLayout.closeMenu();
        } else if (fm.getBackStackEntryCount() > 0) {
            if (!fm.getBackStackEntryAt(fm.getBackStackEntryCount()-1).getName().equals("games")) {
//                fm.beginTransaction().remove(currentFragment).commit();
                fm.popBackStack();
            } else {
                finish();
                System.exit(0);
            }
        } else {
            finish();
            System.exit(0);
        }
    }
}

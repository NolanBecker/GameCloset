package net.nolanbecker.gamecloset;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.support.design.widget.NavigationView;

public class MenuListFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        NavigationView vNavigation = (NavigationView) view.findViewById(R.id.vNavigation);
        vNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                if (menuItem.getTitle().equals("My Games")) {
                    MainActivity.currentFragment = new GamesFragment();
                    loadFragment("games");
                    MainActivity.drawerLayout.closeMenu(true);
                } else if (menuItem.getTitle().equals("Find A Game")) {
                    MainActivity.currentFragment = new SearchFragment();
                    loadFragment("search");
                    MainActivity.drawerLayout.closeMenu(true);
                } else {
                    Toast.makeText(getActivity(), menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        return view;
    }

    private void loadFragment(String name) {
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = MainActivity.fm.beginTransaction();
        // replace the FrameLayout with the new Fragment
        fragmentTransaction.replace(R.id.mainFrameLayout, MainActivity.currentFragment);
        fragmentTransaction.addToBackStack(name);
        fragmentTransaction.commit();
    }

}
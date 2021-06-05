package com.itheamc.meatprocessing;

import android.os.Build;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.itheamc.meatprocessing.databinding.ActivityMainBinding;
import com.itheamc.meatprocessing.interfaces.MainActivityInterface;
import com.itheamc.meatprocessing.viewmodel.InterfaceViewModel;
import com.itheamc.meatprocessing.viewmodel.SharedViewModel;

public class MainActivity extends AppCompatActivity implements MainActivityInterface {
    private static final String TAG = "MainActivity";
    private ActivityMainBinding mainBinding;
    private NavController navController;
    private SharedViewModel sharedViewModel;
    private InterfaceViewModel interfaceViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        interfaceViewModel = new ViewModelProvider(this).get(InterfaceViewModel.class);
        interfaceViewModel.setMainActivityInterface(this);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_nav_host);

        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
            NavigationUI.setupActionBarWithNavController(this, navController);
            NavigationUI.setupWithNavController(mainBinding.bottomNavigation, navController);
            handleNavigationView();
            observeBadgeNumber();
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        navController.navigateUp();
        return super.onSupportNavigateUp();
    }

    /**
     * Creating method to handle bottom navigation view
     * This method contains all the function related to the bottom navigation view
     */
    private void handleNavigationView() {
        BottomNavigationView bottomNavigation = mainBinding.bottomNavigation;
        // Implementing onDestinationChange listener on navigation view
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                // NavView will be visible
                boolean isShown = showBackButton(destination);
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setDisplayShowHomeEnabled(isShown);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(isShown);
                }

                // Bottom navigation visibility
                TransitionManager.beginDelayedTransition(mainBinding.bottomNavigation);
                bottomNavigation.setVisibility(!isShown ? View.VISIBLE : View.GONE);
                observeBadgeNumber();
            }
        });

    }

    // Creating function to check the destination and return boolean value
    // to handleNavigationActionButton
    private boolean showBackButton(NavDestination destination) {
        return destination.getId() != R.id.homeFragment &&
                destination.getId() != R.id.cartFragment &&
                destination.getId() != R.id.distributionsFragment &&
                destination.getId() != R.id.settingFragment;
    }

    // Observer method for the cart badge
    private void observeBadgeNumber() {

        //Creating Badge for the cart navview icon
        BadgeDrawable badge = mainBinding.bottomNavigation.getOrCreateBadge(R.id.cartFragment);
        badge.setVisible(sharedViewModel.getCartItemsList().size() > 0);
        badge.setNumber(sharedViewModel.getCartItemsList().size());

//        sharedViewModel.getCartItemSize().observe(this, new Observer<Integer>() {
//            @Override
//            public void onChanged(Integer integer) {
//                //Creating Badge for the cart navview icon
//                BadgeDrawable badge = mainBinding.bottomNavigation.getOrCreateBadge(R.id.cartFragment);
//                badge.setVisible(integer > 0);
//                badge.setNumber(integer);
//            }
//        });
    }

    @Override
    public void updateBadgeNumber() {
        observeBadgeNumber();
    }
}
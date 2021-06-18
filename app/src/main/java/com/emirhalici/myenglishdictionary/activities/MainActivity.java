package com.emirhalici.myenglishdictionary.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.emirhalici.myenglishdictionary.fragments.AddMFragment;
import com.emirhalici.myenglishdictionary.fragments.QuizFragment;
import com.emirhalici.myenglishdictionary.R;
import com.emirhalici.myenglishdictionary.models.WordModel;
import com.emirhalici.myenglishdictionary.fragments.AboutFragment;
import com.emirhalici.myenglishdictionary.fragments.AddFragment;
import com.emirhalici.myenglishdictionary.fragments.DisplayWordFragment;
import com.emirhalici.myenglishdictionary.fragments.HomeFragment;
import com.emirhalici.myenglishdictionary.fragments.QuizEndFragment;
import com.emirhalici.myenglishdictionary.utils.DatabaseHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private long backPressedTime;
    DatabaseHelper databaseHelper;
    public ArrayList<String> finalTypeList = new ArrayList<>();
    public String searchQuery;
    public ArrayList<WordModel> finalWordList = new ArrayList<>();
    Toast backToast;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        Fragment myFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (myFragment instanceof QuizEndFragment) {outState.putString("fragmentType","quizEndFragment");}
        else if (myFragment instanceof AddMFragment) {outState.putString("fragmentType","addMFragment");}
        else if (myFragment instanceof DisplayWordFragment) {outState.putString("fragmentType", "displayWordFragment");}
        else if (myFragment instanceof AboutFragment) {outState.putString("fragmentType", "aboutFragment");}
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // hotfix for android.os.NetworkOnMainThreadException
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        // holy shit it really worked.
        // i don't know what i'm doing.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String sortType = sharedPreferences.getString("sortType","date_asc");
        Log.e("onCreate",sortType + " on create");
        boolean isDarkMode = sharedPreferences.getBoolean("isDarkMode",false);
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); }

        // after rotated, check if this is saved state or just new activity
        if (savedInstanceState!=null) {
            switch (savedInstanceState.getString("fragmentType", "homeFragment")) {
                case "homeFragment":
                    openHomeFragment(sortType);
                case "addMFragment":
                    getSupportFragmentManager().popBackStack("addMFragment",0);
                case "aboutFragment":
                    getSupportFragmentManager().popBackStack("aboutFragment",0);
                case "quizEndFragment":
                    getSupportFragmentManager().popBackStack("quizEndFragment", 0);
                    //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new QuizEndFragment()).addToBackStack("quizEndFragment").commit();
            }

        } else {
            openHomeFragment(sortType);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_navigation, menu);

        if ((Build.VERSION.SDK_INT < Build.VERSION_CODES.N)) {
            menu.findItem(R.id.word_asc).setVisible(false);
            menu.findItem(R.id.word_desc).setVisible(false);
            menu.findItem(R.id.type_asc).setVisible(false);
            menu.findItem(R.id.type_desc).setVisible(false);
        }


        // search
        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView search = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment(),"homeFragment").addToBackStack("homeFragment").commit();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                searchQuery = newText;
                applyQueryTextChange();
                return false;
            }
        });
        return true;
    }

    public boolean applyQueryTextChange() {
        finalWordList.removeAll(finalWordList);
        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        ArrayList<WordModel> initialList = databaseHelper.getEveryWord();
        for (WordModel wordModel : initialList) {
            String wordName = wordModel.getWord().toLowerCase();
            if (wordName.contains(searchQuery.toLowerCase())) {
                finalWordList.add(wordModel);
            }
        }
        return true;
    }


    // call invalidateOptionsMenu() to call this method
    // it is for disabling most menu items when not in home fragment
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        // if fragment is aboutfragment, disable aboutme option from topbar
        if (fragment instanceof AboutFragment) {
            menu.findItem(R.id.aboutme).setVisible(false);
        }
        // if fragment isn't homefragment, disable most topbar options
        if (!(fragment instanceof HomeFragment)) {
            menu.findItem(R.id.sort).setVisible(false);
            menu.findItem(R.id.filter).setVisible(false);
            menu.findItem(R.id.compact).setVisible(false);
            menu.findItem(R.id.app_bar_search).setVisible(false);
            menu.findItem(R.id.dark).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    

    // top navigation menu listener
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        SharedPreferences sharedPreferences;
        boolean CardType;
        SharedPreferences.Editor editor;
        boolean isDarkMode;
        switch (item.getItemId()) {
            case R.id.dark:
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                isDarkMode = sharedPreferences.getBoolean("isDarkMode",false);
                editor = sharedPreferences.edit();
                if (isDarkMode) {
                    editor.putBoolean("isDarkMode", false);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                else {
                    editor.putBoolean("isDarkMode", true);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                editor.apply();
                return true;
            case R.id.app_bar_search:
                return true;
            case R.id.filter:
                // open dialog with checkboxes
                DatabaseHelper databaseHelper = new DatabaseHelper(this);
                ArrayList<String> typeList = databaseHelper.getEveryType();
                String[] typeListArray = typeList.toArray(new String[0]);
                boolean[] typeListChecked = new boolean[typeListArray.length];
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
                String alertTitle = "Type Filter";
                builder.setTitle(alertTitle);
                builder.setMultiChoiceItems(typeListArray, typeListChecked, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        typeListChecked[which] = isChecked;
                    }
                });
                builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finalTypeList.clear();
                        int c=0;
                        for (int i=0; i<typeListChecked.length; i++) {
                            if (typeListChecked[i]) {
                                c++;
                                finalTypeList.add(typeListArray[i]);
                            }
                        }
                        if (c==0) {
                            Toast.makeText(MainActivity.this, getString(R.string.tickbox), Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Fragment fragment = new HomeFragment();
                            Bundle args = new Bundle();
                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            String sortType = sharedPreferences.getString("sortType", "date_asc");
                            args.putString("sortType", sortType);
                            args.putStringArrayList("typeFilter", finalTypeList);
                            fragment.setArguments(args);
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment,"homeFragment").addToBackStack("homeFragment").commit();
                        }
                    }
                });
                builder.setNeutralButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.create().show();
                return true;
            case R.id.aboutme:
                invalidateOptionsMenu();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AboutFragment(), "aboutFragment").addToBackStack("aboutFragment").commit();
                return true;
            case R.id.sort:
                return true;
            case R.id.compact:
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                CardType = sharedPreferences.getBoolean("CardType",true);
                editor = sharedPreferences.edit();
                String sortType = sharedPreferences.getString("sortType","date_asc");
                if (CardType) {
                    editor.putBoolean("CardType", false);
                    item.setTitle("Default Mode");
                }
                else {
                    editor.putBoolean("CardType", true);
                    item.setTitle("Compact Mode");
                }
                editor.apply();
                openHomeFragment(sortType);
                return true;
            case R.id.date_asc:
                openHomeFragment("date_asc");
                return true;
            case R.id.date_desc:
                openHomeFragment("date_desc");
                return true;
            case R.id.word_asc:
                openHomeFragment("word_asc");
                return true;
            case R.id.word_desc:
                openHomeFragment("word_desc");
                return true;
            case R.id.type_asc:
                openHomeFragment("type_asc");
                return true;
            case R.id.type_desc:
                openHomeFragment("type_desc");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void openHomeFragment(String sortType) {
        Fragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString("sortType", sortType);
        fragment.setArguments(args);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("sortType", sortType);
        editor.apply();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment,"homeFragment").addToBackStack("homeFragment").commit();
    }

    // bottom navigation bar listener
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.nav_home:
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    String sortType = sharedPreferences.getString("sortType","word_asc");
                    selectedFragment = new HomeFragment();
                    Bundle args = new Bundle();
                    args.putString("sortType", sortType);
                    selectedFragment.setArguments(args);
                    invalidateOptionsMenu();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment, "homeFragment").addToBackStack("homeFragment").commit();
                    break;
                case R.id.nav_add:
                    selectedFragment = new AddFragment();
                    invalidateOptionsMenu();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).addToBackStack("addFragment").commit();
                    break;
                case R.id.nav_quiz:
                    selectedFragment = new QuizFragment();
                    invalidateOptionsMenu();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).addToBackStack("quizFragment").commit();
                    break;
            }

            return true;

        }
    };

    @Override
    public void onBackPressed() {
        Fragment myFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (myFragment != null && (myFragment instanceof HomeFragment || myFragment instanceof QuizFragment ||
                myFragment instanceof AddFragment || myFragment instanceof QuizEndFragment)) {
            if (backPressedTime+1000>System.currentTimeMillis()) {
                backToast.cancel();
                return;
            } else {
                backToast = Toast.makeText(this, getString(R.string.pressToExit), Toast.LENGTH_SHORT);
                backToast.show();
            }
            backPressedTime = System.currentTimeMillis();
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                invalidateOptionsMenu();
                if (myFragment instanceof DisplayWordFragment) {
                    getSupportFragmentManager().popBackStack("homeFragment",0);
                } else
                {
                    getSupportFragmentManager().popBackStack();
                }
                invalidateOptionsMenu();
            }
        }


    }
}
package com.ghofrani.classapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.ghofrani.classapp.R;
import com.ghofrani.classapp.event.CollapseLists;
import com.ghofrani.classapp.fragment.Classes;
import com.ghofrani.classapp.fragment.Homework;
import com.ghofrani.classapp.fragment.Overview;
import com.ghofrani.classapp.fragment.timetable.Friday;
import com.ghofrani.classapp.fragment.timetable.Monday;
import com.ghofrani.classapp.fragment.timetable.Saturday;
import com.ghofrani.classapp.fragment.timetable.Sunday;
import com.ghofrani.classapp.fragment.timetable.Thursday;
import com.ghofrani.classapp.fragment.timetable.Tuesday;
import com.ghofrani.classapp.fragment.timetable.Wednesday;
import com.ghofrani.classapp.module.DataSingleton;
import com.ghofrani.classapp.module.Utils;

import org.greenrobot.eventbus.EventBus;
import org.joda.time.DateTimeConstants;

import java.util.ArrayList;
import java.util.List;

public class Main extends AppCompatActivity implements DrawerLayout.DrawerListener {

    private final float TAB_LAYOUT_LAYOUT_LEFT_MARGIN = 60.5f;
    private final int TAB_LAYOUT_LAYOUT_ANIMATION_DURATION = 200;
    private final int RESULT_OK = 0;
    private final int ID_OVERVIEW = 0;
    private final int ID_TIMETABLE = 1;
    private final int ID_CLASSES = 2;
    private final int ID_HOMEWORK = 3;
    private final int ID_SETTINGS = 4;

    private FloatingActionButton floatingActionButton;
    private DrawerLayout drawerLayout;
    private ScrollView scrollView;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private NavigationView navigationView;
    private int currentView = ID_OVERVIEW;
    private boolean operateOnDrawerClosed;
    private int drawerViewToSwitchTo;
    private boolean floatingActionButtonContrast = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        floatingActionButtonContrast = Utils.setThemeGetContrastFlag(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Overview");
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);

        final ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);

        drawerLayout.addDrawerListener(this);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();

        scrollView = (ScrollView) findViewById(R.id.main_scroll_view);

        tabLayout = (TabLayout) findViewById(R.id.main_tab_layout);
        viewPager = (ViewPager) findViewById(R.id.main_view_pager);

        navigationView = (NavigationView) findViewById(R.id.main_navigation_view);
        navigationView.setCheckedItem(R.id.overview);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                drawerLayout.closeDrawers();

                if (!menuItem.isChecked()) {

                    operateOnDrawerClosed = true;

                    if (menuItem.getItemId() == R.id.settings) {

                        if (currentView == ID_OVERVIEW)
                            EventBus.getDefault().post(new CollapseLists());

                        drawerViewToSwitchTo = ID_SETTINGS;

                    } else {

                        switch (menuItem.getItemId()) {

                            case R.id.overview:

                                if (floatingActionButton.isShown())
                                    floatingActionButton.hide();

                                toolbar.setTitle("Overview");

                                tabLayout.setVisibility(AppBarLayout.GONE);
                                viewPager.setVisibility(LinearLayout.GONE);

                                scrollView.setVisibility(LinearLayout.VISIBLE);

                                navigationView.setCheckedItem(R.id.overview);

                                currentView = ID_OVERVIEW;
                                drawerViewToSwitchTo = ID_OVERVIEW;

                                break;

                            case R.id.timetable:

                                if (!floatingActionButton.isShown())
                                    floatingActionButton.show();

                                if (floatingActionButtonContrast)
                                    floatingActionButton.setImageResource(R.drawable.edit_black);
                                else
                                    floatingActionButton.setImageResource(R.drawable.edit_white);

                                toolbar.setTitle("Timetable");

                                scrollView.setVisibility(LinearLayout.GONE);

                                navigationView.setCheckedItem(R.id.timetable);

                                currentView = ID_TIMETABLE;
                                drawerViewToSwitchTo = ID_TIMETABLE;

                                break;

                            case R.id.classes:

                                if (!floatingActionButton.isShown())
                                    floatingActionButton.show();

                                if (floatingActionButtonContrast)
                                    floatingActionButton.setImageResource(R.drawable.add_black);
                                else
                                    floatingActionButton.setImageResource(R.drawable.add_white);

                                toolbar.setTitle("Classes");

                                tabLayout.setVisibility(AppBarLayout.GONE);
                                viewPager.setVisibility(LinearLayout.GONE);

                                scrollView.setVisibility(LinearLayout.VISIBLE);

                                navigationView.setCheckedItem(R.id.classes);

                                currentView = ID_CLASSES;
                                drawerViewToSwitchTo = ID_CLASSES;

                                break;

                            case R.id.homework:

                                if (!floatingActionButton.isShown())
                                    floatingActionButton.show();

                                if (floatingActionButtonContrast)
                                    floatingActionButton.setImageResource(R.drawable.add_black);
                                else
                                    floatingActionButton.setImageResource(R.drawable.add_white);

                                toolbar.setTitle("Homework");

                                tabLayout.setVisibility(AppBarLayout.GONE);
                                viewPager.setVisibility(LinearLayout.GONE);

                                scrollView.setVisibility(LinearLayout.VISIBLE);

                                navigationView.setCheckedItem(R.id.homework);

                                currentView = ID_HOMEWORK;
                                drawerViewToSwitchTo = ID_HOMEWORK;

                                break;

                        }

                    }

                }

                return true;

            }

        });

        floatingActionButton = (FloatingActionButton) findViewById(R.id.main_floating_action_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (currentView == ID_TIMETABLE) {

                    if (!DataSingleton.getInstance().getAllClassNamesArrayList().isEmpty())
                        startActivity(new Intent(Main.this, EditDay.class).putExtra("day", DataSingleton.getInstance().getSelectedTabPosition()));
                    else
                        Toast.makeText(Main.this, "Add classes first!", Toast.LENGTH_LONG).show();

                } else if (currentView == ID_CLASSES) {

                    startActivityForResult(new Intent(Main.this, AddClass.class), RESULT_OK);

                } else if (currentView == ID_HOMEWORK) {

                    if (DataSingleton.getInstance().getAllClassNamesArrayList().isEmpty())
                        Toast.makeText(Main.this, "Add classes first!", Toast.LENGTH_LONG).show();
                    else
                        startActivity(new Intent(Main.this, AddHomework.class));

                }

            }

        });

        switchToView((getIntent().hasExtra("fragment") ? getIntent().getIntExtra("fragment", ID_OVERVIEW) : ID_OVERVIEW));

    }

    @Override
    protected void onResume() {

        super.onResume();

        if (toolbar == null)
            toolbar = (Toolbar) findViewById(R.id.main_toolbar);

        if (floatingActionButton == null)
            floatingActionButton = (FloatingActionButton) findViewById(R.id.main_floating_action_button);

        if (scrollView == null)
            scrollView = (ScrollView) findViewById(R.id.main_scroll_view);

        if (navigationView == null)
            navigationView = (NavigationView) findViewById(R.id.main_navigation_view);

        if (drawerLayout == null)
            drawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);

        if (tabLayout == null)
            tabLayout = (TabLayout) findViewById(R.id.main_tab_layout);

        if (viewPager == null)
            viewPager = (ViewPager) findViewById(R.id.main_view_pager);

        if (DataSingleton.getInstance().isRecreate()) {

            performRecreate();

        } else if (DataSingleton.getInstance().isChangedFirstDay()) {

            performRecreate();

        }

    }

    private void performRecreate() {

        DataSingleton.getInstance().setRecreate(false);
        DataSingleton.getInstance().setAnimated(false);
        DataSingleton.getInstance().setChangedFirstDay(false);
        DataSingleton.getInstance().setSelectedTabPosition(0);

        drawerLayout = null;
        toolbar = null;
        tabLayout = null;
        viewPager = null;
        scrollView = null;
        navigationView = null;
        floatingActionButton = null;

        finish();

        startActivity(getIntent());

    }

    @Override
    public void onTrimMemory(int level) {

        if (level == TRIM_MEMORY_UI_HIDDEN) {

            drawerLayout = null;
            toolbar = null;
            tabLayout = null;
            viewPager = null;
            scrollView = null;
            navigationView = null;
            floatingActionButton = null;

        }

        super.onTrimMemory(level);

    }

    @Override
    public void onDrawerOpened(View drawerView) {
    }

    @Override
    public void onDrawerClosed(View drawerView) {

        if (operateOnDrawerClosed) {

            performExpensiveSwitchOperations(drawerViewToSwitchTo, getSupportFragmentManager().beginTransaction());

            operateOnDrawerClosed = false;

        }

    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
    }

    @Override
    public void onDrawerStateChanged(int newState) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent resultIntent) {

        super.onActivityResult(requestCode, resultCode, resultIntent);

        if (resultCode == RESULT_OK) {

            if (resultIntent.getStringExtra("class").equals("AddClass")) {

                onResume();

                switchToView(resultIntent.getIntExtra("switch_to_timetable", requestCode));
                Toast.makeText(this, "Add your class into the timetable!", Toast.LENGTH_LONG).show();

            }

        }

    }

    private void switchToView(int viewToSwitchTo) {

        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        switch (viewToSwitchTo) {

            case ID_OVERVIEW:

                if (floatingActionButton.isShown())
                    floatingActionButton.hide();

                toolbar.setTitle("Overview");

                tabLayout.setVisibility(AppBarLayout.GONE);
                viewPager.setVisibility(LinearLayout.GONE);

                scrollView.setVisibility(LinearLayout.VISIBLE);

                navigationView.setCheckedItem(R.id.overview);

                currentView = viewToSwitchTo;

                break;

            case ID_TIMETABLE:

                if (!floatingActionButton.isShown())
                    floatingActionButton.show();

                if (floatingActionButtonContrast)
                    floatingActionButton.setImageResource(R.drawable.edit_black);
                else
                    floatingActionButton.setImageResource(R.drawable.edit_white);

                toolbar.setTitle("Timetable");

                scrollView.setVisibility(LinearLayout.GONE);

                navigationView.setCheckedItem(R.id.timetable);

                currentView = viewToSwitchTo;

                break;

            case ID_CLASSES:

                if (!floatingActionButton.isShown())
                    floatingActionButton.show();

                if (floatingActionButtonContrast)
                    floatingActionButton.setImageResource(R.drawable.add_black);
                else
                    floatingActionButton.setImageResource(R.drawable.add_white);

                toolbar.setTitle("Classes");

                tabLayout.setVisibility(AppBarLayout.GONE);
                viewPager.setVisibility(LinearLayout.GONE);

                scrollView.setVisibility(LinearLayout.VISIBLE);

                navigationView.setCheckedItem(R.id.classes);

                currentView = viewToSwitchTo;

                break;

            case ID_HOMEWORK:

                if (!floatingActionButton.isShown())
                    floatingActionButton.show();

                if (floatingActionButtonContrast)
                    floatingActionButton.setImageResource(R.drawable.add_black);
                else
                    floatingActionButton.setImageResource(R.drawable.add_white);

                toolbar.setTitle("Homework");

                tabLayout.setVisibility(AppBarLayout.GONE);
                viewPager.setVisibility(LinearLayout.GONE);

                scrollView.setVisibility(LinearLayout.VISIBLE);

                navigationView.setCheckedItem(R.id.homework);

                currentView = viewToSwitchTo;

                break;

            case ID_SETTINGS:

                if (currentView == ID_OVERVIEW)
                    EventBus.getDefault().post(new CollapseLists());

                break;

        }

        performExpensiveSwitchOperations(viewToSwitchTo, fragmentTransaction);

    }

    @Override
    protected void onNewIntent(Intent intent) {

        super.onNewIntent(intent);

        switchToView((intent.hasExtra("fragment") ? intent.getIntExtra("fragment", ID_OVERVIEW) : ID_OVERVIEW));

    }

    private void performExpensiveSwitchOperations(int viewID, final FragmentTransaction fragmentTransaction) {

        switch (viewID) {

            case ID_OVERVIEW:

                final Overview overviewFragment = new Overview();
                fragmentTransaction.replace(R.id.main_scroll_view, overviewFragment, "overview_fragment");
                fragmentTransaction.commit();

                break;

            case ID_TIMETABLE:

                tabLayout.setVisibility(AppBarLayout.VISIBLE);
                viewPager.setVisibility(LinearLayout.VISIBLE);

                if (getSupportFragmentManager().findFragmentById(R.id.main_scroll_view) != null)
                    fragmentTransaction.remove(getSupportFragmentManager().findFragmentById(R.id.main_scroll_view)).commit();

                final ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

                final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

                switch (Integer.parseInt(sharedPreferences.getString("first_day_of_week", "1"))) {

                    case DateTimeConstants.MONDAY:

                        adapter.addFragment(new Monday(), "MONDAY");
                        adapter.addFragment(new Tuesday(), "TUESDAY");
                        adapter.addFragment(new Wednesday(), "WEDNESDAY");
                        adapter.addFragment(new Thursday(), "THURSDAY");
                        adapter.addFragment(new Friday(), "FRIDAY");
                        adapter.addFragment(new Saturday(), "SATURDAY");
                        adapter.addFragment(new Sunday(), "SUNDAY");

                        break;

                    case DateTimeConstants.TUESDAY:

                        adapter.addFragment(new Tuesday(), "TUESDAY");
                        adapter.addFragment(new Wednesday(), "WEDNESDAY");
                        adapter.addFragment(new Thursday(), "THURSDAY");
                        adapter.addFragment(new Friday(), "FRIDAY");
                        adapter.addFragment(new Saturday(), "SATURDAY");
                        adapter.addFragment(new Sunday(), "SUNDAY");
                        adapter.addFragment(new Monday(), "MONDAY");

                        break;

                    case DateTimeConstants.WEDNESDAY:

                        adapter.addFragment(new Wednesday(), "WEDNESDAY");
                        adapter.addFragment(new Thursday(), "THURSDAY");
                        adapter.addFragment(new Friday(), "FRIDAY");
                        adapter.addFragment(new Saturday(), "SATURDAY");
                        adapter.addFragment(new Sunday(), "SUNDAY");
                        adapter.addFragment(new Monday(), "MONDAY");
                        adapter.addFragment(new Tuesday(), "TUESDAY");

                        break;

                    case DateTimeConstants.THURSDAY:

                        adapter.addFragment(new Thursday(), "THURSDAY");
                        adapter.addFragment(new Friday(), "FRIDAY");
                        adapter.addFragment(new Saturday(), "SATURDAY");
                        adapter.addFragment(new Sunday(), "SUNDAY");
                        adapter.addFragment(new Monday(), "MONDAY");
                        adapter.addFragment(new Tuesday(), "TUESDAY");
                        adapter.addFragment(new Wednesday(), "WEDNESDAY");

                        break;

                    case DateTimeConstants.FRIDAY:

                        adapter.addFragment(new Friday(), "FRIDAY");
                        adapter.addFragment(new Saturday(), "SATURDAY");
                        adapter.addFragment(new Sunday(), "SUNDAY");
                        adapter.addFragment(new Monday(), "MONDAY");
                        adapter.addFragment(new Tuesday(), "TUESDAY");
                        adapter.addFragment(new Wednesday(), "WEDNESDAY");
                        adapter.addFragment(new Thursday(), "THURSDAY");

                        break;

                    case DateTimeConstants.SATURDAY:

                        adapter.addFragment(new Saturday(), "SATURDAY");
                        adapter.addFragment(new Sunday(), "SUNDAY");
                        adapter.addFragment(new Monday(), "MONDAY");
                        adapter.addFragment(new Tuesday(), "TUESDAY");
                        adapter.addFragment(new Wednesday(), "WEDNESDAY");
                        adapter.addFragment(new Thursday(), "THURSDAY");
                        adapter.addFragment(new Friday(), "FRIDAY");

                        break;

                    case DateTimeConstants.SUNDAY:

                        adapter.addFragment(new Sunday(), "SUNDAY");
                        adapter.addFragment(new Monday(), "MONDAY");
                        adapter.addFragment(new Tuesday(), "TUESDAY");
                        adapter.addFragment(new Wednesday(), "WEDNESDAY");
                        adapter.addFragment(new Thursday(), "THURSDAY");
                        adapter.addFragment(new Friday(), "FRIDAY");
                        adapter.addFragment(new Saturday(), "SATURDAY");

                        break;

                }

                viewPager.setAdapter(adapter);

                tabLayout.clearOnTabSelectedListeners();
                tabLayout.removeAllTabs();

                tabLayout.setupWithViewPager(viewPager);

                tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {

                    @Override
                    public void onTabSelected(TabLayout.Tab tabLayoutTab) {

                        super.onTabSelected(tabLayoutTab);

                        if (tabLayoutTab.getPosition() != 0) {

                            if (!DataSingleton.getInstance().isAnimated()) {

                                final LinearLayout mainTabLayoutLayout = (LinearLayout) findViewById(R.id.main_tab_layout_layout);
                                final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mainTabLayoutLayout.getLayoutParams();

                                final Animation animation = new Animation() {

                                    @Override
                                    protected void applyTransformation(float interpolatedTime, Transformation t) {

                                        params.leftMargin = (int) (getPixelFromDP(TAB_LAYOUT_LAYOUT_LEFT_MARGIN) - (getPixelFromDP(TAB_LAYOUT_LAYOUT_LEFT_MARGIN) * interpolatedTime));

                                        mainTabLayoutLayout.setLayoutParams(params);

                                    }
                                };

                                animation.setDuration(TAB_LAYOUT_LAYOUT_ANIMATION_DURATION);
                                drawerLayout.startAnimation(animation);

                                DataSingleton.getInstance().setAnimated(true);

                            }

                        } else {

                            if (DataSingleton.getInstance().isAnimated()) {

                                final LinearLayout mainTabLayoutLayout = (LinearLayout) findViewById(R.id.main_tab_layout_layout);
                                final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mainTabLayoutLayout.getLayoutParams();

                                final Animation animation = new Animation() {

                                    @Override
                                    protected void applyTransformation(float interpolatedTime, Transformation t) {

                                        params.leftMargin = (int) (getPixelFromDP(TAB_LAYOUT_LAYOUT_LEFT_MARGIN) * interpolatedTime);

                                        mainTabLayoutLayout.setLayoutParams(params);

                                    }
                                };

                                animation.setDuration(TAB_LAYOUT_LAYOUT_ANIMATION_DURATION);
                                drawerLayout.startAnimation(animation);

                                DataSingleton.getInstance().setAnimated(false);

                            }

                        }

                        DataSingleton.getInstance().setSelectedTabPosition(tabLayoutTab.getPosition());

                    }

                });

                tabLayout.getTabAt(DataSingleton.getInstance().getSelectedTabPosition()).select();

                if (DataSingleton.getInstance().getSelectedTabPosition() != 0) {

                    if (!DataSingleton.getInstance().isAnimated()) {

                        final LinearLayout mainTabLayoutLayout = (LinearLayout) findViewById(R.id.main_tab_layout_layout);
                        final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mainTabLayoutLayout.getLayoutParams();

                        final Animation animation = new Animation() {

                            @Override
                            protected void applyTransformation(float interpolatedTime, Transformation t) {

                                params.leftMargin = (int) (getPixelFromDP(TAB_LAYOUT_LAYOUT_LEFT_MARGIN) - (getPixelFromDP(TAB_LAYOUT_LAYOUT_LEFT_MARGIN) * interpolatedTime));

                                mainTabLayoutLayout.setLayoutParams(params);

                            }
                        };

                        animation.setDuration(TAB_LAYOUT_LAYOUT_ANIMATION_DURATION);
                        drawerLayout.startAnimation(animation);

                        DataSingleton.getInstance().setAnimated(true);

                    }

                } else {

                    if (DataSingleton.getInstance().isAnimated()) {

                        final LinearLayout mainTabLayoutLayout = (LinearLayout) findViewById(R.id.main_tab_layout_layout);
                        final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mainTabLayoutLayout.getLayoutParams();

                        final Animation animation = new Animation() {

                            @Override
                            protected void applyTransformation(float interpolatedTime, Transformation t) {

                                params.leftMargin = (int) (getPixelFromDP(TAB_LAYOUT_LAYOUT_LEFT_MARGIN) * interpolatedTime);

                                mainTabLayoutLayout.setLayoutParams(params);

                            }
                        };

                        animation.setDuration(TAB_LAYOUT_LAYOUT_ANIMATION_DURATION);
                        drawerLayout.startAnimation(animation);

                        DataSingleton.getInstance().setAnimated(false);

                    }

                }

                break;

            case ID_CLASSES:

                final Classes classesFragment = new Classes();
                fragmentTransaction.replace(R.id.main_scroll_view, classesFragment, "classes_fragment");
                fragmentTransaction.commit();

                break;

            case ID_HOMEWORK:

                final Homework homeworkFragment = new Homework();
                fragmentTransaction.replace(R.id.main_scroll_view, homeworkFragment, "homework_fragment");
                fragmentTransaction.commit();

                break;

            case ID_SETTINGS:

                startActivity(new Intent(this, Settings.class));

                break;

        }

    }

    private int getPixelFromDP(float dPtoConvert) {

        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dPtoConvert * scale + 0.5f);

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fragmentManager) {

            super(fragmentManager);

        }

        @Override
        public Fragment getItem(int position) {

            return fragmentList.get(position);

        }

        @Override
        public int getCount() {

            return fragmentList.size();

        }

        public void addFragment(Fragment fragment, String title) {

            fragmentList.add(fragment);
            fragmentTitleList.add(title);

        }

        @Override
        public CharSequence getPageTitle(int position) {

            return fragmentTitleList.get(position);

        }

    }

}
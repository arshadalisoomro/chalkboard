package com.ghofrani.classapp.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
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
import com.ghofrani.classapp.modules.DataStore;
import com.ghofrani.classapp.service.Background;

import java.util.ArrayList;
import java.util.List;

public class Main extends AppCompatActivity implements DrawerLayout.DrawerListener {

    private FloatingActionButton floatingActionButton;
    private DrawerLayout drawerLayout;
    private ScrollView scrollView;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private NavigationView navigationView;
    private int currentView = 0;
    private boolean operateOnDrawerClosed;
    private int drawerViewToSwitchTo;
    private boolean floatingActionButtonContrast = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        int colorPrimary = PreferenceManager.getDefaultSharedPreferences(this).getInt("primary_color", ContextCompat.getColor(this, R.color.teal));

        if (colorPrimary == ContextCompat.getColor(this, R.color.red))
            getTheme().applyStyle(R.style.primary_red, true);
        else if (colorPrimary == ContextCompat.getColor(this, R.color.pink))
            getTheme().applyStyle(R.style.primary_pink, true);
        else if (colorPrimary == ContextCompat.getColor(this, R.color.purple))
            getTheme().applyStyle(R.style.primary_purple, true);
        else if (colorPrimary == ContextCompat.getColor(this, R.color.deep_purple))
            getTheme().applyStyle(R.style.primary_deep_purple, true);
        else if (colorPrimary == ContextCompat.getColor(this, R.color.indigo))
            getTheme().applyStyle(R.style.primary_indigo, true);
        else if (colorPrimary == ContextCompat.getColor(this, R.color.blue))
            getTheme().applyStyle(R.style.primary_blue, true);
        else if (colorPrimary == ContextCompat.getColor(this, R.color.light_blue))
            getTheme().applyStyle(R.style.primary_light_blue, true);
        else if (colorPrimary == ContextCompat.getColor(this, R.color.cyan))
            getTheme().applyStyle(R.style.primary_cyan, true);
        else if (colorPrimary == ContextCompat.getColor(this, R.color.teal))
            getTheme().applyStyle(R.style.primary_teal, true);
        else if (colorPrimary == ContextCompat.getColor(this, R.color.green))
            getTheme().applyStyle(R.style.primary_green, true);
        else if (colorPrimary == ContextCompat.getColor(this, R.color.light_green))
            getTheme().applyStyle(R.style.primary_light_green, true);
        else if (colorPrimary == ContextCompat.getColor(this, R.color.lime))
            getTheme().applyStyle(R.style.primary_lime, true);
        else if (colorPrimary == ContextCompat.getColor(this, R.color.orange))
            getTheme().applyStyle(R.style.primary_orange, true);
        else if (colorPrimary == ContextCompat.getColor(this, R.color.deep_orange))
            getTheme().applyStyle(R.style.primary_deep_orange, true);
        else if (colorPrimary == ContextCompat.getColor(this, R.color.blue_grey))
            getTheme().applyStyle(R.style.primary_blue_grey, true);

        int colorAccent = PreferenceManager.getDefaultSharedPreferences(this).getInt("accent_color", ContextCompat.getColor(this, R.color.deep_orange_accent));

        if (colorAccent == ContextCompat.getColor(this, R.color.red_accent)) {
            getTheme().applyStyle(R.style.accent_red, true);
        } else if (colorAccent == ContextCompat.getColor(this, R.color.pink_accent)) {
            getTheme().applyStyle(R.style.accent_pink, true);
        } else if (colorAccent == ContextCompat.getColor(this, R.color.purple_accent)) {
            getTheme().applyStyle(R.style.accent_purple, true);
        } else if (colorAccent == ContextCompat.getColor(this, R.color.deep_purple_accent)) {
            getTheme().applyStyle(R.style.accent_deep_purple, true);
        } else if (colorAccent == ContextCompat.getColor(this, R.color.indigo_accent)) {
            getTheme().applyStyle(R.style.accent_indigo, true);
        } else if (colorAccent == ContextCompat.getColor(this, R.color.blue_accent)) {
            getTheme().applyStyle(R.style.accent_blue, true);
        } else if (colorAccent == ContextCompat.getColor(this, R.color.light_blue_accent)) {
            getTheme().applyStyle(R.style.accent_light_blue, true);
            floatingActionButtonContrast = true;
        } else if (colorAccent == ContextCompat.getColor(this, R.color.cyan_accent)) {
            getTheme().applyStyle(R.style.accent_cyan, true);
            floatingActionButtonContrast = true;
        } else if (colorAccent == ContextCompat.getColor(this, R.color.teal_accent)) {
            getTheme().applyStyle(R.style.accent_teal, true);
        } else if (colorAccent == ContextCompat.getColor(this, R.color.green_accent)) {
            getTheme().applyStyle(R.style.accent_green, true);
        } else if (colorAccent == ContextCompat.getColor(this, R.color.lime_accent)) {
            getTheme().applyStyle(R.style.accent_lime, true);
            floatingActionButtonContrast = true;
        } else if (colorAccent == ContextCompat.getColor(this, R.color.yellow_accent)) {
            getTheme().applyStyle(R.style.accent_yellow, true);
            floatingActionButtonContrast = true;
        } else if (colorAccent == ContextCompat.getColor(this, R.color.amber_accent)) {
            getTheme().applyStyle(R.style.accent_amber, true);
        } else if (colorAccent == ContextCompat.getColor(this, R.color.orange_accent)) {
            getTheme().applyStyle(R.style.accent_orange, true);
        } else if (colorAccent == ContextCompat.getColor(this, R.color.deep_orange_accent)) {
            getTheme().applyStyle(R.style.accent_deep_orange, true);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService(new Intent(this, Background.class));

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
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                drawerLayout.closeDrawers();

                if (!menuItem.isChecked()) {

                    operateOnDrawerClosed = true;

                    if (menuItem.getItemId() == R.id.settings) {

                        if (currentView == 0)
                            LocalBroadcastManager.getInstance(Main.this).sendBroadcast(new Intent("collapse_lists"));

                        drawerViewToSwitchTo = 4;

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

                                currentView = 0;
                                drawerViewToSwitchTo = 0;

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

                                currentView = 1;
                                drawerViewToSwitchTo = 1;

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

                                currentView = 2;
                                drawerViewToSwitchTo = 2;

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

                                currentView = 3;
                                drawerViewToSwitchTo = 3;

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

                if (currentView == 1) {

                    if (!DataStore.allClassNamesArrayList.isEmpty())
                        startActivity(new Intent(Main.this, EditDay.class).putExtra("day", DataStore.selectedTabPosition));
                    else
                        Toast.makeText(Main.this, "Add classes first!", Toast.LENGTH_LONG).show();

                } else if (currentView == 2) {

                    startActivityForResult(new Intent(Main.this, AddClass.class), 0);

                } else if (currentView == 3) {

                    if (DataStore.allClassNamesArrayList.isEmpty())
                        Toast.makeText(Main.this, "Add classes first!", Toast.LENGTH_LONG).show();
                    else
                        startActivity(new Intent(Main.this, AddHomework.class));

                }

            }

        });

        switchToView((getIntent().hasExtra("fragment") ? getIntent().getIntExtra("fragment", 0) : 0));

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

        if (DataStore.recreate) {

            DataStore.recreate = false;
            DataStore.isAnimated = false;
            DataStore.selectedTabPosition = 0;

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

            final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            switch (drawerViewToSwitchTo) {

                case 0:

                    Overview overviewFragment = new Overview();
                    fragmentTransaction.replace(R.id.main_scroll_view, overviewFragment, "overview_fragment");
                    fragmentTransaction.commit();

                    break;

                case 1:

                    tabLayout.setVisibility(AppBarLayout.VISIBLE);
                    viewPager.setVisibility(LinearLayout.VISIBLE);

                    fragmentTransaction.remove(getSupportFragmentManager().findFragmentById(R.id.main_scroll_view)).commit();

                    final ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

                    adapter.addFragment(new Sunday(), "SUNDAY");
                    adapter.addFragment(new Monday(), "MONDAY");
                    adapter.addFragment(new Tuesday(), "TUESDAY");
                    adapter.addFragment(new Wednesday(), "WEDNESDAY");
                    adapter.addFragment(new Thursday(), "THURSDAY");
                    adapter.addFragment(new Friday(), "FRIDAY");
                    adapter.addFragment(new Saturday(), "SATURDAY");

                    viewPager.setAdapter(adapter);

                    tabLayout.setupWithViewPager(viewPager);

                    tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {

                        @Override
                        public void onTabSelected(TabLayout.Tab tabLayoutTab) {

                            super.onTabSelected(tabLayoutTab);

                            if (tabLayoutTab.getPosition() != 0) {

                                if (!DataStore.isAnimated) {

                                    final LinearLayout mainTabLayoutLayout = (LinearLayout) findViewById(R.id.main_tab_layout_layout);
                                    final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mainTabLayoutLayout.getLayoutParams();

                                    final Animation animation = new Animation() {

                                        @Override
                                        protected void applyTransformation(float interpolatedTime, Transformation t) {

                                            params.leftMargin = (int) (getPixelFromDP(48) - (getPixelFromDP(48) * interpolatedTime));

                                            mainTabLayoutLayout.setLayoutParams(params);

                                        }
                                    };

                                    animation.setDuration(200);
                                    drawerLayout.startAnimation(animation);

                                    DataStore.isAnimated = true;

                                }

                            } else {

                                if (DataStore.isAnimated) {

                                    final LinearLayout mainTabLayoutLayout = (LinearLayout) findViewById(R.id.main_tab_layout_layout);
                                    final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mainTabLayoutLayout.getLayoutParams();

                                    final Animation animation = new Animation() {

                                        @Override
                                        protected void applyTransformation(float interpolatedTime, Transformation t) {

                                            params.leftMargin = (int) (getPixelFromDP(48) * interpolatedTime);

                                            mainTabLayoutLayout.setLayoutParams(params);

                                        }
                                    };

                                    animation.setDuration(200);
                                    drawerLayout.startAnimation(animation);

                                    DataStore.isAnimated = false;

                                }

                            }

                            DataStore.selectedTabPosition = tabLayoutTab.getPosition();

                        }

                    });

                    tabLayout.getTabAt(DataStore.selectedTabPosition).select();

                    if (DataStore.selectedTabPosition != 0) {

                        if (!DataStore.isAnimated) {

                            final LinearLayout mainTabLayoutLayout = (LinearLayout) findViewById(R.id.main_tab_layout_layout);
                            final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mainTabLayoutLayout.getLayoutParams();

                            final Animation animation = new Animation() {

                                @Override
                                protected void applyTransformation(float interpolatedTime, Transformation t) {

                                    params.leftMargin = (int) (getPixelFromDP(48) - (getPixelFromDP(48) * interpolatedTime));

                                    mainTabLayoutLayout.setLayoutParams(params);

                                }
                            };

                            animation.setDuration(200);
                            drawerLayout.startAnimation(animation);

                            DataStore.isAnimated = true;

                        }

                    } else {

                        if (DataStore.isAnimated) {

                            final LinearLayout mainTabLayoutLayout = (LinearLayout) findViewById(R.id.main_tab_layout_layout);
                            final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mainTabLayoutLayout.getLayoutParams();

                            final Animation animation = new Animation() {

                                @Override
                                protected void applyTransformation(float interpolatedTime, Transformation t) {

                                    params.leftMargin = (int) (getPixelFromDP(48) * interpolatedTime);

                                    mainTabLayoutLayout.setLayoutParams(params);

                                }
                            };

                            animation.setDuration(200);
                            drawerLayout.startAnimation(animation);

                            DataStore.isAnimated = false;

                        }

                    }

                    break;

                case 2:

                    Classes classesFragment = new Classes();
                    fragmentTransaction.replace(R.id.main_scroll_view, classesFragment, "classes_fragment");
                    fragmentTransaction.commit();

                    break;

                case 3:

                    Homework homeworkFragment = new Homework();
                    fragmentTransaction.replace(R.id.main_scroll_view, homeworkFragment, "homework_fragment");
                    fragmentTransaction.commit();

                    break;

                case 4:

                    startActivity(new Intent(this, Settings.class));

                    break;

            }

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

        if (resultCode == 0) {

            if (resultIntent.getStringExtra("class").equals("AddClass")) {

                switchToView(resultIntent.getIntExtra("switch_to_timetable", requestCode));
                Toast.makeText(this, "Add your class into the timetable!", Toast.LENGTH_LONG).show();

            }

        }

    }

    private void switchToView(int viewToSwitchTo) {

        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        switch (viewToSwitchTo) {

            case 0:

                if (floatingActionButton.isShown())
                    floatingActionButton.hide();

                toolbar.setTitle("Overview");

                tabLayout.setVisibility(AppBarLayout.GONE);
                viewPager.setVisibility(LinearLayout.GONE);

                scrollView.setVisibility(LinearLayout.VISIBLE);

                navigationView.setCheckedItem(R.id.overview);

                currentView = viewToSwitchTo;

                Overview overviewFragment = new Overview();
                fragmentTransaction.replace(R.id.main_scroll_view, overviewFragment, "overview_fragment");
                fragmentTransaction.commit();

                break;

            case 1:

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

                tabLayout.setVisibility(AppBarLayout.VISIBLE);
                viewPager.setVisibility(LinearLayout.VISIBLE);

                fragmentTransaction.remove(getSupportFragmentManager().findFragmentById(R.id.main_scroll_view)).commit();

                final ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

                adapter.addFragment(new Sunday(), "SUNDAY");
                adapter.addFragment(new Monday(), "MONDAY");
                adapter.addFragment(new Tuesday(), "TUESDAY");
                adapter.addFragment(new Wednesday(), "WEDNESDAY");
                adapter.addFragment(new Thursday(), "THURSDAY");
                adapter.addFragment(new Friday(), "FRIDAY");
                adapter.addFragment(new Saturday(), "SATURDAY");

                viewPager.setAdapter(adapter);

                tabLayout.setupWithViewPager(viewPager);

                tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {

                    @Override
                    public void onTabSelected(TabLayout.Tab tabLayoutTab) {

                        super.onTabSelected(tabLayoutTab);

                        if (tabLayoutTab.getPosition() != 0) {

                            if (!DataStore.isAnimated) {

                                final LinearLayout mainTabLayoutLayout = (LinearLayout) findViewById(R.id.main_tab_layout_layout);
                                final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mainTabLayoutLayout.getLayoutParams();

                                final Animation animation = new Animation() {

                                    @Override
                                    protected void applyTransformation(float interpolatedTime, Transformation t) {

                                        params.leftMargin = (int) (getPixelFromDP(48) - (getPixelFromDP(48) * interpolatedTime));

                                        mainTabLayoutLayout.setLayoutParams(params);

                                    }
                                };

                                animation.setDuration(200);
                                drawerLayout.startAnimation(animation);

                                DataStore.isAnimated = true;

                            }

                        } else {

                            if (DataStore.isAnimated) {

                                final LinearLayout mainTabLayoutLayout = (LinearLayout) findViewById(R.id.main_tab_layout_layout);
                                final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mainTabLayoutLayout.getLayoutParams();

                                final Animation animation = new Animation() {

                                    @Override
                                    protected void applyTransformation(float interpolatedTime, Transformation t) {

                                        params.leftMargin = (int) (getPixelFromDP(48) * interpolatedTime);

                                        mainTabLayoutLayout.setLayoutParams(params);

                                    }
                                };

                                animation.setDuration(200);
                                drawerLayout.startAnimation(animation);

                                DataStore.isAnimated = false;

                            }

                        }

                        DataStore.selectedTabPosition = tabLayoutTab.getPosition();

                    }

                });

                tabLayout.getTabAt(DataStore.selectedTabPosition).select();

                if (DataStore.selectedTabPosition != 0) {

                    if (!DataStore.isAnimated) {

                        final LinearLayout mainTabLayoutLayout = (LinearLayout) findViewById(R.id.main_tab_layout_layout);
                        final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mainTabLayoutLayout.getLayoutParams();

                        final Animation animation = new Animation() {

                            @Override
                            protected void applyTransformation(float interpolatedTime, Transformation t) {

                                params.leftMargin = (int) (getPixelFromDP(48) - (getPixelFromDP(48) * interpolatedTime));

                                mainTabLayoutLayout.setLayoutParams(params);

                            }
                        };

                        animation.setDuration(200);
                        drawerLayout.startAnimation(animation);

                        DataStore.isAnimated = true;

                    }

                } else {

                    if (DataStore.isAnimated) {

                        final LinearLayout mainTabLayoutLayout = (LinearLayout) findViewById(R.id.main_tab_layout_layout);
                        final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mainTabLayoutLayout.getLayoutParams();

                        final Animation animation = new Animation() {

                            @Override
                            protected void applyTransformation(float interpolatedTime, Transformation t) {

                                params.leftMargin = (int) (getPixelFromDP(48) * interpolatedTime);

                                mainTabLayoutLayout.setLayoutParams(params);

                            }
                        };

                        animation.setDuration(200);
                        drawerLayout.startAnimation(animation);

                        DataStore.isAnimated = false;

                    }

                }

                break;

            case 2:

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

                Classes classesFragment = new Classes();
                fragmentTransaction.replace(R.id.main_scroll_view, classesFragment, "classes_fragment");
                fragmentTransaction.commit();

                break;

            case 3:

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

                Homework homeworkFragment = new Homework();
                fragmentTransaction.replace(R.id.main_scroll_view, homeworkFragment, "homework_fragment");
                fragmentTransaction.commit();

                break;

            case 4:

                if (currentView == 0)
                    LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent("collapse_lists"));

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
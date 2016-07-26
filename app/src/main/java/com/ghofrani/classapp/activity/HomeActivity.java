package com.ghofrani.classapp.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
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
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.ghofrani.classapp.R;
import com.ghofrani.classapp.fragment.ClassesFragment;
import com.ghofrani.classapp.fragment.HomeworkFragment;
import com.ghofrani.classapp.fragment.OverviewFragment;
import com.ghofrani.classapp.fragment.timetable.Friday;
import com.ghofrani.classapp.fragment.timetable.Monday;
import com.ghofrani.classapp.fragment.timetable.Saturday;
import com.ghofrani.classapp.fragment.timetable.Sunday;
import com.ghofrani.classapp.fragment.timetable.Thursday;
import com.ghofrani.classapp.fragment.timetable.Tuesday;
import com.ghofrani.classapp.fragment.timetable.Wednesday;
import com.ghofrani.classapp.modules.DataStore;
import com.ghofrani.classapp.modules.DatabaseHelper;
import com.ghofrani.classapp.service.TimeService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private MenuItem menuItemDrawer;
    private DrawerLayout drawerLayout;
    private Toolbar homeToolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ScrollView scrollView;
    private NavigationView navigationView;
    private FloatingActionButton floatingActionButton;
    private int currentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        startService(new Intent(getApplicationContext(), TimeService.class));

        int extraPassed = getIntent().hasExtra("fragment") ? getIntent().getExtras().getInt("fragment") : 0;

        homeToolbar = (Toolbar) findViewById(R.id.home_toolbar);
        homeToolbar.setTitleTextColor(Color.WHITE);

        switch (extraPassed) {

            case 0:

                homeToolbar.setTitle("Overview");

                break;

            case 1:

                homeToolbar.setTitle("Timetable");

                break;

            case 2:

                homeToolbar.setTitle("Classes");

                break;

            case 3:

                homeToolbar.setTitle("Homework");

                break;

        }

        setSupportActionBar(homeToolbar);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                if (currentView == 2)
                    startActivity(new Intent(getApplicationContext(), AddClassActivity.class));
                else if (currentView == 3)
                    startActivity(new Intent(getApplicationContext(), AddHomeworkActivity.class));

            }

        });

        scrollView = (ScrollView) findViewById(R.id.home_activity_scroll_view);

        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        navigationView.setCheckedItem(R.id.overview);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                drawerLayout.closeDrawers();

                menuItemDrawer = menuItem;

                if (!menuItemDrawer.isChecked()) {

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {

                            if (menuItemDrawer.getItemId() == R.id.settings) {

                                switchToView(4);

                            } else {

                                switch (menuItemDrawer.getItemId()) {

                                    case R.id.overview:

                                        switchToView(0);

                                        break;

                                    case R.id.timetable:

                                        switchToView(1);

                                        break;

                                    case R.id.classes:

                                        switchToView(2);

                                        break;

                                    case R.id.homework:

                                        switchToView(3);

                                        break;

                                }

                            }

                        }

                    }, 250);

                }

                return true;

            }

        });

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, homeToolbar, R.string.drawer_open, R.string.drawer_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setVisibility(AppBarLayout.GONE);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setVisibility(LinearLayout.GONE);

        /*

        DatabaseHelper db = new DatabaseHelper(this);

        db.insertClassIntoDay(new String[]{"English", "0820", "0920"}, Calendar.SUNDAY);
        db.insertClassIntoDay(new String[]{"Economics", "0920", "1020"}, Calendar.SUNDAY);
        db.insertClassIntoDay(new String[]{"Physics", "1100", "1200"}, Calendar.SUNDAY);
        db.insertClassIntoDay(new String[]{"Spanish", "1200", "1300"}, Calendar.SUNDAY);

        db.insertClassIntoDay(new String[]{"English", "0820", "0920"}, Calendar.MONDAY);
        db.insertClassIntoDay(new String[]{"Economics", "0920", "1020"}, Calendar.MONDAY);
        db.insertClassIntoDay(new String[]{"Mathematics", "1100", "1200"}, Calendar.MONDAY);
        db.insertClassIntoDay(new String[]{"Spanish", "1200", "1300"}, Calendar.MONDAY);

        db.insertClassIntoDay(new String[]{"Physics", "0820", "0920"}, Calendar.TUESDAY);
        db.insertClassIntoDay(new String[]{"Free", "0920", "1020"}, Calendar.TUESDAY);
        db.insertClassIntoDay(new String[]{"Chemistry", "1100", "1200"}, Calendar.TUESDAY);
        db.insertClassIntoDay(new String[]{"Mathematics", "1200", "1300"}, Calendar.TUESDAY);

        db.insertClassIntoDay(new String[]{"English", "0820", "0920"}, Calendar.WEDNESDAY);
        db.insertClassIntoDay(new String[]{"Economics", "0920", "1020"}, Calendar.WEDNESDAY);
        db.insertClassIntoDay(new String[]{"Physics", "1100", "1200"}, Calendar.WEDNESDAY);
        db.insertClassIntoDay(new String[]{"Chemistry", "1200", "1300"}, Calendar.WEDNESDAY);

        db.insertClassIntoDay(new String[]{"Chemistry", "0820", "0920"}, Calendar.THURSDAY);
        db.insertClassIntoDay(new String[]{"TOK", "0920", "1020"}, Calendar.THURSDAY);
        db.insertClassIntoDay(new String[]{"Mathematics", "1100", "1200"}, Calendar.THURSDAY);
        db.insertClassIntoDay(new String[]{"Spanish", "1200", "1300"}, Calendar.THURSDAY);

        db.addClass(new String[]{"Economics", "Ms. Hiba", "250"});
        db.addClass(new String[]{"English", "Ms. King", "58"});
        db.addClass(new String[]{"TOK", "Ms. Keogh", "2C"});
        db.addClass(new String[]{"Chemistry", "Ms. Moss", "255"});
        db.addClass(new String[]{"Spanish", "Ms. Morgan", "246"});
        db.addClass(new String[]{"Mathematics", "Mr. Nabeel", "150"});
        db.addClass(new String[]{"Physics", "Mr. Derus", "71"});
        db.addClass(new String[]{"Free", "Ms. Schmidt", "Library"});

        db.close();

        */

        switchToView(extraPassed);

    }

    private void switchToView(int id) {

        switch (id) {

            case 0:

                floatingActionButton.setVisibility(View.INVISIBLE);

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                homeToolbar.setTitle("Overview");

                OverviewFragment overviewFragment = new OverviewFragment();
                fragmentTransaction.replace(R.id.home_activity_scroll_view, overviewFragment, "overview_fragment");
                fragmentTransaction.commit();

                tabLayout.setVisibility(AppBarLayout.GONE);
                viewPager.setVisibility(LinearLayout.GONE);

                scrollView.setVisibility(LinearLayout.VISIBLE);

                navigationView.setCheckedItem(R.id.overview);

                currentView = 0;

                break;

            case 1:

                floatingActionButton.setVisibility(View.INVISIBLE);

                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.remove(fragmentManager.findFragmentById(R.id.home_activity_scroll_view)).commit();

                homeToolbar.setTitle("Timetable");

                ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

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
                    public void onTabSelected(TabLayout.Tab tabLayout) {

                        super.onTabSelected(tabLayout);

                        if (tabLayout.getPosition() != 0) {

                            if (!DataStore.isAnimationState()) {

                                Animation animation = new Animation() {

                                    @Override
                                    protected void applyTransformation(float interpolatedTime, Transformation t) {

                                        LinearLayout layout = (LinearLayout) findViewById(R.id.tab_layout_layout);
                                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) layout.getLayoutParams();

                                        params.leftMargin = (int) (getPixelFromDP(48) - (getPixelFromDP(48) * interpolatedTime));

                                        layout.setLayoutParams(params);

                                    }
                                };

                                animation.setDuration(200);
                                drawerLayout.startAnimation(animation);

                                DataStore.setAnimationState(true);

                            }

                        } else {

                            if (DataStore.isAnimationState()) {

                                Animation animation = new Animation() {

                                    @Override
                                    protected void applyTransformation(float interpolatedTime, Transformation t) {

                                        LinearLayout layout = (LinearLayout) findViewById(R.id.tab_layout_layout);
                                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) layout.getLayoutParams();

                                        params.leftMargin = (int) (getPixelFromDP(48) * interpolatedTime);

                                        layout.setLayoutParams(params);

                                    }
                                };

                                animation.setDuration(200);
                                drawerLayout.startAnimation(animation);

                                DataStore.setAnimationState(false);

                            }

                        }

                    }

                });

                scrollView.setVisibility(LinearLayout.GONE);

                tabLayout.setVisibility(AppBarLayout.VISIBLE);
                viewPager.setVisibility(LinearLayout.VISIBLE);

                navigationView.setCheckedItem(R.id.timetable);

                currentView = 1;

                break;

            case 2:

                floatingActionButton.setVisibility(View.VISIBLE);

                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();

                homeToolbar.setTitle("Classes");

                ClassesFragment classesFragment = new ClassesFragment();
                fragmentTransaction.replace(R.id.home_activity_scroll_view, classesFragment, "classes_fragment");
                fragmentTransaction.commit();

                tabLayout.setVisibility(AppBarLayout.GONE);
                viewPager.setVisibility(LinearLayout.GONE);

                scrollView.setVisibility(LinearLayout.VISIBLE);

                navigationView.setCheckedItem(R.id.classes);

                currentView = 2;

                break;

            case 3:

                floatingActionButton.setVisibility(View.VISIBLE);

                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();

                homeToolbar.setTitle("Homework");

                HomeworkFragment homeworkFragment = new HomeworkFragment();
                fragmentTransaction.replace(R.id.home_activity_scroll_view, homeworkFragment, "homework_fragment");
                fragmentTransaction.commit();

                tabLayout.setVisibility(AppBarLayout.GONE);
                viewPager.setVisibility(LinearLayout.GONE);

                scrollView.setVisibility(LinearLayout.VISIBLE);

                navigationView.setCheckedItem(R.id.homework);

                currentView = 3;

                break;

            case 4:

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(HomeActivity.this);
                Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(intent, options.toBundle());

                break;

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_home, menu);

        return true;

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
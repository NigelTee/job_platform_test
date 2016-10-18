package materialtest.example.user.materialtest.activity;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;

import java.util.HashMap;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;
import materialtest.example.user.materialtest.R;
import materialtest.example.user.materialtest.extra.SearchInterface;
import materialtest.example.user.materialtest.fragments.FragmentGraduate;
import materialtest.example.user.materialtest.fragments.FragmentInternship;
import materialtest.example.user.materialtest.fragments.FragmentPartTime;
import materialtest.example.user.materialtest.fragments.FragmentVolunteer;
import materialtest.example.user.materialtest.front.SessionManagement;


public class MainActivity extends AppCompatActivity implements MaterialTabListener, View.OnClickListener, SearchView.OnQueryTextListener, NavigationView.OnNavigationItemSelectedListener {

    private MaterialTabHost tabHost;
    private ViewPager viewPager;
    public static final int MOVIES_SEARCH_RESULTS = 0;
    public static final int MOVIES_HITS = 1;
    public static final int MOVIES_UPCOMING = 2;
    public static final int VOLUNTEER = 3;
    private MyPagerAdapter adapter;
    private DrawerLayout mDrawerLayout;
    SessionManagement sessionManagement;
    private SearchView searchView;
    private static MainActivity instance;
    private String newString;
    private static int tabs_number = 0;
    private LogoutReceiver logoutReceiver;

    //set activity context for drawer to read email address string value
    public static Context getContext() {
        return instance.getApplicationContext();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_main);
/*
        // Register the logout receiver
        logoutReceiver = new LogoutReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.package.ACTION_LOGOUT");
        registerReceiver(logoutReceiver, intentFilter);

        //Session class instance
        sessionManagement = new SessionManagement(getApplicationContext());
        sessionManagement.checkLogin();
        HashMap<String, String> user = sessionManagement.getUserDetails();
        // Get username for Header
        String name = user.get(SessionManagement.KEY_NAME);
        // Get email for Header
        String email = user.get(SessionManagement.KEY_EMAIL);

        Toast.makeText(getApplicationContext(), "User Login Status: " + sessionManagement.isLoggedIn(), Toast.LENGTH_LONG).show();
*/
        //create toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            setSupportActionBar(toolbar); //activate custom action bar
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);//show home button

            //search function
            getSupportActionBar().setTitle(getString(R.string.search_title));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //tabs
        tabHost = (MaterialTabHost) findViewById(R.id.materialTabHost);
        adapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(adapter);
        //notify the pager when when user do a swipe the selected tab change
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // update tab number to the page selected
                tabHost.setSelectedNavigationItem(position);
            }
        });

        // insert all tabs from pagerAdapter data
        for (int i = 0; i < adapter.getCount(); i++) {
            tabHost.addTab(
                    tabHost.newTab()
                            .setText(adapter.getPageTitle(i))
                            .setTabListener(this)
            );
        }

        viewPager.setCurrentItem(tabs_number - 1);// offset the 0 from array

        //Floating action button
        ImageView imageView = new ImageView(this); // Create an icon
        imageView.setImageResource(R.mipmap.ic_launcher); //change this later

        FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                .setBackgroundDrawable(R.drawable.selector_button)//edit this
                .setContentView(imageView)
                .build();

        actionButton.setOnClickListener(this);

        //Navigation Drawer
        mDrawerLayout = (DrawerLayout) this.findViewById(R.id.drawer_layout);
        NavigationView mDrawer = (NavigationView) findViewById(R.id.mainActivity_drawer);
        TextView username = (TextView) findViewById(R.id.header_username);
        //username.setText(name);
        TextView userEmail = (TextView) findViewById(R.id.header_email);
        //userEmail.setText(email);
        mDrawer.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();


        //String from floating action button's spinner
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            newString = null;
        } else {
            newString = extras.getString("STRING_I_NEED");
        }

        Toast.makeText(this, newString, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        getMenuInflater().inflate(R.menu.search, menu);

        SearchManager searchManager = (SearchManager)
                getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchMenuItem = menu.findItem(R.id.menu_search);
        searchView = (SearchView) searchMenuItem.getActionView();

        searchView.setSearchableInfo(searchManager.
                getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
     *-------------------------------------------Navigation Drawer------------------------------------------
     */

    //Navigation Drawer Item List
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        Intent intent;
        if (menuItem.getItemId() == R.id.navigation_item_1) {

        }
        if (menuItem.getItemId() == R.id.navigation_item_2) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            intent = new Intent(this, SavedJobsActivity.class);
            startActivity(intent);
            return true;
        }
        if (menuItem.getItemId() == R.id.navigation_item_3) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            intent = new Intent(this, AppliedJobsActivity.class);
            startActivity(intent);
            return true;
        }
        if (menuItem.getItemId() == R.id.navigation_item_4) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        }
        if (menuItem.getItemId() == R.id.navigation_item_5) {
            Toast.makeText(this, "LogOut", Toast.LENGTH_SHORT).show();
            sessionManagement.logoutUser();
        }

        return false;
    }

    /*
     *---------------------------------------------Search Bar------------------------------------------
     */

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Fragment fragment = (Fragment) adapter.instantiateItem(viewPager, viewPager.getCurrentItem());
        if (fragment instanceof SearchInterface) {
            ((SearchInterface) fragment).onSearchBar(newText);
        }
        return true;
    }

    /*
     *----------------------------------------------Back Function----------------------------------------
     */
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {// close drawer
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else if (!searchView.isIconified()) {
            searchView.setQuery("", false);//close searchbox
            searchView.setIconified(true);
        } else {
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("com.package.ACTION_LOGOUT");
            sendBroadcast(broadcastIntent);
            tabs_number = 0;
            finish();
        }
    }

    /*
     *---------------------------------------------Tabs Adapter------------------------------------------
     */

    private class MyPagerAdapter extends FragmentStatePagerAdapter {

        String[] tabs;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            tabs = getResources().getStringArray(R.array.tabs);
        }

        //instance of tabs
        @Override
        public Fragment getItem(int num) {

            Fragment myFragment = null;
            switch (num) {
                case MOVIES_UPCOMING:
                    myFragment = FragmentGraduate.newInstance("", "");
                    tabs_number = num;
                    break;

                case MOVIES_HITS:
                    myFragment = FragmentInternship.newInstance(newString, "");
                    tabs_number = num;
                    break;

                case MOVIES_SEARCH_RESULTS:
                    myFragment = FragmentPartTime.newInstance("", "");
                    tabs_number = num;
                    break;

                case VOLUNTEER:
                    myFragment = FragmentVolunteer.newInstance("", "");
                    tabs_number = num;
                    break;

            }
            return myFragment;
        }

        //page title of tabs
        @Override
        public CharSequence getPageTitle(int position) {
            return getResources().getStringArray(R.array.tabs)[position];
        }

        //number of tabs
        @Override
        public int getCount() {
            return 4;
        }

    }

    //update the page number to the tab number selected
    @Override
    public void onTabSelected(MaterialTab materialTab) {
        viewPager.setCurrentItem(materialTab.getPosition());
    }

    @Override
    public void onTabReselected(MaterialTab materialTab) {

    }

    @Override
    public void onTabUnselected(MaterialTab materialTab) {

    }

    /*
     *---------------------------------------------Floating Action Button------------------------------------------
     */
    //floating action button click
    @Override
    public void onClick(View v) {

        startActivity(new Intent(this, FilterActivity.class));
    }

    /*
     *--------------------------------------------------Logout------------------------------------------------------
     */
    //Logout
    public class LogoutReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.package.ACTION_LOGOUT")) {
                finish();
            }
        }
    }

    /*
     *----------------------------------------------------Destroy---------------------------------------------------
     */
/*
    @Override
    protected void onDestroy() {
        // Unregister the logout receiver
        unregisterReceiver(logoutReceiver);
        super.onDestroy();
    }
*/
}
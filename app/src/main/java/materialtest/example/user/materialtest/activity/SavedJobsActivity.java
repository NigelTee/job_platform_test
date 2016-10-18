package materialtest.example.user.materialtest.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import materialtest.example.user.materialtest.R;
import materialtest.example.user.materialtest.front.SessionManagement;

public class SavedJobsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    SavedJobsDB myDb;
    private String genus;
    private String species;
    private String cultivar;
    private ListView myList;
    private GestureDetector gestureDetector;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private SessionManagement sessionManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_jobs_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        NavigationView mDrawer = (NavigationView) findViewById(R.id.savedJobs_drawer);
        mDrawer.setNavigationItemSelectedListener(this);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.savedJobs_layout);
        drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        //Session class instance
        sessionManagement = new SessionManagement(getApplicationContext());
        //sessionManagement.checkLogin();
        //Toast.makeText(getApplicationContext(), "User Login Status: " + sessionManagement.isLoggedIn(), Toast.LENGTH_LONG).show();

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            setSupportActionBar(toolbar); //activate custom action bar
            getSupportActionBar().setTitle(getString(R.string.saved_jobs));
        }

        myList = (ListView) findViewById(R.id.list);
        gestureDetector = new GestureDetector(this, new GestureListener());
        TouchListener onTouchListener = new TouchListener();
        myList.setOnTouchListener(onTouchListener);

        myDb = new SavedJobsDB(this);
        myDb.open();
        displayListView();
        registerListCallBack();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sub, menu);
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
            Intent upIntent = new Intent(this, BookmarkActivity.class);
            startActivity(upIntent);

            return true;
        }

        if (id == android.R.id.home) {//when home button is clicked
            Intent upIntent = new Intent(this, MainActivity.class);
            upIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(upIntent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private void displayListView() {
        Cursor cursor = myDb.getAllRows();

        // The desired columns to be bound
        String[] columns = new String[]{
                SavedJobsDB.KEY_COMPANYNAME,
                SavedJobsDB.KEY_QUALIFICATION,
                SavedJobsDB.KEY_DAYS,
        };

        // the XML defined views which the data will be bound to
        int[] to = new int[]{
                R.id.tvName,
                R.id.tvStatus,
                R.id.tvAmount
        };

        // create the adapter using the cursor pointing to the desired data 
        //as well as the layout information
        SimpleCursorAdapter dataAdapter = new SimpleCursorAdapter(
                this,
                R.layout.saved_jobs_custom_row,
                cursor,
                columns,
                to,
                0);
        // Assign adapter to ListView
        ListView myList = (ListView) findViewById(R.id.list);
        myList.setAdapter(dataAdapter);
    }

    private void registerListCallBack() {

        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View viewCLicked,
                                    int position, long idinDb) {
                Log.d("test", "clicked");

                Cursor cursor = myDb.getRow(idinDb);
                if (cursor.moveToFirst()) {
                    //long idDB = cursor.getLong(SavedJobsDB.COL_ROWID);
                    genus = cursor.getString(SavedJobsDB.COL_COMPANYNAME);
                    species = cursor.getString(SavedJobsDB.COL_QUALIFICATION);
                    cultivar = cursor.getString(SavedJobsDB.COL_DAYS);

                    //myDb.deleteRow(idDB);
                    //displayListView();

                    //String message = "User: " + user + " Amount: " + amount + " Status: " + status + " is deleted.";
                    //Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();

                    cursor.close();

                    Intent intent = new Intent(SavedJobsActivity.this, SubActivity.class);
                    Bundle extras = new Bundle();
                    //extras.putString("id", id);
                    extras.putString("city", genus);
                    extras.putString("place", species);
                    extras.putString("station", cultivar);
                    intent.putExtras(extras);
                    SavedJobsActivity.this.startActivity(intent);

                }

            }
        });

        myList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                //myDb.deleteRow(l);
                //displayListView();

                return false;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myDb.close();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        Intent intent;
        if (menuItem.getItemId() == R.id.navigation_item_1) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        if (menuItem.getItemId() == R.id.navigation_item_2) {

        }
        if (menuItem.getItemId() == R.id.navigation_item_3) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            intent = new Intent(this, AppliedJobsActivity.class);
            startActivity(intent);
            return true;
        }
        if (menuItem.getItemId() == R.id.navigation_item_4) {

        }
        if (menuItem.getItemId() == R.id.navigation_item_5) {
            /*
            Toast.makeText(this, "LogOut", Toast.LENGTH_SHORT).show();
            sessionManagement.logoutUser();*/
        }

        return false;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }

    protected class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_MIN_DISTANCE = 150;
        private static final int SWIPE_MAX_OFF_PATH = 100;
        private static final int SWIPE_THRESHOLD_VELOCITY = 100;

        private MotionEvent mLastOnDownEvent = null;

        @Override
        public boolean onDown(MotionEvent e) {
            mLastOnDownEvent = e;
            return super.onDown(e);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1 == null) {
                e1 = mLastOnDownEvent;
            }
            if (e1 == null || e2 == null) {
                return false;
            }

            float dX = e2.getX() - e1.getX();
            float dY = e1.getY() - e2.getY();

            if (Math.abs(dY) < SWIPE_MAX_OFF_PATH && Math.abs(velocityX) >= SWIPE_THRESHOLD_VELOCITY && Math.abs(dX) >= SWIPE_MIN_DISTANCE) {
                if (dX > 0) {
                    Toast.makeText(getApplicationContext(), "Right Swipe", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Left Swipe", Toast.LENGTH_SHORT).show();
                }
                return true;
            } else if (Math.abs(dX) < SWIPE_MAX_OFF_PATH && Math.abs(velocityY) >= SWIPE_THRESHOLD_VELOCITY && Math.abs(dY) >= SWIPE_MIN_DISTANCE) {
                if (dY > 0) {
                    Toast.makeText(getApplicationContext(), "Up Swipe", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Down Swipe", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
            return false;
        }

    }

    protected class TouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent e) {
            return gestureDetector.onTouchEvent(e);
        }
    }

}

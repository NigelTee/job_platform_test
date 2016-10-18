package materialtest.example.user.materialtest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import materialtest.example.user.materialtest.R;

public class FilterActivity extends AppCompatActivity {

    private TextView salary_amount;
    private RecyclerView rv;
    private FilterAdapter filterAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_recyclerview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            setSupportActionBar(toolbar); //activate custom action bar
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);//show home button

        }
        getSupportActionBar().setHomeButtonEnabled(true);//enable home button on top left corner
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //recycler view
        rv = (RecyclerView) findViewById(R.id.rv_filter);
        rv.setHasFixedSize(true);
        //adapter for recycler view
        filterAdapter = new FilterAdapter(this, getData());
        rv.setAdapter(filterAdapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

        //seekbar
        SeekBar seekBar = (SeekBar) findViewById(R.id.sb_salary);
        salary_amount = (TextView) findViewById(R.id.tv_salary_amount);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                salary_amount.setText("RM" + i);
                Toast.makeText(getApplicationContext(), "Changing seekbar's progress", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Toast.makeText(getApplicationContext(), "Started tracking seekbar", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(getApplicationContext(), "Stopped tracking seekbar", Toast.LENGTH_SHORT).show();

            }
        });
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

    public static List<FilterCustomRow> getData() {
        List<FilterCustomRow> data = new ArrayList<>();
        String[] desc = {"Location", "Industry", "Position"};
        String[] selected = {"All", "All", "All"};
        int[] photoId = {R.mipmap.ic_place_grey600_24dp, R.mipmap.ic_work_grey600_24dp, R.mipmap.ic_play_install_grey600_24dp};

        for (int i = 0; i < desc.length; i++) {
            FilterCustomRow filterCustomRow = new FilterCustomRow();
            filterCustomRow.desc = desc[i];
            filterCustomRow.selected = selected[i];
            filterCustomRow.photoId = photoId[i];
            data.add(filterCustomRow);
        }
        return data;
    }

    public void transfer(View view) {

        Intent start = new Intent(this, MainActivity.class);
        start.putExtra("STRING_I_NEED", FilterAdapter.location);
        startActivity(start);
        finish();
    }

    public void reset(View view) {
        FilterAdapter.location = "All";
        FilterAdapter.industry = "All";
        FilterAdapter.position = "All";

        rv.setAdapter(filterAdapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
    }
}


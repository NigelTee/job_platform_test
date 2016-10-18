package materialtest.example.user.materialtest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import materialtest.example.user.materialtest.R;


public class SubActivity extends AppCompatActivity {

    TextView text1, text2, text3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            setSupportActionBar(toolbar); //activate custom action bar
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);//show home button

            //search function
            getSupportActionBar().setTitle(getString(R.string.search_title));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        getSupportActionBar().setHomeButtonEnabled(true);//enable home button on top left corner
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        text1 = (TextView) findViewById(R.id.tv_ID);
        text2 = (TextView) findViewById(R.id.tv_Genus);
        text3 = (TextView) findViewById(R.id.tv_Species);

        Bundle extra = getIntent().getExtras();
        String newid = extra.getString("city");
        String newgenus = extra.getString("place");
        String species = extra.getString("station");

        text1.setText(newid);
        text2.setText(newgenus);
        text3.setText(species);

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
            //NavUtils.navigateUpFromSameTask(this);//navigate back to activity ("this" is the source of main activity)//return homescreen
        }

        return super.onOptionsItemSelected(item);
    }

}

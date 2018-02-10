package com.example.vip.abdr_app_a_d;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by v i p on 3/13/2017.
 */

public class BaseActivity extends AppCompatActivity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenue, menu);

        //return super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //handle presses on the action bar items
        switch (item.getItemId()) {

            case R.id.home:
               startActivity(new Intent(this, mainbookings.class));
                return true;
            case R.id.info:
                finish();
                startActivity(new Intent(this, userinfo.class));
                return true;

            case R.id.cbooking:
                startActivity(new Intent(this, current_booking.class));
                return true;
            case R.id.support:
                finish();
                startActivity(new Intent(this, mainsupport.class));
                return true;

            case R.id.logout:
                new localuser(this).clear();
                finish();
                startActivity(new Intent(this, mainpage.class));

                return true;


        }

        return super.onOptionsItemSelected(item);
    }
}

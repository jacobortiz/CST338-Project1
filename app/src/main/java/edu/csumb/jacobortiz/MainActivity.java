package edu.csumb.jacobortiz;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import edu.csumb.jacobortiz.DB.AppDatabase;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    public static String username = null;
    public static MainActivity instance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        instance = this;

        Log.d("MainActivity", "onCreate called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // load database
        AppDatabase.getAppDatabase(MainActivity.this).loadData(this);

        Button create_account_button = findViewById(R.id.create_account);
        create_account_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "onClick for create account called");
                Intent intent = new Intent(MainActivity.this, CreateAccountActivity.class);
                startActivity(intent);
            }
        });

        Button reservation_button = findViewById(R.id.reserve_seat);
        reservation_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "onClick for reservation called");
                Intent intent = new Intent(MainActivity.this, ReservationActivity.class);
                startActivity(intent);
            }
        });

        Button cancel_reservation = findViewById(R.id.cancel_reservation);
        cancel_reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // call the Delete Reservation activity
                Log.d("MainActivity", "onClick for delete reservation called");
                Intent intent = new Intent(MainActivity.this, CancelReservationActivity.class);
                startActivity(intent);
            }
        });

        Button manage_systems_button = findViewById(R.id.manage);
        manage_systems_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MainActivity", "onClick for manage systems called");
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.putExtra("type", "manage");
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

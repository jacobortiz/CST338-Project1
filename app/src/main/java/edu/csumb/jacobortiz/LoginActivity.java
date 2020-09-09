package edu.csumb.jacobortiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import edu.csumb.jacobortiz.DB.AppDatabase;
import edu.csumb.jacobortiz.DB.FlightDao;
import edu.csumb.jacobortiz.DB.User;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final EditText username = findViewById(R.id.username);
        final EditText password = findViewById(R.id.password);

        if(getIntent().getStringExtra("type").equals("manage")) {
            username.setHint("Admin Username");
        }

        Button login_button = findViewById(R.id.login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username_ = username.getText().toString();
                String password_ = password.getText().toString();

                if (username_.equals("admin2") && password_.equals("admin2")
                        && getIntent().getStringExtra("type").equals("manage")) {
                    MainActivity.username = username_;

                    Intent intent = new Intent(LoginActivity.this, ManageSystemActivity.class);
                    startActivity(intent);

                    finish();
                    return;
                }

                FlightDao dao = AppDatabase.getAppDatabase(LoginActivity.this).dao();
                User user = dao.login(username_, password_);
                if (user == null) {
                    // Unsuccessful login
                    TextView msg = findViewById(R.id.message);
                    msg.setText(R.string.invalid_username_password);

                } else {
                    // Successful
                    MainActivity.username = username_;
                    Log.i("LoginActivity", username_ + " " + password_);

                    if (getIntent().getStringExtra("type").equals("reservation")) {
                        ReservationActivity.instance.login();
                    } else if (getIntent().getStringExtra("type").equals("cancel")) {
                        CancelReservationActivity.instance.loadReservations();
                    }
                    finish();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Log.d("LoginActivity", "login canceled");
        if (getIntent().getStringExtra("type").equals("cancel")) {
            finish();
            CancelReservationActivity.instance.finish();
        }
        finish();
    }
}

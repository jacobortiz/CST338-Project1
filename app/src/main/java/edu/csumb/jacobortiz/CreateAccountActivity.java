package edu.csumb.jacobortiz;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Date;

import edu.csumb.jacobortiz.DB.AppDatabase;
import edu.csumb.jacobortiz.DB.FlightDao;
import edu.csumb.jacobortiz.DB.LogRecord;
import edu.csumb.jacobortiz.DB.User;

public class CreateAccountActivity  extends AppCompatActivity {

    int errors = 0;

    // three letters, two numbers
    private static boolean requirement(String input) {
        char current;
        int letter_counter = 0;
        int number_counter = 0;

        for(int i = 0; i < input.length(); i++) {
            current = input.charAt(i);
            if(Character.isLetter(current)) {
                letter_counter++;
            }

            if(Character.isDigit(current)) {
                number_counter++;
            }
        }

        return letter_counter >= 3 && number_counter >= 1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("CrateAccountActivity", "onCreate called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button create_button = findViewById(R.id.create_account_button);
        create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });
    }

    public void createAccount() {
        TextView message = findViewById(R.id.message);
        EditText username = findViewById(R.id.username);
        EditText password = findViewById(R.id.password);

        String username_temp = username.getText().toString();
        String password_temp = password.getText().toString();

        if(errors > 1) {
            AlertDialog.Builder alert = new AlertDialog.Builder(CreateAccountActivity.this);
            alert.setTitle("Error");
            alert.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            alert.setMessage("Too many failed attempts");
            alert.show();
        }

        if(!requirement(username_temp)) {
            message.setText(R.string.invalid_username);
            errors++;
            return;
        }

        if (!requirement(password_temp)) {
            message.setText(R.string.invalid_password);
            errors++;
            return;
        }

        if (username_temp.equals("admin2")) {
            message.setText(R.string.invalid_username);
            return;
        }

        User user = AppDatabase.getAppDatabase(CreateAccountActivity.this).
                dao().getUserByName(username_temp);

        if (user == null) {

            User newUser = new User(username_temp, password_temp);
            FlightDao dao = AppDatabase.getAppDatabase(CreateAccountActivity.this).dao();
            dao.addUser(newUser);

            LogRecord log = new LogRecord(new Date(), "New Account", String.format("Username: %s\n", username_temp));
            dao.addLog(log);

            AlertDialog.Builder builder = new AlertDialog.Builder(CreateAccountActivity.this);
            builder.setTitle("Account successfully created.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();

        } else {
            // username already exists.
            message.setText(R.string.taken_username);
            errors++;
        }


    }
}
package edu.csumb.jacobortiz;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Date;

import edu.csumb.jacobortiz.DB.AppDatabase;
import edu.csumb.jacobortiz.DB.Flight;
import edu.csumb.jacobortiz.DB.FlightDao;
import edu.csumb.jacobortiz.DB.LogRecord;

public class AddFlightActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("AddFlightActivity", "onCreate called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_flight);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button submit_button = findViewById(R.id.submit);
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkInputs();
            }
        });
    }

    public void alert(String error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddFlightActivity.this);
        builder.setTitle("Error");
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setMessage(error);
        dialog.show();
    }

    public void checkInputs() {
        EditText flight_num = findViewById(R.id.flight_num);
        EditText departure = findViewById(R.id.departure);
        EditText arrival = findViewById(R.id.arrival);
        EditText depart_time = findViewById(R.id.departure_time);
        EditText capacity = findViewById(R.id.capacity);
        EditText price = findViewById(R.id.price);

        FlightDao dao = AppDatabase.getAppDatabase(AddFlightActivity.this).dao();
        String flight_num_temp = flight_num.getText().toString();


        if (flight_num_temp.isEmpty()) {
            alert("No flight number entered");
            return;
        } else if (dao.getFlight(flight_num_temp).size() > 0) {
            Flight temp = dao.getFlight(flight_num_temp).get(0);
            alert(String.format("Flight number %s is taken", temp.getFlightNumber()));
            return;
        }

        String departure_temp = departure.getText().toString();
        if (departure_temp.isEmpty()) {
            alert("No departure entered");
            return;
        }

        String arrival_temp = arrival.getText().toString();
        if (arrival_temp.isEmpty()) {
            alert("No arrival entered");
            return;
        }

        String depart_time_temp = depart_time.getText().toString();
        if (depart_time_temp.isEmpty()) {
            alert("No departure time entered");
            return;
        }

        try {
            int capacity_temp = Integer.parseInt(capacity.getText().toString());
            double price_temp = Double.parseDouble(price.getText().toString());

            Flight flight = new Flight(flight_num_temp, departure_temp, arrival_temp, depart_time_temp,
                    capacity_temp, price_temp);

            dao.addFlight(flight);

            String info_temp = getFlightInformation(flight_num_temp, departure_temp, arrival_temp,
                    depart_time_temp, capacity_temp, price_temp);

            LogRecord logRecord = new LogRecord(new Date(), "New Flight", info_temp);
            dao.addLog(logRecord);

        } catch (NumberFormatException e) {
            alert("Please input a number");
            return;
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(AddFlightActivity.this);
        builder.setTitle("Success");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setMessage("Flight has been added!");
        dialog.show();
    }

    @SuppressLint("DefaultLocale")
    public String getFlightInformation(String flight_number, String departure, String arrival,
                                       String departure_time, int capacity, double price) {

        return String.format("Flight Number: %s\n", flight_number) +
                String.format("Departure: %s\n", departure) +
                String.format("Arrival: %s\n", arrival) +
                String.format("Departure Time: %s\n", departure_time) +
                String.format("Available Seats: %d\n", capacity) +
                String.format("Price: $%.2f\n", price);
    }

}

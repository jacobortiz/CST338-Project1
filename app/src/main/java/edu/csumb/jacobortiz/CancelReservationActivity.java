package edu.csumb.jacobortiz;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.csumb.jacobortiz.DB.AppDatabase;
import edu.csumb.jacobortiz.DB.Flight;
import edu.csumb.jacobortiz.DB.FlightDao;
import edu.csumb.jacobortiz.DB.LogRecord;
import edu.csumb.jacobortiz.DB.Reservation;

public class CancelReservationActivity extends AppCompatActivity {

    public static CancelReservationActivity instance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        instance = this;

        Log.d("CancelResActivity", "onCreate called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_reservation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button main_menu = findViewById(R.id.main_menu);
        main_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_list_item_1, new ArrayList<String>());

        ListView lv = findViewById(R.id.reservations);
        lv.setAdapter(arrayAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);

                String reservation_number = selectedItem.split("\n")[0].split(" ")[1];

                confirmDelete(Integer.parseInt(String.valueOf(reservation_number.charAt(1))));
            }
        });

        Intent intent = new Intent(CancelReservationActivity.this, LoginActivity.class);
        intent.putExtra("type", "cancel");
        startActivity(intent);
    }

    @SuppressLint("DefaultLocale")
    public void loadReservations() {
        FlightDao dao = AppDatabase.getAppDatabase(CancelReservationActivity.this).dao();
        List<Reservation> results = dao.getReservations(MainActivity.username);

        if(results.isEmpty()) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("ERROR");
            alert.setMessage("You haven no reservations, go make some");
            alert.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            alert.show();
        }

        ListView lv = findViewById(R.id.reservations);
        List<String> rows = new ArrayList<>();

        for (Reservation reservation : results) {
            rows.add(String.format("Reservation: #%d\nUsername: %s\nFlightNumber: %s\n" +
                            "Number of Tickets: %d\nTotal Price: $%.2f",
                    reservation.getId(), reservation.getUsername(), reservation.getFlightNumber(),
                    reservation.getTickets(), reservation.getPrice()));
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>
                (instance, android.R.layout.simple_list_item_1, rows);
        lv.setAdapter(arrayAdapter);
    }

    public void confirmDelete(final int id) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Confirmation");
        alert.setMessage("Are you sure you wanna delete reservation " + id + "?");
        alert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                deleteReservation(id);
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    @SuppressLint("DefaultLocale")
    public void deleteReservation(int id) {
        FlightDao dao = AppDatabase.getAppDatabase(CancelReservationActivity.this).dao();

        Reservation reservation = dao.getReservation(id).get(0);
        Flight flight = dao.getFlight(reservation.getFlightNumber()).get(0);
        int amount_tickets =  reservation.getTickets();

        String message = String.format("Reservation Number: %d\n", id) +
                String.format("Username: %s\n", MainActivity.username) +
                String.format("Flight Number: %s\n", reservation.getFlightNumber()) +
                String.format("Departure: %s\n", flight.getDeparture()) +
                String.format("Arrival: %s\n", flight.getArrival()) +
                String.format("Tickets: %d\n", amount_tickets) +
                String.format("Price: $%.2f\n", reservation.getPrice());

        flight.setAvailableSeats(flight.getAvailableSeats() + amount_tickets);

        LogRecord record = new LogRecord(new Date(), "Cancel Reservation", message);

        dao.addLog(record);
        dao.updateFlight(flight);
        dao.deleteReservation(id);

        loadReservations();
    }
}

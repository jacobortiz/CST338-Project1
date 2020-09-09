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
import android.widget.EditText;
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

public class ReservationActivity extends AppCompatActivity {

    public static ReservationActivity instance = null;

    List<Flight> flights = new ArrayList<>();
    Flight flight = null;
    int ticket_amount = 0;
    long id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        instance = this;

        Log.d("ReservationActivity", "onCreate called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button return_main_button = findViewById(R.id.return_button);
        return_main_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ReservationActivity", "onClick return called");
                finish();
            }
        });

        ListView lv = findViewById(R.id.flights);

        Button search_button = findViewById(R.id.search_button);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ReservationActivity", "onClick search called");
                EditText departure = findViewById(R.id.departure);
                EditText arrival = findViewById(R.id.arrival);
                flights = AppDatabase.getAppDatabase(ReservationActivity.this).dao().
                        searchFlight(departure.getText().toString(),
                                arrival.getText().toString());

                if(flights.isEmpty()) {
                    alert("Error", "No available flight for this departure/arrival");
                } else {
                    updateList();
                }
            }
        });

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_list_item_1, new ArrayList<String>());

        lv.setAdapter(arrayAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from ListView
                String selectedItem = (String) parent.getItemAtPosition(position);

                // Get flight number
                String flight_number = selectedItem.split("\n")[0].split(" ")[2];
                selectFlight(flight_number);
            }
        });
    }

    public void login() {
        addReservation();
        confirmReservation();
    }

    public void addReservation() {
        Reservation reservation = new Reservation(new Date(), MainActivity.username,
                flight.getFlightNumber(), ticket_amount, flight.getPrice() * ticket_amount);

        FlightDao dao = AppDatabase.getAppDatabase(ReservationActivity.this).dao();
        id = dao.addReservation(reservation);

        flight.setAvailableSeats(flight.getAvailableSeats() - ticket_amount);
        dao.updateFlight(flight);
    }

    @SuppressLint("DefaultLocale")
    public void confirmReservation() {
        Log.d("ReservationActivity", "Successful Login!");

        new AlertDialog.Builder(this)
                .setTitle("Confirm Reservation")
                .setMessage(getFlightInformation())
                .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        logReservation();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        cancelReservation();
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        cancelReservation();
                    }
                }).show();
    }

    public void cancelReservation() {
        AlertDialog.Builder alert = new AlertDialog.Builder(ReservationActivity.this);
        alert.setTitle("Cancel Reservation");
        alert.setPositiveButton("Do not cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FlightDao dao = AppDatabase.getAppDatabase(ReservationActivity.this).dao();

                LogRecord logRecord = new LogRecord(new Date(), "Reservation", getFlightInformation());
                dao.addLog(logRecord);

                alert("Success!", "Reservation created successfully");
                dialog.dismiss();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                FlightDao dao = AppDatabase.getAppDatabase(ReservationActivity.this).dao();
                int id = (int) ReservationActivity.instance.id;
                Reservation reservation = dao.getReservation(id).get(0);
                int quantity =  reservation.getTickets();

                Flight flight = dao.getFlight(reservation.getFlightNumber()).get(0);
                flight.setAvailableSeats(flight.getAvailableSeats() + quantity);

                dao.updateFlight(flight);
                dao.deleteReservation(id);

                AlertDialog.Builder error = new AlertDialog.Builder(ReservationActivity.this);
                error.setTitle("Error");
                error.setMessage("Reservation failed!");
                error.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                error.show();

            }
        });
        alert.show();
    }

    public void logReservation() {
        Log.d("ReservationActivity", "Successful Reservation");

        AlertDialog.Builder builder = new AlertDialog.Builder(ReservationActivity.this);
        builder.setTitle("Reservation successfully created.");
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        FlightDao dao = AppDatabase.getAppDatabase(ReservationActivity.this).dao();
        LogRecord record = new LogRecord(new Date(), "Reservation", getFlightInformation());
        dao.addLog(record);

    }

    public void alert(String title, final String message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        final String button_text;

        if(message.equals("No available flight for this departure/arrival")) {
            button_text = "Exit";
        } else {
            button_text = "Okay";
        }

        builder.setCancelable(true);
        builder.setNegativeButton(button_text, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(button_text.equals("Exit")) {
                            finish();
                        } else {
                            dialog.dismiss();
                        }
                    }
                });

        AlertDialog alert = builder.create();
        alert.setTitle(title);
        alert.show();
    }

    @SuppressLint("DefaultLocale")
    public void updateList() {

        ListView lv = findViewById(R.id.flights);
        List<String> rows = new ArrayList<>();

        for (Flight flight : flights) {
            rows.add(String.format("Flight Number: %s\nDeparture: %s\n" +
                    "Arrival: %s\nTime: %s\nAvailable Seats: %d\nPrice: $%.2f",
                    flight.getFlightNumber(), flight.getDeparture(), flight.getArrival(),
                    flight.getDepartureTime(), flight.getAvailableSeats(), flight.getPrice()));
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>
                (instance, android.R.layout.simple_list_item_1, rows);
        lv.setAdapter(arrayAdapter);
    }

    public void selectFlight(String flight_num) {
        flight = AppDatabase.getAppDatabase(ReservationActivity.this).dao().
                getFlight(flight_num).get(0);

        EditText amount = findViewById(R.id.tickets);
        ticket_amount = Integer.parseInt(amount.getText().toString());

        if(ticket_amount > 7) {
            alert("Error", "Cannot purchase more than 7 tickets!");
        } else if(flight.getAvailableSeats() < ticket_amount) {
            alert("Error", "Cannot purchase more tickets than available!");
        } else {
            Intent intent = new Intent(ReservationActivity.this, LoginActivity.class);
            intent.putExtra("type", "reservation");
            startActivity(intent);
        }
    }

    @SuppressLint("DefaultLocale")
    public String getFlightInformation() {
        return (String.format("Reservation Number: %d\n", id) +
                String.format("Username: %s\n", MainActivity.username) +
                String.format("Flight Number: %s\n", flight.getFlightNumber()) +
                String.format("Departure: %s at %s\n", flight.getDeparture(), flight.getDepartureTime()) +
                String.format("Arrival: %s\n", flight.getArrival()) +
                String.format("Tickets: %d\n", ticket_amount) +
                String.format("Price: $%.2f\n", flight.getPrice() * ticket_amount));
    }
}
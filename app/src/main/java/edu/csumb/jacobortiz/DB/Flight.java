package edu.csumb.jacobortiz.DB;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity
public class Flight {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    @NonNull
    private String flightNumber;
    @NonNull
    private String departure;
    @NonNull
    private String arrival;
    @NonNull
    private String departureTime;
    @NonNull
    private int capacity;
    @NonNull
    private double price;
    @NonNull
    private int availableSeats;

    public Flight() {}

    @Ignore
    public Flight(String flightNumber, String departure, String arrival, String departureTime, int capacity, double price) {
        this.flightNumber = flightNumber;
        this.departure = departure;
        this.arrival = arrival;
        this.departureTime = departureTime;
        this.capacity = capacity;
        this.price = price;
        this.availableSeats = capacity;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(@NonNull String flightNo) {
        this.flightNumber = flightNo;
    }

    @NonNull
    public String getDeparture() {
        return departure;
    }

    public void setDeparture(@NonNull String departure) {
        this.departure = departure;
    }

    @NonNull
    public String getArrival() {
        return arrival;
    }

    public void setArrival(@NonNull String arrival) {
        this.arrival = arrival;
    }

    @NonNull
    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(@NonNull String departureTime) {
        this.departureTime = departureTime;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    @NotNull
    @SuppressLint("DefaultLocale")
    @Override
    public String toString() {
        return String.format("ID: %d Flight Number: %s\nDeparture: %s\nArrival: %s\nDeparture Time: %s", id, flightNumber, departure, arrival, departureTime);
    }

}

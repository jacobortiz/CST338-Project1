package edu.csumb.jacobortiz.DB;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

@Entity
public class Reservation {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private long time;
    @NonNull
    private String username;
    @NonNull
    private String flightNumber;
    private int tickets;
    private double price;

    public Reservation() {}

    @Ignore
    public Reservation(Date datetime, @NotNull String username, @NotNull String flight_number, int tickets, double price) {
        this.time = datetime.getTime();
        this.username = username;
        this.flightNumber = flight_number;
        this.tickets = tickets;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public Date getDateTime() {
        return new Date(time);
    }

    public long getTime() { return time;}

    public void setTime(long time) { this.time = time; }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    @NonNull
    public String getUsername() {
        return username;
    }

    @NonNull
    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(@NonNull String flight_number) {
        this.flightNumber = flight_number;
    }

    public int getTickets() {
        return tickets;
    }

    public void setTickets(int tickets) {
        this.tickets = tickets;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

package edu.csumb.jacobortiz.DB;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

@Entity
public class LogRecord {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private long time;
    @NonNull
    private String transactionType;
    @NonNull
    private String detailed_message;

    public LogRecord(){
    }

    @Ignore
    public LogRecord(Date datetime, @NotNull String type, @NotNull String message){
        this.time = datetime.getTime();
        this.transactionType = type;
        this.detailed_message = message;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    private Date getDatetime() {
        return new Date(time);
    }

    public long getTime() { return time;}
    public void setTime(long time) { this.time = time; }

    @NonNull
    String getTransactionType() {
        return transactionType;
    }

    void setTransactionType(@NonNull String transaction_type) {
        this.transactionType = transaction_type;
    }

    @NonNull
    public String getDetailed_message() {
        return detailed_message;
    }

    public void setDetailed_message(@NonNull String detailed_message) {
        this.detailed_message = detailed_message;
    }

    @NotNull
    @SuppressLint("DefaultLocale")
    @Override
    public String toString() {

        return String.format("Log ID: %d\n", id) +
                String.format("Transaction Type: (%s)\n", transactionType) +
                getDetailed_message() +
                String.format("Date: %s\n", getDatetime());
    }
}

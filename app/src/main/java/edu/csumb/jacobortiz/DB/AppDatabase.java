package edu.csumb.jacobortiz.DB;

import android.content.Context;
import android.util.Log;

import java.util.List;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities={User.class, Flight.class, LogRecord.class, Reservation.class}, version=6)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;
    public abstract FlightDao dao();

    public static AppDatabase getAppDatabase(final Context context){
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class,
                    "FlightDB")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public void loadData(Context context){
        List<User> user_list = AppDatabase.getAppDatabase(context).dao().getAllUsers();
        if (user_list.size() == 0) {
            loadUsers(context);
            loadFlights(context);
        }
    }

    private void loadUsers(Context context) {
        FlightDao dao = getAppDatabase(context).dao();

        User alice = new User("alice5", "csumb100");
        User brian = new User("brian77","123ABC");
        User chris = new User("chris21", "CHRIS21");
        dao.addUser(alice, brian, chris);
        Log.d("AppDatabase", "3 users added");
    }

    private void loadFlights(Context context){
        FlightDao dao = getAppDatabase(context).dao();

        Flight otter101 = new Flight("Otter101", "Monterey", "Los Angeles", "10:00(AM)", 10, 150);
        Flight otter102 = new Flight("Otter102", "Los Angeles", "Monterey", "1:00(PM)", 10, 150);
        Flight otter201 = new Flight("Otter201", "Monterey", "Seattle", "11:00(AM)", 5, 200.50);
        Flight otter205 = new Flight("Otter205", "Monterey", "Seattle", "3:00(PM)", 15, 150.00);
        Flight otter202 = new Flight("Otter202", "Seattle", "Monterey", "2:00(PM)", 5, 200.50);
        dao.addFlight(otter101, otter102, otter201, otter205, otter202);
        Log.d("AppDatabase", "5 flights added");
    }
}

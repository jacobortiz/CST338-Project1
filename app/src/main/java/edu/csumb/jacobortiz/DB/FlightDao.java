package edu.csumb.jacobortiz.DB;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface FlightDao {

    @Query("select * from User")
    List<User> getAllUsers();

    @Query("select * from User where username = :username and password= :password")
    User login(String username, String password);

    @Query("select * from User where username = :username")
    User getUserByName(String username);

    @Insert
    void addUser(User...user);

    @Query("select * from Flight")
    List<Flight> getAllFlights();

    @Query("select * from Flight where departure=:departure and arrival=:arrival")
    List<Flight> searchFlight(String departure, String arrival);

    @Query("select * from Flight where flightNumber = :num")
    List<Flight> getFlight(String num);

    @Insert
    void addFlight(Flight...flight);

    @Update
    void updateFlight(Flight flight);

    @Insert
    void addLog(LogRecord record);

    @Query("select * from LogRecord order by time desc")
    List<LogRecord> getRecords();

    @Insert
    long addReservation(Reservation reservation);

    @Query("select * from Reservation where username = :username")
    List<Reservation> getReservations(String username);

    @Query("select * from Reservation where id = :id")
    List<Reservation> getReservation(int id);

    @Query("select * from Reservation where flightNumber = :flight_num")
    List<Reservation> getReservation(String flight_num);

    @Query("delete from Reservation where id = :id")
    void deleteReservation(int id);
}

package com.csumb.luism.bookrentalsystem;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BookRentalSystemDao {
    @Insert
    void insert(Book... books);

    @Insert
    void insert(Customer... customers);

    @Update
    void update(Book... books);

    @Query("SELECT * FROM Customer WHERE username = :username")
    List<Customer> checkUsernameAvailability(String username);

    @Query("SELECT * FROM Book")
    List<Book> getAllBooks();

    @Query("SELECT * FROM Customer")
    List<Customer> getAllCustomers();

    @Insert
    void insert(Librarian... librarians);

    @Query("SELECT * FROM Librarian WHERE username = :username AND password = :password")
    List<Librarian> verifyLibrarianLogin(String username, String password);

    @Query("SELECT * FROM Librarian")
    List<Librarian> getAllLibrarians();

    @Query("SELECT COUNT(*) FROM Book WHERE title = :title")
    boolean doesBookExist(String title);

    @Insert
    void insertLogEntry(LogEntry logEntry);

    @Query("SELECT * FROM log_table ORDER BY timestamp DESC")
    List<LogEntry> getAllLogs();

    @Update
    void updateBookReservationStatus(Book book);

    @Query("SELECT * FROM Book WHERE title = :title")
    Book getBookByTitle(String title);

    @Query("SELECT * FROM Customer WHERE username = :username AND password = :password")
    List<Customer> verifyCustomerLogin(String username, String password);

    @Query("SELECT * FROM Book WHERE customerUsername = :customerUsername AND availability LIKE '%-'")
    List<Book> getReservedBooksForCustomer(String customerUsername);

    @Query("UPDATE Book SET availability = REPLACE(availability, '-', '+') WHERE customerUsername = :customerUsername")
    void cancelBookHold(String customerUsername);






}

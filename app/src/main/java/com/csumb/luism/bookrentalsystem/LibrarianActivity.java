package com.csumb.luism.bookrentalsystem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;


import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class LibrarianActivity extends AppCompatActivity {

    private EditText bookTitleEditText;
    private EditText authorEditText;
    private EditText feeEditText;
    private Button addBookButton, backButton, displayBooksButton, displayCustomersButton, displayLogsButton;
    private BookRentalSystemDao bookRentalSystemDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_librarian);

        bookRentalSystemDao = BookRentalSystemDatabase.getDatabase(this).getBookRentalSystemDao();

        bookTitleEditText = findViewById(R.id.bookTitleEditText);
        authorEditText = findViewById(R.id.authorEditText);
        feeEditText = findViewById(R.id.feeEditText);
        addBookButton = findViewById(R.id.addBookButton);

        showOperationLogsDialog();


        addBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bookTitle = bookTitleEditText.getText().toString().trim();
                String author = authorEditText.getText().toString().trim();
                String feeString = feeEditText.getText().toString().trim();

                if (isValidInput(bookTitle, author, feeString)) {
                    double fee = Double.parseDouble(feeString);

                    Book newBook = new Book(bookTitle, author, fee);

                    if (newBook.getTitle().isEmpty() || newBook.getAuthor().isEmpty() || newBook.getFee() <= 0) {
                        Toast.makeText(LibrarianActivity.this, "Invalid information. Please provide all details.", Toast.LENGTH_SHORT).show();
                        navigateToMainMenu();
                    } else if (bookRentalSystemDao.doesBookExist(newBook.getTitle())) {
                        Toast.makeText(LibrarianActivity.this, "Book with this title already exists", Toast.LENGTH_SHORT).show();
                        navigateToMainMenu();
                    } else {
                        insertBookIntoDatabase(newBook);

                        Toast.makeText(LibrarianActivity.this, "Book added successfully", Toast.LENGTH_SHORT).show();

                        bookTitleEditText.setText("");
                        authorEditText.setText("");
                        feeEditText.setText("");

                        navigateToMainMenu();
                    }
                } else {
                    Toast.makeText(LibrarianActivity.this, "Invalid input. Please check your entries.", Toast.LENGTH_SHORT).show();
                    navigateToMainMenu();
                }
            }
        });


        displayBooksButton = findViewById(R.id.displayBooksButton);
        displayBooksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAllBooksDialog();
            }
        });

        displayCustomersButton = findViewById(R.id.displayCustomersButton);
        displayCustomersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAllCustomersDialog();
            }
        });

        displayLogsButton = findViewById(R.id.displayLogsButton);
        displayLogsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOperationLogsDialog();
            }
        });
    }

    private boolean isValidInput(String bookTitle, String author, String feeString) {
        return !bookTitle.isEmpty() && !author.isEmpty() && !feeString.isEmpty();
    }

    private void insertBookIntoDatabase(Book book) {
        bookRentalSystemDao.insert(book);
    }



    private void showLoginDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Librarian Login");

        View view = getLayoutInflater().inflate(R.layout.dialog_librarian_login, null);
        final EditText usernameEditText = view.findViewById(R.id.usernameEditText);
        final EditText passwordEditText = view.findViewById(R.id.passwordEditText);

        builder.setView(view);

        builder.setPositiveButton("Login", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (verifyLibrarianLogin(username, password)) {
                    Toast.makeText(LibrarianActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LibrarianActivity.this, "Invalid login. Please try again.", Toast.LENGTH_SHORT).show();
                    showLoginDialog();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        builder.create().show();
    }

    private boolean verifyLibrarianLogin(String username, String password) {
        List<Librarian> librarians = bookRentalSystemDao.verifyLibrarianLogin(username, password);
        return !librarians.isEmpty();
    }

    private void showAllBooksDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("All Books");

        List<Book> allBooks = bookRentalSystemDao.getAllBooks();

        StringBuilder booksString = new StringBuilder();
        for (Book book : allBooks) {
            booksString.append("Title: ").append(book.getTitle()).append("\n");
            booksString.append("Author: ").append(book.getAuthor()).append("\n");
            booksString.append("Fee per Hour: $").append(String.format("%.2f", book.getFee())).append("\n\n");
        }

        builder.setMessage(booksString.toString());

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.create().show();
    }

    private void navigateToMainMenu() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showAllCustomersDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("All Customers");

        List<Customer> allCustomers = bookRentalSystemDao.getAllCustomers();

        StringBuilder customersString = new StringBuilder();
        for (Customer customer : allCustomers) {
            customersString.append("Username: ").append(customer.getUsername()).append("\n");
        }

        builder.setMessage(customersString.toString());

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.create().show();
    }

    private void showOperationLogsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Operation Logs");

        List<LogEntry> operationLogs = bookRentalSystemDao.getAllLogs(); // Assuming you have a method to get all logs

        StringBuilder logsString = new StringBuilder();

        for (LogEntry log : operationLogs) {
            logsString.append(log.getOperationType()).append(" - ").append(log.getDetails())
                    .append(" at ").append(log.getTimestamp()).append("\n");
        }

        builder.setMessage(logsString.toString());

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                showAddBookPrompt();
            }
        });

        builder.create().show();
    }

    private void showAddBookPrompt() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add a New Book?");
        builder.setMessage("Do you want to add a new book?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                navigateToMainMenu();
            }
        });

        builder.create().show();
    }


}

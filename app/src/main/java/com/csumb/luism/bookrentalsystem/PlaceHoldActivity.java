package com.csumb.luism.bookrentalsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class PlaceHoldActivity extends AppCompatActivity {

    private TextView monthYearTextView;
    private EditText pickupDayEditText, returnDayEditText;
    private Button showAvailableBooksButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_hold);

        monthYearTextView = findViewById(R.id.monthYearTextView);
        pickupDayEditText = findViewById(R.id.pickupDayEditText);
        returnDayEditText = findViewById(R.id.returnDayEditText);
        showAvailableBooksButton = findViewById(R.id.showAvailableBooksButton);

        monthYearTextView.setText("December 2023");

        showAvailableBooksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAvailableBooks();
            }
        });
    }

    private void showAvailableBooks() {
        String pickupDayText = pickupDayEditText.getText().toString().trim();
        String returnDayText = returnDayEditText.getText().toString().trim();


        if (pickupDayText.isEmpty() || returnDayText.isEmpty()) {
            Toast.makeText(this, "Please enter day values for both pickup and return dates.", Toast.LENGTH_SHORT).show();
            return;
        }

        int pickupDay = Integer.parseInt(pickupDayText);
        int returnDay = Integer.parseInt(returnDayText);

        if (pickupDay < 1 || pickupDay > 31 || returnDay < 1 || returnDay > 31) {
            Toast.makeText(this, "Please enter valid day values (1 to 31).", Toast.LENGTH_SHORT).show();
            return;
        }

        if (returnDay - pickupDay > 7 || returnDay < pickupDay) {
            Toast.makeText(this, "Invalid rental period. Please check your dates.", Toast.LENGTH_SHORT).show();
        } else {
            // Pass day values between activities as integers
            Intent intent = new Intent(this, AvailableBooksActivity.class);
            intent.putExtra("pickupDate", pickupDay);
            intent.putExtra("returnDate", returnDay);
            startActivity(intent);
        }
    }

}


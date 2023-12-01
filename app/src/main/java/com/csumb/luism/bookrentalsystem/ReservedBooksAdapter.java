package com.csumb.luism.bookrentalsystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ReservedBooksAdapter extends ArrayAdapter<Book> {

    private int resourceLayout;
    private int selectedItemPosition = -1; // Initially, no item is selected

    public ReservedBooksAdapter(Context context, int resource, List<Book> items) {
        super(context, resource, items);
        this.resourceLayout = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(resourceLayout, parent, false);
        }

        Book book = getItem(position);

        if (book != null) {
            TextView titleTextView = v.findViewById(R.id.titleTextView);
            TextView pickupDateTextView = v.findViewById(R.id.pickupDateTextView);
            TextView returnDateTextView = v.findViewById(R.id.returnDateTextView);

            if (titleTextView != null) {
                titleTextView.setText(book.getTitle());
            }

            // Check if the layout contains TextViews for pickup and return dates
            if (pickupDateTextView != null && returnDateTextView != null) {
                pickupDateTextView.setText("Pickup Date: December 2023");
                returnDateTextView.setText("Return Date: December 2023");
            }

            // Highlight the selected item
            if (position == selectedItemPosition) {
                v.setActivated(true);
            } else {
                v.setActivated(false);
            }
        }

        return v;
    }

    // Method to set the selected item position
    public void setSelectedItemPosition(int position) {
        selectedItemPosition = position;
        notifyDataSetChanged(); // Refresh the ListView to apply the changes
    }

    public int getSelectedItemPosition() {
        return selectedItemPosition;
    }
}

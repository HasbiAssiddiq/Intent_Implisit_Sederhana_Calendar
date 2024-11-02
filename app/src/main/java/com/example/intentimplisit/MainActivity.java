package com.example.intentimplisit;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private EditText titleEditText, locationEditText, descriptionEditText;
    private TextView dateTextView, timeTextView;
    private Button createEventButton;

    private Calendar eventDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titleEditText = findViewById(R.id.titleEditText);
        locationEditText = findViewById(R.id.locationEditText);
        dateTextView = findViewById(R.id.dateTextView);
        timeTextView = findViewById(R.id.timeTextView);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        createEventButton = findViewById(R.id.createEventButton);


        eventDate = Calendar.getInstance();
        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        timeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });
        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCalendarEvent();
            }
        });
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                MainActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Set tanggal yang dipilih ke Calendar
                        eventDate.set(Calendar.YEAR, year);
                        eventDate.set(Calendar.MONTH, month);
                        eventDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateDateTextView();
                    }
                },
                eventDate.get(Calendar.YEAR),
                eventDate.get(Calendar.MONTH),
                eventDate.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }
    private void updateDateTextView() {

        String dateString = String.format("%02d/%02d/%04d",
                eventDate.get(Calendar.DAY_OF_MONTH),
                eventDate.get(Calendar.MONTH) + 1,
                eventDate.get(Calendar.YEAR));
        dateTextView.setText(dateString);
    }
    private void showTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                MainActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        eventDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        eventDate.set(Calendar.MINUTE, minute);
                        updateTimeTextView();
                    }
                },
                eventDate.get(Calendar.HOUR_OF_DAY),
                eventDate.get(Calendar.MINUTE),
                true
        );
        timePickerDialog.show();
    }
    private void updateTimeTextView() {

        String timeString = String.format("%02d:%02d",
                eventDate.get(Calendar.HOUR_OF_DAY),
                eventDate.get(Calendar.MINUTE));
        timeTextView.setText(timeString);
    }
    private void createCalendarEvent() {
        String title = titleEditText.getText().toString();
        String location = locationEditText.getText().toString();
        String description = descriptionEditText.getText().toString();

        long startMillis = eventDate.getTimeInMillis();
        long endMillis = startMillis + 60 * 60 * 1000;

        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setData(CalendarContract.Events.CONTENT_URI);
        intent.putExtra(CalendarContract.Events.TITLE, title);
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, location);
        intent.putExtra(CalendarContract.Events.DESCRIPTION, description);
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis);
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endMillis);
        intent.setPackage("com.google.android.calendar");

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
            clearInputs();
        } else {
            intent.setPackage(null);
            startActivity(intent);
            clearInputs();
        }
    }
    private void clearInputs() {
        titleEditText.setText("");
        locationEditText.setText("");
        descriptionEditText.setText("");
        dateTextView.setText("Select Date");
        timeTextView.setText("Select Time");
        eventDate = Calendar.getInstance();
    }
}

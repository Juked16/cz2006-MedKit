package com.example.medkit2006;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.medkit2006.entity.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class AccountSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accountsettings);
    }

    @Override
    protected void onResume() {
        super.onResume();
        User user = MainActivity.accountMgr.getLoggedInUser();
        if (user != null) {
            //read-only
            EditText username = findViewById(R.id.accountSettingsUsername);
            username.setEnabled(false);
            username.setText(user.getUsername());

            EditText email = findViewById(R.id.accountSettingsEmail);
            email.setEnabled(false);
            email.setText(user.getEmail());

            CheckBox verified = findViewById(R.id.accountSettingsVerified);
            verified.setEnabled(false);
            verified.setChecked(user.getVerified());

            //editable
            EditText firstName = findViewById(R.id.accountSettingsFirstName);
            firstName.setText(user.getFirstName());

            EditText lastName = findViewById(R.id.accountSettingsLastName);
            lastName.setText(user.getLastName());

            Spinner gender = (findViewById(R.id.accountSettingsGender));
            gender.setAdapter(ArrayAdapter.createFromResource(this, R.array.gender_array, R.layout.textview));
            String[] genderArray = getResources().getStringArray(R.array.gender_array);
            int i = 0;
            for(String str : genderArray) {
                if (str.equals(user.getGender())){
                    gender.setSelection(i);
                    break;
                }
                i++;
            }

            Spinner bloodType = (findViewById(R.id.accountSettingsBloodType));
            bloodType.setAdapter(ArrayAdapter.createFromResource(this, R.array.blood_type_array, R.layout.textview));
            String[] bloodTypeArray = getResources().getStringArray(R.array.blood_type_array);
            i = 0;
            for(String str : bloodTypeArray) {
                if (str.equals(user.getBloodType())){
                    bloodType.setSelection(i);
                    break;
                }
                i++;
            }

            EditText dateOfBirth = findViewById(R.id.accountSettingsDOB);
            Date d = user.getDateOfBirth();
            if(d != null){
                dateOfBirth.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(d));
            }
            dateOfBirth.setFocusable(false);
            dateOfBirth.setOnClickListener(btn -> {
                DatePickerDialog dialog = new DatePickerDialog(this);
                Date date = user.getDateOfBirth();
                if (date != null) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    dialog.updateDate(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH));
                }
                dialog.setOnDateSetListener((datePicker, year, month, day) -> {
                    dateOfBirth.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.US).format((new GregorianCalendar(year, month, day)).getTime()));
                });
                dialog.show();
            });

            findViewById(R.id.accountSettingsSaveBtn).setOnClickListener(btn -> {
                Date dob;
                TextView status = findViewById(R.id.accountSettingsSaveStatus);
                if (dateOfBirth.getText().length() > 0) {
                    try {
                        dob = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(dateOfBirth.getText().toString());
                    } catch (ParseException e) {
                        status.setText("Invalid date");
                        return;
                    }
                    assert dob != null;
                    if(dob.toInstant().isAfter(Instant.now())){
                        status.setText("Date of birth cannot be future date");
                        return;
                    }
                    user.setDateOfBirth(dob);
                }
                user.setFirstName(firstName.getText().toString());
                user.setLastName(lastName.getText().toString());
                user.setGender(gender.getSelectedItem().toString());
                user.setBloodType(bloodType.getSelectedItem().toString());
                status.setText("Saving");
                MainActivity.accountMgr.saveLoggedInUserDetails(() -> status.setText("Saved"), e -> status.setText(e.getMessage()));
            });
        }
    }
}

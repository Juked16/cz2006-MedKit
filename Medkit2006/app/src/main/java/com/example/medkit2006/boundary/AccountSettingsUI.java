package com.example.medkit2006.boundary;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.medkit2006.MainActivity;
import com.example.medkit2006.R;
import com.example.medkit2006.entity.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class AccountSettingsUI extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountsettings);
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

            CheckBox verified = findViewById(R.id.accountSettingsVerified);
            verified.setEnabled(false);

            Button verifyBtn = findViewById(R.id.accountSettingsVerifyBtn);
            verifyBtn.setOnClickListener(btn -> {
                finish();
                startActivity(new Intent(this, VerificationUI.class));
            });

            if (user.getVerified()) {
                verified.setChecked(true);
            } else {
                verifyBtn.setVisibility(View.VISIBLE);
            }

            //editable
            EditText email = findViewById(R.id.accountSettingsEmail);
            email.setText(user.getEmail());

            email.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    verifyBtn.setVisibility(View.INVISIBLE);
                }
            });
            EditText firstName = findViewById(R.id.accountSettingsFirstName);
            firstName.setText(user.getFirstName());

            EditText lastName = findViewById(R.id.accountSettingsLastName);
            lastName.setText(user.getLastName());

            Spinner gender = (findViewById(R.id.accountSettingsGender));
            gender.setAdapter(ArrayAdapter.createFromResource(this, R.array.gender_array, R.layout.textview));
            String[] genderArray = getResources().getStringArray(R.array.gender_array);
            int i = 0;
            for (String str : genderArray) {
                if (str.equals(user.getGender())) {
                    gender.setSelection(i);
                    break;
                }
                i++;
            }

            Spinner bloodType = (findViewById(R.id.accountSettingsBloodType));
            bloodType.setAdapter(ArrayAdapter.createFromResource(this, R.array.blood_type_array, R.layout.textview));
            String[] bloodTypeArray = getResources().getStringArray(R.array.blood_type_array);
            i = 0;
            for (String str : bloodTypeArray) {
                if (str.equals(user.getBloodType())) {
                    bloodType.setSelection(i);
                    break;
                }
                i++;
            }

            EditText dateOfBirth = findViewById(R.id.accountSettingsDOB);
            Date d = user.getDateOfBirth();
            if (d != null) {
                dateOfBirth.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(d));
            }
            dateOfBirth.setFocusable(false);
            dateOfBirth.setOnClickListener(btn -> {
                DatePickerDialog dialog = new DatePickerDialog(this);
                Date date = user.getDateOfBirth();
                if (date != null) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    dialog.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                }
                dialog.setOnDateSetListener((datePicker, year, month, day) -> dateOfBirth.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.US).format((new GregorianCalendar(year, month, day)).getTime())));
                dialog.show();
            });
            Button save = findViewById(R.id.accountSettingsSaveBtn);
            save.setOnClickListener(btn -> {
                TextView status = findViewById(R.id.accountSettingsStatus);
                String emailText = email.getText().toString();
                if (!emailText.equals(user.getEmail())) {
                    if (MainActivity.accountMgr.validEmail(emailText)) {
                        status.setText("Checking email");
                        MainActivity.accountMgr.emailExist(emailText, exist -> {
                            if (!exist) {
                                user.setEmail(emailText);
                                user.setVerified(false);
                                verified.setChecked(false);
                                afterEmailCheck(true);
                            } else
                                status.setText("Please use another email");
                        }, e -> status.setText(e.getMessage()));
                    } else
                        status.setText("Invalid email");
                    return;
                }
                afterEmailCheck(false);
            });
        }
    }

    private void afterEmailCheck(boolean emailChanged) {
        EditText dateOfBirth = findViewById(R.id.accountSettingsDOB);
        EditText firstName = findViewById(R.id.accountSettingsFirstName);
        EditText lastName = findViewById(R.id.accountSettingsLastName);
        TextView status = findViewById(R.id.accountSettingsStatus);
        Spinner gender = (findViewById(R.id.accountSettingsGender));
        Spinner bloodType = (findViewById(R.id.accountSettingsBloodType));

        User user = MainActivity.accountMgr.getLoggedInUser();
        if (dateOfBirth.getText().length() > 0) {
            Date dob;
            try {
                dob = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(dateOfBirth.getText().toString());
            } catch (ParseException e) {
                status.setText("Invalid date");
                return;
            }
            assert dob != null;
            if (dob.toInstant().isAfter(Instant.now())) {
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
        AlertDialog dialog = new AlertDialog.Builder(this).setView(new ProgressBar(this)).setTitle("Saving").setMessage("Please wait").setCancelable(false).show();
        MainActivity.accountMgr.saveLoggedInUserDetails(() -> {
            dialog.dismiss();
            status.setText("Saved");
            if(emailChanged)
                findViewById(R.id.accountSettingsVerifyBtn).setVisibility(View.VISIBLE);
        }, e -> {
            dialog.dismiss();
            status.setText(e.getMessage());
        });
    }
}

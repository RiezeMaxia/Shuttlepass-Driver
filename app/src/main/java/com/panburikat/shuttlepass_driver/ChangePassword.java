package com.panburikat.shuttlepass_driver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class ChangePassword extends AppCompatActivity {

    private String id;
    private String acc_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        id = getIntent().getStringExtra("accID");
        acc_email = getIntent().getStringExtra("email");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Change Password");
        getSupportActionBar().setHomeActionContentDescription("");
        TextInputLayout email = findViewById(R.id.email);
        TextInputLayout old_pass = findViewById(R.id.old_pass);
        TextInputLayout new_pass = findViewById(R.id.new_pass);
        TextInputLayout confirm_pass = findViewById(R.id.confirm_pass);
        Button save_changes = findViewById(R.id.save_changes);
        email.getEditText().setText(acc_email);


        save_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldpass = old_pass.getEditText().getText().toString().trim();
                String newpass = new_pass.getEditText().getText().toString().trim();
                String confirmpass = confirm_pass.getEditText().getText().toString().trim();
                old_pass.setErrorEnabled(false);
                new_pass.setErrorEnabled(false);
                confirm_pass.setErrorEnabled(false);

                if (!oldpass.equals("") || !newpass.equals("") || !confirmpass.equals("")) {
                    if (confirmpass.equals(newpass)) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                //Starting Write and Read data with URL
                                //Creating array for parameters
                                String[] field = new String[3];
                                field[0] = "id";
                                field[1] = "oldpass";
                                field[2] = "newpass";
                                //Creating array for data
                                String[] data = new String[3];
                                data[0] = id;
                                data[1] = oldpass;
                                data[2] = newpass;
                                PutData putData = new PutData("https://jamora.leon.svdphs.ph/changePass.php", "POST", field, data);
                                if (putData.startPut()) {
                                    if (putData.onComplete()) {
                                        String result = putData.getResult();
                                        if (!result.equals("Error: Database connection") || !result.equals("No accountID")) {
                                            if (result.equals("oldpassERR")) {
                                                old_pass.setErrorEnabled(true);
                                                old_pass.setError("Old Password does not match!");
                                            } else {
                                                finish();
                                            }
                                        }
                                    }
                                }
                            }
                        }); //End Write and Read data with URL

                    } else {
                        new_pass.setErrorEnabled(true);
                        confirm_pass.setErrorEnabled(true);
                        new_pass.setError("New Password does not match with Confirm Password!");
                        confirm_pass.setError("New Password does not match with Confirm Password!");
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please fill in all required fields!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }
}
package com.example.georg.test2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by georg on 18/02/2018.
 */

public class RegisterActivity extends AppCompatActivity {
    Button registerButton;
    Button returnButton;
    //private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_screen);
// ...
        // mAuth = FirebaseAuth.getInstance();
        returnButton = (Button) findViewById(R.id.button4) ;
        registerButton = (Button) findViewById(R.id.button2);

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this,WelcomeActivity.class);
                startActivity(intent);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),
                        "Registered Successfully!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
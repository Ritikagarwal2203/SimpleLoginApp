package com.example.android.userentry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ForgotPassword extends AppCompatActivity {
    private EditText passwordEmail;
    private Button ResetButton;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        passwordEmail=(EditText)findViewById(R.id.Emailentered);
        ResetButton=(Button)findViewById(R.id.PasswordReset);
        firebaseAuth =FirebaseAuth.getInstance();
        ResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String useremail=passwordEmail.getText().toString().trim();
                if(useremail.equals("")){
                    Toast.makeText(ForgotPassword.this,"please enter  your registered email id",Toast.LENGTH_SHORT).show();
                }
                else {
                    firebaseAuth.sendPasswordResetEmail(useremail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {


                                Toast.makeText(ForgotPassword.this, "Email sent", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(ForgotPassword.this, MainActivity.class));
                            }
                            else
                            {
                                Toast.makeText(ForgotPassword.this, "failed,Hint: make sure the email is registered", Toast.LENGTH_SHORT).show();
                            }
                        }


                    });
                }
            }
        });
    }
}
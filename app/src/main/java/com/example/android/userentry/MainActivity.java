package com.example.android.userentry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private long backPressedTime;
    private EditText Name;
    private EditText Password;
    private TextView Info;
    private Button Login;
    private int counter =4;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Name=(EditText)findViewById(R.id.userId);
        Password=(EditText)findViewById(R.id.enteredPassword);
        Info=(TextView) findViewById(R.id.info);
        Login=(Button) findViewById(R.id.login);
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser user=firebaseAuth.getCurrentUser();
        progressDialog=new ProgressDialog(this);
        if(user!=null){

            startActivity(new Intent(MainActivity.this,Details.class));
        }
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validate(Name.getText().toString(),Password.getText().toString());

            }
        });


    }

    public void forgot(View view){
        Intent i=new Intent(this,ForgotPassword.class);

        startActivity(i);

    }
    public void create(View view) {
        Intent i = new Intent(this, Register.class);
        startActivity(i);
        finish();
    }


    @Override
    public void onBackPressed() {

        if (backPressedTime+2000>System.currentTimeMillis()){
            super.onBackPressed();
            System.exit(0);
            return;
        }
        else {
            Toast.makeText(getBaseContext(),"Press again to exit",Toast.LENGTH_SHORT).show();

        }
        backPressedTime=System.currentTimeMillis();

    }
    public void validate( String userName, String userPassword){
        progressDialog.setMessage("Wait kerle re bhai");
        progressDialog.show();
            firebaseAuth.signInWithEmailAndPassword(userName,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                     if (task.isSuccessful()){
                         progressDialog.dismiss();
                        CheckEmailVerifiction();
                }
                     else {
                         Toast.makeText(MainActivity.this,"Login failed",Toast.LENGTH_SHORT).show();
                         counter--;
                         Info.setText("No. of attempts remaining: "+counter);
                         progressDialog.dismiss();
                         if(counter ==0){
                             Login.setEnabled(false);
                         }
                     }
            }
        });
    }
    private void CheckEmailVerifiction(){
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        startActivity(new Intent(MainActivity.this,Details.class));
        Boolean emailFlag=firebaseUser.isEmailVerified();
        if(emailFlag){
            finish();

        }
        else{
            Toast.makeText(MainActivity.this,"Verify your email just recieved",Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }

}
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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

        progressDialog=new ProgressDialog(this);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(check()){
                    firebaseAuth=FirebaseAuth.getInstance();
                    FirebaseUser user=firebaseAuth.getCurrentUser();


                        validate(Name.getText().toString(),Password.getText().toString());

                }


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
        progressDialog.setMessage("Please wait");
        progressDialog.show();
            firebaseAuth.signInWithEmailAndPassword(userName,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                     if (task.isSuccessful()){



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

        Boolean emailFlag=firebaseUser.isEmailVerified();
        if(emailFlag){
            isUser();



        }
        else{
            Toast.makeText(MainActivity.this,"Verify your email just recieved",Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
            progressDialog.dismiss();

        }
    }
    private Boolean check(){
        Boolean n=true;
        Name=(EditText)findViewById(R.id.userId);
        Password=(EditText)findViewById(R.id.enteredPassword);
        String User_name=Name.getText().toString().trim();
        String User_password=Password.getText().toString().trim();
        if (User_name.isEmpty()||User_password.isEmpty()){
            Toast.makeText(MainActivity.this,"Fields cannot be empty",Toast.LENGTH_SHORT).show();
            n=false;
            return n;
        }
        else{
            return n;
        }

    }
    private void isUser(){
        Name=(EditText)findViewById(R.id.userId);
        Password=(EditText)findViewById(R.id.enteredPassword);
        final String User_name=Encoder.encodeUserEmail(Name.getText().toString().trim());
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("hi");
        Query checkuser=reference.orderByChild("email").equalTo(User_name);
        checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    String nameFromDB=snapshot.child(User_name).child("name").getValue(String.class);
                    String emailFromDB=snapshot.child(User_name).child("email").getValue(String.class);
                    String mobileNoFromDB=snapshot.child(User_name).child("mobileNo").getValue(String.class);
                    Intent i=new Intent(MainActivity.this,Details.class);
                    i.putExtra("name",nameFromDB);
                    i.putExtra("email",emailFromDB);
                    i.putExtra("mobileNo",mobileNoFromDB);
                progressDialog.dismiss();
                    startActivity(i);
                    finish();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}
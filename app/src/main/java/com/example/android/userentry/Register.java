package com.example.android.userentry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.PriorityQueue;

public class Register extends AppCompatActivity {
    private EditText FirstName,LastName,Email,Password,ConfirmPassword,Mobileno;
    private TextView signUp,signIn;

    private CheckBox terms;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setupIViews();
        firebaseAuth=FirebaseAuth.getInstance();
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.setMessage("Please wait"+FirstName.getText().toString().trim());
                progressDialog.show();
                if(validate()){
                    String userEmail=Email.getText().toString().trim();
                    String password=Password.getText().toString().trim();
                    firebaseAuth.createUserWithEmailAndPassword(userEmail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                sendEmailVerification();

                            }
                             else {
                                Toast.makeText(Register.this,"Registration failed",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Register.this,MainActivity.class));
                                progressDialog.dismiss();
                            }
                        }
                    });
                }
            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Register.this,MainActivity.class);

                startActivity(i);
                finish();

            }
        });
    }
         public void setupIViews(){
        FirstName=(EditText)findViewById(R.id.firstName);
        LastName=(EditText)findViewById(R.id.SecondName);
        Email=(EditText)findViewById(R.id.ProvidedEmail);
        Password=(EditText)findViewById(R.id.ProvidedPassword);
        ConfirmPassword=(EditText)findViewById(R.id.ConfirmPassword);
        signIn=(TextView) findViewById(R.id.SignIn);
        signUp=(TextView)findViewById(R.id.SignUp);
        terms=(CheckBox)findViewById(R.id.Check);
        Mobileno=(EditText)findViewById(R.id.mobno);
        progressDialog=new ProgressDialog(this);

    }
    private Boolean validate(){
        setupIViews();
        Boolean result=false;
        String name=FirstName.getText().toString();
        String Lname=LastName.getText().toString();
        String email=Email.getText().toString();
        String mobno=Mobileno.getText().toString();

        String password=Password.getText().toString();
        String confirmPassword=ConfirmPassword.getText().toString();
        Boolean m=terms.isChecked();

        if(name.isEmpty()||Lname.isEmpty()||email.isEmpty()||password.isEmpty()||confirmPassword.isEmpty()||mobno.isEmpty()){
            progressDialog.dismiss();
            Toast.makeText(this,"Please enter all the details",Toast.LENGTH_SHORT).show();

        }
        else {

            if (m){
                if(password.equals(confirmPassword)){
                    result=true;
                }
                else {
                    Toast.makeText(Register.this,"Password didn't match",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
            else{
                progressDialog.dismiss();
                Toast.makeText(Register.this,"Please accept all the terms and conditions",Toast.LENGTH_SHORT).show();
            }
        }
        return result;
    }
    private void sendEmailVerification(){
        final FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser!=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        Toast.makeText(Register.this, "successfully registered, verification mail sent", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Register.this,MainActivity.class));
                        progressDialog.dismiss();
                        firebaseAuth.signOut();
                        sendUserData();
                        finish();
                    }
                    else{
                        Toast.makeText(Register.this, "mail not sent", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                }
            });
        }
    }
    private void sendUserData(){
        firebaseDatabase=FirebaseDatabase.getInstance();
        myRef= firebaseDatabase.getReference("hi");
        String name=FirstName.getText().toString().trim();
        String Lname=LastName.getText().toString().trim();
        String emailiser=Encoder.encodeUserEmail(Email.getText().toString().trim());

        String mobno=Mobileno.getText().toString().trim();
        String FullName=name+" "+Lname;
        UserProfile profile=new UserProfile(FullName,emailiser,mobno);

        myRef.child(emailiser).setValue(profile);
    }
    @Override
    public void onBackPressed() {
            startActivity(new Intent(Register.this,MainActivity.class));
            finish();

    }
}
package com.example.android.userentry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Details extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    private TextView Email;
    private EditText name,Mobileno;
    private Button Logout,update,AllUSers;
    String username,useremail,usermobileNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        setup();
        ShowUserData();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        reference=FirebaseDatabase.getInstance().getReference("hi");


        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(Details.this,MainActivity.class));

            }
        });
        AllUSers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Details.this,allUsers.class));
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNameChanged()){
                    if (isMobileNoChanged()){
                        Toast.makeText(Details.this,"Data has been updated",Toast.LENGTH_SHORT).show();
                    }

                    else{
                        Toast.makeText(Details.this,"Data has been updated",Toast.LENGTH_SHORT).show();
                    }


                }
                else if (isMobileNoChanged()){
                    Toast.makeText(Details.this,"Data has been updated",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(Details.this,"nothing changed to update",Toast.LENGTH_SHORT).show();

                }
            }

            private boolean isNameChanged() {
                if (username.equals(name.getText().toString())){

                    return false;
                }
                else{
                    if (check()) {
                        username=name.getText().toString();
                        reference.child(useremail).child("name").setValue(name.getText().toString());
                        return true;
                    }
                    else{
                        return false;
                    }
                }
            }
            private boolean isMobileNoChanged(){
                if(usermobileNo.equals(Mobileno.getText().toString())){
                    return false;

                }
                else{
                    if(check()) {
                        usermobileNo=Mobileno.getText().toString();
                         reference.child(useremail).child("mobileNo").setValue(Mobileno.getText().toString());
                        return true;
                    }
                    else{
                        return false;
                    }

                }

            }
        });
    }
    public void setup(){
        name=(EditText) findViewById(R.id.userId);
        Email=(TextView)findViewById((R.id.enteredPassword));
        Mobileno=(EditText) findViewById(R.id.MobileNumber);
        Logout=(Button)findViewById(R.id.logout);
        update=(Button)findViewById(R.id.update);
        AllUSers=(Button)findViewById(R.id.AllUsers);
    }
    private void ShowUserData(){
        Intent i=getIntent();
        username=i.getStringExtra("name");
        useremail=i.getStringExtra("email");
        usermobileNo=i.getStringExtra("mobileNo");
        name.setText(username);
        Email.setText(Encoder.decodeUserEmail(useremail));
        Mobileno.setText(usermobileNo);
    }
    private Boolean check(){
        Boolean n=true;
        setup();
        String User_name=name.getText().toString().trim();
        String Userno=Mobileno.getText().toString().trim();
        if (User_name.isEmpty()||Userno.isEmpty()){
            Toast.makeText(Details.this,"Fields cannot be empty",Toast.LENGTH_SHORT).show();
            n=false;
            return n;
        }
        else{
            return n;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Details.this,MainActivity.class));
        firebaseAuth.signOut();
        finish();
    }
}
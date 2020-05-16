package com.example.loginapp;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private EditText Name;
    private EditText Password;
    private TextView Info;
    private Button Login;
    private TextView userRegistration;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private TextView forgotPassword;
    private int counter=5;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Name = (EditText)findViewById(R.id.etName);
        Password = (EditText)findViewById(R.id.etPassword);
        Info = (TextView)findViewById(R.id.tvInfo);
        Login = (Button)findViewById(R.id.btnLogin);
        userRegistration = (TextView)findViewById(R.id.tvRegister);
        forgotPassword = (TextView)findViewById(R.id.tvFogortPassword);

        Info.setText("No of attempts remaining: 5");
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser User = firebaseAuth.getCurrentUser();
        progressDialog = new ProgressDialog(this);

//        if(User != null){
//            finish();
//
//            startActivity(new Intent(MainActivity.this, RegistrationActivity.class));
//
//        }


        Login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View View){
               if (Name.getText().toString().equals("") || Password.getText().toString().equals("")){
                   Toast.makeText(MainActivity.this, "Username and Password are required", Toast.LENGTH_SHORT).show();
               }else{
                   validate(Name.getText().toString(), Password.getText().toString());
               }

            }


        });

        userRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegistrationActivity.class));
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PasswordActivity.class));
            }
        });

    }

    private void validate(String userName, String userPassword){

        progressDialog.setMessage("Loading!! Please wait.");
        progressDialog.show();


        firebaseAuth.signInWithEmailAndPassword(userName,
                userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    progressDialog.dismiss();
                    checkEmailVerification();
//                    Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                }else{

                    Toast.makeText(MainActivity.this, task.getException()+ "Login Failed", Toast.LENGTH_LONG).show();
                    counter--;

                    Info.setText("No of attempts remaining: " + counter);
                    progressDialog.dismiss();

                    if(counter==0){
                        Login.setEnabled(false);

                    }
                }

            }
        });

    }

    private void checkEmailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        Boolean emailFlag = firebaseUser.isEmailVerified();
         if(emailFlag){
             finish();
             startActivity(new Intent(MainActivity.this, SecondActivity.class));
         }else{
             Toast.makeText(this, "Verify your email", Toast.LENGTH_SHORT).show();

             firebaseAuth.signOut();
         }
    }

}

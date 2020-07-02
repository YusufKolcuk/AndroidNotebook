package com.yusufkolcuk.notdefterimpinsoft;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginPage extends AppCompatActivity {
    EditText loginMail,loginParola;
    Button loginButon;
    private FirebaseAuth mAuth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        loginButon=findViewById(R.id.loginButon);
        loginMail=findViewById(R.id.loginMail);
        loginParola=findViewById(R.id.loginParola);

        mAuth = FirebaseAuth.getInstance();

        loginButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mailText=loginMail.getText().toString();
                loginMail.setText("");
                String parolaText=loginParola.getText().toString();
                loginParola.setText("");
                if(!mailText.equals("")&&!parolaText.equals("")){
                    loginUser(mailText,parolaText);
                }
            }
        });
    }

    private void loginUser(String email, String parola){
        mAuth.signInWithEmailAndPassword(email,parola).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent=new Intent(LoginPage.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Kullanıcı Adı ve/veya şifre hatalı!!!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}

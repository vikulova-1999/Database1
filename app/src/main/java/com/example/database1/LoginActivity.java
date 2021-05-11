package com.example.database1;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText edLogin, edPassword;
    private FirebaseAuth mAuth;
    private Button bStart, bSignUp, bSignIn, bSignOut;
    private TextView tvUserEmail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        init();
    }
    //проверить, зарегестрирован ли уже пользователь в системе
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser cUser = mAuth.getCurrentUser();
        if(cUser != null)
        {
            showSigned();
            String userEmail = "Вы вошли как " + cUser.getEmail();
            tvUserEmail.setText(userEmail);
            Toast.makeText(this, "User not null", Toast.LENGTH_SHORT).show();
        }
        else
        {
            notSigned();
            Toast.makeText(this, "User null", Toast.LENGTH_SHORT).show();
        }
    }

    private void init()
    {
        bSignOut = findViewById(R.id.bSignOut);
        tvUserEmail = findViewById(R.id.tvUserEmail);
        bStart = findViewById(R.id.bStart);
        bSignIn = findViewById(R.id.bSignIn);
        bSignUp = findViewById(R.id.bSignUp);
        edLogin = findViewById(R.id.edLogin);
        edPassword = findViewById(R.id.edPassword);
        mAuth = FirebaseAuth.getInstance();
    }
    //кнопка регистрации
    public void onClickSignUp(View view)
    {
        //проверка, что поля Email и Password не пустые
        if(!TextUtils.isEmpty(edLogin.getText().toString()) && !TextUtils.isEmpty(edPassword.getText().toString()))
        {
            mAuth.createUserWithEmailAndPassword(edLogin.getText().toString(),edPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        showSigned();
                        sendEmailUser();
                        Toast.makeText(getApplicationContext(), "User SignUp Successful", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        notSigned();
                        Toast.makeText(getApplicationContext(), "User SignUp failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        else
        {
            Toast.makeText(getApplicationContext(), "Please entre Email and Password", Toast.LENGTH_SHORT).show();
        }
    }

    //кнопка входа
    public void onClickSignIn(View view)
    {
        if(!TextUtils.isEmpty(edLogin.getText().toString()) && !TextUtils.isEmpty(edPassword.getText().toString())){
            mAuth.signInWithEmailAndPassword(edLogin.getText().toString(),edPassword.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        showSigned();
                        Toast.makeText(getApplicationContext(), "User SignIn Successful", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        notSigned();
                        Toast.makeText(getApplicationContext(), "User SignIn failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    //кнопка "Выйти"
    public void onClickSignOut(View view)
    {
        FirebaseAuth.getInstance().signOut();
        //сделать элементы логина и пароля видимыми
        notSigned();
    }

    private void showSigned()
    {
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        if(user.isEmailVerified())
        {
            String userEmail = "Вы вошли как " + user.getEmail() + ". Хотите продолжить?";
            tvUserEmail.setText(userEmail);
        //сдлеть элементы логина и пароля невидимыми
        bStart.setVisibility(View.VISIBLE);
        bSignOut.setVisibility(View.VISIBLE);
        tvUserEmail.setVisibility(View.VISIBLE);
        edLogin.setVisibility(View.GONE);
        edPassword.setVisibility(View.GONE);
        bSignUp.setVisibility(View.GONE);
        bSignIn.setVisibility(View.GONE);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Проверьте Вашу почту для подтверждения регистрации", Toast.LENGTH_SHORT).show();

        }
    }

    private void notSigned()
    {
        //сделать элементы логина и пароля видимыми
        bStart.setVisibility(View.GONE);
        bSignOut.setVisibility(View.GONE);
        tvUserEmail.setVisibility(View.GONE);
        edLogin.setVisibility(View.VISIBLE);
        edPassword.setVisibility(View.VISIBLE);
        bSignUp.setVisibility(View.VISIBLE);
        bSignIn.setVisibility(View.VISIBLE);
    }

    //кнопка "Начать"
    public void onClickStart(View view)
    {
        Intent i = new Intent (LoginActivity.this, MainActivity.class);
        startActivity(i);
    }
    //отправка сообщения с подтверждением регистрации на почту
    private void sendEmailUser()
    {

        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(getApplicationContext(), "Проверьте Вашу почту для подтверждения регистрации", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Send Email failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
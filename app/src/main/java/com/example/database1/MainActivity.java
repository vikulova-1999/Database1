package com.example.database1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private EditText edName, edEmail, edPass;
    private DatabaseReference mDataBase;
    //User - таблица в БД, USER_KEY - переменная
    private String USER_KEY = "User";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //запускаем функцию
        init();
    }
    //инициализация всех объектов
    private void init() {
        edName = findViewById(R.id.edName);
        edEmail = findViewById(R.id.edEmail);
        edPass = findViewById(R.id.edPass);
        mDataBase = FirebaseDatabase.getInstance().getReference(USER_KEY);

    }

    //слушатель нажатия кнопки SAVE
    public void onClickSave(View view) {
        //БД автоматически вписывает id
        String id = mDataBase.getKey();
        String name = edName.getText().toString();
        String email = edEmail.getText().toString();
        String password = edPass.getText().toString();
        //пользователь вбивает данные в окошки, а вбитые данные передаются в БД
        User newUser = new User(id, name, email, password);
        //проверка, чтобы все поля содержали текст
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password))
        {
            //сохранение пользователя в БД
            mDataBase.push().setValue(newUser);
            Toast.makeText(this, " Сохранено!", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Пустое поле", Toast.LENGTH_SHORT).show();
        }

    }

    //слушатель нажатия кнопки READ
    public void onClickRead(View view){
        Intent i = new Intent(MainActivity.this, ReadActivity.class);
        startActivity(i);
    }
}
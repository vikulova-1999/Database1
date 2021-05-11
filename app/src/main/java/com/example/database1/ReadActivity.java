package com.example.database1;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReadActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter <String> adapter;
    private List<String> listData;
    private List<User> listTemp;
    private DatabaseReference mDataBase;

    //User - таблица в БД, USER_KEY - переменная
    private String USER_KEY = "User";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_layout);
        init();
        getDataFromDB();
        setOnClickItem();
    }

    //инициализация всех объектов
    private void init() {
        listView = findViewById(R.id.listView);
        listData = new ArrayList<>();
        listTemp = new ArrayList<>();
        //simple_list_item1 - передача сюда созданный listData
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        listView.setAdapter(adapter);
        mDataBase = FirebaseDatabase.getInstance().getReference(USER_KEY);
    }

    //функция считывания с БД и загружающая данные в listView
    private void getDataFromDB() {
        ValueEventListener vListener = new ValueEventListener() {
            //изменяется информация о БД
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //убедимся, что список чист
                if (listData.size() > 0) listData.clear();
                if(listTemp.size() > 0)listTemp.clear();

                for (DataSnapshot ds:snapshot.getChildren()) {
                    User user = ds.getValue(User.class);
                    //проверка, что лист не пустой
                    assert user != null;
                    //сохранение предыдущего пользователя
                    listData.add(user.name);
                    listTemp.add(user);
                }
                //оповещение адаптера, что данные в listData изменены и адаптер должен обновиться
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        mDataBase.addValueEventListener(vListener);
    }
    private void setOnClickItem()
    {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = listTemp.get(position);
                Intent i = new Intent(ReadActivity.this, ShowActivity.class);
                i.putExtra(Constant.USER_NAME,user.name);
                i.putExtra(Constant.USER_EMAIL,user.email);
                i.putExtra(Constant.USER_PASS,user.password);
                startActivity(i);
            }
        });
    }
}

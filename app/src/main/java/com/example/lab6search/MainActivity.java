package com.example.lab6search;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {


    // make a reference to buttons
    Button btn_add, btn_view;
    EditText et_name, et_age;
    ListView lv_StudentList;
    ArrayAdapter studentArrayAdapter;
    DataBaseHelper dataBaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        // on create, give value
        btn_add = findViewById(R.id.btn_add);
        btn_view = findViewById(R.id.btn_view);
        et_age=findViewById(R.id.et_age);
        et_name = findViewById(R.id.et_name);
        lv_StudentList = findViewById(R.id.lv_StudentList);

        dataBaseHelper = new DataBaseHelper(MainActivity.this);
        ShowStudentsOnListView(dataBaseHelper);


        //Listeners:

        btn_view.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v){
                dataBaseHelper = new DataBaseHelper(MainActivity.this);
                ShowStudentsOnListView(dataBaseHelper);

                //Toast.makeText(MainActivity.this, everyone.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        btn_add.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create model
                StudentMod studentMod;
                try {
                    studentMod = new StudentMod(-1, et_name.getText().toString(), Integer.parseInt(et_age.getText().toString()));
                    Toast.makeText(MainActivity.this, studentMod.toString(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Enter Valid input", Toast.LENGTH_SHORT).show();
                    studentMod = new StudentMod(-1, "ERROR", 0);
                }

                DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this);
                boolean b = dataBaseHelper.addOne(studentMod);
                Toast.makeText(MainActivity.this, "SUCCESS= "+ b, Toast.LENGTH_SHORT).show();

                ShowStudentsOnListView(dataBaseHelper);

            }
        });

        lv_StudentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                StudentMod ClickedStudent = (StudentMod) adapterView.getItemAtPosition(i);
                dataBaseHelper.DeleteOne(ClickedStudent);
                ShowStudentsOnListView(dataBaseHelper);
                Toast.makeText(MainActivity.this, "Deleted" + ClickedStudent.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ShowStudentsOnListView(DataBaseHelper dataBaseHelper) {
        studentArrayAdapter = new ArrayAdapter<StudentMod>(MainActivity.this, android.R.layout.simple_list_item_1, dataBaseHelper.getEveryone());
        lv_StudentList.setAdapter(studentArrayAdapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Type here to search");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // onQueryTextSubmit method will be called when user types some character(s) and presses Enter
            @Override
            public boolean onQueryTextSubmit(String s) {
                List<StudentMod> searchResult = dataBaseHelper.searchStudent(s);
                if(searchResult.isEmpty())
                {
                    Toast.makeText(MainActivity.this, "Student not found!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    studentArrayAdapter = new
                            ArrayAdapter<StudentMod>(MainActivity.this,
                            android.R.layout.simple_list_item_1, searchResult);
                    lv_StudentList.setAdapter(studentArrayAdapter);
                }
                return false;
            }
            // onQueryTextChange method will be called once user types a character
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        return true;
    }



    }
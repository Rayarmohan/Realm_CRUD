package com.example.realm_crud;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Realm realm;
    TextView show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        realm = Realm.getDefaultInstance();
        Button insert = findViewById(R.id.insert_btn);
        Button read = findViewById(R.id.read_btn);
        Button update = findViewById(R.id.update_btn);
        Button delete = findViewById(R.id.delete_btn);
         show = findViewById(R.id.textView);

        insert.setOnClickListener(MainActivity.this);
        read.setOnClickListener(MainActivity.this);
        update.setOnClickListener(MainActivity.this);
        delete.setOnClickListener(MainActivity.this);

    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.insert_btn)
        {
            ShowInsertDialog();
        }
        if(view.getId()==R.id.read_btn)
        {
            ShowData();
        }
        if(view.getId()==R.id.update_btn)
        {
            ShowUpdate();

        }
        if(view.getId()==R.id.delete_btn)
        {
          DeleteData();
        }

    }

    private void ShowUpdate() {
        AlertDialog.Builder al = new AlertDialog.Builder(MainActivity.this);
        View view = getLayoutInflater().inflate(R.layout.delete_dialog,null);
        al.setView(view);

        EditText dataid = view.findViewById(R.id.data_id_txt);
        Button update = view.findViewById(R.id.deletedata_btn);
        final AlertDialog alertDialog = al.show();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long id = Long.parseLong(dataid.getText().toString().trim());
                DataModal dataModal = realm.where(DataModal.class).equalTo("id",id).findFirst();
               alertDialog.dismiss();
               Updating(dataModal);
            }
        });
    }

    private void Updating(final DataModal dataModal) {
        AlertDialog.Builder al = new AlertDialog.Builder(MainActivity.this);
        View view = getLayoutInflater().inflate(R.layout.data_input_dialog,null);
        al.setView(view);




        EditText name = view.findViewById(R.id.name_txt);
        EditText age = view.findViewById(R.id.age_txt);
        Spinner gender = view.findViewById(R.id.spinner);
        Button save = view.findViewById(R.id.save_btn);
        final AlertDialog alertDialog = al.show();

        name.setText(dataModal.getName());
        age.setText(""+dataModal.getAge());
        if(dataModal.getGender().equalsIgnoreCase("Male"))
        {
            gender.setSelection(0);
        } else if (dataModal.getGender().equalsIgnoreCase("Female")) {
            gender.setSelection(1);
        }else {
            gender.setSelection(2);
        }


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();


                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        dataModal.setName(name.getText().toString().trim());
                        dataModal.setAge(Integer.parseInt(age.getText().toString().trim()));
                        dataModal.setGender(gender.getSelectedItem().toString().trim());

                        realm.copyToRealmOrUpdate(dataModal);
                    }
                });
            }
        });

    }

    private void DeleteData() {
        AlertDialog.Builder al = new AlertDialog.Builder(MainActivity.this);
        View view = getLayoutInflater().inflate(R.layout.delete_dialog,null);
        al.setView(view);

        EditText dataid = view.findViewById(R.id.data_id_txt);
        Button deletedata = view.findViewById(R.id.deletedata_btn);
        final AlertDialog alertDialog = al.show();

        deletedata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long id = Long.parseLong(dataid.getText().toString().trim());
                DataModal dataModal = realm.where(DataModal.class).equalTo("id",id).findFirst();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        alertDialog.dismiss();
                        dataModal.deleteFromRealm();
                    }
                });
            }
        });
    }

    private void ShowData() {
        List<DataModal> dataModalList = realm.where(DataModal.class).findAll();
        show.setText("");
        for(int i=0;i<dataModalList.size();i++)
        {
            show.append("ID: "+dataModalList.get(i).getId()+"Name: "+dataModalList.get(i).getName()+"Age:"+dataModalList.get(i).getAge()+"Gender: "+dataModalList.get(i).getGender()+"\n");
        }
    }

    private void ShowInsertDialog() {
        AlertDialog.Builder al = new AlertDialog.Builder(MainActivity.this);
        View view = getLayoutInflater().inflate(R.layout.data_input_dialog,null);
        al.setView(view);




        EditText name = view.findViewById(R.id.name_txt);
        EditText age = view.findViewById(R.id.age_txt);
        Spinner gender = view.findViewById(R.id.spinner);
         Button save = view.findViewById(R.id.save_btn);
        final AlertDialog alertDialog = al.show();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();

                Number current_id = realm.where(DataModal.class).max("id");
                long nextId;
                if(current_id==null)
                {
                    nextId = 1;
                }
                else
                {
                    nextId = current_id.intValue()+1;
                }
                DataModal dataModal = new DataModal();
                dataModal.setId(nextId);
                dataModal.setName(name.getText().toString().trim());
                dataModal.setAge(Integer.parseInt(age.getText().toString().trim()));
                dataModal.setGender(gender.getSelectedItem().toString().trim());

                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.copyToRealm(dataModal);
                    }
                });
            }
        });




    }
}
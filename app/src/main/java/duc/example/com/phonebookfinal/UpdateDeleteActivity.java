package duc.example.com.phonebookfinal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import duc.example.com.phonebookfinal.database.DatabaseHelper;
import duc.example.com.phonebookfinal.model.UserModel;



public class UpdateDeleteActivity extends AppCompatActivity {

    private UserModel userModel;
    private Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
    private EditText etName, etNumber;
    private Button btnupdate, btndelete;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete);

        Intent intent = getIntent();
        userModel = (UserModel) intent.getSerializableExtra("user");

        databaseHelper = new DatabaseHelper(this);

        etName = (EditText) findViewById(R.id.etName);
        etNumber = (EditText) findViewById(R.id.etNumber);
        btndelete = (Button) findViewById(R.id.btnDelete);
        btnupdate = (Button) findViewById(R.id.btnUpdate);
        spinner=(Spinner)findViewById(R.id.spin);
        adapter= ArrayAdapter.createFromResource(this,R.array.Groups,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);



        etName.setText(userModel.getName());
        etNumber.setText(userModel.getNumber());
        spinner.setSelection(0);

        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.updateUser(userModel.getId(),etName.getText().toString(), etNumber.getText().toString(),spinner.getSelectedItem().toString());
                Toast.makeText(UpdateDeleteActivity.this, "Updated Successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UpdateDeleteActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.deleteUSer(userModel.getId());
                Toast.makeText(UpdateDeleteActivity.this, "Deleted Successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UpdateDeleteActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }
}
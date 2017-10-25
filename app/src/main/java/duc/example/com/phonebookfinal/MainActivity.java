package duc.example.com.phonebookfinal;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;



import duc.example.com.phonebookfinal.database.DatabaseHelper;


public class MainActivity extends AppCompatActivity {
    private Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
    private Button btnStore, btnGetall, btnPdf;
    private EditText etName, etNumber;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);
        btnPdf = (Button) findViewById(R.id.btnpdf);
        btnStore = (Button) findViewById(R.id.btnstore);
        btnGetall = (Button) findViewById(R.id.btnget);
        etName = (EditText) findViewById(R.id.etName);
        etNumber = (EditText) findViewById(R.id.etNumber);
        spinner=(Spinner)findViewById(R.id.spin);
        adapter= ArrayAdapter.createFromResource(this,R.array.Groups,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        btnStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputOk()) {

                databaseHelper.addUser(etName.getText().toString(), etNumber.getText().toString(), spinner.getSelectedItem().toString());
                    etName.setText("");
                    etNumber.setText("");
                    spinner.setSelection(0);
                    Toast.makeText(MainActivity.this, "Stored Successfully!", Toast.LENGTH_SHORT).show();
            }
            }
        });

        btnGetall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GetAllUsersActivity.class);
                startActivity(intent);
            }
        });

        btnPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    createPdf();
                    viewPdf();
                }catch (FileNotFoundException e){
                 e.printStackTrace();
                }catch (DocumentException e){
                    e.printStackTrace();
                }
            }
        });

    }
    private void createPdf() throws FileNotFoundException, DocumentException {


        SQLiteDatabase db=databaseHelper.getWritableDatabase();

        Cursor c1=db.rawQuery("SELECT * FROM users", null);
        Cursor c2=db.rawQuery("SELECT * FROM groups",null);
        Document document=new Document();
        String outpout=Environment.getExternalStorageDirectory()+"/phonebook.pdf";


        PdfWriter.getInstance(document, new FileOutputStream(outpout));

        document.open();



        PdfPTable table=new PdfPTable(3);
        PdfPCell cell=new PdfPCell(new Paragraph("DATABASE REPORT"));
        cell.setColspan(4);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(BaseColor.GREEN);
        table.addCell(cell);

        table.addCell("Name");
        table.addCell("Number");
        table.addCell("Group");
        while(c1.moveToNext()&& c2.moveToNext()){
            String user=c1.getString(1);
            String number=c1.getString(2);
            String group=c2.getString(1);
            table.addCell(user);
            table.addCell(number);
            table.addCell(group);


        }
        document.add(table);
        Toast.makeText(this, "Subject saved", Toast.LENGTH_SHORT).show();
        document.addCreationDate();
        document.close();

    }
    public void viewPdf(){
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/phonebook.pdf");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);

    }







    private boolean inputOk() {
        if (etName.getText().toString().length() == 0) {
            Toast.makeText(this, "Name is not set", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etNumber.getText().toString().length() == 0) {
            Toast.makeText(this, "Number is not set", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}
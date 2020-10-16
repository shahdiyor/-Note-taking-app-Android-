package com.example.mynotes.activities;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.mynotes.DB.notesDB;
import com.example.mynotes.R;
import com.example.mynotes.entities.Notes;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
public class CreaeteNoteActivity extends AppCompatActivity {

    private Notes alreadyAvailable;
    private EditText notetitle,subinp,noteinp;
    private TextView DateTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creaete_note);
        ImageView imgBack = findViewById(R.id.imgback);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                onBackPressed();
            }
        });

        notetitle = findViewById(R.id.notetitle);
        subinp = findViewById(R.id.subinp);
        noteinp = findViewById(R.id.noteinp);
        DateTime = findViewById(R.id.DateTime);
        DateTime.setText(
                new SimpleDateFormat("EEEE, dd MMMM yyyy, HH:mm ", Locale.getDefault())
                        .format(new Date())
        );

        ImageView imgdone = findViewById(R.id.imgdone);
        imgdone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });
        if(getIntent().getBooleanExtra("ViewOrUpdate",false)){
            alreadyAvailable=(Notes) getIntent().getSerializableExtra("note");
            ViewOrUpdate();
        }
        ImageView imgdel =findViewById(R.id.imgdel);
        if(alreadyAvailable!=null){
            imgdel.setVisibility(View.VISIBLE);
            imgdel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteNote();
                }
            });
        }
    }

    private void deleteNote(){
                closeKeyboard();
                @SuppressLint("StaticFieldLeak")
                class DeleteNote extends AsyncTask<Void,Void,Void>{
                    @Override
                    protected Void doInBackground(Void... voids) {
                        notesDB.getDatabase(getApplicationContext()).dao().deleteNote(alreadyAvailable);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        Intent intent=new Intent();
                        intent.putExtra("isNoteDeleted",true);
                        setResult(RESULT_OK,intent);
                        onBackPressed();
                        finish();
                    }
                }
                new DeleteNote().execute();
            }


    private void ViewOrUpdate(){
        notetitle.setText(alreadyAvailable.getTitle());
        subinp.setText(alreadyAvailable.getSub());
        noteinp.setText(alreadyAvailable.getTxt());
        DateTime.setText(alreadyAvailable.getDateTime());


    }
        private void saveNote() {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            if(notetitle.getText().toString().trim().isEmpty()){
                Toast.makeText(this,"Note title can't be empty",Toast.LENGTH_SHORT).show();
                        return;

            }else if(subinp.getText().toString().trim().isEmpty()
            && noteinp.getText().toString().trim().isEmpty()){
                Toast.makeText(this,"Note can't be empty",Toast.LENGTH_SHORT).show();
                return;
            }

            final Notes note=new Notes();
            note.setTitle(notetitle.getText().toString());
            note.setSub(subinp.getText().toString());
            note.setTxt(noteinp.getText().toString());
            note.setDateTime(DateTime.getText().toString());

            if(alreadyAvailable!=null){
                note.setId(alreadyAvailable.getId());
            }
            @SuppressLint("StaticFieldLeak")
            class SaveNote extends AsyncTask<Void,Void,Void>{
                @Override
                protected Void doInBackground(Void... voids) {

                    notesDB.getDatabase(getApplicationContext()).dao().insertNote(note);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    Intent intent = new Intent();
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
            new SaveNote().execute();
            closeKeyboard();
        }
        private void closeKeyboard(){
         View view = this.getCurrentFocus();
         if(view!= null){
             InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
             imm.hideSoftInputFromWindow(view.getWindowToken(),0);
         }
    }
}

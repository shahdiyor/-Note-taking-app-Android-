package com.example.mynotes.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.mynotes.DB.notesDB;
import com.example.mynotes.R;
import com.example.mynotes.adapters.notesAdapter;
import com.example.mynotes.entities.Notes;
import com.example.mynotes.listeners.noteslistener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements noteslistener {
    public static final int REQUEST_ADD=1;
    public static final int REQUEST_UPDATE=2;
    public static final int REQUEST_SHOW=3;
    private RecyclerView notesView;
    private List<Notes> notelist;
    private notesAdapter notesAdapter;
    private int noteClickedpos=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView imageAdd=findViewById(R.id.imgadd);
        imageAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        new Intent(getApplicationContext(),CreaeteNoteActivity.class),REQUEST_ADD
                );
            }
        });
        notesView=findViewById(R.id.notesView);
        notesView.setLayoutManager(
                new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        );
        notelist=new ArrayList<>();
        notesAdapter=new notesAdapter(notelist,this);
        notesView.setAdapter(notesAdapter);
        getNotes(REQUEST_SHOW, false );
    }

    @Override
    public void onNoteClick(Notes note, int position) {
        noteClickedpos=position;
        Intent intent = new Intent(getApplicationContext(),CreaeteNoteActivity.class);
        intent.putExtra("ViewOrUpdate",true);
        intent.putExtra("note",note);
        startActivityForResult(intent,REQUEST_UPDATE);
    }

    private void getNotes(final int requestCode, final boolean isNoteDeleted){
        @SuppressLint("StaticFieldLeak")
        class Get extends AsyncTask<Void,Void, List<Notes>>{
            @Override
            protected List<Notes> doInBackground(Void... voids) {
                return notesDB.getDatabase(getApplicationContext()).dao().getAllNotes();
            }

            @Override
            protected void onPostExecute(List<Notes> notes) {
                super.onPostExecute(notes);
               if(requestCode==REQUEST_SHOW){
                   notelist.addAll(notes);
                   notesAdapter.notifyDataSetChanged();
               }else if(requestCode==REQUEST_ADD){
                   notelist.add(0,notes.get(0));
                   notesAdapter.notifyItemInserted(0);
                   notesView.smoothScrollToPosition(0);
               }else if(requestCode==REQUEST_UPDATE){
                   notelist.remove(noteClickedpos);

                   if(isNoteDeleted){
                       notesAdapter.notifyItemRemoved(noteClickedpos);
                   }else{
                       notelist.add(noteClickedpos,notes.get(noteClickedpos));
                       notesAdapter.notifyItemChanged(noteClickedpos);
                   }
               }
            }
        }
        new Get().execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_ADD&&resultCode==RESULT_OK){
            getNotes(REQUEST_ADD,false);
        }else if (requestCode==REQUEST_UPDATE&&resultCode==RESULT_OK){
            if(data !=null){
                getNotes(REQUEST_UPDATE,data.getBooleanExtra("isNoteDeleted",false));
            }
        }
    }
}
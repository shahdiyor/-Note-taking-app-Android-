package com.example.mynotes.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mynotes.R;
import com.example.mynotes.entities.Notes;
import com.example.mynotes.listeners.noteslistener;

import java.util.List;

public class notesAdapter extends RecyclerView.Adapter<notesAdapter.NoteViewHolder>{

    private List<Notes> notes;
    private noteslistener Noteslistener;

    public notesAdapter(List<Notes> notes,noteslistener Noteslistener) {
        this.notes = notes;
        this.Noteslistener=Noteslistener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.cont_note,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.setNote(notes.get(position));
        holder.layoutNote.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Noteslistener.onNoteClick(notes.get(position),position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder{
        TextView title,sub,date;
        LinearLayout layoutNote;
         NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title);
            sub = itemView.findViewById(R.id.sub);
            date=itemView.findViewById(R.id.date);
            layoutNote=itemView.findViewById(R.id.cont);
        }
        void setNote (Notes note){
             title.setText(note.getTitle());
             if(note.getSub().trim().isEmpty()) {
                 sub.setVisibility(View.GONE);
             }else{
                 sub.setText(note.getSub());
             }
             date.setText(note.getDateTime());

        }
    }
}

package com.mobile.architecture.architectureexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mobile.architecture.architectureexample.model.Note;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private static final int ADD_REQUEST_CODE = 1;
    private static final int UPDATE_REQUEST_CODE = 2;
    private NoteViewModel noteViewModel;
    RecyclerView rvNotes;
    NotesAdapter adapter;
    List<Note> notes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvNotes = findViewById(R.id.rv_notes);
        notes = new ArrayList<>();
        adapter = new NotesAdapter();
        rvNotes.setLayoutManager(new LinearLayoutManager(this));
        rvNotes.setHasFixedSize(true);
        rvNotes.setAdapter(adapter);
        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                //called when data changed so you can update ui or anything...
                adapter.submitList(notes);
            }
        });

        FloatingActionButton fabAddNote = findViewById(R.id.fab_add_note);
        fabAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
                startActivityForResult(intent, ADD_REQUEST_CODE);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT |
                ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                noteViewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Note deleted.!", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(rvNotes);

        adapter.setOnItemClickListener(new NotesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                Intent intent =new Intent(MainActivity.this, AddEditNoteActivity.class);
                intent.putExtra(AddEditNoteActivity.EXTRA_ID,note.getId());
                intent.putExtra(AddEditNoteActivity.EXTRA_TITLE,note.getTitle());
                intent.putExtra(AddEditNoteActivity.EXTRA_DESCRIPTION,note.getDescription());
                intent.putExtra(AddEditNoteActivity.EXTRA_PRIORITY,note.getPriority());
                startActivityForResult(intent,UPDATE_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == ADD_REQUEST_CODE && data != null) {
            String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 1);
            noteViewModel.insert(new Note(title, description, priority));
            Toast.makeText(this, "Note Saved.!", Toast.LENGTH_SHORT).show();
        }
        else if (resultCode == RESULT_OK && requestCode == UPDATE_REQUEST_CODE && data != null){
            int id =data.getIntExtra(AddEditNoteActivity.EXTRA_ID,-1);
            if (id == -1){
                Toast.makeText(this, "Note can't be updated.!", Toast.LENGTH_SHORT).show();
            }
            else {
                String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
                String description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
                int priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 1);
                Note note =new Note(title, description, priority);
                note.setId(id);
                noteViewModel.update(note);
                Toast.makeText(this, "Note Updated.!", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "Saving Cancelled.!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.acton_delete_all_notes:
                noteViewModel.deleteAllNotes();
                Toast.makeText(this, "All Notes Delete.!", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
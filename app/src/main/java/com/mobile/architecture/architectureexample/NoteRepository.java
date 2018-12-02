package com.mobile.architecture.architectureexample;

import android.app.Application;
import android.os.AsyncTask;

import com.mobile.architecture.architectureexample.model.Note;

import java.util.List;

import androidx.lifecycle.LiveData;

public class NoteRepository {

    private NoteDao noteDao;
    private LiveData<List<Note>> allNotes;

    public NoteRepository(Application application){
        NoteDatabase database =NoteDatabase.getInstance(application);
        noteDao =database.noteDao();
        allNotes =noteDao.getAllNotes();
    }

    public void insert(Note note){
        new InsertNoteAsyncTask(noteDao).execute(note);
    }

    public void update(Note note){
        new UpdateNoteAsyncTask(noteDao).execute(note);
    }

    public void delete(Note note){
        new DeleteNoteAsyncTask(noteDao).execute(note);
    }

    public LiveData<List<Note>> getAllNotes(){
        return allNotes;
    }

    public void deleteAllNotes(){
        new DeleteAllNotesAsyncTask(noteDao).execute();
    }

    private static class InsertNoteAsyncTask extends AsyncTask<Note,Void,Void>{
        NoteDao noteDao;

        InsertNoteAsyncTask(NoteDao noteDao){
            this.noteDao =noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.insert(notes[0]);
            return null;
        }
    }

    private static class UpdateNoteAsyncTask extends AsyncTask<Note,Void,Void>{
        NoteDao noteDao;

        UpdateNoteAsyncTask(NoteDao noteDao){
            this.noteDao =noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.update(notes[0]);
            return null;
        }
    }

    private static class DeleteNoteAsyncTask extends AsyncTask<Note,Void,Void>{
        NoteDao noteDao;

        DeleteNoteAsyncTask(NoteDao noteDao){
            this.noteDao =noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.delete(notes[0]);
            return null;
        }
    }

    private static class DeleteAllNotesAsyncTask extends AsyncTask<Void,Void,Void>{
        NoteDao noteDao;

        DeleteAllNotesAsyncTask(NoteDao noteDao){
            this.noteDao =noteDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.deleteAllNotes();
            return null;
        }
    }
}

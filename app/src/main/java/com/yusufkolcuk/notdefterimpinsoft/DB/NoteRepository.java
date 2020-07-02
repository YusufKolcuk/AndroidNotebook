package com.yusufkolcuk.notdefterimpinsoft.DB;


import android.arch.lifecycle.LiveData;
import android.content.Context;


import com.yusufkolcuk.notdefterimpinsoft.Task.Delete;
import com.yusufkolcuk.notdefterimpinsoft.Task.Insert;
import com.yusufkolcuk.notdefterimpinsoft.Task.Update;
import com.yusufkolcuk.notdefterimpinsoft.Model.Note;

import java.util.List;

public class NoteRepository {

    private NoteDatabase mNoteDatabase;

    public NoteRepository(Context context) {
        mNoteDatabase = NoteDatabase.getInstance(context);
    }

    public void insertNoteTask(Note note){
        new Insert(mNoteDatabase.getNoteDao()).execute(note);
    }

    public void updateNoteTask(Note note){
        new Update(mNoteDatabase.getNoteDao()).execute(note);
    }

    public LiveData<List<Note>> retrieveNotesTask() {
        return mNoteDatabase.getNoteDao().getNotes();
    }

    public void deleteNoteTask(Note note){
        new Delete(mNoteDatabase.getNoteDao()).execute(note);
    }
}














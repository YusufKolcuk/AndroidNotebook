package com.yusufkolcuk.notdefterimpinsoft.Task;

import android.os.AsyncTask;

import com.yusufkolcuk.notdefterimpinsoft.Model.Note;
import com.yusufkolcuk.notdefterimpinsoft.DB.NoteDao;

public class Insert extends AsyncTask<Note, Void, Void> {

    private NoteDao mNoteDao;

    public Insert(NoteDao dao) {
        mNoteDao = dao;
    }

    @Override
    protected Void doInBackground(Note... notes) {
        mNoteDao.insertNotes(notes);
        return null;
    }

}
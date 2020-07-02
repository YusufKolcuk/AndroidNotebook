package com.yusufkolcuk.notdefterimpinsoft.Task;

import android.os.AsyncTask;


import com.yusufkolcuk.notdefterimpinsoft.Model.Note;
import com.yusufkolcuk.notdefterimpinsoft.DB.NoteDao;

public class Delete extends AsyncTask<Note, Void, Void> {

    private NoteDao mNoteDao;

    public Delete(NoteDao dao) {
        mNoteDao = dao;
    }

    @Override
    protected Void doInBackground(Note... notes) {
        mNoteDao.delete(notes);
        return null;
    }

}
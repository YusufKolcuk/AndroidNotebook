package com.yusufkolcuk.notdefterimpinsoft.Task;


import android.os.AsyncTask;

import com.yusufkolcuk.notdefterimpinsoft.Model.Note;
import com.yusufkolcuk.notdefterimpinsoft.DB.NoteDao;

public class Update extends AsyncTask<Note, Void, Void> {

    private NoteDao mNoteDao;

    public Update(NoteDao dao) {
        mNoteDao = dao;
    }

    @Override
    protected Void doInBackground(Note... notes) {
        mNoteDao.updateNotes(notes);
        return null;
    }

}
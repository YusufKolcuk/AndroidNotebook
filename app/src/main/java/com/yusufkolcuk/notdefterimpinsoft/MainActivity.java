package com.yusufkolcuk.notdefterimpinsoft;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.yusufkolcuk.notdefterimpinsoft.Adapter.NoteAdapter;
import com.yusufkolcuk.notdefterimpinsoft.Model.Note;
import com.yusufkolcuk.notdefterimpinsoft.DB.NoteRepository;
import com.yusufkolcuk.notdefterimpinsoft.Oth.Decorator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        NoteAdapter.OnNoteListener,
        FloatingActionButton.OnClickListener
{

    private static final String TAG = "MainActivity";

    // Recycler View
    private RecyclerView mRecyclerView;

    // List-Adapter
    private ArrayList<Note> mNotes = new ArrayList<>();
    private NoteAdapter mNoteRecyclerAdapter;
    private NoteRepository mNoteRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.recyclerView);



        initRecyclerView();


        mNoteRepository = new NoteRepository(this);
        retrieveNotes();

        setSupportActionBar((Toolbar)findViewById(R.id.notes_toolbar));
    }


    private void retrieveNotes() {
        mNoteRepository.retrieveNotesTask().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                if(mNotes.size() > 0){
                    mNotes.clear();
                }
                if(notes != null){
                    mNotes.addAll(notes);
                }
                mNoteRecyclerAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        boolean sonuc=true;
        Intent intent;
        switch (id){


            case R.id.menu_yeniplan:
                intent = new Intent(getApplicationContext(), NoteActivity.class);
                startActivity(intent);
                //AddPlan();
                break;

            case R.id.logout:
                intent=new Intent(getApplicationContext(),LoginPage.class);
                startActivity(intent);
                finish();
                break;
        }
        return sonuc;
    }


    private void initRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        Decorator itemDecorator = new Decorator(10);
        mRecyclerView.addItemDecoration(itemDecorator);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);
        mNoteRecyclerAdapter = new NoteAdapter(mNotes, this);
        mRecyclerView.setAdapter(mNoteRecyclerAdapter);
    }


    @Override
    public void onNoteClick(int position) {
        Intent intent = new Intent(this, NoteActivity.class);
        intent.putExtra("selected_note", mNotes.get(position));
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getApplicationContext(), NoteActivity.class);
        startActivity(intent);
    }

    private void deleteNote(Note note) {
        mNotes.remove(note);
        mNoteRecyclerAdapter.notifyDataSetChanged();

        mNoteRepository.deleteNoteTask(note);
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            deleteNote(mNotes.get(viewHolder.getAdapterPosition()));
        }
    };
}



















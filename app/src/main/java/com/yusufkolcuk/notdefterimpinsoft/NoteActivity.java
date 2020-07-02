package com.yusufkolcuk.notdefterimpinsoft;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yusufkolcuk.notdefterimpinsoft.Model.FNote;
import com.yusufkolcuk.notdefterimpinsoft.Model.Note;
import com.yusufkolcuk.notdefterimpinsoft.DB.NoteRepository;
import com.yusufkolcuk.notdefterimpinsoft.Oth.LineList;
import com.yusufkolcuk.notdefterimpinsoft.Oth.Utility;

import java.text.DateFormat;
import java.util.Date;

public class NoteActivity extends AppCompatActivity implements
        View.OnTouchListener,
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener,
        View.OnClickListener,
        TextWatcher
{

    private static final String TAG = "NoteActivity";
    private static final int EDIT_MODE_ENABLED = 1;
    private static final int EDIT_MODE_DISABLED = 0;

    //Firebase

    DatabaseReference mDatabase;
    FirebaseDatabase database;

    private boolean mIsNewNote;
    private Note mNoteInitial;
    private GestureDetector mGestureDetector;
    private int mMode;
    private NoteRepository mNoteRepository;
    private Note mNoteFinal;

    private LineList mLineList;
    private EditText mEditTitle;
    private TextView mViewTitle;
    private RelativeLayout mCheckContainer, mBackArrowContainer;
    private ImageButton mCheck, mBackArrow ;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        mLineList = findViewById(R.id.note_text);
        mEditTitle = findViewById(R.id.note_edit_title);
        mViewTitle = findViewById(R.id.note_text_title);
        mCheck = findViewById(R.id.toolbar_check);
        mBackArrow = findViewById(R.id.toolbar_back_arrow);
        mCheckContainer = findViewById(R.id.check_container);
        mBackArrowContainer = findViewById(R.id.back_arrow_container);





        mNoteRepository = new NoteRepository(this);

        setListeners();

        if(getIncomingIntent()){
            setNewNoteProperties();
            enableEditMode();
        }
        else{
            setNoteProperties();
            disableContentInteraction();
        }
        //Firebase
        mDatabase=database.getInstance().getReference().child("All Note");

        /*
        trial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseTransactions();
            }
        });
        */

    }


    private void saveChanges(){
        if(mIsNewNote){
            saveNewNote();
        }else{
            updateNote();
        }
    }

    public void updateNote() {
        mNoteRepository.updateNoteTask(mNoteFinal);
    }

    public void saveNewNote() {
        mNoteRepository.insertNoteTask(mNoteFinal);
    }

    private void setListeners(){
        mGestureDetector = new GestureDetector(this, this);
        mLineList.setOnTouchListener(this);
        mCheck.setOnClickListener(this);
        mViewTitle.setOnClickListener(this);
        mBackArrow.setOnClickListener(this);
        mEditTitle.addTextChangedListener(this);
    }

    private boolean getIncomingIntent(){
        if(getIntent().hasExtra("selected_note")){
            mNoteInitial = getIntent().getParcelableExtra("selected_note");

            mNoteFinal = new Note();
            mNoteFinal.setTitle(mNoteInitial.getTitle());
            mNoteFinal.setContent(mNoteInitial.getContent());
            mNoteFinal.setTimestamp(mNoteInitial.getTimestamp());
            mNoteFinal.setId(mNoteInitial.getId());

            mMode = EDIT_MODE_ENABLED;
            mIsNewNote = false;
            return false;
        }
        mMode = EDIT_MODE_ENABLED;
        mIsNewNote = true;
        return true;
    }

    private void disableContentInteraction(){
        mLineList.setKeyListener(null);
        mLineList.setFocusable(false);
        mLineList.setFocusableInTouchMode(false);
        mLineList.setCursorVisible(false);
        mLineList.clearFocus();
    }

    private void enableContentInteraction(){
        mLineList.setKeyListener(new EditText(this).getKeyListener());
        mLineList.setFocusable(true);
        mLineList.setFocusableInTouchMode(true);
        mLineList.setCursorVisible(true);
        mLineList.requestFocus();
    }

    private void enableEditMode(){
        mBackArrowContainer.setVisibility(View.GONE);
        mCheckContainer.setVisibility(View.VISIBLE);

        mViewTitle.setVisibility(View.GONE);
        mEditTitle.setVisibility(View.VISIBLE);

        mMode = EDIT_MODE_ENABLED;

        enableContentInteraction();
    }

    private void disableEditMode(){
        Log.d(TAG, "disableEditMode");
        mBackArrowContainer.setVisibility(View.VISIBLE);
        mCheckContainer.setVisibility(View.GONE);

        mViewTitle.setVisibility(View.VISIBLE);
        mEditTitle.setVisibility(View.GONE);

        mMode = EDIT_MODE_DISABLED;

        disableContentInteraction();

        String temp = mLineList.getText().toString();
        temp = temp.replace("\n", "");
        temp = temp.replace(" ", "");
        if(temp.length() > 0){
            mNoteFinal.setTitle(mEditTitle.getText().toString());
            mNoteFinal.setContent(mLineList.getText().toString());
            String timestamp = Utility.getCurrentTimeStamp();
            mNoteFinal.setTimestamp(timestamp);

            Log.d(TAG, "disableEditModeInitial: " + mNoteInitial.toString());
            Log.d(TAG, "disableEditModeFinal: " + mNoteFinal.toString());

            if(!mNoteFinal.getContent().equals(mNoteInitial.getContent())
                    || !mNoteFinal.getTitle().equals(mNoteInitial.getTitle())){
                Log.d(TAG, "disableEditModeCalled?");
                saveChanges();
            }
        }
    }

    public void FirebaseTransactions(){

        String mTitle=mViewTitle.getText().toString().trim();

        String mNote= mLineList.getText().toString().trim();

        String mDate=DateFormat.getDateInstance().format(new Date());

        String id=mDatabase.push().getKey();

        FNote fNote=new FNote(mTitle,mNote,id,mDate);

        mDatabase.child(id).setValue(fNote);

        Toast.makeText(getApplicationContext(),"Data Inserted",Toast.LENGTH_LONG).show();

        Log.d("FirebaseTrs",mTitle+mDate+mNote);

    }

    private void setNewNoteProperties(){
        mViewTitle.setText("Not Başlığı");
        mEditTitle.setText("Not Başlığı");

        mNoteFinal = new Note();
        mNoteInitial = new Note();
        mNoteInitial.setTitle("Not Başlığı");
    }

    private void setNoteProperties(){
        mViewTitle.setText(mNoteInitial.getTitle());
        mEditTitle.setText(mNoteInitial.getTitle());
        mLineList.setText(mNoteInitial.getContent());
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return mGestureDetector.onTouchEvent(motionEvent);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        Log.d(TAG, "onDoubleTap");
        enableEditMode();
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.toolbar_back_arrow:{
                finish();
                break;
            }
            case R.id.toolbar_check:{
                disableEditMode();

                //Firebase Trial
                FirebaseTransactions();
                Log.d("clickTool","click");

                break;
            }

            case R.id.note_text_title:{
                enableEditMode();
                // Log.d("clickTitle","click");
                //mViewTitle.setText("");
                mEditTitle.requestFocus();
                mEditTitle.setSelection(mEditTitle.length());
                break;
            }
        }
    }
/*
    @Override
    public void onBackPressed() {
        if(mMode == EDIT_MODE_ENABLED){
            onClick(mCheck);
        }
        else{
            super.onBackPressed();
        }
    }
*/

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("mode", mMode);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mMode = savedInstanceState.getInt("mode");
        if(mMode == EDIT_MODE_ENABLED){
            enableEditMode();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        mViewTitle.setText(charSequence.toString());
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}




















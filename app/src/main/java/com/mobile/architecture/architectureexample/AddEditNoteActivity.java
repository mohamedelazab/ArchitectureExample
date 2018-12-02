package com.mobile.architecture.architectureexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddEditNoteActivity extends AppCompatActivity {

    public static final String EXTRA_ID="com.mobile.architecture.architectureexample.EXTRA_ID";
    public static final String EXTRA_TITLE="com.mobile.architecture.architectureexample.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION="com.mobile.architecture.architectureexample.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY="com.mobile.architecture.architectureexample.EXTRA_PRIORITY";

    private EditText etNoteTitle, etNoteDescription;
    private NumberPicker numberPickerPriority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        etNoteTitle = findViewById(R.id.et_note_title);
        etNoteDescription = findViewById(R.id.et_note_description);
        numberPickerPriority = findViewById(R.id.priority_num_picker);
        numberPickerPriority.setMinValue(1);
        numberPickerPriority.setMaxValue(10);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        Intent intent =getIntent();
        if (intent.hasExtra(EXTRA_ID)){
            setTitle("Update Note");
            etNoteTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            etNoteDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            numberPickerPriority.setValue(intent.getIntExtra(EXTRA_PRIORITY,1));
        }
        else {
            setTitle("Add Note");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveNote() {
        String title =etNoteTitle.getText().toString().trim();
        String description =etNoteDescription.getText().toString().trim();
        int priority =numberPickerPriority.getValue();
        if (title.isEmpty() || description.isEmpty()){
            Toast.makeText(this, "Please insert the title and description..", Toast.LENGTH_SHORT).show();
        }
        else {
            Intent dataIntent =new Intent();
            dataIntent.putExtra(EXTRA_TITLE, title);
            dataIntent.putExtra(EXTRA_DESCRIPTION, description);
            dataIntent.putExtra(EXTRA_PRIORITY, priority);

            int id =getIntent().getIntExtra(EXTRA_ID,-1);
            if (id != -1){
                dataIntent.putExtra(EXTRA_ID,id);
            }
            setResult(RESULT_OK,dataIntent);
            finish();
        }
    }
}

package com.artemkinko.lab4_8.ui.write;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.artemkinko.lab4_8.databinding.FragmentWriteBinding;
import com.artemkinko.lab4_8.db.Note;
import com.artemkinko.lab4_8.db.NoteDAO;
import com.artemkinko.lab4_8.db.NoteDB;

import java.util.List;

public class WriteFragment extends Fragment {

    private FragmentWriteBinding binding;

    public NoteDB noteDB;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        WriteViewModel writeViewModel =
                new ViewModelProvider(this).get(WriteViewModel.class);

        binding = FragmentWriteBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // logic
        binding.buttonWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.titleInput.getText().toString().equals("") &&
                        !binding.textInput.getText().toString().equals("")) {
                    new DBRequest().execute(binding.titleInput.getText().toString(), binding.textInput.getText().toString());
                }
                else {
                    Toast.makeText(getActivity().getApplicationContext(), "Заполните все поля!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
    }

    private class DBRequest extends AsyncTask<String, Void, Void> {

        List<Note> notes;

        @Override
        protected Void doInBackground(String... strings) {
            noteDB = NoteDB.getInstance(getContext());
            NoteDAO noteDAO = noteDB.getNoteDAO();
            Note note = new Note();
            note.title = strings[0];
            note.text = strings[1];
            note.isphoto = "no";
            noteDAO.insertAll(note);
            //notes = noteDAO.getAll();
            //noteDAO.deleteAll();
            //notes = noteDAO.getAll();
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            Toast.makeText(getActivity().getApplicationContext(), "Заметка успешно создана!", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

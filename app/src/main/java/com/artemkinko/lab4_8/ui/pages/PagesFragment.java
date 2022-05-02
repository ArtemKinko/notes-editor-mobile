package com.artemkinko.lab4_8.ui.pages;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import com.artemkinko.lab4_8.R;
import com.artemkinko.lab4_8.databinding.FragmentPagesBinding;
import com.artemkinko.lab4_8.databinding.FragmentWeatherBinding;
import com.artemkinko.lab4_8.db.Note;
import com.artemkinko.lab4_8.db.NoteDAO;
import com.artemkinko.lab4_8.db.NoteDB;
import com.artemkinko.lab4_8.ui.weather.WeatherViewModel;

import java.util.List;

public class PagesFragment extends Fragment {

    private FragmentPagesBinding binding;

    public NoteDB noteDB;

    LinearLayout pagesLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PagesViewModel pagesViewModel =
                new ViewModelProvider(this).get(PagesViewModel.class);

        binding = FragmentPagesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        pagesLayout = binding.pagesLayout;

        // logic
        new DBRequest().execute("get");


//        LayoutInflater infl = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
//        View childLayout = infl.inflate(R.layout.card, pagesLayout, false);
//        View childLayout2 = infl.inflate(R.layout.card, pagesLayout, false);
//        View childLayout3 = infl.inflate(R.layout.card, pagesLayout, false);
//        View childLayout4 = infl.inflate(R.layout.card, pagesLayout, false);
//        View childLayout5 = infl.inflate(R.layout.card, pagesLayout, false);
//        View childLayout6 = infl.inflate(R.layout.card, pagesLayout, false);
//        View childLayout7 = infl.inflate(R.layout.card, pagesLayout, false);
//
//        pagesLayout.addView(childLayout);
//        pagesLayout.addView(childLayout2);
//        pagesLayout.addView(childLayout3);
//        pagesLayout.addView(childLayout4);
//        pagesLayout.addView(childLayout5);
//        pagesLayout.addView(childLayout6);
//        pagesLayout.addView(childLayout7);
        //
        return root;
    }


    private class DBRequest extends AsyncTask <String, Void, Void> {

        List<Note> notes;

        @Override
        protected Void doInBackground(String... strings) {
            noteDB = NoteDB.getInstance(getContext());
            NoteDAO noteDAO = noteDB.getNoteDAO();
            notes = noteDAO.getAll();
            //noteDAO.deleteAll();
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            for (Note note: notes) {
                if (note.isphoto.equals("no")) {
                    LayoutInflater infl = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                    View childLayout = infl.inflate(R.layout.card, pagesLayout, false);

                    // ставим заголовок из поля
                    TextView title = (TextView) childLayout.findViewById(R.id.card_title);
                    title.setText(note.title);

                    // ставим текст из поля
                    TextView text = (TextView) childLayout.findViewById(R.id.card_text);
                    text.setText(note.text);

                    // добавляем слушателя на кнопку закрытия
                    Button button = (Button) childLayout.findViewById(R.id.card_button);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            pagesLayout.removeView(childLayout);
                            new DBDelete().execute(note.title, note.text);
                        }
                    });

                    pagesLayout.addView(childLayout);
                }
                else {
                    LayoutInflater infl = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                    View childLayout = infl.inflate(R.layout.photo_card, pagesLayout, false);

                    ImageView imageView = (ImageView) childLayout.findViewById(R.id.imageView);
                    imageView.setImageBitmap(BitmapFactory.decodeFile(note.text));

                    // добавляем слушателя на кнопку закрытия
                    Button button = (Button) childLayout.findViewById(R.id.card_button);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            pagesLayout.removeView(childLayout);
                            new DBDelete().execute(note.title, note.text);
                        }
                    });

                    pagesLayout.addView(childLayout);
                }
            }
            if (notes.isEmpty())
                binding.textEmpty.setVisibility(View.VISIBLE);
        }
    }

    private class DBDelete extends AsyncTask <String, Void, Void> {

        List<Note> notes;

        @Override
        protected Void doInBackground(String... strings) {
            noteDB = NoteDB.getInstance(getContext());
            NoteDAO noteDAO = noteDB.getNoteDAO();
            noteDAO.deleteByTitleAndText(strings[0], strings[1]);
            notes = noteDAO.getAll();
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            if (notes.isEmpty())
                binding.textEmpty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

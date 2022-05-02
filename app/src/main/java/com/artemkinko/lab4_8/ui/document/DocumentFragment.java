package com.artemkinko.lab4_8.ui.document;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import com.artemkinko.lab4_8.databinding.FragmentDocumentBinding;
import com.artemkinko.lab4_8.db.Note;
import com.artemkinko.lab4_8.db.NoteDAO;
import com.artemkinko.lab4_8.db.NoteDB;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DocumentFragment extends Fragment {

    private FragmentDocumentBinding binding;

    private NoteDB noteDB;

    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private ImageCapture imageCapture;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        DocumentViewModel documentViewModel =
                new ViewModelProvider(this).get(DocumentViewModel.class);

        binding = FragmentDocumentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // logic
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                1);
        while (getContext().checkCallingOrSelfPermission(Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED){}

//        ActivityCompat.requestPermissions(getActivity(),
//                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                1);
//
//        while (getContext().checkCallingOrSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
//                PackageManager.PERMISSION_GRANTED){}

        cameraProviderFuture = ProcessCameraProvider.getInstance(getActivity());
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                cameraProvider.unbindAll();

                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build();

                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(binding.preview.getSurfaceProvider());

                imageCapture = new ImageCapture.Builder()
                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                        .build();

                cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview, imageCapture);

            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(getContext()));

        binding.buttonPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long timestamp = System.currentTimeMillis();

                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, timestamp);
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");

                imageCapture.takePicture(new ImageCapture.OutputFileOptions.Builder(
                        getActivity().getContentResolver(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        contentValues
                        ).build(),
                        ContextCompat.getMainExecutor(getContext()),
                        new ImageCapture.OnImageSavedCallback() {
                            @Override
                            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                                Toast.makeText(getActivity(), "Фото сделано успешно!", Toast.LENGTH_SHORT).show();
                                new DBRequest().execute("", "/sdcard/Pictures/" + contentValues.getAsString(MediaStore.MediaColumns.DISPLAY_NAME) + ".jpg");
                            }

                            @Override
                            public void onError(@NonNull ImageCaptureException exception) {
                                Toast.makeText(getActivity(), "Error:" + exception.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
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
            note.isphoto = "yes";
            noteDAO.insertAll(note);
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            Toast.makeText(getActivity().getApplicationContext(), "Фото сделано успешно!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}


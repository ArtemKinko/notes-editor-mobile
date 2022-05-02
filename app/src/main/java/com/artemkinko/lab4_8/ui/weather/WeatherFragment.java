package com.artemkinko.lab4_8.ui.weather;

import static android.content.Context.LOCATION_SERVICE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.artemkinko.lab4_8.GPSTracker;
import com.artemkinko.lab4_8.MainActivity;
import com.artemkinko.lab4_8.RequestTask;
import com.artemkinko.lab4_8.databinding.FragmentWeatherBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class WeatherFragment extends Fragment {

    private FragmentWeatherBinding binding;

    private FusedLocationProviderClient client;

    private TextView text_town;

    private TextView text_temp;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        WeatherViewModel weatherViewModel =
                new ViewModelProvider(this).get(WeatherViewModel.class);

        binding = FragmentWeatherBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        // logic


        View temp_view = binding.viewBad;
        client = LocationServices.getFusedLocationProviderClient(getActivity());

        temp_view.setOnClickListener(new View.OnClickListener() {
            @Override
            @SuppressLint("MissingPermission")
            public void onClick(View view) {

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        1);
                while (getContext().checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED){}

                LocationManager locationManager = (LocationManager) getActivity()
                        .getSystemService(LOCATION_SERVICE);
                client.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if(location != null) {
                            text_town = binding.textWeatherTown;
                            text_temp = binding.textWeatherTemp;
                            String url_weather = "https://api.openweathermap.org/data/2.5/weather?lat=" + String.valueOf(location.getLatitude())
                                    + "&lon=" + String.valueOf(location.getLongitude()) + "&appid=f7041b2a252c3c59f0bc2ed014859381";
                            String respond = "";
                            try {
                                respond = new RequestTask().execute(url_weather).get();
                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                            }
                            String[] arr = respond.split("\"");
                            String tempK = arr[30].substring(1, arr[30].length() - 1);
                            String tempC = String.format("%.2f", Double.parseDouble(tempK) - 273.15) + "°C";
                            String town = arr[75];
                            text_temp.setTextSize(100);
                            text_temp.setText(tempC);
                            text_town.setText(town);
                        }
                    }
                });
            }
        });

        return root;
    }

    @WorkerThread
    public void getLocationAndShowIt(String link) throws IOException {
        URL weatherEndpoint = new URL(link);
        HttpsURLConnection connection = (HttpsURLConnection) weatherEndpoint.openConnection();
        InputStream responseBody = connection.getInputStream();
        Scanner s = new Scanner(responseBody).useDelimiter("\\A");
        String result = s.hasNext() ? s.next() : "";
        text_town.setText("Bruh");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public Location getLocation() {
        GPSTracker tracker = new GPSTracker(getContext());
        return tracker.GetLocation();
    }
}

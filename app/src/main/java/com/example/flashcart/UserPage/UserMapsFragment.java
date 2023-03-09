package com.example.flashcart.UserPage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.viewmodel.CreationExtras;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flashcart.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;


public class UserMapsFragment extends Fragment implements OnMapReadyCallback {


    GoogleMap googleMap;
    double myLatitude;
    double myLongitude;
    double shopLatitude;
    double shopLongitude;

    TextView distanceTextView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_maps, container, false);

        // Get the latitude and longitude values from your activity or arguments

        Bundle bundle = getArguments();
        myLatitude = bundle.getDouble("mylatitude");
        myLongitude = bundle.getDouble("mylongitude");
        shopLatitude = bundle.getDouble("shoplatitude");
        shopLongitude = bundle.getDouble("shoplongitude");

        Toast.makeText(getContext(), String.valueOf(myLatitude), Toast.LENGTH_SHORT).show();
        Toast.makeText(getContext(), String.valueOf(myLongitude), Toast.LENGTH_SHORT).show();
        Toast.makeText(getContext(), String.valueOf(shopLatitude), Toast.LENGTH_SHORT).show();
        Toast.makeText(getContext(), String.valueOf(shopLongitude), Toast.LENGTH_SHORT).show();


        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return rootView;
    }


    @Override
    public void onMapReady(@NonNull GoogleMap map) {

        googleMap = map;

        // Add a marker at the user's current location
        LatLng myLocation = new LatLng(myLatitude, myLongitude);
        googleMap.addMarker(new MarkerOptions().position(myLocation).title("My Location"));

        // Add a marker at the destination location
        LatLng shopLocation = new LatLng(shopLatitude, shopLongitude);
        googleMap.addMarker(new MarkerOptions().position(shopLocation).title("Shop Location"));




        // Add a path between the two locations
        PolylineOptions polylineOptions = new PolylineOptions()
                .add(myLocation)
                .add(shopLocation)
                .color(Color.RED)
                .width(5);
        googleMap.addPolyline(polylineOptions);

        // Calculate the distance between the two markers and display it in the TextView
        float[] results = new float[1];
        Location.distanceBetween(myLocation.latitude, myLocation.longitude, shopLocation.latitude, shopLocation.longitude, results);
        float distance = results[0] / 1000; // Convert meters to kilometers
        TextView distanceText = getView().findViewById(R.id.distance_text);
        distanceText.setText("Distance: " + distance + " km");

        // Move the camera to the destination location and zoom in
        CameraPosition cameraPosition = new CameraPosition.Builder().target(shopLocation).zoom(14).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

}
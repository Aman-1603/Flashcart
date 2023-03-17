package com.example.flashcart.UserPage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.viewmodel.CreationExtras;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.TravelMode;

import java.util.List;


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

//        Toast.makeText(getContext(), String.valueOf(myLatitude), Toast.LENGTH_SHORT).show();
//        Toast.makeText(getContext(), String.valueOf(myLongitude), Toast.LENGTH_SHORT).show();
//        Toast.makeText(getContext(), String.valueOf(shopLatitude), Toast.LENGTH_SHORT).show();
//        Toast.makeText(getContext(), String.valueOf(shopLongitude), Toast.LENGTH_SHORT).show();


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



        //requesting for direction map
        requestDirections(myLocation, shopLocation);




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



    //adding navigation features in map fragmnet


    private void requestDirections(LatLng origin, LatLng destination) {
        String apiKey = "AIzaSyDulp_wrPCM85XxTBY26VXxoCfZM1KTuAk"; // Replace with your Google Maps API key
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(apiKey)
                .build();
        DirectionsApiRequest request = DirectionsApi.newRequest(context)
                .origin(new com.google.maps.model.LatLng(origin.latitude, origin.longitude))
                .destination(new com.google.maps.model.LatLng(destination.latitude, destination.longitude))
                .mode(TravelMode.DRIVING); // Use driving mode for navigation
        request.setCallback(new PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {
                // Process the directions result and display the route on the map
                if (result.routes != null && result.routes.length > 0) {
                    DirectionsRoute route = result.routes[0];
                    PolylineOptions polylineOptions = new PolylineOptions()
                            .color(Color.BLUE)
                            .width(5);
                    for (DirectionsLeg leg : route.legs) {
                        for (DirectionsStep step : leg.steps) {
                            List<com.google.maps.model.LatLng> points = PolylineEncoding.decode(step.polyline.getEncodedPath());
                            for (com.google.maps.model.LatLng point : points) {
                                polylineOptions.add(new LatLng(point.lat, point.lng));
                            }
                        }
                    }
                    googleMap.addPolyline(polylineOptions);
                }
            }

            @Override
            public void onFailure(Throwable e) {
                // Handle the error if the directions request fails
//                Toast.makeText(getContext(), "Failed to get directions: " + e.getMessage(), Toast.LENGTH_SHORT).show();

                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
    }



}
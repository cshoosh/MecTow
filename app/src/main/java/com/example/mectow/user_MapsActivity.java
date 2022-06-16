package com.example.mectow;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.skyfishjy.library.RippleBackground;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.view.View.GONE;

public class user_MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, RoutingListener {
    GoogleMap mMap, mMap2;
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient fusedLocationProviderClient;
    Double lat, lng;
    FirebaseAuth auth;
    String call = "";
    public static String servicetype;
    double totalcharges = 0;
    public static String mechanic_id;
    private List<Polyline> polylines;
    String type;
    ConstraintLayout innerBox, uperBox;
    TextView name_text, Service_field, charges_field;
    static RippleBackground mRippleBg;
    LottieAnimationView message, calldial;
    private static final int[] COLORS = new int[]{R.color.PolyBlue};
    public static String customerrequestuid;
    Button searchworker;
    Double customerlat, customerlng, mechaniclat, mechaniclng;
    Marker mWorkerMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        auth = FirebaseAuth.getInstance();
        supportMapFragment.getMapAsync(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(user_MapsActivity.this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mRippleBg = findViewById(R.id.ripple_bg);
        Service_field = findViewById(R.id.Service_field);
        charges_field = findViewById(R.id.chargesmap);
        name_text = findViewById(R.id.name_text);
        polylines = new ArrayList<>();
        innerBox = findViewById(R.id.innerBox);
        // searchworker = findViewById(R.id.searchworker);

        uperBox = findViewById(R.id.uperBox);
        message = findViewById(R.id.message);
        calldial = findViewById(R.id.call);
        getNotifications(customerrequestuid);
//        searchworker.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (Mechanic_Questions.type=="mechanic") {
//
//                    searchworker.setVisibility(View.GONE);
//                    submit_mechanicComplaint(Mechanic_Questions.spinnerval, Mechanic_Questions.spinner1val, Mechanic_Questions.spinner2val, Mechanic_Questions.problemdescription, Mechanic_Questions.imageurl, lat, lng,Mechanic_Questions.service_charges);
//                    check_complaint_status();
//                }
//                else if (Cartow_Question.type=="cartow"){
//                    searchworker.setVisibility(View.GONE);
//                    submit_cartowComplaint(Cartow_Question.towspinnerval,Cartow_Question.towspinner1val,Cartow_Question.towspinner2val,Cartow_Question.probtowtext,lat,lng);
//                    check_complaint_status();
//                }
//                else{
//                    Toast.makeText(user_MapsActivity.this, "Something went Wrong!", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MessagingActivity.class);
                intent.putExtra("mid", mechanic_id);
                startActivity(intent);
            }
        });
        getNotifications(customerrequestuid);
        calldial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + call));
                startActivity(intent);
            }
        });
        if (getIntent().getExtras().getString("type") != null) {
            type = getIntent().getExtras().getString("type");
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(user_MapsActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(user_MapsActivity.this
                , Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getcurrentlocation();

        } else {
            ActivityCompat.requestPermissions(user_MapsActivity.this
                    , new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    }, 100);
        }
    }

    private void submit_mechanicComplaint(String spinnerval, String spinner1val, String spinner2val, String problemdescription, String imageurl, Double lat, Double lng, String charges) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Complaint").push();
        HashMap<String, String> data = new HashMap<>();
        data.put("service", spinnerval);
        data.put("Category", spinner1val);
        data.put("Subcategory", spinner2val);
        data.put("Description", problemdescription);
        data.put("image", imageurl);
        data.put("uid", auth.getUid());
        data.put("lat", lat.toString());
        data.put("lng", lng.toString());
        data.put("charges", charges);
        data.put("type", "mechanic");
        data.put("date", new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()));
        data.put("time", Calendar.getInstance().getTime().toString());
        servicetype = "mechanic";

        reference.setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    String key = reference.getKey();
                    DatabaseReference refer = FirebaseDatabase.getInstance().getReference("Complaint").child(key);
                    HashMap<String, Object> dataupdate = new HashMap<>();
                    dataupdate.put("KeyId", key);
                    refer.updateChildren(dataupdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(user_MapsActivity.this, "Complaint uploaded successful", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });
        mRippleBg.startRippleAnimation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && (grantResults[0] + grantResults[1]
                == PackageManager.PERMISSION_GRANTED)) {
            getcurrentlocation();
        } else {
            Toast.makeText(getApplicationContext(), "permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingPermission")
    private void getcurrentlocation() {
        LocationManager locationManager = (LocationManager) getSystemService(
                Context.LOCATION_SERVICE
        );
        if (locationManager.isProviderEnabled(locationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(locationManager.NETWORK_PROVIDER)) {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null) {
                        lat = location.getLatitude();
                        lng = location.getLongitude();
                        String.valueOf(location.getLatitude());
                        String.valueOf(location.getLongitude());
                        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(@NonNull GoogleMap googleMap) {
                                mMap2 = googleMap;
                                LatLng latLng = new LatLng(lat, lng);
                                MarkerOptions options = new MarkerOptions().position(latLng);
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                                googleMap.addMarker(options);
                                if (Mechanic_Questions.type == "mechanic") {

                                    //searchworker.setVisibility(View.GONE);
                                    submit_mechanicComplaint(Mechanic_Questions.spinnerval, Mechanic_Questions.spinner1val, Mechanic_Questions.spinner2val, Mechanic_Questions.problemdescription, Mechanic_Questions.imageurl, lat, lng, Mechanic_Questions.service_charges);

                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            check_complaint_status();
                                        }
                                    }, 10000);

                                } else if (Cartow_Question.type == "cartow") {
                                    //searchworker.setVisibility(View.GONE);
                                    submit_cartowComplaint(Cartow_Question.towspinnerval, Cartow_Question.towspinner1val, Cartow_Question.towspinner2val, Cartow_Question.probtowtext, lat, lng);
                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            check_complaint_status();
                                        }
                                    }, 10000);

                                } else {
                                    Toast.makeText(user_MapsActivity.this, "Something went Wrong!", Toast.LENGTH_SHORT).show();
                                }
                                // getAllWorkers(Mechanic_Questions.type);


                            }
                        });
                    } else {
                        LocationRequest locationRequest = new LocationRequest()
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(10000)
                                .setFastestInterval(10000)
                                .setNumUpdates(1);
                        LocationCallback locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(@NonNull LocationResult locationResult) {
                                Location location1 = locationResult.getLastLocation();
                                String.valueOf(location1.getLatitude());
                                String.valueOf(location1.getLongitude());
                            }
                        };
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest
                                , locationCallback, Looper.myLooper());
                    }
                }
            });
        } else {
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    private void getAllWorkers(String workertype) {
        FirebaseDatabase.getInstance().getReference("Mechanic")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            if (workertype.equals(dataSnapshot.child("service").getValue().toString())) {
                                fetchWorkerLatLng(dataSnapshot.getKey());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void fetchWorkerLatLng(String key) {
        FirebaseDatabase.getInstance().getReference().child(key)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.child("status").exists() && snapshot.child("status").getValue().toString().equals("online")) {
                            Double lat = Double.valueOf(snapshot.child("latitude").getValue().toString());
                            Double lng = Double.valueOf(snapshot.child("longitude").getValue().toString());
                            LatLng latLng = new LatLng(lat, lng);
                            MarkerOptions options = new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).position(latLng);
                            mMap2.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                            mMap2.addMarker(options);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void submit_cartowComplaint(String spinnerval, String spinner1val, String spinner2val, String problemdescription, Double lat, Double lng) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Complaint").push();
        HashMap<String, String> data = new HashMap<>();
        data.put("service", spinnerval);
        data.put("Category", spinner1val);
        data.put("Subcategory", spinner2val);
        data.put("Description", problemdescription);
        data.put("uid", auth.getUid());
        data.put("lat", lat.toString());
        data.put("lng", lng.toString());
        data.put("type", "cartow");
        data.put("date", new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()));
        data.put("time", Calendar.getInstance().getTime().toString());
        servicetype = "cartow";

        reference.setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    String key = reference.getKey();
                    DatabaseReference refer = FirebaseDatabase.getInstance().getReference("Complaint").child(key);
                    HashMap<String, Object> dataupdate = new HashMap<>();
                    dataupdate.put("KeyId", key);
                    refer.updateChildren(dataupdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(user_MapsActivity.this, "Complaint uploaded successful", Toast.LENGTH_SHORT).show();

                        }
                    });
                    // Intent intent = new Intent(Cartow_Question.this, user_MapsActivity.class);
                    //startActivity(intent);
                }
            }
        });
        mRippleBg.startRippleAnimation();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    public void check_complaint_status() {
        FirebaseDatabase.getInstance().getReference("Complaint")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            customerrequestuid = dataSnapshot.getKey();
                            String uid = dataSnapshot.child("uid").getValue().toString();

                            if (auth.getUid().equals(uid)) {
                                if (dataSnapshot.child("mid").exists()) {
                                    mechanic_id = dataSnapshot.child("mid").getValue().toString();
                                    DatabaseReference workerStateReference = FirebaseDatabase.getInstance().getReference("WorkerState");
                                    ValueEventListener listener = new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                            double lat = Double.valueOf(snapshot.child("latitude").getValue().toString());
                                            double lng = Double.valueOf(snapshot.child("longitude").getValue().toString());

                                            LatLng latLong = new LatLng(lat, lng);
                                            MarkerOptions options = new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).position(latLong);
                                            mMap2.animateCamera(CameraUpdateFactory.newLatLngZoom(latLong, 10));
                                            if (mWorkerMarker == null) {
                                                mWorkerMarker = mMap2.addMarker(options);
                                            }

                                            mWorkerMarker.setPosition(latLong);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    };

                                    if (
                                            dataSnapshot.child("state").getValue().toString().equals("moving") ||
                                                    dataSnapshot.child("state").getValue().toString().equals("arrived") ||
                                                    dataSnapshot.child("state").getValue().toString().equals("startworking")
                                    ) {
                                        workerStateReference.child(mechanic_id).addValueEventListener(listener);
                                    } else if (dataSnapshot.child("state").getValue().toString().equals("end")) {
                                        workerStateReference.removeEventListener(listener);
                                        workerStateReference.child(mechanic_id).child("state").setValue("free");
                                    }

                                    uperBox.setVisibility(View.VISIBLE);
                                    if (dataSnapshot.child("charges").exists()) {
                                        String charge = dataSnapshot.child("charges").getValue().toString();
                                        charges_field.setText(charge);
                                    }
                                    FirebaseDatabase.getInstance().getReference("Mechanic").child(mechanic_id).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            Service_field.setText(snapshot.child("service").getValue().toString());
                                            name_text.setText(snapshot.child("name").getValue().toString());
                                            call = snapshot.child("phone_number").getValue().toString();
                                            //fetchUserLatLng();
                                            mRippleBg.stopRippleAnimation();
                                            fetchUserLatLng();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void fetchUserLatLng() {
        FirebaseDatabase.getInstance().getReference("Complaint").child(customerrequestuid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        customerlat = Double.valueOf(snapshot.child("lat").getValue().toString());
                        customerlng = Double.valueOf(snapshot.child("lng").getValue().toString());
                        fetchMechanicLatLng();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void fetchMechanicLatLng() {
        FirebaseDatabase.getInstance().getReference("WorkerState").child(mechanic_id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        mechaniclat = Double.valueOf(snapshot.child("latitude").getValue().toString());
                        mechaniclng = Double.valueOf(snapshot.child("longitude").getValue().toString());
                        LatLng customerlatlng = new LatLng(customerlat, customerlng);
                        LatLng mechaniclatlng = new LatLng(mechaniclat, mechaniclng);
                        getRouteToMarker(customerlatlng, mechaniclatlng);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    private void getRouteToMarker(LatLng starting, LatLng ending) {

        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(false)
                .key("AIzaSyBQUC-3IP9u-CsIHB97MHIOX9vpoYvsloU")
                .waypoints(starting, ending)
                .build();
        routing.execute();
    }

    @Override
    public void onRoutingFailure(RouteException e) {
        if (e != null) {
            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortindex) {
        if (polylines.size() > 0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i < route.size(); i++) {

            //In case of more than 5 alternative routes
            int colorIndex = i % COLORS.length;

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = mMap2.addPolyline(polyOptions);
            polylines.add(polyline);
            getNotifications(customerrequestuid);
            Toast.makeText(getApplicationContext(), route.get(i).getDistanceText(), Toast.LENGTH_SHORT).show();

            if (servicetype == "mechanic") {
                totalcharges = ((30 * (Double.parseDouble(String.valueOf(route.get(i).getDistanceValue())))) + (Double.parseDouble(charges_field.getText().toString())));
            } else if (servicetype == "cartow") {
                totalcharges = ((30 * (Double.parseDouble(String.valueOf(route.get(i).getDistanceValue())))) + 400);
            }
        }
    }

    @Override
    public void onRoutingCancelled() {

    }

    public void getNotifications(String complaintId) {
        if (complaintId != null) {
            DatabaseReference datareff = FirebaseDatabase.getInstance().getReference("Complaint").child(complaintId);
            datareff.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("state").exists()) {
                        if (dataSnapshot.child("state").getValue().equals("accept")) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                NotificationChannel channel =
                                        new NotificationChannel("n", "n", NotificationManager.IMPORTANCE_DEFAULT);
                                NotificationManager manager = getSystemService(NotificationManager.class);
                                manager.createNotificationChannel(channel);
                            }
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "n")
                                    .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                                    .setContentTitle("MecTow")
                                    .setContentText("Accepted")
                                    .setStyle(new NotificationCompat.BigTextStyle()
                                            .bigText("Get Ready! Mectow Mechanic has accepted your request")
                                            .setBigContentTitle("Accepted"))
                                    .setAutoCancel(true);

                            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getApplicationContext());
                            managerCompat.notify(999, builder.build());
                        }
                    }
                    if (dataSnapshot.child("state").exists()) {
                        if (dataSnapshot.child("state").getValue().equals("moving")) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                NotificationChannel channel =
                                        new NotificationChannel("n", "n", NotificationManager.IMPORTANCE_DEFAULT);
                                NotificationManager manager = getSystemService(NotificationManager.class);
                                manager.createNotificationChannel(channel);
                            }

                            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "n")
                                    .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                                    .setContentTitle("MecTow")
                                    .setContentText("Coming")
                                    .setStyle(new NotificationCompat.BigTextStyle()
                                            .bigText("Mechanic is on it's way")
                                            .setBigContentTitle("Coming"))
                                    .setAutoCancel(true);

                            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getApplicationContext());
                            managerCompat.notify(999, builder.build());
                        }
                    }
                    if (dataSnapshot.child("state").exists()) {
                        if (dataSnapshot.child("state").getValue().equals("arrived")) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                NotificationChannel channel =
                                        new NotificationChannel("n", "n", NotificationManager.IMPORTANCE_DEFAULT);
                                NotificationManager manager = getSystemService(NotificationManager.class);
                                manager.createNotificationChannel(channel);
                            }

                            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "n")
                                    .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                                    .setContentTitle("MecTow")
                                    .setContentText("Arrived")
                                    .setStyle(new NotificationCompat.BigTextStyle()
                                            .bigText("MecTow is around at your corner")
                                            .setBigContentTitle("Arrived"))
                                    .setAutoCancel(true);

                            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getApplicationContext());
                            managerCompat.notify(999, builder.build());
                        }
                    }
                    if (dataSnapshot.child("state").exists()) {
                        if (dataSnapshot.child("state").getValue().equals("startworking")) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                NotificationChannel channel =
                                        new NotificationChannel("n", "n", NotificationManager.IMPORTANCE_DEFAULT);
                                NotificationManager manager = getSystemService(NotificationManager.class);
                                manager.createNotificationChannel(channel);
                            }
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "n")
                                    .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                                    .setContentTitle("MecTow")
                                    .setContentText("Started")
                                    .setStyle(new NotificationCompat.BigTextStyle()
                                            .bigText("MecTow is now going to start your work")
                                            .setBigContentTitle("Started"))
                                    .setAutoCancel(true);
                            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getApplicationContext());
                            managerCompat.notify(999, builder.build());
                        }
                    }
                    if (dataSnapshot.child("state").exists()) {
                        if (dataSnapshot.child("state").getValue().equals("end")) {

                            DatabaseReference reff = (DatabaseReference) FirebaseDatabase.getInstance().getReference("Complaint").child(customerrequestuid);
                            HashMap<String, Object> updateData = new HashMap<>();
                            updateData.put("state", "view");

                            reff.updateChildren(updateData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Intent intent = new Intent(getApplicationContext(), Activity_rate.class);
                                    startActivity(intent);


                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        NotificationChannel channel =
                                                new NotificationChannel("n", "n", NotificationManager.IMPORTANCE_DEFAULT);
                                        NotificationManager manager = getSystemService(NotificationManager.class);
                                        manager.createNotificationChannel(channel);
                                    }
                                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "n")
                                            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                                            .setContentTitle("MecTow")
                                            .setContentText("Completed")
                                            .setStyle(new NotificationCompat.BigTextStyle()
                                                    .bigText("Your Work is completed and Please give rating to Mechanic")
                                                    .setBigContentTitle("Completed"))
                                            .setAutoCancel(true);

                                    NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getApplicationContext());
                                    managerCompat.notify(999, builder.build());
                                }
                            });


                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
    }
}

package ders.yasin.googlemapapp;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import ders.tuba.googlemapapp.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    ImageButton ibSearch;
    EditText etLocation;
    Button btnType;
    ZoomControls zoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ibSearch=findViewById(R.id.ib_Search);
        etLocation=findViewById(R.id.et_Location);
        btnType=findViewById(R.id.btn_Type);
        zoom=findViewById(R.id.zoom);
        ibSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String location=etLocation.getText().toString();
                if(!TextUtils.isEmpty(location)){
                    Geocoder geocoder=new Geocoder(MapsActivity.this);
                    try {
                        List<Address> addressList= geocoder.getFromLocationName(location,5);
                        Address address=addressList.get(0);
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(latLng).title("Marker in "+location));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        btnType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mMap.getMapType()==GoogleMap.MAP_TYPE_SATELLITE){
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    btnType.setText("Sat");
                }else if((mMap.getMapType()==GoogleMap.MAP_TYPE_NORMAL)){
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    btnType.setText("Map");
                }
            }
        });
        zoom.setOnZoomInClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.moveCamera(CameraUpdateFactory.zoomIn());
            }
        });
        zoom.setOnZoomOutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.moveCamera(CameraUpdateFactory.zoomOut());
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        checkLocationPermission();
    }

    private void checkLocationPermission() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            mMap.setMyLocationEnabled(true);
        }else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},200);
    }

}
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==200){
           if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
               checkLocationPermission();
           else
               Toast.makeText(getApplicationContext(),"User denied location permission",Toast.LENGTH_SHORT).show();
        }
    }
}
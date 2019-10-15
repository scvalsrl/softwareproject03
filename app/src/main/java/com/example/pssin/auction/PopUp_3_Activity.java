package com.example.pssin.auction;

import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class PopUp_3_Activity extends AppCompatActivity
        implements OnMapReadyCallback {

    private GoogleMap mMap;
    Button btn;
    Projection projection ;
    LatLng markerLocation ;
    MarkerOptions markerOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_3_);
        btn = (Button)findViewById(R.id.btn);




        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            projection = mMap.getProjection();
            markerLocation = markerOptions.getPosition();
            intent.putExtra("result", markerLocation);
            setResult(RESULT_OK, intent);

            finish();

        }
});

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        mMap = googleMap;

        LatLng SEOUL = new LatLng(37.56, 126.97);

         markerOptions = new MarkerOptions();
        markerOptions.position(SEOUL);
        markerOptions.title("서울");
        markerOptions.snippet("한국의 수도");

        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));


    }

}

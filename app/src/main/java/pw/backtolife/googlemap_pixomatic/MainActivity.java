package pw.backtolife.googlemap_pixomatic;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng mFrom;
    private LatLng mTo;
    private AppCompatTextView mTextViewDistance;
    private AppCompatButton mAppCompatButtonClear;

    private int mMarkerCounter = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextViewDistance = findViewById(R.id.tv_distance);
        mAppCompatButtonClear = findViewById(R.id.btn_clear);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mAppCompatButtonClear.setOnClickListener(new ClearClick());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setAllGesturesEnabled(true);

        mMap.setOnMapLongClickListener(new LongClick());
    }

    private void showDistance() {
        double distance = SphericalUtil.computeDistanceBetween(mFrom, mTo);
        Log.e("marker_distance", String.valueOf(distance));

        PolylineOptions rectOptions = new PolylineOptions()
                .add(mFrom)
                .add(mTo);

        mMap.addPolyline(rectOptions);

        mTextViewDistance.setVisibility(View.VISIBLE);

        mTextViewDistance.setText(getString(R.string.distance) + distance/1000 + " km");
        mMap.setOnMapLongClickListener(null);
    }

    private class LongClick implements GoogleMap.OnMapLongClickListener {
        @Override
        public void onMapLongClick(LatLng latLng) {
            switch (mMarkerCounter){
                case 1:
                    mFrom = new LatLng(latLng.latitude, latLng.longitude);
                    Log.e("marker_from", String.valueOf(latLng));
                    break;
                case 2:
                    mTo = new LatLng(latLng.latitude, latLng.longitude);
                    Log.e("marker_to", String.valueOf(latLng));
                    showDistance();
                    break;
            }
            mMap.addMarker(new MarkerOptions().position(latLng)
                    .title("Marker #" + String.valueOf(mMarkerCounter++)));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }

    private class ClearClick implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            mMap.clear();
            mMarkerCounter = 1;
            mTextViewDistance.setVisibility(View.GONE);
            mMap.setOnMapLongClickListener(new LongClick());
        }
    }
}

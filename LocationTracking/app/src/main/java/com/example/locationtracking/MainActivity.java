package com.example.locationtracking;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.LocationOverlay;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.PathOverlay;

import java.util.Arrays;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private NaverMap mMap;
    LatLng prev_LOC = null;
    LatLng curr_LOC;
    Marker mk = new Marker();

    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 지도를 출력할 르레그먼트 영역 인식
        MapFragment mapFragment = (MapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.map, mapFragment).commit();
        }

        // 지도 사용이 준비되면 onMapReady 콜백 함수 호출
        mapFragment.getMapAsync(this);
    }

    @UiThread
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        // 지도 객체를 여러 메소도에서 사용할 수 있도록 글로벌 객체로 할당
        mMap = naverMap;

        locationListener = new LocationListener() {
            // 위치가 변할 때마다 호출
            public void onLocationChanged(Location location) {
                updateMap(location);
            }

            // 위치서비스가 변경될 때
            public void onStatusChanged(String provider, int status, Bundle extras) {
                alertStatus(provider);
            }

            // 사용자에 의해 Provider 가 사용 가능하게 설정될 때
            public void onProviderEnabled(String provider) {
                alertProvider(provider);
            }

            // 사용자에 의해 Provider 가 사용 불가능하게 설정될 때
            public void onProviderDisabled(String provider) {
                checkProvider(provider);
            }
        };

        // 시스템 위치 서비스 관리 객체 생성
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // 정확한 위치 접근 권한이 설정되어 있지 않으면 사용자에게 권한 요구
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            return;
        }

        String locationProvider;
        // GPS 에 의한 위치 변경 요구
        locationProvider = LocationManager.GPS_PROVIDER;
        locationManager.requestLocationUpdates(locationProvider, 1, 1, locationListener);
        // 통신사 기지국에 의한 위치 변경 요구
        locationProvider = LocationManager.NETWORK_PROVIDER;
        locationManager.requestLocationUpdates(locationProvider, 1, 1, locationListener);
    }

    public void checkProvider(String provider) {
        Toast.makeText(this, provider + "에 의한 위치서비스가 꺼져 있습니다. 켜주세요...", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
    }

    public void alertProvider(String provider) {
        Toast.makeText(this, provider + "서비스가 켜졌습니다!", Toast.LENGTH_LONG).show();
    }

    public void alertStatus(String provider) {
        Toast.makeText(this, "위치서비스가 " + provider + "로 변경되었습니다!", Toast.LENGTH_LONG).show();
    }

    public void updateMap(Location location) {
        // 위도
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        // 경도
        curr_LOC = new LatLng(latitude, longitude);

        // 이전 위치가 없는 경우
        if (prev_LOC == null) {
            // 지도 크기
            CameraUpdate cameraUpdate = CameraUpdate.zoomTo(15);
            mMap.moveCamera(cameraUpdate);

            // 위치 오버레이 표시(원)
            LocationOverlay locationOverlay = mMap.getLocationOverlay();
            locationOverlay.setVisible(true);
            locationOverlay.setPosition(curr_LOC);

            // 현재 위치를 이전 위치로 설정
            prev_LOC = curr_LOC;

        // 이전 위치가 있는 경우
        } else {
            // 지도 중심
            CameraUpdate cameraUpdate1 = CameraUpdate.scrollTo(curr_LOC);
            mMap.moveCamera(cameraUpdate1);

            // 경로 표시
            PathOverlay path = new PathOverlay();
            path.setCoords(Arrays.asList(
                    new LatLng(prev_LOC.latitude, prev_LOC.longitude),
                    new LatLng(curr_LOC.latitude, curr_LOC.longitude)
            ));
            path.setMap(mMap);
            path.setOutlineColor(Color.BLACK);
            path.setColor(Color.YELLOW);
            path.setWidth(30);

            // 현재 위치에 마커 표시
            mk.setVisible(false);
            mk.setPosition(curr_LOC);
            mk.setMap(mMap);
            mk.setVisible(true);

            // 현재 경로를 이전 경로로 설정
            prev_LOC = curr_LOC;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationManager != null)
            locationManager.removeUpdates(locationListener);
    }
}

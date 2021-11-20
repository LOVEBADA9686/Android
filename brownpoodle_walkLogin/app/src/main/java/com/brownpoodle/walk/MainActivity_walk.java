package com.brownpoodle.walk;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.annotation.NonNull;
import androidx.annotation.UiThread;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.brownpoodle.MainActivity;
import com.brownpoodle.login.RegisterActivity;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.LocationOverlay;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.PathOverlay;
import com.brownpoodle.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Arrays;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity_walk extends FragmentActivity implements OnMapReadyCallback {

    private NaverMap mMap;
    LatLng prev_LOC = null;
    LatLng curr_LOC;
    Marker mk = new Marker();

    LocationManager locationManager;
    LocationListener locationListener;

    // json데이터로 pojo 클래스(trackingdata) 이용
    WalkVO tdata = new WalkVO();
    static int pre_loc = 0;
    static int cur_loc = 0;
    // list 형태로 좌표 값 저장 -> POJO에 담아 JSON 파일로 저장 :  fail
    //JSONArray longitude_jarr = new JSONArray();
    //JSONArray latitude_jarr = new JSONArray();
    //*************************************************************************************
    // String으로 위치정보 넘기기! 이클립스에서 ',' 혹은 ' '로 값을 나누어 리스트형태로 저장해서 좌표찍기
    //*************************************************************************************
    static String longitude_str_all = "";
    static String latitude_str_all = "";

    ImageView iconView;
    TextView weatherView;
    TextView tempView;
    Chronometer chronometer;
    Button startBtn, stopBtn;
    long stopTime;
    String saveTime;

    static RequestQueue requestQueue;

    // 파일명
    LocalDateTime date = LocalDateTime.now();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk_main);
        System.out.println("onCreate >>> : ");

        // 날씨---------------------------------------------
        iconView = findViewById(R.id.iconView);
        weatherView = findViewById(R.id.weatherView);
        tempView = findViewById(R.id.tempView);

        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        // 날씨 함수
        CurrentWeatherCall();

        // 스톱워치---------------------------------
        chronometer = findViewById(R.id.chronometer);
        startBtn = findViewById(R.id.startBtn);
        stopBtn = findViewById(R.id.stopBtn);

        startBtn.setOnClickListener(v -> {
            chronometer.setBase(SystemClock.elapsedRealtime() + stopTime);
            chronometer.start();
            startBtn.setVisibility(View.GONE);
            stopBtn.setVisibility(View.VISIBLE);
        });

        stopBtn.setOnClickListener(v -> {
            stopTime = SystemClock.elapsedRealtime() - chronometer.getBase();
            chronometer.stop();
            stopBtn.setVisibility(View.GONE);
            startBtn.setVisibility(View.VISIBLE);
            int time = (int)(stopTime/1000);
            int min = time%(60*60)/60;
            int sec = time%60;
            saveTime = Integer.toString(min).concat(":").concat(Integer.toString(sec));

            stopTime = 0;

            saveData();
        });

        // 지도---------------------------------------------
        // 지도를 출력할 프래그먼트 영역 인식
        MapFragment mapFragment = (MapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        if(mapFragment == null){
            mapFragment = MapFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.map, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @UiThread
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        mMap = naverMap;

        locationListener = new LocationListener() {

            // 위치가 변할 때마다 호출
            @Override
            public void onLocationChanged(Location location) {
                updateMap(location);
            }

            // 위치서비스가 변경될 때
            public void onStatusChanged(String provider, int status, Bundle extras){
                alertStatus(provider);
            }

            // 사용자에 의해 Provider가 사용 가능하게 설정될 때
            public void onProviderEnabled(String provider){
                alertProvider(provider);
            }

            // 사용자에 의해 Provider가 사용 불가능하게 설정될 때
            public void onProviderDisabled(String provider){
                checkProvider(provider);
            }
        };

        //시스템 위치 서비스 관리 객체 생성
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // 정확한 위치 접근 권한이 설정되어 있지 않으면 사용자에게 권한 요구
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            return;
        }

        String locationProvider;
        // GPS에 의한 위치 변경 요구
        locationProvider = LocationManager.GPS_PROVIDER;
        locationManager.requestLocationUpdates(locationProvider, 1, 1, locationListener);
        //통신사 기지국에 의한 위치 변경 요구
        locationProvider = LocationManager.NETWORK_PROVIDER;
        locationManager.requestLocationUpdates(locationProvider,1,1,locationListener);
    }

    public void checkProvider(String provider){
        Toast.makeText(this, provider + "에 의한 위치서비스가 꺼져 있습니다", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
    }

    public void alertProvider(String provider){
        Toast.makeText(this, provider + "서비스가 켜졌습니다", Toast.LENGTH_LONG).show();
    }

    public void alertStatus(String provider){
        Toast.makeText(this, "위치서비스가 " + provider + "로 변경되었습니다!", Toast.LENGTH_LONG).show();
    }

    public void updateMap(Location location){
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        System.out.println("latitude >>> : " + latitude);
        System.out.println("longitude >>> : " + longitude);

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

            // json data : 첫 현재 위치 값 0번째로 받음
            String latitude_str = Double.toString(curr_LOC.latitude);
            String longitude_str = Double.toString(curr_LOC.longitude);

            longitude_str_all += (longitude_str + ",");
            latitude_str_all += (latitude_str + ",");
            System.out.println("longitude_str_all >>> : " + longitude_str_all);
            System.out.println("latitude_str_all >>> : " + latitude_str_all);

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
            path.setColor(Color.BLUE);
            path.setWidth(25);

            // 현재 위치에 마커 표시
            mk.setVisible(false);
            mk.setPosition(curr_LOC);
            mk.setMap(mMap);
            mk.setVisible(true);

            System.out.println("prev_LOC >>> : " + prev_LOC.latitude + ", " + prev_LOC.longitude );
            System.out.println("prev_LOC >>> : " + curr_LOC.latitude + ", " + curr_LOC.longitude );

            String latitude_str = Double.toString(curr_LOC.latitude);
            String longitude_str = Double.toString(curr_LOC.longitude);

            longitude_str_all += (longitude_str + ",");
            latitude_str_all += (latitude_str + ",");
            System.out.println("longitude_str_all >>> : " + longitude_str_all);
            System.out.println("latitude_str_all >>> : " + latitude_str_all);

            tdata.setLongitude_str(longitude_str_all);
            tdata.setLongitude_str(latitude_str_all);
            System.out.println("longitude_str_all >>> : " + tdata.getLongitude_str());
            System.out.println("latitude_str_all >>> : " + tdata.getLatitude_str());
        }
    }

    public void saveData(){

        ObjectMapper mapper = new ObjectMapper();
        //tdata.setM_id("111");
        try {

            String result = null;

            TaskActivity task = new TaskActivity();
            System.out.println("tdata.getLongitude_str() >>> : " + tdata.getLongitude_str());
            System.out.println("tdata.getLatitude_str() >>> : " + tdata.getLatitude_str());
            result = task.execute(longitude_str_all, latitude_str_all, saveTime).get();
            System.out.println("result >>> : " + result);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    }

    //날씨함수 --------------------------------------------------------------------------------
    private void CurrentWeatherCall(){
        String url = "https://api.openweathermap.org/data/2.5/weather?q=Seoul";
        String key = "&appid=98cd05d2b75d3fddaf2dab718820d6fb";
        String fa = "&units=metric";
        String lang = "&lang=kr";
        //String location = "&lat=37.48348&lon=126.87509";

        String api = url.concat(lang).concat(fa).concat(key);

        StringRequest request = new StringRequest(Request.Method.GET, api, new Response.Listener<String>() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    // 아이콘 이미지
                    JSONArray iconJson = jsonObject.getJSONArray("weather");
                    JSONObject iconObj = iconJson.getJSONObject(0);

                    String icon = iconObj.getString("icon").substring(0,2);
                    Log.v("아이콘", "아이콘 불러오나?"+icon);
                    switch (icon) {
                        case "01":
                            iconView.setImageResource(R.drawable.nb01);
                            break;
                        case "02":
                            iconView.setImageResource(R.drawable.nb02);
                            break;
                        case "03":
                        case "04":
                            iconView.setImageResource(R.drawable.nb03);
                            break;
                        case "09":
                            iconView.setImageResource(R.drawable.nb20);
                            break;
                        case "10":
                            iconView.setImageResource(R.drawable.nb08);
                            break;
                        case "11":
                            iconView.setImageResource(R.drawable.nb14);
                            break;
                        case "13":
                            iconView.setImageResource(R.drawable.nb11);
                            break;
                        case "50":
                            iconView.setImageResource(R.drawable.nb15);
                            break;
                    }

                    // 날씨 문장
                    JSONArray weatherJson = jsonObject.getJSONArray("weather");
                    JSONObject weatherObj = weatherJson.getJSONObject(0);

                    String weather = weatherObj.getString("main");
                    weatherView.setText(weather);

                    // 온도
                    JSONObject tempK = new JSONObject(jsonObject.getString("main"));
                    String temp = (tempK.getString("temp")).substring(0,4);
                    Log.v("온도", "온도 불러오나?"+temp);
                    tempView.setText(temp + "°C");

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, error -> System.out.println("error!!")){
            protected Map<String, String> getParams() {
                return new HashMap<>();
            }
        };
        request.setShouldCache(false);
        requestQueue.add(request);
    }
}
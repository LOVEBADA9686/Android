package g.g.d.com.map;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import net.daum.mf.map.api.MapCircle;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import g.g.d.com.ApiInterface;
import g.g.d.com.LOGDT;
import g.g.d.com.R;
import g.g.d.com.ReviewRegisterActivity;
import g.g.d.com.view.ReviewSelectAll;
import g.g.d.com.vo.Document;
import g.g.d.com.vo.KaKaoVO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class KakaoMap extends AppCompatActivity implements MapView.POIItemEventListener {


    private final String TAG = getClass().getSimpleName();
    private final String API_KEY = "KakaoAK 7f2e06967eed82b02c906c7548de7e0c";
    ArrayList<Document> aList;
    private String mid, keyword;
    private Button kakao_list;
    String y, x;
    MapView mapView;
    ViewGroup mapViewContainer;
    EditText kewordsearch;
    String maddr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kakao_map);




        //String keyword = "치킨";

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            LOGDT data = extras.getParcelable("data");
            if (data != null) {
                keyword = data.sType;
                System.out.println(">>>>>>>>>>>> : " + keyword);
                mid = data.mid;
            }
        }


        ReviewRegisterActivity task = new ReviewRegisterActivity();
        try {
            maddr = task.execute("ADDR", mid).get();
            System.out.println(" >>>>>>>>>>>>>> : " + maddr);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String defaultxy[] = defaultGeo(maddr);
        if(defaultxy != null && defaultxy.length > 0){
            x = defaultxy[1];
            y = defaultxy[0];
        }else{
            y = "126.99599512792346";
            x = "35.976749396987046";
        }




 //       y = "126.99599512792346";
 //       x = "35.976749396987046";
        mapView = new MapView(this);
        mapRoad(keyword, x, y);

        Button nowgeo = (Button)findViewById(R.id.nowgeo);
        nowgeo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String xy[] = nowGeo();
                y = xy[0];
                x = xy[1];
                mapRoad(keyword, x, y);

            }
        });


        Button newgeo = (Button)findViewById(R.id.newgeo);
        newgeo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String xy[] = null;
                xy = newGeo();
                if(xy != null && xy.length > 0){
                    y = xy[0];
                    x = xy[1];
                    mapRoad(keyword, x, y);
                }else{
                    Toast toast = Toast.makeText(getApplication(), "주소를 잘못 입력하셨습니다.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        Button defaultgeo = (Button)findViewById(R.id.defaultgeo);
        defaultgeo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String xy[] = null;
                xy = defaultGeo(maddr);
                if(xy != null && xy.length > 0){
                    y = xy[0];
                    x = xy[1];
                    mapRoad(keyword, x, y);
                }else{
                    Toast toast = Toast.makeText(getApplication(), "주소를 잘못 입력하셨습니다.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });



//        EditText editText = (EditText) findViewById(R.id.keyword);
        kakao_list = (Button) findViewById(R.id.kakao_list);



        kakao_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent kakaoList = new Intent(KakaoMap.this, KakaoList.class);
                kakaoList.putExtra("keyword", keyword);
                kakaoList.putExtra("mid", mid);
                kakaoList.putExtra("x", x);
                kakaoList.putExtra("y", y);
                startActivity(kakaoList);
            }
        });



        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);
        mapView.setPOIItemEventListener(this);

        //mapRoad(keyword, x, y);



    }

    public String[] nowGeo(){

        final LocationManager lm = (LocationManager)  getSystemService(Context.LOCATION_SERVICE);
        String[] geo = null;

        if ( Build.VERSION.SDK_INT >= 23 &&
            ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions( KakaoMap.this, new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  },
                    0 );
        }
        else{
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            String provider = location.getProvider();
            double longitude = location.getLongitude();
            double latitue = location.getLatitude();

            System.out.println(longitude);
            System.out.println(latitue);

            String xx = Double.toString(longitude);
            String yy = Double.toString(latitue);
            geo = new String[]{xx, yy};
        }

        return geo;
    }

    public String[] newGeo(){

        final Geocoder geocoder = new Geocoder(this);
        String[] geo = null;

        List<Address> list = null;
        kewordsearch = (EditText)findViewById(R.id.kewordsearch);

        String str = kewordsearch.getText().toString();
        try {
            list = geocoder.getFromLocationName(
                    str, // 지역 이름
                    10); // 읽을 개수
            System.out.println(list.size());
            System.out.println(list);
//            System.out.println(list.get(0));
//            System.out.println(list.get(0).getLatitude());
//            System.out.println(list.get(0).getLongitude());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("test","입출력 오류 - 서버에서 주소변환시 에러발생");
            finish();
        }

        if (list != null) {
            if (list.size() == 0) {
               // tv.setText("해당되는 주소 정보는 없습니다");
            } else {
                //tv.setText(list.get(0).toString());
                //          list.get(0).getCountryName();  // 국가명
                //          list.get(0).getLatitude();        // 위도
                //          list.get(0).getLongitude();    // 경도
                String xx = Double.toString(list.get(0).getLongitude());
                String yy = Double.toString(list.get(0).getLatitude());
                geo = new String[]{xx, yy};
            }
        }


        return geo;
    }

    public String[] defaultGeo(String keywordsearch){

        final Geocoder geocoder = new Geocoder(this);
        String[] geo = null;

        List<Address> list = null;


        String str = keywordsearch;
        try {
            list = geocoder.getFromLocationName(
                    str, // 지역 이름
                    10); // 읽을 개수
            System.out.println(list.size());
            System.out.println(list);
//            System.out.println(list.get(0));
//            System.out.println(list.get(0).getLatitude());
//            System.out.println(list.get(0).getLongitude());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("test","입출력 오류 - 서버에서 주소변환시 에러발생");
            finish();
        }

        if (list != null) {
            if (list.size() == 0) {
               // tv.setText("해당되는 주소 정보는 없습니다");
            } else {
                //tv.setText(list.get(0).toString());
                //          list.get(0).getCountryName();  // 국가명
                //          list.get(0).getLatitude();        // 위도
                //          list.get(0).getLongitude();    // 경도
                String xx = Double.toString(list.get(0).getLongitude());
                String yy = Double.toString(list.get(0).getLatitude());
                geo = new String[]{xx, yy};
            }
        }


        return geo;
    }


    public void mapRoad(String keyword, String x, String y){

        aList = new ArrayList<Document>();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://dapi.kakao.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface api = retrofit.create(ApiInterface.class);
        Call<KaKaoVO> call = api.getSearchLocation(API_KEY, keyword, y, x, 5000);
//        MapView mapView = new MapView(this);
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(x), Double.parseDouble(y)), true);
        mapView.setZoomLevel(2, true);
        call.enqueue(new Callback<KaKaoVO>() {
            @Override
            public void onResponse(Call<KaKaoVO> call, Response<KaKaoVO> response) {
                if (response.isSuccessful()) {
                    mapView.removeAllPOIItems();
                    mapView.removeAllCircles();
                    System.out.println(response.body().getDocuments().get(0).getAddressName());
                    int resSize = response.body().getDocuments().size();
                    for (int i=0; i<resSize; i++) {
                        Document document = response.body().getDocuments().get(i);
                        aList.add(document);
                        MapCircle circle = new MapCircle(
                                MapPoint.mapPointWithGeoCoord(Double.parseDouble(x), Double.parseDouble(y)),
                                10,
                                Color.argb(255,255,0,0),
                                Color.argb(255,255,0,0)
                        );

                        MapPOIItem marker = new MapPOIItem();
                        marker.setItemName(document.getPlaceName());
                        marker.setTag(i);
                        marker.setMapPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(document.getY()), Double.parseDouble(document.getX())));
                        marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
                        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
                        mapView.addPOIItem(marker);
                        mapView.addCircle(circle);
                    }
                    //MapPoint mapPoint= null;
                    //mapPoint = new MapPoint();


                    Log.d(TAG, "onResponse: 성공, 결과\n" + response.body().getDocuments().get(0).getAddressName());
                } else {
                    Log.d(TAG, "onResoponse: 실패");
                }
            }

            @Override
            public void onFailure(Call<KaKaoVO> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });


//        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
 //       mapViewContainer.removeView();

    }


    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String ss = aList.get(mapPOIItem.getTag()).getId();
        //builder.setTitle("콩콩").setMessage(ss);
        //builder.show();
        Intent intent = new Intent(KakaoMap.this, ReviewSelectAll.class);
        intent.putExtra("str", ss);
        intent.putExtra("mid", mid);
        startActivity(intent);
    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }


}

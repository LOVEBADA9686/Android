package g.g.d.com;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Gallery;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import g.g.d.com.R;
import g.g.d.com.common.GogodaUtil;
import g.g.d.com.map.KakaoMap;


public class Emotion extends AppCompatActivity {

//    private static String IP_ADDRESS = "stgarosu.ipdisk.co.kr";
    private static String IP_ADDRESS = GogodaUtil.URL_PATH;
    private String TAG = "Emotion";
    ArrayList<Float> jsonList = new ArrayList<>(); // ArrayList 선언
    ArrayList<String> labelList = new ArrayList<>(); // ArrayList 선언
    ArrayList<String> mArrayList= new ArrayList<>(); // ArrayList 선언
    ArrayList<String> fArrayList= new ArrayList<>(); // ArrayList 선언
    private String[] fList ;
    private String[] mList ;
    ArrayList<String> gArrayList = new ArrayList<>(); // ArrayList 선언
    private String mJsonString;
    private String wMode ;
    private Gallery fGallery;
    private Gallery mGallery;
    EditText food;
    EditText movie;
    private String emotionTop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion);

        fArrayList.clear();
        mArrayList.clear();
        jsonList.clear();
        labelList.clear();
        food = (EditText)findViewById(R.id.food) ;
        movie=(EditText)findViewById(R.id.movie) ;
        fGallery = (Gallery)findViewById(R.id.fGallery);
        mGallery = (Gallery)findViewById(R.id.mGallery);
        food.setText("추천 음식");
        movie.setText("추천 영화");
        fArrayList = new ArrayList<>();
        mArrayList = new ArrayList<>();

        init();

        fGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //SaveSharedPreference.getUserName(Emotion.this)
                LOGDT data = new LOGDT(SaveSharedPreference.getUserName(Emotion.this),fArrayList.get(position) ,"");
                Log.d(TAG, "food - Detail page 전송 ID : "+ fArrayList.get(position) );
                Intent intent = new Intent(getApplicationContext(), KakaoMap.class);/// detail food 정보 클래스 변경
                intent.putExtra("data", data);
                startActivity(intent);
            }
        });

        mGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //mArrayList.get(position)

                LOGDT data = new LOGDT(mArrayList.get(position),"" ,"");
                Log.d(TAG, "Movie - Detail page 전송 ID : "+ position + " : " + mArrayList.get(position));
                Intent intent = new Intent(getApplicationContext(), VMovie.class);/// detail movie 정보 클래스 변경
                intent.putExtra("data", data);
                 startActivity(intent);
            }
        });

    }

    public void init(){

        Log.d(TAG, "init - 시작 ");
        wMode = "emo";
        GetData task = new GetData();
        task.execute( "http://" + IP_ADDRESS + "/springProject/","ref" ,"emo");
        Log.d(TAG, "emo - 마감 ");

    }

    private void BarChartGraph(ArrayList<String> labelList, ArrayList<Float> valList) {
        // BarChart 메소드

        BarChart barChart = (BarChart) findViewById(R.id.chart);
        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
        for(int i=0; i < valList.size();i++){
            entries.add(new BarEntry((Float)valList.get(i), i));
        }


      // barChart.setDescription("");
        /**
         ArrayList NoOfEmp = new ArrayList();
         NoOfEmp.add(new BarEntry(945f, 0));
         NoOfEmp.add(new BarEntry(1040f, 1)); NoOfEmp.add(new BarEntry(1133f, 2));
         NoOfEmp.add(new BarEntry(1240f, 3)); NoOfEmp.add(new BarEntry(1369f, 4));
         NoOfEmp.add(new BarEntry(1487f, 5)); NoOfEmp.add(new BarEntry(1501f, 6));
         NoOfEmp.add(new BarEntry(1645f, 7)); NoOfEmp.add(new BarEntry(1578f, 8));
         NoOfEmp.add(new BarEntry(1695f, 9));
         ArrayList year = new ArrayList();
         year.add("2008"); year.add("2009"); year.add("2010"); year.add("2011");
         year.add("2012"); year.add("2013"); year.add("2014"); year.add("2015");
         year.add("2016"); year.add("2017");
         BarDataSet bardataset = new BarDataSet(NoOfEmp, "No Of Employee");
         chart.animateY(5000); BarData data = new BarData(year, bardataset);
         // MPAndroidChart v3.X 오류 발생 bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
         chart.setData(data);

         **/
        ArrayList<String> labels = new ArrayList<String>();
        for(int i=0; i < labelList.size(); i++){
            labels.add((String) labelList.get(i));
        }
        BarDataSet depenses = new BarDataSet (entries, "감정 요소"); // 변수로 받아서 넣어줘도 됨
        depenses.setAxisDependency(YAxis.AxisDependency.RIGHT);
        BarData data = new BarData(labels, depenses); // 라이브러리 v3.x 사용하면 에러 발생함
        depenses.setColors(ColorTemplate.LIBERTY_COLORS); //

        barChart.setData(data);
        barChart.animateXY(1000,1000);
        barChart.invalidate();
    }

    private class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(Emotion.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

            Log.d(TAG, "response - " + result);

            if (result == null){
                wMode="rsh" ;
                Toast.makeText(getApplicationContext(), "데이터를 갖고오지 못했습니다.", Toast.LENGTH_SHORT).show();
            }
            else {
                mJsonString = result;
                if (wMode=="emo"){
                    graphResult();
                    Log.d(TAG, "Gdata - " + result);

                }
                else if (wMode=="food"){
                    if(result.length() < 20){
                        wMode = "rsh";
                        Toast.makeText(getApplicationContext(), "저장된 내용이 없습니다.", Toast.LENGTH_SHORT).show();
                    }else{
                        foodResult();
                    }
                }
                else if (wMode=="movie"){
                    if(result.length() < 20){
                        wMode = "rsh";
                        Toast.makeText(getApplicationContext(), "저장된 내용이 없습니다.", Toast.LENGTH_SHORT).show();
                    }else{
                        movieResult();
                    }
                }

            }
        }


        @Override
        protected String doInBackground(String... params) {

//            String serverURL = params[0];
            String serverURL = params[0];
            String key = (String) params[1];
            String value = (String) params[2];
            String postParameters = "";
            if (wMode.equals("emo")){
                serverURL += "emotion/androidEmotionSearch.ggd";
                Intent getintent = getIntent();
                String text = getintent.getStringExtra("text").toString();
//                postParameters = key + "=" + value  ;
                postParameters = "text=" + text  ;

            }else if(wMode.equals("food")){
                serverURL += "emotion/androidFoodSearch.ggd";
                LOGDT data = new LOGDT(SaveSharedPreference.getUserName(Emotion.this), "" ,"");
                postParameters = "id=" + data.mid + "&emoText=" + emotionTop;
            }else if(wMode.equals("movie")){
                serverURL += "emotion/androidMovieSearch.ggd";
                LOGDT data = new LOGDT(SaveSharedPreference.getUserName(Emotion.this), "" ,"");
                postParameters = "id=" + data.mid;
            }

//            String key1 = (String) params[3];
//            String value1 = (String) params[4];
//            String postParameters = key + "=" + value +"&" + key1 + "=" + "value1" ;
            //if (wMode=="ins" || wMode=="dbup"){

          //      for (int i=0;i<aList.size();i++){
           //         postParameters = postParameters + "&" ;
           //         postParameters = postParameters + aList.get(i) + "=" + mArrayList.get(i).getSch_Ack().toString();
         //       }
         //   }
            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                bufferedReader.close();
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>> : " + sb.toString());

                return sb.toString().trim();


            } catch (Exception e) {

                Log.d(TAG, "GetData : Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }


    private void movieResult(){

        String TAG_JSON="M";
     //   String TAG_MID = "mid";
        String TAG_MNAME = "movielink";
        String TAG_MIMG = "movieimg";

        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            mList = new String[jsonArray.length()];
            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

             //   String mid = item.getString(TAG_MID);
               String mname = item.getString(TAG_MNAME);
                String mimg = item.getString(TAG_MIMG);
                mList[i] = mimg;
                mArrayList.add(mname);
            }
            GDGalleryAdapter mAdapter = new GDGalleryAdapter(getApplicationContext(), mList);
            mGallery.setAdapter(mAdapter);

        } catch (JSONException e) {

            Log.d(TAG, "movieResult : ", e);
        }

    }

    private void graphResult(){

        String TAG_JSON="result";
        String TAG_MIMG = "mimg";
        Log.d(TAG, "graphResult - 시작 ");

        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            emotionTop = jsonArray.get(0).toString().split(":")[0];
            for(int i=0;i<jsonArray.length();i++){

                String mimg = jsonArray.get(i).toString();
                String[] res = mimg.split(":");
                Float grade = Float.parseFloat(res[1]);

                labelList.add(res[0]);
                jsonList.add(grade);
            }
            Log.d(TAG, "jsonList ( " + jsonList.size() + " ) : " + jsonList.get(0));
            BarChartGraph(labelList, jsonList);
            wMode = "food";
            GetData task = new GetData();
            task.execute( "http://" + IP_ADDRESS + "/springProject/","ref" ,"food");
               Log.d(TAG, "food - 마감 ");

        } catch (JSONException e) {

            Log.d(TAG, "graphResult : ", e);
        }

    }

    private void foodResult(){

        String TAG_JSON="F";
      //  String TAG_MFVID = "mid";
        String TAG_MNAME = "food";
        String TAG_MFIMG = "foodImg";
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            fList = new String[jsonArray.length()];
            for( int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

         //       String mvid = item.getString(TAG_MFVID);
                String mname = item.getString(TAG_MNAME);
                String mimg = "http://"+ GogodaUtil.URL_PATH +"/springProject/img/" + item.getString(TAG_MFIMG) + ".jpg";
                fList[i]=mimg;
                fArrayList.add(mname);
            }

            GDGalleryAdapter fAdapter = new GDGalleryAdapter(getApplicationContext(), fList);
            fGallery.setAdapter(fAdapter);

            wMode = "movie";
            GetData task = new GetData();
            task.execute( "http://" + IP_ADDRESS + "/springProject/","ref" ,"movie");
        } catch (JSONException e) {

            Log.d(TAG, "foodResult : ", e);
        }
    }

    private String DateChange(String sysdate){

        String year = sysdate.split("-")[0];

        String month = sysdate.split("-")[1];

        String day = sysdate.split("-")[2];



        StringBuilder finalString = new StringBuilder();

        finalString.append(year.substring(2,4));

        finalString.append(".");

        finalString.append(month);

        //finalString.append(".");

        //finalString.append(day);

        return finalString.toString();

    }


/**
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.back_btn:

                finish();

                // overridePendingTransition(R.anim.rightin, R.anim.rightout);

                break;

            case R.id.home_btn:

                Intent intent = new Intent(this, Emotion.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                startActivity(intent);

                break;

            default:

                break;

        }

    }
**/
}


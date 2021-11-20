package g.g.d.com.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import g.g.d.com.ApiInterface;
import g.g.d.com.R;
import g.g.d.com.adapter.ListReviewViewAdapter;
import g.g.d.com.common.GogodaUtil;
import g.g.d.com.vo.ReviewJson;
import g.g.d.com.vo.ReviewVO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReviewSelectAll extends AppCompatActivity {

    String sss;
    String kakaoid;
    //Disposable backgroundtask;
    private final String TAG = getClass().getSimpleName();
    TextView info;
    ArrayList<ReviewVO> aList;
    private Button button;

    private ListView listView2;
    private ListReviewViewAdapter adapter;

    // 세션 ID 변경 해야됌
    private String mid;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }



    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.review_list);

        adapter = new ListReviewViewAdapter();
        listView2 = (ListView) findViewById(R.id.review_list);


//        ImageView ima = (ImageView)findViewById(R.id.img_prof);
//        ima.setImageBitmap();

        Intent intent = getIntent();

//        String kakaoid = "";
        kakaoid = intent.getStringExtra("str");
        mid = intent.getStringExtra("mid");

        button = (Button) findViewById(R.id.reviewInsertMove);
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReviewSelectAll.this, ReviewInsert.class);
                intent.putExtra("kakaoid", kakaoid);
                intent.putExtra("mid", mid);
                startActivity(intent);
            }
        });

        //       TextView info = (TextView) findViewById(R.id.tv_info);
        //       System.out.println("ReviewClicked >>> : " + intent.getStringExtra("str"));


//        dbString(kakaoid);

        Gson json = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + GogodaUtil.URL_PATH + "/")
                .addConverterFactory(GsonConverterFactory.create(json))
                .build();

        ApiInterface db = retrofit.create(ApiInterface.class);
        Call<ReviewJson> call = db.getDBReview(kakaoid);


        call.enqueue(new Callback<ReviewJson>() {
            @Override
            public void onResponse(Call<ReviewJson> call, Response<ReviewJson> response) {
                if (response.isSuccessful()) {

//                    listView2.setAdapter(adapter);


                    aList = new ArrayList<ReviewVO>();
                    System.out.println(">>> : " + response.body().getReview_VO());
                    //System.out.println(response.body().getReview_VO().size());

                    if(response.body().getReview_VO() != null && response.body().getReview_VO().size() > 0){
                        for (ReviewVO rvo : response.body().getReview_VO()) {
                            System.out.println(" >>>>>> : rvo >>>>>>>> : " + rvo);
                            try {
                                System.out.println(">>>>>>>>>>>>>content >>>>>>>>>>>>>>" + new String(rvo.getRecontent().getBytes("8859_1"),"UTF-8"));
                                rvo.setRecontent(new String(rvo.getRecontent().getBytes("8859_1"),"UTF-8"));
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            listView2.setAdapter(adapter);
                            aList.add(rvo);
                            adapter.addItem(rvo);


                            adapter.notifyDataSetChanged();
                            listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                    ReviewVO rvo = null;

                                    boolean bool = false;
                                    bool = mid.equals(aList.get(i).getRenickname());
                                    rvo = aList.get(i);
                                    if(!bool){
                                        Intent intent = new Intent(ReviewSelectAll.this, ReviewSelect.class);
                                        intent.putExtra("rvo", rvo);
                                        intent.putExtra("mid", mid);
                                        startActivity(intent);
                                    }else{
                                        Intent intent = new Intent(ReviewSelectAll.this, ReviewSelectUD.class);
                                        intent.putExtra("rvo",rvo);
                                        startActivity(intent);

                                    }
                                }
                            });
                        }
                    }else{
                        Intent intent = new Intent(ReviewSelectAll.this, ReviewNull.class);
                        intent.putExtra("kakaoid", kakaoid);
                        intent.putExtra("mid", mid);
                        startActivity(intent);
                        finish();
                    }


                    System.out.println("aList size() >>> : " + aList.size());
//                    sss = response.body().getReview_VO().get(0).getRenickname();
                    //                   info = (TextView) findViewById(R.id.tv_info);
                    //                  info.setText(sss);

                    Log.d(TAG, "onResponse : 성공");
                } else {
                    Log.d(TAG, "onResponse : 실패");
                }
            }

            @Override
            public void onFailure(Call<ReviewJson> call, Throwable t) {
                Log.d(TAG, "onFailure : " + t.getMessage());
            }
        });


    }

}

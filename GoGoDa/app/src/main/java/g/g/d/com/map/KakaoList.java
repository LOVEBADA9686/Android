package g.g.d.com.map;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import g.g.d.com.ApiInterface;
import g.g.d.com.R;
import g.g.d.com.ReviewRegisterActivity;
import g.g.d.com.adapter.ListViewAdapter;
import g.g.d.com.view.ReviewSelectAll;
import g.g.d.com.vo.Document;
import g.g.d.com.vo.KaKaoVO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class KakaoList extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    private final String API_KEY = "KakaoAK 7f2e06967eed82b02c906c7548de7e0c";
    String sss;
    private ListView listView;
    private ListViewAdapter adapter;
    private ImageView imageView;
    // 세션 ID 변경
    private String mid, keyword;
    private String x, y;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kakao_list_view);




    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent getKakao = getIntent();
        keyword = getKakao.getStringExtra("keyword");
        mid = getKakao.getStringExtra("mid");
        x = getKakao.getStringExtra("x");
        y = getKakao.getStringExtra("y");

        imageView = (ImageView) findViewById(R.id.img_prof);

        adapter = new ListViewAdapter();
        listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(adapter);
        // ArrayList<Document> aList = null;
        ArrayList<Document> aList = new ArrayList<Document>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://dapi.kakao.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface api = retrofit.create(ApiInterface.class);
        Call<KaKaoVO> call = api.getSearchLocation(API_KEY, keyword, y,x,5000);

        call.enqueue(new Callback<KaKaoVO>() {
            @Override
            public void onResponse(Call<KaKaoVO> call, Response<KaKaoVO> response) {
                if(response.isSuccessful()){
                    System.out.println(response.body().getDocuments().get(0).getAddressName());
                    int resSize = response.body().getDocuments().size();
                    for(Document document : response.body().getDocuments()){

                        aList.add(document);
                        ReviewRegisterActivity task = new ReviewRegisterActivity();
                        String rating = "";
                        try {
                            rating = task.execute("RA", document.getId()).get();
                            System.out.println(">>>>>>>>>>>>>> : " + rating);
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        document.setPlaceUrl(rating);
                        adapter.addItem(document);
                    }

                    adapter.notifyDataSetChanged();
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view,
                                                int pos, long l) {

                            Intent intent = new Intent(KakaoList.this,
                                    ReviewSelectAll.class);
                            intent.putExtra("str", aList.get(pos).getId());


                            intent.putExtra("mid", mid);
                            startActivity(intent);
                            //finish();
                        }
                    });
                    Log.d(TAG, "onResponse: 성공, 결과\n" + response.body().getDocuments().get(0).getAddressName());
                }else{
                    Log.d(TAG, "onResoponse: 실패");
                }
            }

            @Override
            public void onFailure(Call<KaKaoVO> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}

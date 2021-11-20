package g.g.d.com.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import g.g.d.com.R;
import g.g.d.com.ReviewRegisterActivity;
import g.g.d.com.vo.ReviewVO;

public class ReviewInsert extends AppCompatActivity {

    EditText recontent;
    TextView renickname;
    RatingBar rerating;
    Button insertBtn;
    String kakaoid, mid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_insert);

        setTitle("리뷰 등록");

        insertBtn = (Button) findViewById(R.id.reviewInsertbtn);

        renickname = (TextView) findViewById(R.id.renicknameIns);
//        repass = (EditText)findViewById(R.id.repassIns);
        recontent = (EditText)findViewById(R.id.recontentIns);
        rerating = (RatingBar)findViewById(R.id.reratingIns);
//        rerating = (EditText)findViewById(R.id.reratingIns);

        Intent intent = getIntent();
        kakaoid = intent.getStringExtra("kakaoid");
        mid = intent.getStringExtra("mid");
        renickname.setText(mid + "님");

        insertBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                try{
                    String result = "";
                    String nickname = renickname.getText().toString();
//                    String pass = repass.getText().toString();
                    String content = recontent.getText().toString();
                    float rating = rerating.getRating();
                    String ratingstr = Float.toString(rating);
                    System.out.println(">>>> : " + nickname);
                    System.out.println(" >>>>>>>>>>>>>>>>>>> : " + content);
                    ReviewVO rvo = null;
                    rvo = new ReviewVO();

//                    rvo.setKakaoid(kakaoid);
//                    rvo.setRenickname(nickname);
//                    rvo.setRepass(pass);
//                    rvo.setRecontent(content);
//                    rvo.setRerating(ratingstr);

                    ReviewRegisterActivity task = new ReviewRegisterActivity();
                    result = task.execute("I",kakaoid, mid, content, ratingstr).get();
                    System.out.println(result);
                    if(result.toUpperCase().equals("GOOD")){
                        finish();
                    }else{
                        Toast.makeText(ReviewInsert.this, "등록 실패", Toast.LENGTH_SHORT).show();
                    }
                }catch(Exception e){
                    Log.i("DBtest", "Error >>>>>>>>>>> : ");
                }
            }
        });
    }
}

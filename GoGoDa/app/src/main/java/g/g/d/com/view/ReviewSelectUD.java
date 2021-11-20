package g.g.d.com.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.util.concurrent.ExecutionException;

import g.g.d.com.R;
import g.g.d.com.ReviewRegisterActivity;
import g.g.d.com.common.GogodaUtil;
import g.g.d.com.vo.ReviewVO;

public class ReviewSelectUD extends AppCompatActivity {

    private Button updatebtn, deletebtn;
    private TextView renickname, reinsertdate;
    private EditText recontent;
    private String kakaoid, renum;
    private RatingBar rating;
    private ImageView rephoto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_select_ud);

        Intent intent = getIntent();
        ReviewVO rvo = (ReviewVO)intent.getSerializableExtra("rvo");
        System.out.println(rvo.getRenickname());

        updatebtn = (Button)findViewById(R.id.updatebtn);
        deletebtn = (Button)findViewById(R.id.deletebtn);

        renickname = (TextView) findViewById(R.id.renicknameud);
        reinsertdate = (TextView) findViewById(R.id.reinsertdateud) ;
        recontent = (EditText) findViewById(R.id.recontentud);
        rating = (RatingBar)findViewById(R.id.reratingud);
        rephoto = (ImageView)findViewById(R.id.rephotoud) ;

        renickname.setText(rvo.getRenickname() + "님");
        reinsertdate.setText(rvo.getReinsertdate());
        recontent.setText(rvo.getRecontent());
        rating.setRating(Float.parseFloat(rvo.getRerating()));
        if(rvo.getRephoto() != null && rvo.getRephoto().length() > 0){
            Picasso.get().load("http://" + GogodaUtil.URL_PATH + "/project_test2/uploadstorage/" + rvo.getRephoto()).into(rephoto);
        }else{
            rephoto.setVisibility(View.GONE);
            //table3.setVisibility(View.GONE);
        }


        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ReviewSelectUD.this);
                builder.setMessage("정말로 수정 하시겠습니까? ");
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String result = "";
                        try{

                            kakaoid = rvo.getKakaoid();
                            renum = rvo.getRenum();
                            System.out.println(renum);
                            ReviewRegisterActivity task = new ReviewRegisterActivity();
                            result = task.execute("U",kakaoid, renum, recontent.getText().toString(), Float.toString(rating.getRating())).get();
                            System.out.println(result);
                            if(result.toUpperCase().equals("GOOD")){
                                finish();
                            }else{
                                Toast.makeText(ReviewSelectUD.this, "수정 실패", Toast.LENGTH_SHORT).show();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }

                    }
                });

                builder.setNegativeButton("아니오", null);
                builder.create().show();
            }
        });

        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ReviewSelectUD.this);
                builder.setMessage("정말로 삭제 하시겠습니까? ");
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String result = "";
                        try{

                            kakaoid = rvo.getKakaoid();
                            renum = rvo.getRenum();
                            System.out.println(renum);
                            ReviewRegisterActivity task = new ReviewRegisterActivity();
                            result = task.execute("D",kakaoid, renum).get();
                            System.out.println(result);
                            if(result.toUpperCase().equals("GOOD")){
                                finish();
                            }else{
                                Toast.makeText(ReviewSelectUD.this, "삭제 실패", Toast.LENGTH_SHORT).show();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }

                    }
                });

                builder.setNegativeButton("아니오", null);
                builder.create().show();

            }
        });

    }
}

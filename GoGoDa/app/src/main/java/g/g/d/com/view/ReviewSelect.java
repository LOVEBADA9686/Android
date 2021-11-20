package g.g.d.com.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import g.g.d.com.R;
import g.g.d.com.common.GogodaUtil;
import g.g.d.com.vo.ReviewVO;

public class ReviewSelect extends AppCompatActivity {

    private TextView renickname, reinsertdate, recontent;
    private RatingBar rerating;
    private ImageView rephoto;
    private TableRow table3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_select);

        Intent intent = getIntent();
        ReviewVO rvo = (ReviewVO)intent.getSerializableExtra("rvo");
        System.out.println(rvo.getRenickname());

        renickname = (TextView)findViewById(R.id.renicknameSel);
        reinsertdate = (TextView)findViewById(R.id.repassSel);
        recontent = (TextView)findViewById(R.id.recontentSel);

        rerating = (RatingBar)findViewById(R.id.reratingSel);
        rephoto = (ImageView)findViewById(R.id.img_proff) ;

        //table3 = (TableRow)findViewById(R.id.table3);

        renickname.setText(rvo.getRenickname() + "님");
        reinsertdate.setText(rvo.getReinsertdate());
        recontent.setText(rvo.getRecontent());

        rerating.setRating(Float.parseFloat(rvo.getRerating()));
        if(rvo.getRephoto() != null && rvo.getRephoto().length() > 0){
            Picasso.get().load("http://" + GogodaUtil.URL_PATH +"/project_test2/uploadstorage/" + rvo.getRephoto()).into(rephoto);
        }else{
            rephoto.setVisibility(View.GONE);
            //table3.setVisibility(View.GONE);
        }

    }
}

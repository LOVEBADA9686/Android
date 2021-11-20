package g.g.d.com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import g.g.d.com.R;
import g.g.d.com.common.GogodaUtil;
import g.g.d.com.vo.ReviewVO;

public class ListReviewViewAdapter extends BaseAdapter {

    private TextView renickname, recontent, reinsertdate;
    private ImageView rephoto;
    private RatingBar rerating;

    private ArrayList<ReviewVO> reviewItemList = new ArrayList<ReviewVO>();

    public ListReviewViewAdapter(){}

    @Override
    public int getCount() {
        return reviewItemList.size();
    }

    @Override
    public Object getItem(int i) {
        return reviewItemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //int pos = i;
        Context context = viewGroup.getContext();

        if (view == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.review_list_item, viewGroup, false);
        }

        renickname = (TextView) view.findViewById(R.id.renickname);
        recontent = (TextView) view.findViewById(R.id.recontent);
        rephoto = (ImageView) view.findViewById(R.id.img_prof);
        rerating = (RatingBar)view.findViewById(R.id.reratingAll);
        reinsertdate = (TextView) view.findViewById(R.id.reinsertdateAll) ;

        ReviewVO rvo = reviewItemList.get(i);

        renickname.setText(rvo.getRenickname());
        recontent.setText(rvo.getRecontent());
        Picasso.get().load("http://" + GogodaUtil.URL_PATH + "/springProject/reviewuploadstorage/" + rvo.getRephoto()).into(rephoto);
        rerating.setRating(Float.parseFloat(rvo.getRerating()));
        reinsertdate.setText(rvo.getReinsertdate().substring(0, 10));

        return view;
    }

    public void addItem(String renickname, String recontent, String rephoto, String rerating, String reinsertdate){
        ReviewVO rvo = new ReviewVO();

        rvo.setRenickname(renickname);
        rvo.setRecontent(recontent);
        rvo.setRephoto(rephoto);
        rvo.setRerating(rerating);
        rvo.setReinsertdate(reinsertdate);
    }

    public void addItem(ReviewVO rvo){
        reviewItemList.add(rvo);
    }
}

package g.g.d.com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import g.g.d.com.R;
import g.g.d.com.vo.Document;

public class ListViewAdapter extends BaseAdapter {

    private TextView placeName, placeAddr, placePhone, ratingavg, amount;
    private RatingBar ratingBar;
    private ArrayList<Document> listViewItemList = new ArrayList<Document>();



    public ListViewAdapter(){}


    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public Object getItem(int i) {
        return listViewItemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final int pos = i;
        final Context context = viewGroup.getContext();

        if (view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item, viewGroup, false);
        }

        placeName = (TextView) view.findViewById(R.id.placeName);
        placeAddr = (TextView) view.findViewById(R.id.placeAddr);
        placePhone = (TextView) view.findViewById(R.id.placePhone);
        ratingavg = (TextView) view.findViewById(R.id.ratingavg);
        amount = (TextView) view.findViewById(R.id.amount);
        ratingBar = (RatingBar) view.findViewById(R.id.list_rating);

        Document doc = listViewItemList.get(i);
        if(doc.getPlaceUrl() != null && doc.getPlaceUrl().length() > 0){
            String rating[] = doc.getPlaceUrl().split(",");
            if(rating[0].length() > 3) {
                ratingavg.setText(rating[0].substring(0, 3));
                ratingBar.setRating(Float.parseFloat(rating[0].substring(0, 3)));
            }else{
                ratingavg.setText(rating[0]);
                ratingBar.setRating(Float.parseFloat(rating[0]));
            }
            amount.setText(" (" + rating[1] + ")");

        }

        placeName.setText(doc.getPlaceName());
        placeAddr.setText(doc.getAddressName());
        placePhone.setText(doc.getPhone());
        ratingBar.setStepSize(Float.parseFloat("0.1"));

        /*
        LinearLayout cmdArea = (LinearLayout)view.findViewById(R.id.cmdArea);
        cmdArea.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                Toast.makeText(v.getContext(), listViewItemList.get(pos).getId(), Toast.LENGTH_SHORT).show();
            }
        });
         */
        return view;
    }

    public void addItem(String placeName, String placeAddr, String placePhone){
        Document doc = new Document();

        doc.setPlaceName(placeName);
        doc.setAddressName(placeAddr);
        doc.setPhone(placePhone);
    }

    public void addItem(Document document){
        listViewItemList.add(document);
    }
}

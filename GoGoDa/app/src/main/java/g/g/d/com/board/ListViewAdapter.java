package g.g.d.com.board;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import g.g.d.com.R;

// 게시글 리스트 뷰 어댑터 및 필터
public class ListViewAdapter extends BaseAdapter implements Filterable {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList (원본 데이터 리스트)
    private ArrayList<ListViewItem> listViewItemList;

    // 필터링 된 결과 데이터를 저장하기 위한 ArrayList : 최초에는 전체 리스트 보유
    private ArrayList<ListViewItem> filteredItemList;

    Filter listFilter;

    // ListViewAdapter의 생성자
    public ListViewAdapter() {
        this.listViewItemList = new ArrayList<ListViewItem>();
        filteredItemList = listViewItemList;
    }
    public ListViewAdapter(ArrayList<ListViewItem> listViewItemList) {
        this.listViewItemList = listViewItemList;
        filteredItemList = listViewItemList;
    }

    // Adapter에 사용되는 데이터의 개수를 리턴
    @Override
    public int getCount() {
        return filteredItemList.size();
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // list_item 레이아웃을 inflate하여 convertView 참조 획득
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_board, parent, false);
        }

        // 화면에 표시될 View(레이아웃이 infalte된) 으로부터 위젯에 대한 참조 획득
        ImageView bfile = (ImageView) convertView.findViewById(R.id.bfile);
        TextView bsubject = (TextView) convertView.findViewById(R.id.bsubject);
        TextView bname = (TextView) convertView.findViewById(R.id.bname);
        TextView binsertdate = (TextView) convertView.findViewById(R.id.binsertdate);

        // Data set(listViewItemList) 에서 position에 위치한 데이터 참조 획득
        ListViewItem listViewItem = filteredItemList.get(position);


        // 아이템 내 각 위젯에 데이터 반영


        bfile.setImageBitmap(listViewItem.getFile());
        bsubject.setText(listViewItem.getBsubject());
        bname.setText(listViewItem.getBname());
        binsertdate.setText(listViewItem.getBinsertdate());

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 지정된 위치(position)에 있는 데이터 리턴
    @Override
    public Object getItem(int position) {
        return filteredItemList.get(position);
    }

    // 아이템 데이터 추가를 위한 함수
    public void addItem(JSONObject object) {
        ListViewItem item = new ListViewItem(object);
        listViewItemList.add(item);
    }

    // 검색 필터 : getFilter() 함수 override
    public Filter getFilter() {
        if (listFilter == null) {
            listFilter = new ListFilter();
        }
        return listFilter;
    }

    private class ListFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if(constraint == null || constraint.length() == 0) {
                results.values = listViewItemList;
                results.count = listViewItemList.size();
            } else {
                ArrayList<ListViewItem> itemList = new ArrayList<ListViewItem>();

                for (ListViewItem item : listViewItemList) {
                    if (item.getBsubject().toUpperCase().contains(constraint.toString().toUpperCase())) {
                        itemList.add(item);
                    }
                }

                results.values = itemList;
                results.count = itemList.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            // update listView by filtered data list.
            filteredItemList = (ArrayList<ListViewItem>) results.values;

            // notify
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetChanged();
            }
        }
    }
}

package g.g.d.com.board;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import g.g.d.com.R;

// 댓글 리스트뷰 어댑터
public class RboardAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장히기 위한 ArrayList
    private ArrayList<RboardListViewItem> rboardItemList;

    // RboardAdapter 의 생성자
    public RboardAdapter() {
        rboardItemList = new ArrayList<>();
    }
    public RboardAdapter(ArrayList<RboardListViewItem> items) {
        rboardItemList = items;
        this.notifyDataSetChanged();
    }

    public void reset(ArrayList<RboardListViewItem> items) {
        rboardItemList = items;
        this.notifyDataSetChanged();
    }

    // Adapter에 사용되는 데이터의 개수를 리턴
    @Override
    public int getCount() {
        return rboardItemList.size();
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // rboard_list_item 의 layout을 inflate하여 convertView 참조 획득
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.rboard_list_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView rbname = (TextView) convertView.findViewById(R.id.rbname);
        TextView rbcontent = (TextView) convertView.findViewById(R.id.rbcontent);
        TextView rbdate = (TextView) convertView.findViewById(R.id.rbdate);

        // Data Set(rboardListViewItems)에서 position에 위치한 데이터 참조 획득
        RboardListViewItem rboardListViewItem = rboardItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        rbname.setText(rboardListViewItem.getRbname());
        rbcontent.setText(rboardListViewItem.getRbcontent());
        rbdate.setText(rboardListViewItem.getRbdate());

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row) 의 ID를 리턴
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 지정한 위치(position)에 있는 데이터 리턴
    @Override
    public Object getItem(int position) {
        return rboardItemList.get(position);
    }


}

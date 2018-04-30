package companydomain.nctmanage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by YUJIN on 2018-04-29.
 */

public class CustomChoiceListViewAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<CheckListItem> CheckListItemList = new ArrayList<CheckListItem>() ;

    // ListViewAdapter의 생성자
    public CustomChoiceListViewAdapter() {

    }

    public CustomChoiceListViewAdapter(Context context, ArrayList<HashMap<String, String>> list, int checklist_item, String[] from, int[] to){

    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return CheckListItemList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.checklist_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView check_id = (TextView) convertView.findViewById(R.id.check_id);
        TextView check_item = (TextView) convertView.findViewById(R.id.check_item);
        TextView check_name = (TextView) convertView.findViewById(R.id.check_name);

        // Data Set(CheckListItemList)에서 position에 위치한 데이터 참조 획득
        CheckListItem CheckListItem = CheckListItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        check_id.setText(CheckListItem.getId());
        check_item.setText(CheckListItem.getItem());
        check_name.setText(CheckListItem.getId());
        //iconImageView.setImageDrawable(CheckListItem.getIcon());
        //textTextView.setText(CheckListItem.getText());

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return CheckListItemList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String id, String item, String name) {
        CheckListItem items = new CheckListItem();

        items.setId(id);
        items.setItem(item);
        items.setName(name);

        CheckListItemList.add(items);
    }
}


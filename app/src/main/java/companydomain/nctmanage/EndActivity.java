package companydomain.nctmanage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.Arrays;

public class EndActivity extends AppCompatActivity {

    TextView textView_information;
    TextView app;
    TextView reg;
    TextView date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout);

        Intent toEnd = getIntent();
        String[] information = toEnd.getStringArrayExtra("information");
        String app_id = toEnd.getStringExtra("app_id");
        String reg_num = toEnd.getStringExtra("reg_num");
        String due_date = toEnd.getStringExtra("due_date");

        textView_information = (TextView)findViewById(R.id.end_information);
        textView_information.setText(Arrays.toString(information));

        app = (TextView) findViewById(R.id.end_appid);
        app.setText(app_id);

        reg = (TextView)findViewById(R.id.end_regnum);
        reg.setText(reg_num);

        date = (TextView)findViewById(R.id.end_duedate);
        date.setText(due_date);

    }
    public void ButtonList(View v){
        Intent toList = new Intent (EndActivity.this, ListActivity.class);
        startActivity(toList);
    }
}

package companydomain.nctmanage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by YUJIN on 2018-03-22.
 */

public class CheckActivity extends AppCompatActivity {

    TextView method;
    TextView notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

    }

    public void NextClicked(View v){
        Intent toCheck2 = new Intent(CheckActivity.this, CheckActivity2.class);
        startActivity(toCheck2);
    }

    public void PreviousClicked(View v){
        //Intent toCheck2 = new Intent(CheckActivity.this, CheckActivity2.class);
        //startActivity(toCheck2);
    }

    public void ReadMore1Clicked(View v){
        method = (TextView)findViewById(R.id.Method);
        method.setMaxLines(20);
    }
    public void ReadMore2Clicked(View v){
        notes = (TextView)findViewById(R.id.Notes);
        notes.setMaxLines(20);
    }
}

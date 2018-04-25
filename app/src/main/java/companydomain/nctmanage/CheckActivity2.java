package companydomain.nctmanage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class CheckActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check2);
    }

    public void Next2Clicked(View v){
        Intent toEnd = new Intent(CheckActivity2.this, EndActivity.class);
        startActivity(toEnd);
    }

    public void Precious2Clicked(View v){
        Intent toPrevious = new Intent(CheckActivity2.this, CheckActivity.class);
        startActivity(toPrevious);
    }
}

package companydomain.nctmanage;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import static companydomain.nctmanage.R.id.check_listview;

/**
 * Created by YUJIN on 2018-03-22.
 */

public class CheckActivity extends AppCompatActivity {

    String myJSON;

    String[] information;
    //String[] checkResult;

    TextView testTitle;
    TextView testDescription;
    TextView testNotes;

    Button button1;
    Button button2;

    EditText comments;
    String checkResult = "";

    ScrollView checkScroll;

    private int index = 1; //service brake pedal , ...

    private static final String TAG_RESULTS = "steps";

    private static final String TAG_TESTID = "id";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_NOTES = "notes";

    private static final String TAG_FAILID = "id";
    private static final String TAG_ITEM = "item";
    private static final String TAG_NAME = "name";

    int stepIndex;
    String appointmentId;

    //String idValue;
    //String dateValue;
    //String registrationValue;

    JSONArray peoples = null;
    JSONArray failure = null;

    ArrayList<HashMap<String, String>> personList;

    ListView list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout);

        testDescription = (TextView) findViewById(R.id.test_description);
        testDescription.setMaxLines(3);

        testNotes = (TextView) findViewById(R.id.test_notes);
        testNotes.setMaxLines(3);

        list = (ListView) findViewById(check_listview);

        personList = new ArrayList<HashMap<String, String>>();
        Intent toCheck = getIntent();

        appointmentId = CollectFailure.instance.GetStringAppointmentId();
        stepIndex = toCheck.getIntExtra("stepIndex",0);
        //registrationValue=toCheck.getStringExtra("registration");
        //dateValue=toCheck.getStringExtra("date");

        String url = "https://dev.cute.enterprises/api/mechanic/test/" + appointmentId + "/";
        getData(url);


    }

    protected void showList() {
        try {
            personList.clear();
            //get steps array
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);

            JSONObject data = peoples.getJSONObject(stepIndex);

            String testId = data.getString(TAG_TESTID); //didn't use
            String description = data.getString(TAG_DESCRIPTION);
            String notes = data.getString(TAG_NOTES);
            String testName = data.getString("name");

            testTitle = (TextView) findViewById(R.id.test_title);
            testTitle.setText(testName);

            testDescription = (TextView) findViewById(R.id.test_description);
            testDescription.setText(description);

            testNotes = (TextView) findViewById(R.id.test_notes);
            testNotes.setText(notes);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void getData(String url) {
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];

                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }

                    return sb.toString().trim();

                } catch (Exception e) {
                    return null;
                }


            }

            @Override
            protected void onPostExecute(String result) {
                myJSON = result;
                //Toast.makeText(CheckActivity.this ,myJSON,Toast.LENGTH_LONG).show();
                showList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }



    public void NextClicked(View v)//next누르면 checkactivity2로 넘어간다.
    {
        Log.i("테스트N1",String.valueOf(stepIndex));
       Intent toCheck2 =  new Intent(CheckActivity.this, CheckActivity2.class);
        toCheck2.putExtra("stepIndex",stepIndex);
        startActivity(toCheck2);

        //String url = "https://dev.cute.enterprises/api/mechanic/test/" + idValue + "/";
        //getData(url);
        /*checkScroll = (ScrollView) findViewById(R.id.check_scrollView);
        checkScroll.post(new Runnable() {
            public void run() {
                checkScroll.scrollTo(0, 0);
            }
        });*/

        if (stepIndex > 2) {
            Intent toEnd = new Intent(CheckActivity.this, EndActivity.class);

            //toEnd.putExtra("app_id", idValue);
            //toEnd.putExtra("reg_num", registrationValue);
            //toEnd.putExtra("due_date", dateValue);
            //toEnd.putExtra("information", information);
            //toEnd.putExtra("checkResult", checkResult);
            startActivity(toEnd);
        }
    }

    public void PreviousClicked(View v) {

        Log.i("테스트P1",String.valueOf(stepIndex));
        if(stepIndex==0){
            Toast.makeText(CheckActivity.this, "Go back to appointment list", Toast.LENGTH_SHORT).show();
            Intent toList = new Intent(CheckActivity.this, ListActivity.class);
            CollectFailure.instance.SetInit();
            startActivity(toList);
        }
        else
        {
            stepIndex--;
            Intent toCheck2 = new Intent(CheckActivity.this, CheckActivity2.class);
            toCheck2.putExtra("stepIndex", stepIndex);
            CollectFailure.instance.ClickPrev();
            Toast.makeText(CheckActivity.this, CollectFailure.instance.GetJSonFailureIds().toString(), Toast.LENGTH_LONG).show();
            startActivity(toCheck2);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void ReadMore1Clicked(View v) {

        testDescription = (TextView) findViewById(R.id.test_description);
        button1 = (Button) findViewById(R.id.ReadMoreBtn1);
        int line = testDescription.getMaxLines();

        if(line==3){
            button1.setText("READ LESS");
            testDescription.setMaxLines(100);}
        else{
            button1.setText("READ MORE");
            testDescription.setMaxLines(3);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void ReadMore2Clicked(View v) {

        testNotes = (TextView) findViewById(R.id.test_notes);
        button2 = (Button) findViewById(R.id.ReadMoreBtn2);
        int line2 = testNotes.getMaxLines();
        if(line2==3) {
            button2.setText("READ LESS");
            testNotes.setMaxLines(100);
        }
        else {
            button2.setText("READ MORE");
            testNotes.setMaxLines(3);
        }

    }

}

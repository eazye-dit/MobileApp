package companydomain.nctmanage;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
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

public class CheckActivity2 extends AppCompatActivity {

    int stepIndex;
    int steps=2;
    String myJSON;

    TextView test_Id2;
    TextView testTitle2;

    EditText comment;
    String commentResult;
    ScrollView Scroll;

    private int index = 1; //service brake pedal , ...

    private static final String TAG_RESULTS = "steps";

    private static final String TAG_FAILID = "id";
    private static final String TAG_ITEM = "item";
    private static final String TAG_NAME = "name";

    String appointmentId;
    //String dateValue;
    //String registrationValue;

    JSONArray peoples = null;
    JSONArray failure = null;

    ArrayList<HashMap<String, String>> personList;

    ListView list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check2);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout);

        list = (ListView) findViewById(check_listview);
        personList = new ArrayList<HashMap<String, String>>();

        Intent toCheck = getIntent();

        appointmentId = CollectFailure.instance.GetStringAppointmentId();
        stepIndex = toCheck.getIntExtra("stepIndex",0);

        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        String url = "https://dev.cute.enterprises/api/mechanic/test/" + appointmentId + "/";

        getData(url);

        //new SendServer().execute("https://dev.cute.enterprises/api/login/");
    }


    protected void showList() {
        try {
            personList.clear();
            //get steps array
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);

            Log.i("JSON_CHECK",peoples.toString());

            //get failure array
            JSONObject data = peoples.getJSONObject(stepIndex);
            JSONArray fail = data.getJSONArray("failures");

            String testName = data.getString("name");
            String testId = data.getString("id");


            test_Id2 = (TextView)findViewById(R.id.test_id2);
            test_Id2.setText(testId+". ");

            testTitle2 = (TextView) findViewById(R.id.test_title2);
            testTitle2.setText(testName);


            for (int i = 0; i < fail.length(); i++)
            {
                JSONObject c = fail.getJSONObject(i);
                String id = c.getString(TAG_FAILID);
                String item = c.getString(TAG_ITEM);
                String name = c.getString(TAG_NAME);

                HashMap<String, String> persons = new HashMap<String, String>();

                persons.put(TAG_FAILID, id);
                persons.put(TAG_ITEM, item);
                persons.put(TAG_NAME, name);

                personList.add(persons);

            }

            final ListAdapter adapter = new SimpleAdapter(
                    CheckActivity2.this, personList, R.layout.checklist_item,
                    new String[]{TAG_FAILID, TAG_ITEM, TAG_NAME,},
                    new int[]{R.id.check_id, R.id.check_item, R.id.check_name,}
            );

            list.setAdapter(adapter);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    String str = personList.get(position).get(TAG_FAILID);
                    //parsing for getting id value for CheckActivity
                    //String result = personList.get(position).toString();

                    CollectFailure.instance.ClickFailureId(str);

                    //Toast.makeText(CheckActivity2.this ,"this is : "+str, Toast.LENGTH_SHORT).show();
                    //Toast.makeText(CheckActivity.this ,personList.get(position).toString(), Toast.LENGTH_SHORT).show();


                }
            });

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



    public void Next2Clicked(View v)//마지막이면 endactivity열고 아니면 다음 stepindex의 checkactivity연다.
    {
        Log.i("테스트N2",String.valueOf(stepIndex));
        stepIndex++;

        comment = (EditText)findViewById(R.id.test_comment);
        commentResult=comment.getText().toString();
        CollectFailure.instance.ClickNext(commentResult);
        Toast.makeText(CheckActivity2.this, CollectFailure.instance.GetJSonFailureIds().toString(), Toast.LENGTH_LONG).show();
        Intent toCheck = new Intent(CheckActivity2.this, CheckActivity.class);
        toCheck.putExtra("stepIndex",stepIndex);
        startActivity(toCheck);

    //끝난경우
        if (stepIndex > steps) {
            Intent toEnd = new Intent(CheckActivity2.this, EndActivity.class);

            startActivity(toEnd);
        }
    }

    public void Previous2Clicked(View v) //이전에 checkactivity1화면 나올거임
    {
        //Log.i("테스트P2",String.valueOf(stepIndex));
        Intent toCheck = new Intent(CheckActivity2.this, CheckActivity.class);
        toCheck.putExtra("stepIndex",stepIndex);
        startActivity(toCheck);
        /*
        checkScroll = (ScrollView) findViewById(R.id.check_scrollView);
        checkScroll.post(new Runnable() {
            public void run() {
                checkScroll.scrollTo(0, 0);
            }
        });
        */

    }
}

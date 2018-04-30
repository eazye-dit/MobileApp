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
import static companydomain.nctmanage.R.id.test_comments;

/**
 * Created by YUJIN on 2018-03-22.
 */

public class CheckActivity extends AppCompatActivity {

    int stepIndex = 0;
    String checkResult;

    String myJSON;

    String[] information;

    TextView testTitle;
    TextView testDescription;
    TextView testNotes;

    EditText comments;
    TextView checkId;
    String checkResult = null;

    ScrollView checkScroll;

    private int index = 1; //service brake pedal , ...

    private static final String TAG_RESULTS = "steps";

    private static final String TAG_TESTID = "id";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_NOTES = "notes";

    private static final String TAG_FAILID = "id";
    private static final String TAG_ITEM = "item";
    private static final String TAG_NAME = "name";

    String idValue;
    String dateValue;
    String registrationValue;

    JSONArray peoples = null;
    JSONArray failure = null;

    ArrayList<HashMap<String, String>> personList;

    ListView list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        //CustomChoiceListViewAdapter adapter;

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout);

        list = (ListView) findViewById(check_listview);
        information = new String[3];
        personList = new ArrayList<HashMap<String, String>>();
        //getData("http://192.168.43.135/getcarlist.php"); //수정 필요
        //getData("https://dev.cute.enterprises/api/mechanic/test/<id>/");

        Intent toCheck = getIntent();

        idValue = toCheck.getStringExtra("appointmentId");
        registrationValue=toCheck.getStringExtra("registration");
        dateValue=toCheck.getStringExtra("date");

        //Toast.makeText(getApplicationContext(), "ID : "+ idValue +"& registration" + registrationValue + "& date"+dateValue , Toast.LENGTH_SHORT).show();
        //Toast.makeText(getApplicationContext(), "test : "+ test , Toast.LENGTH_SHORT).show();
        //Toast.makeText(getApplicationContext(), "commentResult : " + commentResult + "& checkResult : " + checkResult, Toast.LENGTH_SHORT).show();

        String url = "https://dev.cute.enterprises/api/mechanic/test/" + idValue + "/";
        //Toast.makeText(getApplicationContext(), "url : "+ url, Toast.LENGTH_SHORT).show();

        getData(url);

        //new SendServer().execute("https://dev.cute.enterprises/api/login/");

    }

    protected void showList() {
        try {
            personList.clear();
            //steps array 받아오기
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);
            Log.i("피플스",peoples.toString());
            //failure array 받아오기
            JSONObject data = peoples.getJSONObject(stepIndex); //0은 페이지 넘어갈 때마다 index로 바뀌게 될 것.
            JSONArray fail = data.getJSONArray("failures");
             Log.i("퓨진이",String.valueOf(stepIndex));
             Log.i("퓨진이2",String.valueOf(fail));

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

            for (int i = 0; i < fail.length(); i++)
            {
                JSONObject c = fail.getJSONObject(i);

                //JSONObject failure = c.getJSONObject(TAG_FAILURE);
                //Toast.makeText(CheckActivity.this ,failure.toString(),Toast.LENGTH_LONG).show();
                String id = c.getString(TAG_FAILID);
                String item = c.getString(TAG_ITEM);
                String name = c.getString(TAG_NAME);

                HashMap<String, String> persons = new HashMap<String, String>();

                persons.put(TAG_FAILID, id);
                persons.put(TAG_ITEM, item);
                persons.put(TAG_NAME, name);

                personList.add(persons);

            }
            Log.i("personList",personList.toString());
            /*
            final ListAdapter adapter = new CustomChoiceListViewAdapter(
                    CheckActivity.this, personList, R.layout.checklist_item,
                    new String[]{TAG_FAILID, TAG_ITEM, TAG_NAME,},
                    new int[]{R.id.check_id, R.id.check_item, R.id.check_name,}
            );
            */

            final ListAdapter adapter = new SimpleAdapter(
                    CheckActivity.this, personList, R.layout.checklist_item,
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

                    checkResult += str;

                    Toast.makeText(CheckActivity.this ,"this is : "+str, Toast.LENGTH_SHORT).show();
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



    public void NextClicked(View v)//모든 값을 server에 넘겨주고, intent를 통해 다음 activity 를 연다.
    {
        comments = (EditText) findViewById(test_comments);
        String commentResult = comments.getText().toString(); //checkResult결과와 함께 server로 보내기

        comments.setText("");

        //information = new String[10];
        information[stepIndex] = "";
        information[stepIndex] += testTitle.getText() + " : " + commentResult+"\n";
        Log.i("오유진",String.valueOf(stepIndex));
        Log.i("오유진2",information[stepIndex]);
        stepIndex++;

        testDescription = (TextView) findViewById(R.id.test_description);
        testDescription.setMaxLines(3);

        testNotes = (TextView) findViewById(R.id.test_notes);
        testNotes.setMaxLines(3);

        String url = "https://dev.cute.enterprises/api/mechanic/test/" + idValue + "/";
        getData(url);
        checkScroll = (ScrollView) findViewById(R.id.check_scrollView);
        checkScroll.post(new Runnable() {
            public void run() {
                checkScroll.scrollTo(0, 0);
            }
        });

        if (stepIndex > 2) {
            Intent toEnd = new Intent(CheckActivity.this, EndActivity.class);

            toEnd.putExtra("app_id", idValue);
            toEnd.putExtra("reg_num", registrationValue);
            toEnd.putExtra("due_date", dateValue);
            toEnd.putExtra("information", information);
            startActivity(toEnd);
        }
    }

    public void PreviousClicked(View v) {

        stepIndex--;
        Log.i("테스트",String.valueOf(stepIndex));
        if(stepIndex<0){
            Toast.makeText(CheckActivity.this, "Go back to appointment list", Toast.LENGTH_SHORT).show();
            stepIndex++;
            Intent toList = new Intent(CheckActivity.this, ListActivity.class);
            startActivity(toList);
        }
        information[stepIndex] = "";

        Log.i("-오유진",String.valueOf(stepIndex));
        Log.i("-오유진2",information[stepIndex]);

        testDescription = (TextView) findViewById(R.id.test_description);
        testDescription.setMaxLines(3);

        testNotes = (TextView) findViewById(R.id.test_notes);
        testNotes.setMaxLines(3);

        String url = "https://dev.cute.enterprises/api/mechanic/test/" + idValue + "/";
        getData(url);
        checkScroll = (ScrollView) findViewById(R.id.check_scrollView);
        checkScroll.post(new Runnable() {
            public void run() {
                checkScroll.scrollTo(0, 0);
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void ReadMore1Clicked(View v) {

        testDescription = (TextView) findViewById(R.id.test_description);
        int line = testDescription.getMaxLines();
        if(line==3)
            testDescription.setMaxLines(100);
        else
            testDescription.setMaxLines(3);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void ReadMore2Clicked(View v) {

        testNotes = (TextView) findViewById(R.id.test_notes);
        int line2 = testNotes.getMaxLines();
        if(line2==3)
            testNotes.setMaxLines(100);
        else
            testNotes.setMaxLines(3);

    }

       public void checkClicked(View v) {
        checkId = (TextView) findViewById(R.id.check_id);
        checkResult = checkId.getText().toString();
           Log.i("체크박스",checkResult);

    }

}

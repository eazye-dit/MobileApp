
package companydomain.nctmanage;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import static companydomain.nctmanage.R.id.car_listView;


public class ListActivity extends AppCompatActivity {

    String myJSON;

    /*private static final String TAG_RESULTS = "result";
    private static final String TAG_REGNUM = "regnum";
    private static final String TAG_DUEDATE = "duedate";
    private static final String TAG_TESTED = "tested";*/
    private static final String TAG_ID = "id";
    private static final String TAG_RESULTS = "appointments";
    private static final String TAG_VEHICLE = "vehicle";
    private static final String TAG_TESTED = "completed";
    private static final String TAG_REGNUM = "registration";
    private static final String TAG_DUEDATE = "date";

    JSONArray peoples = null;

    ArrayList<HashMap<String, String>> personList;

    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout);

        list = (ListView) findViewById(car_listView);
        personList = new ArrayList<HashMap<String, String>>();
        //getData("http://192.168.43.135/getcarlist.php"); //수정 필요
        getData("https://dev.cute.enterprises/api/mechanic/appointments/");
    }


    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);

            //Toast.makeText(ListActivity.this ,jsonObj.toString(),Toast.LENGTH_LONG).show();

            peoples = jsonObj.getJSONArray(TAG_RESULTS);
            for (int i = 0; i < peoples.length(); i++) {
                JSONObject c = peoples.getJSONObject(i);
                String id = c.getString(TAG_ID);
                JSONObject vehicle = c.getJSONObject(TAG_VEHICLE);
                String regnum = vehicle.getString(TAG_REGNUM);
                String duedate = c.getString(TAG_DUEDATE);
                String tested = c.getString(TAG_TESTED);

                String dating[] = duedate.split(":");
                duedate = dating[0]+":"+dating[1];

                HashMap<String, String> persons = new HashMap<String, String>();

                persons.put(TAG_ID, id);
                persons.put(TAG_REGNUM, regnum);
                persons.put(TAG_DUEDATE, duedate);
                persons.put(TAG_TESTED, tested);

                personList.add(persons);
            }

            final ListAdapter adapter = new SimpleAdapter(
                    ListActivity.this, personList, R.layout.list_item,
                    new String[]{TAG_ID, TAG_REGNUM, TAG_DUEDATE, TAG_TESTED},
                    new int[]{R.id.car_id, R.id.car_regnum, R.id.car_duedate, R.id.car_tested}
            );

            list.setAdapter(adapter);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    Intent toCheck = new Intent(ListActivity.this, CheckActivity.class);   // 인텐트 처리
                    //Toast.makeText(ListActivity.this ,personList.get(position).toString(), Toast.LENGTH_LONG).show();

                    //parsing for getting id value for CheckActivity
                    String result = personList.get(position).toString();
                    String spliting[] = result.split(",");

                    String dateResult=spliting[1]+","+spliting[2];
                    String[] dateResult2=dateResult.split("=");
                    String dateResult3=dateResult2[1];
                    Log.i("시간최종",dateResult2[1]);
                    //date = dateResult2[1]

                    String result2 = spliting[3];
                    String spliting2[] = result2.split("=");

                    String reg = spliting[4];
                    String regSplit[] = reg.split("=");

                    String reg2 = regSplit[1];
                    String regSplit2[] = reg2.split("\\}");
                    String reg3 = regSplit2[0];
                    //registration number = reg3

                    String result3 = spliting2[1];
                    //String spliting3[] = result3.split("\\}");

                    //Toast.makeText(ListActivity.this ,"Registration ID = " + spliting3[0], Toast.LENGTH_LONG).show();

                    toCheck.putExtra("appointmentId",result3);
                    toCheck.putExtra("date",dateResult2[1]);
                    toCheck.putExtra("registration",reg3);
                    startActivity(toCheck);
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
                showList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }


}

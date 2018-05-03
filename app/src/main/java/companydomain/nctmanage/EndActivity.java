package companydomain.nctmanage;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class EndActivity extends AppCompatActivity {

    String result;
    String appointmentId;
    TextView textView_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout);

        result = CollectFailure.instance.GetJSonFailureIds();

        textView_result = (TextView)findViewById(R.id.end_result);
        textView_result.setText(result);
        appointmentId = CollectFailure.instance.GetStringAppointmentId();

        String url = "https://dev.cute.enterprises/api/mechanic/test/" + appointmentId + "/";

        Log.i("유알엘",url);
        new SendServer().execute(url);//AsyncTask start

    }
    public void ButtonList(View v){
        Intent toList = new Intent (EndActivity.this, ListActivity.class);
        CollectFailure.instance.SetInit();
        startActivity(toList);

    }


    public class SendServer extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... urls) {

            try {
                String body = result;
                HttpURLConnection con = null;
                BufferedReader reader = null;
                Log.i("바디",result);

                try{

                    //URL url = new URL("http://192.168.0.171/login.php");

                    URL url = new URL(urls[0]);

                    //connect

                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");//Using post method
                    con.setDoOutput(true);//Outstream, post data send
                    con.setDoInput(true);//Inputstream, get response from server
                    con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    con.connect();

                    //making stream for sending server
                    OutputStream outStream = con.getOutputStream();

                    //making buffer and put it
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));

                    //writer.write(jsonObject.toString());
                    writer.write(body); writer.flush(); writer.close();//get buffer

                    //get data from server
                    return String.valueOf(con.getResponseCode());

                } catch (MalformedURLException e){

                    e.printStackTrace();

                } catch (IOException e) {

                    e.printStackTrace();

                } finally {

                    if(con != null){
                        con.disconnect();
                    }

                    try {
                        if(reader != null){
                            reader.close();//close buffer
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return "false";

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if(result.equals("200")) {
                Toast.makeText(getApplicationContext(), "Succeed sending" , Toast.LENGTH_SHORT).show();
            }
            else if(result.equals("400")) {
                Toast.makeText(getApplicationContext(), "JSON not formed well" , Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "Fail sending", Toast.LENGTH_SHORT).show();
            }

            //have to check message work or not
        }

    }

}


package companydomain.nctmanage;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.util.Log.i;


public class MainActivity extends AppCompatActivity {

    //EditText idText = (EditText) findViewById(R.id.idText);
    //EditText passwordText = (EditText) findViewById(R.id.passwordText);
    public String message;
    public static String id;
    public static String password;
    EditText EditTextid;
    EditText EditTextpassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent toSplash = new Intent(this,SplashActivity.class);
        startActivity(toSplash);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout);
    }

    public void ButtonLogin(View v){
        //new activity_provide car list starts

        EditTextid = (EditText) findViewById(R.id.idText);
        EditTextpassword = (EditText) findViewById(R.id.passwordText);

        id = EditTextid.getText().toString();
        password = EditTextpassword.getText().toString();

        i("id",id);

        new SendServer().execute("http://192.168.43.52:3000/getState");//AsyncTask start
        //https://dev.cute.enterprises/api/login/
        //Log.i("message",message);
        //this message comes first, Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show(); //value from server "successful / invalid"


        }



    public class SendServer extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... urls) {

            try {
                //making JSONObject and save key value
                i("id2",id);
               // Toast.makeText(getApplicationContext(),id+" "+password, Toast.LENGTH_SHORT).show();
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("id", id);
                jsonObject.accumulate("password",password);
                //String body = "username="+id+"&"+"password="+password;
                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{

                    //URL url = new URL("http://192.168.0.171/login.php");

                    URL url = new URL(urls[0]);

                    //connect

                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");//Using post method
                   //con.setRequestProperty("Cache-Control", "no-cache");//cache setting
                    //con.setRequestProperty("Content-Type", "application/json");//send "application JSON form"
                    //con.setRequestProperty("Accept", "text/html");//responseof server, get data by html
                    con.setDoOutput(true);//Outstream, post data send
                    con.setDoInput(true);//Inputstream, get response from server
                    con.connect();

                    //making stream for sending server

                    OutputStream outStream = con.getOutputStream();

                    //making buffer and put it
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));

                    //Toast.makeText(getApplicationContext(), jsonObject.toString(), Toast.LENGTH_LONG).show();

                    writer.write(jsonObject.toString());
                    //writer.write(body);
                    writer.flush();
                    writer.close();//get buffer

                    //get data from server

                    InputStream stream = con.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(stream));
                    StringBuffer buffer = new StringBuffer();
                    String line = "";

                    while((line = reader.readLine()) != null){

                        buffer.append(line);

                    }

                    return buffer.toString();//return value from server.

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

            return null;

        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            if(result.equals("success")){
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                Intent toList = new Intent(MainActivity.this, ListActivity.class);
                startActivity(toList);
            }
            else {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                EditTextid.setText("");
                EditTextpassword.setText("");
            }

            //have to check message work or not
        }

    }

}



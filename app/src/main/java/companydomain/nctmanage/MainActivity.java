package companydomain.nctmanage;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.CookieHandler;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


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
        java.net.CookieManager cookieManager = new java.net.CookieManager();
        CookieHandler.setDefault(cookieManager);

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

        //i("username",id);

        new SendServer().execute("https://dev.cute.enterprises/api/login/");//AsyncTask start


    }



    public class SendServer extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... urls) {

            try {
                String body = "username="+id+"&"+"password="+password;
                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{

                    //URL url = new URL("http://192.168.0.171/login.php");

                    URL url = new URL(urls[0]);

                    //connect

                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");//Using post method
                    con.setDoOutput(true);//Outstream, post data send
                    con.setDoInput(true);//Inputstream, get response from server
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
                Toast.makeText(getApplicationContext(), "Welcome! Have a nice day! :)" , Toast.LENGTH_SHORT).show();
                Intent toList = new Intent(MainActivity.this, ListActivity.class);
                startActivity(toList);
            }
            else {
                Toast.makeText(getApplicationContext(), "Incorrect ID or Password", Toast.LENGTH_SHORT).show();
                EditTextid.setText("");
                EditTextpassword.setText("");
            }

            //have to check message work or not
        }

    }

}

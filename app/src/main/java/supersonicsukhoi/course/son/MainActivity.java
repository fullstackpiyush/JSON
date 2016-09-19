package supersonicsukhoi.course.son;

import android.app.ActionBar;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.style.TtsSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    private TextView tvData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btHit = (Button) findViewById(R.id.btHit);
        tvData = (TextView) findViewById(R.id.tvjson);

        btHit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new JSONTask().execute("http://jsonparsing.parseapp.com/jsonData/moviesData.txt");
                //new JSONTask().execute("http://jsonparsing.parseapp.com/jsonData/moviesDemoItem.txt");

            }
        });

    }

    class JSONTask extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection)url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();

                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                String finaljason=buffer.toString();

                JSONObject parentObject=new JSONObject(finaljason);
                JSONArray parentArray=parentObject.getJSONArray("movies");
                //JSONObject head=new JSONObject(String.valueOf(parentObject.getJSONObject("headers")));

                StringBuffer finalBufferedData=new StringBuffer();
                for (int i=0;i<parentArray.length();i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);

                    String moviename = finalObject.getString("movie");
                    int year = finalObject.getInt("year");
                    finalBufferedData.append(moviename+"--"+year+"\n");
                }
                return finalBufferedData.toString();

               // return buffer.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null)
                    connection.disconnect();
                try {
                    if (reader != null)
                        reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        public void onPostExecute(String result) {
            super.onPostExecute(result);
            tvData.setText(result);
        }
    }
}

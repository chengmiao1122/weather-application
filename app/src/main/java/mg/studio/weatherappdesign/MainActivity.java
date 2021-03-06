package mg.studio.weatherappdesign;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public MainActivity() throws UnsupportedEncodingException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btnClick(View view) {
        new DownloadUpdate().execute();
    }
    public void btnRefresh(View view) {new DownloadUpdate().execute();

        //update textview as the day of this week
        String day = getDayOfWeek();
        ((TextView) findViewById(R.id.dayoftheweek)).setText(day);
    }

    public String getDayOfWeek() {
        long time=System.currentTimeMillis();
        Date date=new Date(time);
        SimpleDateFormat format=new SimpleDateFormat("EEEE");
        String dayOfWeek = format.format(date);
        return dayOfWeek;
    }
    String city = java.net.URLEncoder.encode("鍖椾含", "utf-8");
    private class DownloadUpdate extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String stringUrl = String.format("https://www.sojson.com/open/api/weather/json.shtml?city=%s",city);
            HttpURLConnection urlConnection = null;
            BufferedReader reader;

            try {
                URL url = new URL(stringUrl);

                // Create the request to get the information from the server, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Mainly needed for debugging
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                //The temperature
                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

       // @Override
        protected void onPostExecute(String buffer) {
            //Update the temperature displayed
            try {
                JSONObject jsonObject=new JSONObject(buffer);
                JSONObject jsonObject1=jsonObject.getJSONObject("data");
                String tem=jsonObject1.getString("wendu");
                ((TextView) findViewById(R.id.temperature_of_the_day)).setText(tem);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}

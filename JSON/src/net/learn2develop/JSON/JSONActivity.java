package net.learn2develop.JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class JSONActivity extends Activity  {

	
	// it will connect to the specified URL and then read the response from the web server , it return  string as result 

	
	
	/*Android includes two HTTP clients: HttpURLConnection and 
	Apache HTTP Client. Both support HTTPS, streaming uploads and downloads,
	 configurable timeouts, IPv6 and connection pooling.*/
	
	///here is the new change
	//the second changes I made 
	public String readJSONFeed(String URL)
	{
		StringBuilder stringBuilder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(URL);
		try {
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					stringBuilder.append(line);
				}
			} else {
				Log.e("JSON", "Failed to download file");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stringBuilder.toString();
	}
	
    private class ReadJSONFeedTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) 
        {
            return readJSONFeed(urls[0]);
        }
        
        protected void onPostExecute(String result) {
        	try {
        		//to obtain the list of objects in the JSON string , you use JSONArray class
    			JSONArray jsonArray = new JSONArray(result);
    			Log.i("JSON", "Number of surveys in feed: " + 
    					jsonArray.length());

    			//---print out the content of the json feed---
    			//
    			for (int i = 0; i < jsonArray.length(); i++) {
    				
    				
    				JSONObject jsonObject = jsonArray.getJSONObject(i);
    				
    				
    				
    				/*
    				Toast.makeText(getBaseContext(), jsonObject.getString("appeId") + 
    						" - " + jsonObject.getString("inputTime"), 
    						Toast.LENGTH_SHORT).show();                
    				*/
    				
    				
    				
    				
    				/*//to get the key/values from the object you use getString(), getInt.... methods
    				Toast.makeText(getBaseContext(), jsonObject.getString("text") + 
    						" - " + jsonObject.getString("created_at"), 
    						Toast.LENGTH_SHORT).show();
    				*/
    				
    				
    				//to get the key/values from the object you use getString(), getInt.... methods
    				Toast.makeText(getBaseContext(), jsonObject.getString("id") + 
    						" - " + jsonObject.getString("name"), 
    						Toast.LENGTH_SHORT).show();
    			}
    		} catch (Exception e) {
    			e.printStackTrace();
    		}        
        }
    }

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		// first try this , 
		//new ReadJSONFeedTask().execute("http://extjs.org.cn/extjs/examples/grid/survey.html");
		new ReadJSONFeedTask().execute("http://shane.pre.yidio.com/movies/all/0/30?sort=popular&source=65,2,129,214,46,10,42,28,64,45,19,12,1,120,15,132&rating=30&mpaa_rating=g,pg,pg-13,r&hide_watched=0&favorites=0&api_key=49a3jF8zkE3");
		
		/*//second try this, to fetch the latest tweets from Twitter 
		new ReadJSONFeedTask().execute("https://twitter.com/statuses/user_timeline/weimenglee.json");*/
	}
}
package uk.ac.horizon.ug.marketlauncher;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class Launch extends Activity {
    private static final String TAG = "MarketLauncher";

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

	/* (non-Javadoc)
	 * @see android.app.Activity#onNewIntent(android.content.Intent)
	 */
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		// catch new intent
		setIntent(intent);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		// fire new app intent for marketplace
		Intent trigger = getIntent();
		if (!trigger.getAction().equals(Intent.ACTION_VIEW)) {
			error("Called for non-View action ("+trigger.getAction()+")");
			return;
		}
		if (trigger.getData().getPath()==null || trigger.getData().getQuery()==null) {
			error("Called for ill-formed URL ("+trigger.getData()+")");
			return;
			
		}
		String host = trigger.getData().getHost();
		//String path = trigger.getData().getEncodedPath();
		String query = trigger.getData().getEncodedQuery();
		if (("details".equals(host) && query.startsWith("id=") && query.indexOf("&")<0) ||
				("search".equals(host) && query.startsWith("q=") && query.indexOf("&")<0)) {
			Log.i(TAG, "Trying to launch market with "+trigger.getData());
			String marketUri = "market://"+host+query;
			//http://market.android.com/
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://"+host+"?"+query));
			//Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://"+host+"?"+query));
			//intent.setData(new Uri.Builder().scheme("market").authority(host).encodedQuery(query).build());
			intent.addFlags(Intent.FLAG_DEBUG_LOG_RESOLUTION);//Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			try {
				startActivity(intent);
			}
			catch (Exception e) {
				// e.g. no Market on emulator
				Toast.makeText(this, "Sorry, can't open the Market application", Toast.LENGTH_LONG).show();
			}
			finish();
		}
		else 
			error("URL not permitted: host="+host+", query="+query);
	}

	private void error(String string) {
		Log.w(TAG, string);
		// TODO Auto-generated method stub
		Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
		finish();
	}
    
}
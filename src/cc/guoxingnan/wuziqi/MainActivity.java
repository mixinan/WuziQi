package cc.guoxingnan.wuziqi;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {
	private WuziqiPanel wuziqiPanel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		wuziqiPanel = (WuziqiPanel) findViewById(R.id.customView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id==R.id.action_settings) {
			wuziqiPanel.start();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}

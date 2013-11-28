package com.walkers;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class WalkersActivity extends Activity {
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ImageView logo = (ImageView) findViewById(R.id.imageView1);
		logo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent viewIntent = new Intent("android.intent.action.VIEW",
						Uri.parse("https://twitter.com/FrattinJuan"));
				startActivity(viewIntent);
			}
		});

	}

	public void play(View v) {
		Intent intent = new Intent(this, WalkersGameActivity.class);
		startActivity(intent);
	}
	public void opc(View v) {
		Intent intent = new Intent(this, WalkersOptionsActivity.class);
		startActivity(intent);
		//hhjvgvjh
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.item1:
			Toast.makeText(this, "Programador: Juan Frattin",
					Toast.LENGTH_SHORT).show();
			break;
		case R.id.item2:
			finish();
		}
		return false;
	}
}
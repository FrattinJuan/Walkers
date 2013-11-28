package com.walkers;

import android.app.Activity;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

//import com.danieme.tipsandroid.seekbar.R;

public class WalkersOptionsActivity extends Activity {
	private SeekBar seekBar;
	private TextView textViewSeekBar;
	int valor_anterior;
	private int valor_actual = 1;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.opciones);
		seekBar = (SeekBar) findViewById(R.id.seekBar);
		textViewSeekBar = (TextView) findViewById(R.id.textView);
		textViewSeekBar.setText("");
		seekBar.setProgress(valor_anterior);
		seekBar.setMax(10);
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
				int progress = 0;
				int valor = progress;
				seekBar.setProgress(arg1);
				// textViewSeekBar.setText(arg1);
				valor_actual = arg1;

			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// Opciones.setMalos(can);
			}

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				// gbyfvgy
			}

		});
	}

	public void okBoton(View v) {
		if(valor_actual == 0){
			valor_actual = 1;
		}
		Opciones.setMalos(valor_actual);
		finish();

	}
}

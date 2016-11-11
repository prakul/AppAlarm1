package com.episode6.android.appalarm.pro;

import java.net.URISyntaxException;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CustomIntentMaker extends Activity {

	private EditText mEtAction, mEtData, mEtType;
	private Button mBtnOk, mBtnCancel, mBtnTest;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custom_intent);
		setTitle("Custom Intent...");
		
		
		initViews();
		assignListeners();
		
		Bundle extras = getIntent().getExtras();
		mEtAction.setText(extras.getString(AlarmItem.KEY_CUSTOM_ACTION));
		mEtData.setText(extras.getString(AlarmItem.KEY_CUSTOM_DATA));
		mEtType.setText(extras.getString(AlarmItem.KEY_CUSTOM_TYPE));
	}
	


	private void initViews() {
		mEtAction = (EditText)findViewById(R.id.ci_et_action);
		mEtData = (EditText)findViewById(R.id.ci_et_data);
		mEtType = (EditText)findViewById(R.id.ci_et_type);
		
		mBtnOk = (Button)findViewById(R.id.ci_btn_ok);
		mBtnCancel = (Button)findViewById(R.id.ci_btn_cancel);
		mBtnTest = (Button)findViewById(R.id.ci_btn_test);
	}
	
	private void assignListeners() {
		mBtnOk.setOnClickListener(mBtnOkOnClick);
		mBtnCancel.setOnClickListener(mBtnCancelOnClick);
		mBtnTest.setOnClickListener(mBtnTestOnClick);
	}
	
	private View.OnClickListener mBtnOkOnClick = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent i = new Intent();
			i.putExtra(AlarmItem.KEY_CUSTOM_ACTION, mEtAction.getText().toString());
			i.putExtra(AlarmItem.KEY_CUSTOM_DATA, mEtData.getText().toString());
			i.putExtra(AlarmItem.KEY_CUSTOM_TYPE, mEtType.getText().toString());
			setResult(RESULT_OK, i);
			finish();
		}
		
	};
	private View.OnClickListener mBtnCancelOnClick = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			setResult(RESULT_CANCELED);
			finish();
		}
		
	};
	private View.OnClickListener mBtnTestOnClick = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			
			
			String action = mEtAction.getText().toString();
			String data = mEtData.getText().toString();
			String type = mEtType.getText().toString();
			
			Intent i;
			if (AlarmItem.isShortcutIntent(data)) {
				try {
//					i = Intent.parseUri(data, Intent.URI_INTENT_SCHEME);
					i = Intent.getIntent(data);
				} catch (URISyntaxException e) {
					i = new Intent();
					e.printStackTrace();
				}
			} else {
				i = new Intent();
				i.setAction(action);
				if (!data.equals("")) {
					i.setData(Uri.parse(data));
				}
				if (!type.equals("")) {
					i.setType(type);
				}
			}
			

			if (Intent.ACTION_CALL.equals(i.getAction())) {
				AudioManager am = (AudioManager)getBaseContext().getSystemService(AUDIO_SERVICE);
				am.setMode(AudioManager.MODE_IN_CALL);
				am.setSpeakerphoneOn(true);
				am.setStreamVolume(AudioManager.STREAM_VOICE_CALL, am.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL), AudioManager.FLAG_SHOW_UI);

				Thread t = new Thread(mSetSpeakerphoneTask);
				t.setDaemon(true);
				t.start();
			}
			
			//TEST CODE
//			i = new Intent(Intent.ACTION_VIEW);
//			i.setData(Uri.parse("http://woxy.lala.com/stream/vintage-mpg64.pls"));
//			i.setComponent(new ComponentName("com.streamfurious.android.pro", "com.streamfurious.android.activities.PlayerActivity"));
//			i.putExtra(mEtAction.getText().toString(), true);
//			mEtData.setText(i.toUri(Intent.URI_INTENT_SCHEME));
			
			startActivity(i);
		}
		
	};
	
	private final Runnable mSetSpeakerphoneTask = new Runnable() {

		@Override
		public void run() {

				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			AudioManager am = (AudioManager)getBaseContext().getSystemService(AUDIO_SERVICE);
			am.setMode(AudioManager.MODE_IN_CALL);
			if (!am.isSpeakerphoneOn()) {
				am.setSpeakerphoneOn(true);
				am.setStreamVolume(AudioManager.STREAM_VOICE_CALL, am.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL), AudioManager.FLAG_SHOW_UI);
			}	
		}
		
	};

}

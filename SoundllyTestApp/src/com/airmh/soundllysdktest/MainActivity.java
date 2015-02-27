package com.airmh.soundllysdktest;

import java.util.ArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.soundlly.sdk.Soundlly;
import com.soundlly.sdk.SoundllyCore;

/**
 * 어플을 구동하면 보여지는 Main Activity
 * Resume에서 Soundlly Service를 BIND 시키며 BIND 이후에 API_KEY를 등록한다.
 * 정상적으로 APIKEY 등록 Response를 받으면 Button이 활성화 되어 두가지 액션으로 결과를 확인 할 수 있다.
 * 
 * 	1. 액티비티에서 구동확인 : 액티비티가 Foreground인 상태에서만 Wav파일을 재생하여 결과를 확인 할 수 있다.
 * 						액티비티를 종료하면 Service를 종료하므로 Wav파일을 재생하여도 결과를 확인 할 수없다.
 * 						결과를 화면에 노출한 경우 더이상 신호를 받지 않는다.
 * 
 *  2. 액티비티 종료 상타에서 구동확인 : 버튼을 클릭하면 액티비티가 종료된다. Service에서 계속 신호를 탐지하여 어떤 상태에서든 결과를 확인 할 수 있다.
 *		 						결과를 화면에 노출한 경우 더이상 신호를 받지 않는다.
 *
 * @author AirMH
 *
 */
public class MainActivity extends Activity implements OnItemClickListener{
	
	private Soundlly mSoundlly;
	private Button activityRunButton, notActivityRunButton;
	private boolean actvityRun = false;
	private ListView mResultListView;
	private ResultListAdapter mAdapter;
	private ResponseReceiver mReceiver;
	
	/**
	 * TestBroadcastReceiver에서 넘겨주는 결과값을 받는 Receiver
	 * APP KEY 등록과 결과값을 받는 두가지 액션이 있다.
	 * @author AirMH
	 *
	 */
	private class ResponseReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			
			String action = intent.getAction();
			
			if (action.equals(MainApplication.BIND_SUCCESS_ACTION))
			{
				activityRunButton.setEnabled(true);
				notActivityRunButton.setEnabled(true);
				Toast.makeText(context, getString(R.string.bind_success), Toast.LENGTH_SHORT).show();
			}
			
			else if (action.equals(MainApplication.SEND_RECEIVE_INTENT))
			{
				ArrayList<AttributesParcelable> resultList = intent.getParcelableArrayListExtra("ReceiveResult");
				if (resultList.size() > 0)
				{
					// 사운드를 켜놓으면 계속해서 Response가 들어오므로 우선 stop시킨다.
					if (actvityRun)
					{
						Toast.makeText(MainActivity.this, getString(R.string.result_receive_success), Toast.LENGTH_SHORT).show();
						mSoundlly.stopListening(MainApplication.API_KEY);
						mAdapter.setData(resultList);
						mAdapter.notifyDataSetChanged();
					}
				}
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mReceiver = new ResponseReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(MainApplication.BIND_SUCCESS_ACTION);
		intentFilter.addAction(MainApplication.SEND_RECEIVE_INTENT);
		registerReceiver(mReceiver, intentFilter);
		
		mSoundlly = SoundllyCore.getSoundlly();
		mSoundlly.setDeveoloperMode();

		//ListView, Adapter init
		mResultListView = (ListView)findViewById(R.id.resultListView);
		mResultListView.setOnItemClickListener(this);
		mAdapter = new ResultListAdapter(getLayoutInflater());
		mResultListView.setAdapter(mAdapter);
		
		activityRunButton = (Button)findViewById(R.id.activityRunButton);
		notActivityRunButton = (Button)findViewById(R.id.notActivityRunButton);
		activityRunButton.setEnabled(false);
		notActivityRunButton.setEnabled(false);
		
		// "엑티비티에서 구동확인" 버튼 클릭. 
		activityRunButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				actvityRun = true;
				mSoundlly.startListening(MainApplication.API_KEY);		// Start Listening
			}
		});
		
		// "엑티비티 종료 상태에서 구동확인" 버튼 클릭. 
		notActivityRunButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				actvityRun = false;
				mSoundlly.setListenWatermark(MainApplication.API_KEY, true);
				TestBroadcastReceiver.isNotActivity = true;
				Toast.makeText(MainActivity.this, getString(R.string.not_activity_finish_message), Toast.LENGTH_SHORT).show();
				finish();

			}
		});
		
	}	

	@Override
	protected void onResume() {
		super.onResume();
		// Service Bind
		mSoundlly.bindSoundllyService();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		// "엑티비티에서 구동확인" 의 경우 stopListening을 호출한다.
		if (actvityRun)
		{
			mSoundlly.stopListening(MainApplication.API_KEY);
			mSoundlly.unbindSoundllyService();
		}
		
		unregisterReceiver(mReceiver);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,	long id) {
		
		AttributesParcelable item = (AttributesParcelable)mAdapter.getItem(position);
		
		// Key가 Url인 경우에만 WebView 호출
		if (item.getKey().equals("url"))
		{
			Intent intent = new Intent(MainActivity.this, WebViewDialogActivity.class);
			intent.putExtra("url", item.getValue());
			startActivity(intent);
		}
		else
		{
			Toast.makeText(MainActivity.this, getString(R.string.list_item_click_message), Toast.LENGTH_SHORT).show();
		}
	}
	
	

}
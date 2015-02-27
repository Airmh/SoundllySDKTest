package com.airmh.soundllysdktest;

import java.util.ArrayList;

import com.soundlly.sdk.Soundlly;
import com.soundlly.sdk.SoundllyCore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * 액티비티가 종료된 상태에서 결과를 보여주기위한 Activity
 * 홈화면 등에서 노출될 경우가 있으므로 Dialog Theme로 보이도록 한다.
 * @author AirMH
 *
 */
public class DialogActivity extends Activity implements OnItemClickListener{

	private ListView dialogListView;
	private ResultListAdapter mAdapter;
	private Soundlly mSoundlly;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);		// Dialog 처럼 보이기위한 TITLE Bar 제거.
	    setContentView(R.layout.dialog_layout);
	    
	    mSoundlly = SoundllyCore.getSoundlly();
	    mSoundlly.setListenWatermark(MainApplication.API_KEY, false);
	    
	    // Parcelable Object를 가져온다.
	    ArrayList<AttributesParcelable> itemList = getIntent().getParcelableArrayListExtra("ReceiveResult");
	    
	    dialogListView = (ListView)findViewById(R.id.dialogListView);
	    dialogListView.setOnItemClickListener(this);
	    mAdapter = new ResultListAdapter(getLayoutInflater());
	    mAdapter.setData(itemList);
	    dialogListView.setAdapter(mAdapter);
	    
	    findViewById(R.id.dialogCloseButton).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				// Stop Service 후 Activity 종료
				TestBroadcastReceiver.isNotActivity = false;
				if (mSoundlly == null)
					mSoundlly = SoundllyCore.getSoundlly();
				
				mSoundlly.unbindSoundllyService();
				
				finish();
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,	long id) {
		
		AttributesParcelable item = (AttributesParcelable)mAdapter.getItem(position);
		
		// Key가 Url인 경우에만 WebView 호출
		if (item.getKey().equals("url"))
		{
			Intent intent = new Intent(DialogActivity.this, WebViewDialogActivity.class);
			intent.putExtra("url", item.getValue());
			startActivity(intent);
		}
		else
		{
			Toast.makeText(DialogActivity.this, getString(R.string.list_item_click_message), Toast.LENGTH_SHORT).show();
		}
		
	}

}

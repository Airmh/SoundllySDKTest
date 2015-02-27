package com.airmh.soundllysdktest;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.soundlly.sdk.Soundlly;
import com.soundlly.sdk.SoundllyCore;
import com.soundlly.sdk.net.model.AttributesModel;
import com.soundlly.sdk.net.model.ContentsModel;
import com.soundlly.sdk.service.SoundllyService;

/**
 * SoundllyResult를 수신하는 Receiver
 * 액티비티에서 선택한 액션에 따라 결과값을 분기한다.
 * @author AirMH
 *
 */
public class TestBroadcastReceiver extends BroadcastReceiver {
	
	public static boolean isNotActivity = false;
	private Soundlly mSoundlly;

	@Override
	public void onReceive(Context context, Intent intent) {
		
		String action = intent.getAction();
		
		if ((context.getPackageName()+SoundllyService.ACTION_RESULT).equals(action))
		{
			handleSoundllyResult(intent);
		}
		
		else if((context.getPackageName() +SoundllyService.ACTION_ON_BIND).equals(intent.getAction()))
		{
			mSoundlly = SoundllyCore.getSoundlly();
			if (mSoundlly != null)
			{
				mSoundlly.setApplicationId(MainApplication.API_KEY);
				context.sendBroadcast(new Intent(MainApplication.BIND_SUCCESS_ACTION));
			}
			
		}
		
	}
	
	private void handleSoundllyResult(Intent intent){
		switch (intent.getExtras().getInt(Soundlly.EXTRA_STATUS_CODE)) {
		case Soundlly.CODE_OK:
			android.util.Log.v("", "AIRMH CODE OK");
			doLoadContents(intent);
			break;
		case Soundlly.CODE_NO_CONTENTS:
			android.util.Log.v("", "AIRMH CODE CODE_NO_CONTENTS");
			break;
//		case Soundlly.CODE_NOT_FOUND:			
//			break;
		case Soundlly.CODE_TIME_OUT:
			android.util.Log.v("", "AIRMH CODE CODE_TIME_OUT");
			break;
		case Soundlly.CODE_UNAUTHORIZED:
			android.util.Log.v("", "AIRMH CODE CODE_UNAUTHORIZED");
			break;
		case Soundlly.CODE_UNKNOWN_ERROR:
			android.util.Log.v("", "AIRMH CODE CODE_UNKNOWN_ERROR");
			break;
		case Soundlly.CODE_NO_WATERMARK:
			android.util.Log.v("", "AIRMH CODE CODE_NO_WATERMARK");
			break;
		case Soundlly.CODE_MIC_ERROR:
			android.util.Log.v("", "AIRMH CODE CODE_MIC_ERROR");
			break;
		}
	}
	
	private void doLoadContents(Intent intent){
		
		ContentsModel contents = intent.getParcelableExtra(Soundlly.EXTRA_CONTENTS);
		ArrayList<AttributesModel> attr = contents.getAttributes();
		
		if(attr != null) {
			
			ArrayList<AttributesParcelable> resultList = new ArrayList<AttributesParcelable>();
			
			for(AttributesModel model : attr) {
				
				AttributesParcelable attrParcel = new AttributesParcelable();
						
				attrParcel.setType(model.getType());
				attrParcel.setKey(model.getKey());
				attrParcel.setValue(model.getValue());

				resultList.add(attrParcel);
			}
			
			// "엑티비티 종료 상태에서 구동확인" 의 경우
			if (isNotActivity)
			{
				Intent dialogIntent = new Intent(MainApplication.getContext(), DialogActivity.class);
				dialogIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				dialogIntent.putParcelableArrayListExtra("ReceiveResult", resultList);
				MainApplication.getContext().startActivity(dialogIntent);
				
			}
			else		// "엑티비에서 구동확인" 의 경우
			{
				Intent resultIntent = new Intent(MainApplication.SEND_RECEIVE_INTENT);
				resultIntent.putParcelableArrayListExtra("ReceiveResult", resultList);
				MainApplication.getContext().sendBroadcast(resultIntent);
				
			}
		}
	}
}

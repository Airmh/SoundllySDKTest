package com.airmh.soundllysdktest;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * BroadcastReceiver에서 Intent에 담아 Activity로 전달하기 위한 Class
 * @author AirMH
 *
 */
public class AttributesParcelable implements Parcelable{
	
	public String type;
	public String key;
	public String value;
	
	public AttributesParcelable(){
		
	}
	
	public AttributesParcelable(Parcel in){
		this.type = in.readString();
		this.key = in.readString();
		this.value = in.readString();
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}


	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(type);
		dest.writeString(key);
		dest.writeString(value);
	}
	
	public static final Parcelable.Creator<AttributesParcelable> CREATOR = new Creator<AttributesParcelable>() {
		
		@Override
		public AttributesParcelable[] newArray(int size) {
			// TODO Auto-generated method stub
			return new AttributesParcelable[size];
		}
		
		@Override
		public AttributesParcelable createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new AttributesParcelable(source);
		}
	};
}

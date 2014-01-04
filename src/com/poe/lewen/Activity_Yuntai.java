package com.poe.lewen;

import java.util.List;

import com.mm.android.avnetsdk.AVNetSDK;
import com.mm.android.avnetsdk.param.AV_HANDLE;
import com.mm.android.avnetsdk.param.AV_IN_RealPlay;
import com.mm.android.avnetsdk.param.AV_MediaInfo;
import com.mm.android.avnetsdk.param.AV_OUT_RealPlay;
import com.mm.android.avnetsdk.param.AV_PlayPosInfo;
import com.mm.android.avnetsdk.param.AV_Time;
import com.mm.android.avnetsdk.param.IAV_DataListener;
import com.mm.android.avnetsdk.param.IAV_NetWorkListener;
import com.mm.android.avnetsdk.param.IAV_PlayerEventListener;
import com.mm.android.avnetsdk.param.RecordInfo;
import com.mm.android.avplaysdk.render.BasicGLSurfaceView;
import com.poe.lewen.Activity_Video.playTask;
import com.poe.lewen.util.Tool;

import android.R.integer;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

public class Activity_Yuntai extends BaseActivity {
	//---------------------------通道一
		private AV_IN_RealPlay playINParam = null; // 实时监视输入参数
		private AV_OUT_RealPlay playOutParam = null; // 实时监视输出参数
		private BasicGLSurfaceView bsView = null; // 播放的视图
		private AV_HANDLE realPlay = null; // 实时监测句柄
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_yuntai);

	}

	@Override
	public void init() {
		bsView	=	(BasicGLSurfaceView) findViewById(R.id.screenOfYuntai);
		//footer select index default
		lin_yuntai.setBackgroundResource(R.drawable.btn_bg_press	);
		image_yuntai.setImageResource(R.drawable.icon_yuntai_press);
		text_yuntai.setTextColor(Color.WHITE);
	}

	@Override
	public void refresh(Object... param) {
		// TODO Auto-generated method stub

	}
	@Override
	protected void onResume() {
	if(bsView.getRenderer()==null){
		bsView.init(Activity_Yuntai.this);
	}
		//start video defalut channel is 0
		new playTask().execute();
		super.onResume();
	}

@Override
protected void onPause() {
	// TODO Auto-generated method stub
//	bsView.uninit();
	super.onPause();
}
class playTask extends AsyncTask<Void, integer, String>{
	
	@Override
	protected void onPreExecute() {
		playINParam = new AV_IN_RealPlay();
		playINParam.nChannelID = 6; // 测试零号通道
		playINParam.nSubType = 1;
		playINParam.playView = bsView;
		playINParam.dataListener = new IAV_DataListener() {

			@Override
			public int onData(AV_HANDLE arg0, byte[] arg1, int arg2, int arg3,
					AV_MediaInfo arg4, Object arg5) {
				// TODO Auto-generated method stub
				return 0;
			}
		};
		
		playINParam.netWorkListener = new IAV_NetWorkListener() {

			@Override
			public int onConnectStatus(AV_HANDLE arg0, boolean arg1,
					AV_HANDLE arg2, Object arg3) {
				// TODO Auto-generated method stub
				return 0;
			}
		};
		playINParam.playerEventListener = new IAV_PlayerEventListener() {

			@Override
			public void onResolutionChange(AV_HANDLE arg0, int arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onRecordInfo(Object arg0, AV_Time arg1, AV_Time arg2,
					List<RecordInfo> arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public int onPlayPos(AV_HANDLE arg0, AV_PlayPosInfo arg1,
					Object arg2) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public void onNotSupportedEncode(AV_HANDLE arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFrameRateChange(AV_HANDLE arg0, int arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFrameLost(AV_HANDLE arg0) {
				// TODO Auto-generated method stub

			}
		};
		//构造实时监视输出参数
		playOutParam = new AV_OUT_RealPlay();
	}

	@Override
	protected String doInBackground(Void... params) {
		if (MyApplication.log_handle != null) // 登陆成功才能播放
			realPlay = AVNetSDK.AV_RealPlay(MyApplication.log_handle, playINParam,	playOutParam);
		else {
			return "请先登陆！";
		}
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		if(null!=result){
			Tool.showMsg(MyApplication.getInstance().getApplicationContext(), result);
		}
	}
}
}
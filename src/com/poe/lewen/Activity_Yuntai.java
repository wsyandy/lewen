package com.poe.lewen;

import java.util.List;
import com.mm.android.avnetsdk.AVNetSDK;
import com.mm.android.avnetsdk.param.AV_HANDLE;
import com.mm.android.avnetsdk.param.AV_IN_PTZ;
import com.mm.android.avnetsdk.param.AV_IN_RealPlay;
import com.mm.android.avnetsdk.param.AV_MediaInfo;
import com.mm.android.avnetsdk.param.AV_OUT_PTZ;
import com.mm.android.avnetsdk.param.AV_OUT_RealPlay;
import com.mm.android.avnetsdk.param.AV_PTZ_Type;
import com.mm.android.avnetsdk.param.AV_PlayPosInfo;
import com.mm.android.avnetsdk.param.AV_Time;
import com.mm.android.avnetsdk.param.IAV_DataListener;
import com.mm.android.avnetsdk.param.IAV_NetWorkListener;
import com.mm.android.avnetsdk.param.IAV_PlayerEventListener;
import com.mm.android.avnetsdk.param.RecordInfo;
import com.mm.android.avplaysdk.render.BasicGLSurfaceView;
import com.poe.lewen.MyApplication.loaded4login;
import com.poe.lewen.bean.rsp_parise;
import com.poe.lewen.service.XmlToListService;
import com.poe.lewen.util.Tool;
import android.R.integer;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Activity_Yuntai extends BaseActivity {
	//---------------------------通道一
		private AV_IN_RealPlay playINParam = null; // 实时监视输入参数
		private AV_OUT_RealPlay playOutParam = null; // 实时监视输出参数
		private BasicGLSurfaceView bsView = null; // 播放的视图
		private AV_HANDLE realPlay = null; // 实时监测句柄
		private AV_IN_PTZ cloudInParam=null;	//云台控制输入参数
		private AV_OUT_PTZ cloudOutParam=null;	//云台控制输出参数
		
		//功能键
		private RelativeLayout relativeSpeed;
		private ImageView imgSwitch;
		private ImageButton btn_minus1,btn_minus2,btn_minus3;
		private ImageButton btn_plus1,btn_plus2,btn_plus3;
		
		//top bar praise
		private TextView text_all,text_now,text_praise;
		private Button btn_praise;
		private Button back;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_yuntai);
	}

	@Override
	public void init() {
		
		//top bar
		text_all		=	(TextView) findViewById(R.id.textCount1OfToperBarYuntai);
		text_now		=	(TextView) findViewById(R.id.textCount3OfToperBarYuntai);
		text_praise	=	(TextView) findViewById(R.id.textCount2OfToperBarYuntai);
		btn_praise	=	(Button) findViewById(R.id.rightButtonOfToperBarYuntai);
		btn_praise.setOnClickListener(this);
		back		=	(Button) findViewById(R.id.leftButtonOfToperBarYuntai);
		back.setOnClickListener(this);
		relativeSpeed	=	(RelativeLayout) findViewById(R.id.relativeSpeedOfYuntai);
		imgSwitch	=	(ImageView) findViewById(R.id.imgSpeedOfYuntai);
		btn_minus1	=	(ImageButton) findViewById(R.id.btn_minus1OfYuntai);
		btn_minus2	=	(ImageButton) findViewById(R.id.btn_minus2OfYuntai);
		btn_minus3	=	(ImageButton) findViewById(R.id.btn_minus3OfYuntai);
		btn_plus1	=	(ImageButton) findViewById(R.id.btn_plus1OfYuntai);
		btn_plus2	=	(ImageButton) findViewById(R.id.btn_plus2OfYuntai);
		btn_plus3	=	(ImageButton) findViewById(R.id.btn_plus3OfYuntai);
		
		
		bsView	=	(BasicGLSurfaceView) findViewById(R.id.screenOfYuntai);
		//footer select index default
		lin_yuntai.setBackgroundResource(R.drawable.btn_bg_press	);
		image_yuntai.setImageResource(R.drawable.icon_yuntai_press);
		text_yuntai.setTextColor(Color.WHITE);
		
		
		relativeSpeed.setOnTouchListener(new OnTouchListener() {
			
			@SuppressLint("NewApi")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				float x  = event.getX();
				System.out.println("x:"+x);
				float point_x = relativeSpeed.getX();
				System.out.println("point_x:"+point_x);
				if(x>0){
					if(x-0>relativeSpeed.getWidth()){
						imgSwitch.setPadding((int)(relativeSpeed.getWidth()-10), 0, 0, 0);
					}else{
						imgSwitch.setPadding((int)(x), 0, 0, 0);
					}
				}
				return false;
			}
		});
		
		btn_minus1.setOnClickListener(this);

		btn_minus2.setOnClickListener(this);

		btn_minus3.setOnClickListener(this);

		btn_plus1.setOnClickListener(this);

		btn_plus2.setOnClickListener(this);

		btn_plus3.setOnClickListener(this);
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
	
	if(MyApplication.cOnline!=null){
		text_all.setText(MyApplication.cOnline.getHistory_watch());
		text_now.setText(MyApplication.cOnline.getWatch());
		text_praise.setText(MyApplication.cOnline.getPraise());
	}
	
	//start video defalut channel is 0
	if(MyApplication.log_handle!=null){
		new playTask().execute();
	}else{
		MyApplication.getInstance().reLogin(new loaded4login() {
			
			@Override
			public void done() {
				// TODO Auto-generated method stub
				new playTask().execute();
			}
		});
	}
		
		super.onResume();
	}

@Override
protected void onPause() {
	// TODO Auto-generated method stub
//	bsView.uninit();
	super.onPause();
}

@Override
protected void onDestroy() {
	if (realPlay != null) {
		AVNetSDK.AV_StopRealPlay(realPlay); // 停止实时监视
		realPlay = null;
	}
//	if (MyApplication.log_handle != null) {
//		AVNetSDK.AV_Logout(MyApplication.log_handle);
////		MyApplication.log_handle = null;
//	}
//	AVNetSDK.AV_Cleanup(); // 清理网络SDK
	bsView.uninit();	//反初始化播放视图
	super.onDestroy();
}


@Override
public void onClick(View v) {
	cloudInParam=new AV_IN_PTZ();
	cloudInParam.nChannelID=MyApplication.selectChannel;
	cloudInParam.bStop=false;
	cloudOutParam=new AV_OUT_PTZ();
	
	switch (v.getId()) {
	case 0:
		cloudInParam.nType=AV_PTZ_Type.AV_PTZ_Up;
		break;
		
	case 1:
		cloudInParam.nType=AV_PTZ_Type.AV_PTZ_Right;
		break;
		
	case R.id.btn_minus1OfYuntai:
		cloudInParam.nType=AV_PTZ_Type.AV_PTZ_Zoom_Dec;
		break;
		
	case R.id.btn_plus2OfYuntai:
		cloudInParam.nType=AV_PTZ_Type.AV_PTZ_Focus_Add;
		break;
		
	case R.id.btn_plus3OfYuntai:
		cloudInParam.nType=AV_PTZ_Type.AV_PTZ_Aperture_Add;
		break;
		
	case 5:
		cloudInParam.nType=AV_PTZ_Type.AV_PTZ_Down;
		break;
		
	case 6:
		cloudInParam.nType=AV_PTZ_Type.AV_PTZ_Left;
		break;
		
	case R.id.btn_plus1OfYuntai:
		cloudInParam.nType=AV_PTZ_Type.AV_PTZ_Zoom_Add;
		break;
		
	case R.id.btn_minus2OfYuntai:
		cloudInParam.nType=AV_PTZ_Type.AV_PTZ_Focus_Dec;
		break;
		
	case R.id.btn_minus3OfYuntai:
		cloudInParam.nType=AV_PTZ_Type.AV_PTZ_Aperture_Dec;
		break;
	case R.id.rightButtonOfToperBarYuntai:
		if (MyApplication.cOnline != null) {
		MyApplication.packet.praiseChannel(MyApplication.cOnline.getChannelId(), handler_save);
//			MyApplication.getInstance().throwTips("本功能暂未实现，敬请期待！");
		} else {
			MyApplication.getInstance().throwTips("请先登录选择通道！");
		}
		break;
	case R.id.leftButtonOfToperBarYuntai:
		finish();
		break;
	default:
		cloudInParam.bStop=true;
		break;
	}
	
	AVNetSDK.AV_ControlPTZ(MyApplication.log_handle, cloudInParam, cloudOutParam);
	
	
	super.onClick(v);
}

private Handler handler_save = new Handler() {

	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		String result = (String) msg.obj;
		if(null!=result)
			System.out.println(result);
		
		switch (msg.what) {
		case 1:	//收藏
//			if (result != null && result.contains("errdesc")) {
//				String tip = result.substring(result.indexOf("<errdesc>") + 9, result.lastIndexOf("</errdesc>"));
//				MyApplication.getInstance().throwTips(tip);
//			}
			break;
		case 2: //攒次通道
			try {
				rsp_parise  parise =XmlToListService.GetCountOfZan(result);
				if(null!=parise){
					MyApplication.getInstance().throwTips(parise.getErrdesc());
					//set the count of zan on the top bar
					text_praise.setText(parise.getParise_count());
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
	}
};

class playTask extends AsyncTask<Void, integer, String>{
	
	@Override
	protected void onPreExecute() {
		playINParam = new AV_IN_RealPlay();
		playINParam.nChannelID = MyApplication.selectChannel; // 测试零号通道
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

package com.poe.lewen.bean;

public class Constant {

	//大华sdk播放列表
	public static String ip_dahua ="202.136.60.234";//"60.18.152.38";
	public static int prot_dahua=37780;//37779;
	public static String str_login_ip = "202.136.60.234";//"219.146.251.189";
	public static int login_port = 8888;//;//8808;//
	
	public static int REQ_LOGIN = 0XA001;	//用登陆请求XML格式
	
	public static int REQ_GET_ORG_STRUCTURE         = 0XE007;//获取直播组织架构
	public static int RSP_GET_ORG_STRUCTURE         = 0XB403; //响应获取直播组织架构
	
	public static int REQ_HEART				= 0XA000 ;//心跳
	
	public static int REQ_GET_VIDEO_ADDR            = 0XE008;      //获取直播地址
	
	public static int RSP_CUT_OFFLINE         = 0XB03A     ;      //强制离线注销
	
	public static int REQ_ADD_FAVORITES       = 0XC001 ;		//添加到收藏夹 Favorites
	public static int RSP_ADD_FAVORITES       = 0XB101 ; 		//响应添加到收藏夹
	
	public static int REQ_LIST_FAVORITES      = 0XC002;		//从收藏夹查询 Favorites
	public static int RSP_LIST_FAVORITES		= 0XB102;		//响应从收藏夹查询
	
	public static int REQ_LIST_DEMO_ADDR      = 0XC003;     //获取演示地址
	
	public static int REQ_GET_VIDEO_HISTORY_REC = 0xE0009;     //请求获取历史录像
	public static int RSP_GET_VIDEO_HISTORY_REC = 0xE000A;     //返回历史录像	
	
	public static int REQ_PRAISE_CHANNEL      = 0XC004;      //请求赞此通道报文
	public static int RSP_PRAISE_CHANNEL       = 0XB305;      //响应赞此通道报文
	
}

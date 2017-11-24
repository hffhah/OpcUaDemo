package org.zihao.opc.ua;

public interface Constant {
	/** custom server tag */   //work ok
//	public static final String URL = "opc.tcp://localhost:12686/example";
//	public static final String TAG = "HelloWorld/ScalarTypes/Int32";
	
	/** kepware server tag */  //work not ok
	public static final String URL = "opc.tcp://192.168.93.202:49321";
	public static final String TAG = "Channel1.Device1.Tag1";

	/** uacpp server tag */    //work ok
//	public static final String URL = "opc.tcp://zihao-basic:48010/uacpp";
//	public static final String TAG = "Demo.Static.Scalar.Int32";
	
	/** cimplicity server tag */
//	public static final String URL = "";
//	public static final String TAG = "";
}

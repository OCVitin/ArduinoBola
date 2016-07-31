package androidbluetoothle;


public class WriteCharacteristicView
	extends androidbluetoothle.BaseCharacteristicView
	implements
		mono.android.IGCUserPeer
{
	static final String __md_methods;
	static {
		__md_methods = 
			"n_onCreate:(Landroid/os/Bundle;)V:GetOnCreate_Landroid_os_Bundle_Handler\n" +
			"n_onDestroy:()V:GetOnDestroyHandler\n" +
			"";
		mono.android.Runtime.register ("AndroidBluetoothLE.WriteCharacteristicView, AndroidBluetoothLE, Version=1.0.0.0, Culture=neutral, PublicKeyToken=null", WriteCharacteristicView.class, __md_methods);
	}


	public WriteCharacteristicView () throws java.lang.Throwable
	{
		super ();
		if (getClass () == WriteCharacteristicView.class)
			mono.android.TypeManager.Activate ("AndroidBluetoothLE.WriteCharacteristicView, AndroidBluetoothLE, Version=1.0.0.0, Culture=neutral, PublicKeyToken=null", "", this, new java.lang.Object[] {  });
	}


	public void onCreate (android.os.Bundle p0)
	{
		n_onCreate (p0);
	}

	private native void n_onCreate (android.os.Bundle p0);


	public void onDestroy ()
	{
		n_onDestroy ();
	}

	private native void n_onDestroy ();

	java.util.ArrayList refList;
	public void monodroidAddReference (java.lang.Object obj)
	{
		if (refList == null)
			refList = new java.util.ArrayList ();
		refList.add (obj);
	}

	public void monodroidClearReferences ()
	{
		if (refList != null)
			refList.clear ();
	}
}

package com.poscoict.postech.util;

import java.text.DecimalFormat;
import java.util.HashMap;

public class MntHashMap<K,V> extends HashMap<K,V>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1762550466041333232L;
	public String getString(Object key)
	{
		return this.getString(key, null);
	}
	public String getString(Object key, String changeStr)
	{
		Object oGetValue = super.get(key);
		String rtnValue = null;
		
		if(null!=oGetValue)
		{
			rtnValue = String.valueOf(oGetValue); 
		}
		else
		{
			rtnValue = changeStr;
		}
		return rtnValue;
	}
	public int getInt(Object key)
	{
		return Integer.parseInt(this.getString(key));
	}
	public float getFloat(Object key)
	{
		return Float.parseFloat(this.getString(key));
	}
	public double getDouble(Object key)
	{
		return Double.parseDouble(this.getString(key));
	}
	public String getNumberFomatString(Object key)
	{
		return getNumberFomatString(key, "##,000.##");
	}
	public String getNumberFomatString(Object key, String formatStr)
	{
		String sGetStr = this.getString(key);
		if(null!=sGetStr)
		{
			DecimalFormat df = new DecimalFormat(formatStr);
			return df.format(sGetStr);
		}
		return null;
	}

}
package com.sumadhura.util;

public class DatatTypeUtil {

	static public int getIntValueFromReq(Object obj)
	{
		int value = 0;
		if (obj != null) 
			if (!(obj.toString().trim().equals("")))
				try
				{
					value = Integer.valueOf((obj.toString())).intValue();
				}
		catch(Exception ex)
		{
			value = 0;
		}

		return value;
	}
	
	static public long getLongValueFromReq(Object obj)
	{
		long value = 0;
		if (obj != null) 
			if (!(obj.toString().trim().equals("")))
				try
				{
					value = Long.valueOf((obj.toString())).longValue();
				}
		catch(Exception ex)
		{
			value = 0;
		}

		return value;
	}
	
	
	static public float getFloatValueFromReq(Object obj)
	{
		float value = (float)0.0;
		if (obj != null) 
			if (!(obj.toString().trim().equals("")))
				try
		{
					value = Float.valueOf((obj.toString())).floatValue();
		}
		catch(Exception ex)
		{
			value = (float)0.0;
		}

		return value;
	}
	
	static public double getDoubleValueFromReq(Object obj)
	{
		double value = (double)0.0;
		if (obj != null) 
			if (!(obj.toString().trim().equals("")))
				try
		{
					value = Double.valueOf((obj.toString())).doubleValue();
		}
		catch(Exception ex)
		{
			value = (double)0.0;
		}

		return value;
	}
	
	static public String getStringValueFromReq(Object obj)
	{
		String value = ""; 
		if (obj != null) 
			if (!(obj.toString().trim().equals("")))
				try
		{
					value =obj.toString().trim();
		}
		catch(Exception ex)
		{
			value = "";
		}

		return value;
	}

	
}

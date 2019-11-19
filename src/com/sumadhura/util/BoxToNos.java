package com.sumadhura.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class BoxToNos extends UIProperties {
	public Map<String,String> convertBoxToNos(String childProdId,String quantity,String measurementName,String strMeasurementId) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, ClassNotFoundException
	{
		Map<String,String> map = new HashMap<String,String>();
		String methodName = "";
		double doubleQuantity = 0.0;
		methodName  = validateParams.getProperty(childProdId) == null ? "" : validateParams.getProperty(childProdId).toString();

		if(!methodName.equals("")) {	


			String strMesurmentConversionClassName  = validateParams.getProperty("MesurmentConversionClassName") == null ? "" : validateParams.getProperty("MesurmentConversionClassName").toString();

			//String strMesurmentConversionClassName = "comsumadhura.util.MesurmentConversions";
			Class<?> strMesurmentConversionClass = Class.forName(strMesurmentConversionClassName); // convert string classname to class
			Object mesurment = strMesurmentConversionClass.newInstance(); // invoke empty constructor


			double doubleActualQuantity  =  Double.valueOf(validateParams.getProperty(childProdId+"ActualQuantity") == null ? "0" : validateParams.getProperty(childProdId+"ActualQuantity").toString());
			double doubleInputQuantity =  Double.valueOf(quantity);
			String strConversionMesurmentId  =  validateParams.getProperty(childProdId+"ID") == null ? "" : validateParams.getProperty(childProdId+"ID").toString();
			// with multiple parameters
			//methodName = "convertCHP00536";
			Class<?>[] paramTypes = {double.class,double.class, String.class};
			Method printDogMethod = mesurment.getClass().getMethod(methodName, paramTypes);
			doubleQuantity = (Double) printDogMethod.invoke(mesurment, doubleActualQuantity,doubleInputQuantity,measurementName);	            
			int quantity1 = (int)doubleQuantity; /* String.valueOf(doubleQuantity); */
			quantity = String.valueOf(quantity1);
			String measurementId = strConversionMesurmentId;
			measurementName = validateParams.getProperty(childProdId+"IDMNAME") == null ? "" : validateParams.getProperty(childProdId+"IDMNAME").toString();
			map.put("Quantity", quantity);
			map.put("MeasurementId", measurementId);
			map.put("MeasurementName", measurementName);
		}else{
			map.put("Quantity", quantity);
			map.put("MeasurementId", strMeasurementId);
			map.put("MeasurementName", measurementName);
		}
		return map;
	}
}

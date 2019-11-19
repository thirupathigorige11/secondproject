package com.sumadhura.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.TreeMap;

public class MesurmentConversions {



	//converting the kg,grms to Packet(2.5 kg) this for all headless nails  // converting Foam sheet white 6feetx3feetx1mm
	public  Map<String ,String> convertCHP0016(String basicAmount,double doubleActualQuantity,double inputQuantity,String inputMesurment){

		System.out.println("calling PRD002Conv ");	
		//double doublePacketQuantity = 2.5;  //kg
		double convertedQuantity = 0.0;
		Map<String, String> gst = null;
		gst = new TreeMap<String, String>();
		double price=0.0;

		try{

			/*if(inputMesurment.equalsIgnoreCase("Kgs") || inputMesurment.equalsIgnoreCase("Kg")){

				convertedinputvalue =    inputQuantity*1/doubleActualQuantity;

				System.out.println("converted value "+convertedinputvalue);

			}else if(inputMesurment.equalsIgnoreCase("Grms")){

				double getKgs =  inputQuantity*1/1000;   //1000 for 1000grms is 1kgs 

				convertedinputvalue =    getKgs*1/doubleActualQuantity;

			}*/ if(inputMesurment.equalsIgnoreCase("PCS")){  //this is for Foam sheet white 6feetx3feetx1mm



				convertedQuantity =    inputQuantity*1/doubleActualQuantity;
				double doubleConvertedQuantity = Double.parseDouble(new DecimalFormat("##.####").format(convertedQuantity));
				price=(Double.parseDouble(basicAmount))/doubleConvertedQuantity;
				//price=Double.parseDouble(new DecimalFormat("##.##").format(price));
				gst.put(String.valueOf(doubleConvertedQuantity),String.valueOf(Double.parseDouble(new DecimalFormat("##.##").format(price))));

			}
			else if(inputMesurment.equalsIgnoreCase("KGS")){  //this is for Roofing



				convertedQuantity =    inputQuantity*1/doubleActualQuantity;
				double doubleConvertedQuantity = Double.parseDouble(new DecimalFormat("##.####").format(convertedQuantity));
				price=(Double.parseDouble(basicAmount))/doubleConvertedQuantity;
				//price=Double.parseDouble(new DecimalFormat("##.##").format(price));
				gst.put(String.valueOf(doubleConvertedQuantity),String.valueOf(Double.parseDouble(new DecimalFormat("##.##").format(price))));

			}else{

				convertedQuantity = inputQuantity;
				price=(Double.parseDouble(basicAmount))/convertedQuantity;
				gst.put(String.valueOf(convertedQuantity),String.valueOf(Double.parseDouble(new DecimalFormat("##.##").format(price))));
			}


		}catch(Exception e){
			e.printStackTrace();
		}



		return gst;

	}
	//converting the bundle to number for coir rope  //yard to feet to mtrs
	public  double convertCHP0076(double doubleActualQuantity,double inputQuantity,String inputMesurment){
		double convertedinputvalue = 0.0;
		try{
			System.out.println("calling PRD002Conv ");	
			//double doublePacketQuantity = 2.5;  //kg
			try{
				if(inputMesurment.equalsIgnoreCase("ft")){
					convertedinputvalue =    inputQuantity*1/doubleActualQuantity;
					System.out.println("converted value "+convertedinputvalue);

				}else if(inputMesurment.equalsIgnoreCase("mtrs") || inputMesurment.equalsIgnoreCase("mtr")){
					double getMeeter =  inputQuantity*1/3.2;   //1000 for 1000grms is 1kgs 
					convertedinputvalue =    getMeeter*1/doubleActualQuantity;
				}

				else{
					convertedinputvalue = inputQuantity;
				}
			}catch(Exception e){
				e.printStackTrace();
			}

		}catch(Exception e){
			e.printStackTrace();
		}
		return convertedinputvalue;	
	}

	//converting the roll to Meter for level tube 30mtrx8mm  //yard to feet to mtrs
	public  double convertCHP00536(String name ,double doubleActualQuantity,double inputQuantity,String inputMesurment){
		double convertedinputvalue = 0.0;
		try{
			System.out.println("calling PRD002Conv ");	
			//double doublePacketQuantity = 2.5;  //kg
			try{
				convertedinputvalue =    inputQuantity*1/doubleActualQuantity;
				System.out.println("converted value "+convertedinputvalue);
			}catch(Exception e){
				e.printStackTrace();
			}

		}catch(Exception e){
			e.printStackTrace();
		}
		return convertedinputvalue;	
	}

	public  Map<String ,String> convertTiles(String basicAmnt,double doubleActualQuantity,double inputQuantity,String inputMesurment){
		double convertedinputQuantityvalue = 0.0;
		Map<String, String> gst = null;
		gst = new TreeMap<String, String>();
		double price=0.0;
		try{
			System.out.println("calling PRD002Conv ");	
			//double doublePacketQuantity = 2.5;  //kg
			try{
				if(inputMesurment.equalsIgnoreCase("Box")){
					convertedinputQuantityvalue =    inputQuantity*doubleActualQuantity;
					price=(Double.parseDouble(basicAmnt))/convertedinputQuantityvalue;
					//price=Double.parseDouble(new DecimalFormat("##.##").format(price));
					gst.put(String.valueOf(convertedinputQuantityvalue),String.valueOf(Double.parseDouble(new DecimalFormat("##.##").format(price))));


					System.out.println("converted value "+convertedinputQuantityvalue);
				} else{
					convertedinputQuantityvalue = inputQuantity;
					price=(Double.parseDouble(basicAmnt))/convertedinputQuantityvalue;
					gst.put(String.valueOf(convertedinputQuantityvalue),String.valueOf(Double.parseDouble(new DecimalFormat("##.##").format(price))));
				}
			}catch(Exception e){
				e.printStackTrace();
			}

		}catch(Exception e){
			e.printStackTrace();
		}
		return gst;	
	}


	//for rope 100


	public static void main(String[] args) throws NoSuchMethodException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{


		String strMesurmentConversionClassName = "com.sumadhura.util.MesurmentConversions";
		Class<?> strMesurmentConversionClass = Class.forName(strMesurmentConversionClassName); // convert string classname to class
		Object objMesurment = strMesurmentConversionClass.newInstance(); // invoke empty constructor

		String methodName = "";


		// with multiple parameters
		methodName = "convertCHP00536";
		Class<?>[] paramTypes = {String.class,double.class,double.class, String.class};
		Method printDogMethod = objMesurment.getClass().getMethod(methodName, paramTypes);
		double value = (Double) printDogMethod.invoke(objMesurment,"", 1000,500, "kgs"); // pass args

		System.out.println("final valuse .."+value);



	}

	//for rope 100

	//CONVERSTIG M SAND,MP SAND,AGGREGATE(6mm,12mm,20mm,40mm) FROM MT TO CFT.

	public  Map<String ,String> measurementConversionPatternOne(String basicAmnt,double doubleActualQuantity,double inputQuantity,String inputMesurment){
		double convertedinputQuantityvalue = 0.0;
		Map<String, String> gst = null;
		gst = new TreeMap<String, String>();
		double price=0.0;
		try{
			System.out.println("calling PRD005Conv ");	
			//double doublePacketQuantity = 2.5;  //kg
			try{
				if(inputMesurment.equalsIgnoreCase("MT")){
					convertedinputQuantityvalue =    inputQuantity*doubleActualQuantity;
					price=(Double.parseDouble(basicAmnt))/convertedinputQuantityvalue;
					//price=Double.parseDouble(new DecimalFormat("##.##").format(price));
					gst.put(String.valueOf(convertedinputQuantityvalue),String.valueOf(Double.parseDouble(new DecimalFormat("##.##").format(price))));


					System.out.println("converted value "+convertedinputQuantityvalue);
				} else if(inputMesurment.equalsIgnoreCase("Sq.Meter")){
					
					convertedinputQuantityvalue =    inputQuantity*doubleActualQuantity;
					price=(Double.parseDouble(basicAmnt))/convertedinputQuantityvalue;
					//price=Double.parseDouble(new DecimalFormat("##.##").format(price));
					gst.put(String.valueOf(convertedinputQuantityvalue),String.valueOf(Double.parseDouble(new DecimalFormat("##.##").format(price))));
					System.out.println("converted value "+convertedinputQuantityvalue);
					
				}
					else{
					convertedinputQuantityvalue = inputQuantity;
					price=(Double.parseDouble(basicAmnt))/convertedinputQuantityvalue;
					gst.put(String.valueOf(convertedinputQuantityvalue),String.valueOf(Double.parseDouble(new DecimalFormat("##.##").format(price))));
				}
			}catch(Exception e){
				e.printStackTrace();
			}

		}catch(Exception e){
			e.printStackTrace();
		}
		return gst;	
	}

	//CONVERTING NO TO KGS FOR 
	public  Map<String ,String> measurementConversionPatternTwo(String basicAmnt,double doubleActualQuantity,double inputQuantity,String inputMesurment){
		double convertedinputQuantityvalue = 0.0;
		Map<String, String> gst = null;
		gst = new TreeMap<String, String>();
		double price=0.0;
		try{
			System.out.println("calling PRD005Conv ");	
			//double doublePacketQuantity = 2.5;  //kg
			try{
				if(inputMesurment.equalsIgnoreCase("Kgs") ){
					convertedinputQuantityvalue =    inputQuantity/doubleActualQuantity;
					price=(Double.parseDouble(basicAmnt))/inputQuantity;
					//price=Double.parseDouble(new DecimalFormat("##.##").format(price));
					gst.put(String.valueOf(convertedinputQuantityvalue),String.valueOf(Double.parseDouble(new DecimalFormat("##.##").format(price))));
					System.out.println("converted value "+convertedinputQuantityvalue);
				}
				
				
				else{
					convertedinputQuantityvalue = inputQuantity;
					price=(Double.parseDouble(basicAmnt))/convertedinputQuantityvalue;
					gst.put(String.valueOf(convertedinputQuantityvalue),String.valueOf(Double.parseDouble(new DecimalFormat("##.##").format(price))));
				}
			}catch(Exception e){
				e.printStackTrace();
			}

		}catch(Exception e){
			e.printStackTrace();
		}
		return gst;	
	}
	//CONVERTING KG TO NO FOR 
	public  Map<String ,String> measurementConversionPatternThree(String basicAmnt,double doubleActualQuantity,double inputQuantity,String inputMesurment){
		double convertedinputQuantityvalue = 0.0;
		Map<String, String> gst = null;
		gst = new TreeMap<String, String>();
		double price=0.0;
		try{
			System.out.println("calling PRD005Conv ");	
			//double doublePacketQuantity = 2.5;  //kg
			try{
				if(inputMesurment.equalsIgnoreCase("KGS")){
					convertedinputQuantityvalue =    inputQuantity*doubleActualQuantity;
					price=(Double.parseDouble(basicAmnt))/convertedinputQuantityvalue;
					//price=Double.parseDouble(new DecimalFormat("##.##").format(price));
					gst.put(String.valueOf(convertedinputQuantityvalue),String.valueOf(Double.parseDouble(new DecimalFormat("##.##").format(price))));
					System.out.println("converted value "+convertedinputQuantityvalue);
				}
				
				else{
					convertedinputQuantityvalue = inputQuantity;
					price=(Double.parseDouble(basicAmnt))/convertedinputQuantityvalue;
					gst.put(String.valueOf(convertedinputQuantityvalue),String.valueOf(Double.parseDouble(new DecimalFormat("##.##").format(price))));
				}
			}catch(Exception e){
				e.printStackTrace();
			}

		}catch(Exception e){
			e.printStackTrace();
		}
		return gst;	
	}
}

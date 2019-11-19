package com.sumadhura.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WriteTrHistory {
	public static void write(String data)
	{
		File f = new File("D://Tr_History.txt");
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			fw = new FileWriter(f,true);
			bw = new BufferedWriter(fw);
			if(data.substring(0,9).equals("Tr_Opened")){bw.newLine();}
			else if(data.substring(0,12).equals("Tr_Completed")){bw.newLine();}
			bw.write(data);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		finally{
            try {
                bw.close();
                fw.close();
            } catch (IOException el) {
                el.printStackTrace();
            }
        }
	}
}

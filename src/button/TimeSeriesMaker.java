package button;

import java.util.GregorianCalendar;

import org.jfree.data.xy.XYSeries;

public class TimeSeriesMaker {





	public TimeSeriesMaker(String[][] data, String key, XYSeries xy){
		xy.setKey(key);
		String startTime = data[0][0];
		double dt =  getDifTime(data[1][0], startTime);
		int lum = 0;
		double time = 0;	
		for(int i=0;i<data.length;i++){
			String str = data[i][1];
			//System.out.println(str);
			if(str.endsWith(".0")){
				lum = Integer.parseInt(str.substring(0, str.length()-2));
			//	System.out.println("tr"+lum);
			}
			else lum = Integer.parseInt(data[i][1]);
			xy.add(time,lum);
			time = time + dt;
		}

	}
	
	
	private double getDifTime(String x, String a){
		double ans=0;
		ans = (dateToSecond(x)-dateToSecond(a))/60.0/60.0/1000.0;
		return ans;
		
	}
	
	private static long dateToSecond(String date){
		long ans;
		int year =Integer.parseInt(date.substring(0, 4));
		int mon = Integer.parseInt(date.substring(4, 6));
		int day = Integer.parseInt(date.substring(6, 8));
		int hour = Integer.parseInt(date.substring(8, 10));
		int min = Integer.parseInt(date.substring(10, 12));
		int sec = Integer.parseInt(date.substring(12, 14));
		
		ans = new GregorianCalendar(year,mon,day,hour,min,sec).getTimeInMillis();

		
		return ans;
	}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

package tawusel.android.ui.helper;

import java.text.SimpleDateFormat;
import java.util.Date;

import tawusel.android.enums.TourKind;

/**
 * This helper class provides a method to get time string
 * which can be displayed in the tours table. It is calculated
 * out of the depature timestamp and the arrival timestamp retrieved
 * by the webservie. 
 *
 */
public class Time {

	public static String formatString(String depTimeString, String arrTimeString, TourKind tourKind) {
		long depTimeInMillis = Long.parseLong(depTimeString);
		long arrTimeInMillis = Long.parseLong(arrTimeString);
		Date depTime = new Date(depTimeInMillis);
		Date arrTime = new Date(arrTimeInMillis);
		
		if(depTime!=null && arrTime!=null) {
			SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
			String timeString = timeFormat.format(depTime) + " - " + timeFormat.format(arrTime);
			if(!tourKind.equals(TourKind.TEMPLATE)) {
				timeString += " (" + dateFormat.format(depTime) + ")";
			}
			return timeString;
		} else return "";
	}
	
}

package locator;

import net.iakovlev.timeshape.TimeZoneEngine;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class ISSLocator {

  private ISSWebService issWebService;

  public void setWebService(ISSWebService webService) {
    issWebService = webService;
  }

  public String convertTimeStampToUTC(long timeStamp) {
    TimeZone timeZone = TimeZone.getTimeZone("UTC");
    Calendar calender = Calendar.getInstance(timeZone);
    calender.setTimeInMillis(0);
    calender.add(Calendar.SECOND, (int) timeStamp);
    DateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy, HH:mm:ss");
    dateFormat.setTimeZone(timeZone);
    return dateFormat.format(calender.getTime());
  }

  public String convertTimeStampToTimeAtLatLon(
    double lat, double lon, long timeStamp) {
    TimeZoneEngine engine = TimeZoneEngine.initialize();
    String zoneID = engine.query(lat, lon).toString();
    String timeZone = zoneID.substring(
      zoneID.indexOf("[") + 1, zoneID.indexOf("]"));

    TimeZone zone = TimeZone.getTimeZone(timeZone);
    int OffsetUTC = zone.getOffset(System.currentTimeMillis()) / 1000;

    return convertTimeStampToUTC(OffsetUTC + timeStamp);
  }

  public String computeTimeOfFlyOver(double lat, double lon) {
    try {
      long timeStamp = issWebService.fetchIssFlyOverData(lat, lon);
      return convertTimeStampToTimeAtLatLon(lat, lon, timeStamp);
    } catch(Exception ex) {
      return ex.getMessage();
    }
  }

}

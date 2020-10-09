package locator;

import java.net.URL;
import org.json.JSONObject;

import static java.util.stream.Collectors.joining;

public class OpenNotifyIssWebService implements ISSWebService {

  public String parseJSONData(String jsonString) {
    JSONObject jsonText = new JSONObject(jsonString);
    if (jsonText.getString("message").equals("failure"))
      return jsonText.getString("reason");

    else return Integer.toString(jsonText.getJSONArray("response")
      .getJSONObject(0).getInt("risetime"));
  }

  public String callISSService(double lat, double lon) throws Exception {
    final int n = 2;
    String url = "http://api.open-notify.org/iss-pass.json?lat=" + lat + "&lon=" + lon + "&n=" + n;

    try {
      return new java.util.Scanner(
        new URL(url).openStream()).tokens().collect(joining(""));
    } catch(Exception ex) {
      throw new RuntimeException("Invalid Latitude or Longitude");
    }
  }

  public long fetchIssFlyOverData(double lat, double lon) {
    String urlResponse, parsedResponse;

    try {
        urlResponse = callISSService(lat, lon);
        parsedResponse = parseJSONData(urlResponse);
    } catch(Exception ex) {
      throw new RuntimeException(ex.getMessage());
    }
    return Long.parseLong(parsedResponse);
  }
}

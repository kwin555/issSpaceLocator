package locator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OpenNotifyIssWebServiceTest {
  OpenNotifyIssWebService webService, spyWebService;

  @BeforeEach
  void initiateClassesAndMocks() {
    webService = new OpenNotifyIssWebService();
    spyWebService = Mockito.spy(OpenNotifyIssWebService.class);
  }

  String houstonTimeZone = "{\n" +
    "  \"message\": \"success\", \n" +
    "  \"request\": {\n" +
    "    \"altitude\": 100, \n" +
    "    \"datetime\": 1569560097, \n" +
    "    \"latitude\": 29.7216183, \n" +
    "    \"longitude\": -95.3893835, \n" +
    "    \"passes\": 2\n" +
    "  }, \n" +
    "  \"response\": [\n" +
    "    {\n" +
    "      \"duration\": 396, \n" +
    "      \"risetime\": 1569571375\n" +
    "    }\n" +
    "  ]\n" +
    "}";

  String newYorkTimeZone = "{\n" +
    "  \"message\": \"success\", \n" +
    "  \"request\": {\n" +
    "    \"altitude\": 100, \n" +
    "    \"datetime\": 1569560146, \n" +
    "    \"latitude\": 40.7487128, \n" +
    "    \"longitude\": -73.9859724, \n" +
    "    \"passes\": 2\n" +
    "  }, \n" +
    "  \"response\": [\n" +
    "    {\n" +
    "      \"duration\": 648, \n" +
    "      \"risetime\": 1569619892\n" +
    "    }\n" +
    "  ]\n" +
    "}";

  @Test
  void parseDataAndReturnTimeStampForSampleJSON() {
    assertAll(
      () -> assertEquals("1569571375", 
        webService.parseJSONData(houstonTimeZone)),
      () -> assertEquals("1569619892",
        webService.parseJSONData(newYorkTimeZone))
    );
  }

  String latTooLarge = "{\n" +
    "  \"message\": \"failure\", \n" +
    "  \"reason\": \"Latitude must be number between -90.0 and 90.0\"\n" +
    "}";

  String lonTooLarge = "{\n" +
    "  \"message\": \"failure\", \n" +
    "  \"reason\": \"Longitue must be number between -180.0 and 180.0\"\n" +
    "}";

  @Test
  void parseDataWithErrorForLatBeingTooLarge() {
    assertEquals("Latitude must be number between -90.0 and 90.0",
      webService.parseJSONData(latTooLarge));
  }

  @Test
  void parseDataWithErrorForLonBeingTooLarge() {
    assertEquals("Longitue must be number between -180.0 and 180.0",
      webService.parseJSONData(lonTooLarge));
  }

  @Test
  void fetchIssFlyOverDataPassesResponseToParseFunction() throws Exception {
    spyWebService.callISSService(29.7216183, -95.3893835);
    spyWebService.parseJSONData(houstonTimeZone);

    Mockito.verify(spyWebService).callISSService(29.7216183, -95.3893835);
    Mockito.verify(spyWebService).parseJSONData(houstonTimeZone);
  }

  @Test
  void fetchIssFlyOverDataReturnsTimeStampFromParseFunction() throws Exception {
    long mockTimeStamp = Long.parseLong(
      spyWebService.parseJSONData(spyWebService.callISSService(29.7216183, -95.3893835)));

    assertEquals(mockTimeStamp, webService.fetchIssFlyOverData(29.7216183, -95.3893835));
  }

  @Test
  void fetchIssFlyOverDataReturnsErrorFromParseFunction() {
    RuntimeException exception = assertThrows(RuntimeException.class,
      () -> webService.fetchIssFlyOverData(-181, 1992));

    assertEquals("Invalid Latitude or Longitude", exception.getMessage());
  }

  @Test
  void fetchISSFlyOverDataReportsNetworkFailure() throws Exception {
    when(spyWebService.callISSService(1.290270, 103.851959))
      .thenThrow(new IOException("Network Error"));

    RuntimeException exception = assertThrows(RuntimeException.class,
      () -> spyWebService.fetchIssFlyOverData(1.290270, 103.851959));
    assertTrue(exception.getMessage().contains("Network Error"));
  }

  @Test
  void fetchIssFlyOverDataReturnsTimeStamp() {
    assertTrue(webService.fetchIssFlyOverData(29.7199, -95.3422) > 0L);
  }
}

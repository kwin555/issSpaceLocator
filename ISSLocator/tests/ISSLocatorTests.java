package locator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class ISSLocatorTests {
  ISSLocator issLocator;
  ISSWebService webService;

  @Test
  void Canary() {
    assertTrue(true);
  }

  @BeforeEach
  void initiateMocksForTests() {
    webService = mock(ISSWebService.class);
    issLocator = new ISSLocator();
    issLocator.setWebService(webService);
  }

  @Test
  void timeStampShouldReturnAppropriateDate() {
    assertAll(
      () -> assertEquals("January 01, 1970, 00:00:01",
        issLocator.convertTimeStampToUTC(1)),
      () -> assertEquals("January 01, 1970, 00:00:02",
        issLocator.convertTimeStampToUTC(2)),
      () -> assertEquals("January 01, 1970, 00:01:00",
        issLocator.convertTimeStampToUTC(60)),
      () -> assertEquals("September 22, 2019, 05:39:09",
        issLocator.convertTimeStampToUTC(1569130749))
    );
  }

  @Test
  void timeStampShouldReturnHoustonAndTime() {
    assertAll(
      () -> assertEquals("December 31, 1969, 19:00:01",
        issLocator.convertTimeStampToTimeAtLatLon(
          29.7216183, -95.3893835, 1)),
      () -> assertEquals("December 31, 1969, 19:00:02",
        issLocator.convertTimeStampToTimeAtLatLon(
          29.7216183, -95.3893835, 2)),
      () -> assertEquals("December 31, 1969, 19:01:00",
        issLocator.convertTimeStampToTimeAtLatLon(
          29.7216183, -95.3893835, 60)),
      () -> assertEquals("September 22, 2019, 00:39:09",
        issLocator.convertTimeStampToTimeAtLatLon(
          29.7216183, -95.3893835, 1569130749))
    );
  }

  @Test
  void timeStampShouldReturnNewYorkCityAndTime() {
    assertAll(
      () -> assertEquals("December 31, 1969, 20:00:01",
        issLocator.convertTimeStampToTimeAtLatLon(
          40.7487128, -73.9859724, 1)),
      () -> assertEquals("December 31, 1969, 20:00:02",
        issLocator.convertTimeStampToTimeAtLatLon(
          40.7487128, -73.9859724, 2)),
      () -> assertEquals("December 31, 1969, 20:01:00",
        issLocator.convertTimeStampToTimeAtLatLon(
          40.7487128, -73.9859724, 60)),
      () -> assertEquals("September 22, 2019, 01:39:09",
        issLocator.convertTimeStampToTimeAtLatLon(
          40.7487128, -73.9859724, 1569130749))
    );
  }

  @Test
  void timeStampShouldReturnSingaporeAndTime() {
    assertAll(
      () -> assertEquals("January 01, 1970, 08:00:01",
        issLocator.convertTimeStampToTimeAtLatLon(1.290270, 103.851959, 1)),
      () -> assertEquals("January 01, 1970, 08:00:02",
        issLocator.convertTimeStampToTimeAtLatLon(1.290270, 103.851959, 2)),
      () -> assertEquals("January 01, 1970, 08:01:00",
        issLocator.convertTimeStampToTimeAtLatLon(1.290270, 103.851959, 60)),
      () -> assertEquals("September 22, 2019, 13:39:09",
        issLocator.convertTimeStampToTimeAtLatLon(
          1.290270, 103.851959, 1569130749))
    );
  }

  @Test
  void computeTimeOfFlyOverReturnsTimeBasedOnTimeStampFromFetchIssFlyOverData() {
    long timeStamp = 1569130749;
    when(webService.fetchIssFlyOverData(1.290270, 103.851959))
      .thenReturn(timeStamp);

    assertEquals("September 22, 2019, 13:39:09", 
      issLocator.computeTimeOfFlyOver(1.290270, 103.851959));
  }

  @Test
  void computeTimeOfFlyOverReportsErrorFromWebService() {
    when(webService.fetchIssFlyOverData(1.290270, 103.851959))
      .thenThrow(new RuntimeException("Web Service Error"));

    assertEquals("Web Service Error",
      issLocator.computeTimeOfFlyOver(1.290270, 103.851959));
  }

  @Test
  void computeTimeOfFlyOverReportsNetworkFailure() {
    when(webService.fetchIssFlyOverData(1.290270, 103.851959))
      .thenThrow(new RuntimeException("Network Failure"));

    assertEquals("Network Failure",
      issLocator.computeTimeOfFlyOver(1.290270, 103.851959));
  }
}

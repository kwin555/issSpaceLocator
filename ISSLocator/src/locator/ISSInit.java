package locator;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ISSInit {
  public static void main(String[] args) {

    ISSLocator issLocator = new ISSLocator();
    issLocator.setWebService(new OpenNotifyIssWebService());
    List<Point2D> coordinates = new ArrayList<>();

    String[] parts = Stream.of(args)
      .flatMap(a -> Stream.of(a.split(" ")))
      .toArray(String[]::new);

    String[] pairs;
    for (String part : parts) {
      pairs = part.split(",");
      coordinates.add(new Point2D.Double(
        Double.parseDouble(pairs[0]), Double.parseDouble(pairs[1])));
    }

    for (Point2D location : coordinates) {
      System.out.println(issLocator.computeTimeOfFlyOver(
        location.getX(), location.getY()) + "\n");
    }
  }
}

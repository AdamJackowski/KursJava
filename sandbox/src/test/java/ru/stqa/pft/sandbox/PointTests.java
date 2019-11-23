package ru.stqa.pft.sandbox;

import org.testng.Assert;
import org.testng.annotations.Test;

public class PointTests {
  @Test

  public void pointDistances() {
    Point p1 = new Point();
    Point p2 = new Point(1,1);
    Point p3 = new Point(3,4);

    Assert.assertEquals(p1.distance(p2), 1.4142135623730951);
    Assert.assertEquals(p1.distance(p3), 5.0);
    Assert.assertEquals(p2.distance(p3), 3.605551275463989);

  }
}

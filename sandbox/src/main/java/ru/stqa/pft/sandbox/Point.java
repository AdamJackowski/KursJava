package ru.stqa.pft.sandbox;

public class Point {
  public double x;
  public double y;

  public Point()
  {
    x = 0;
    y =0;
  }
  public Point(double x1, double y1)
  {
    x = x1;
    y =y1;
  }
  public double distance(Point p)
  {
    return Math.sqrt(Math.pow(this.x-p.x, 2) + Math.pow(this.y-p.y, 2));
  }


}

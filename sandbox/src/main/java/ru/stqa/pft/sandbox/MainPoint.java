package ru.stqa.pft.sandbox;


public class MainPoint {
  // Static function to calculate distance
  public static double distance(Point p1, Point p2)
  {
    return Math.sqrt(Math.pow(p1.x-p2.x, 2) + Math.pow(p1.y-p2.y, 2));
  }

  public static void main (String[] args) {

    Point p1 = new Point();
    Point p2 = new Point(1,1);
    Point p3 = new Point(3,4);

    // Static function
    System.out.println("Static function");
    System.out.println("Distance p1, p2 "+distance(p1,p2));
    System.out.println("Distance p1, p3 "+distance(p1,p3));
    System.out.println("Distance p3, p2 "+distance(p3,p2)+"\n");

    // Methods
    System.out.println("Methods");
    System.out.println("Distance p1, p2 "+p1.distance(p2));
    System.out.println("Distance p1, p3 "+p1.distance(p3));
    System.out.println("Distance p3, p2 "+p3.distance(p2)+"\n");

    System.out.println("Program is working properly");



  }
}

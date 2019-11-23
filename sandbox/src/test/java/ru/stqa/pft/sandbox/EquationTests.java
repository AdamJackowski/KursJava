package ru.stqa.pft.sandbox;

import org.testng.Assert;
import org.testng.annotations.Test;

public class EquationTests {
  @Test
  public void test0()
  {
    Equation equation = new Equation(1,1,1);
    Assert.assertEquals(equation.rootNumber(), 0);
  }

  @Test
  public void test1()
  {
    Equation equation = new Equation(1,2,1);
    Assert.assertEquals(equation.rootNumber(), 1);
  }
  @Test
  public void test2()
  {
    Equation equation = new Equation(1,5,6);
    Assert.assertEquals(equation.rootNumber(), 2);
  }

  @Test
  public void testLinear()
  {
    Equation equation = new Equation(0,1,1);
    Assert.assertEquals(equation.rootNumber(), 1);
  }
  @Test
  public void testConstant()
  {
    Equation equation = new Equation(0,0,1);
    Assert.assertEquals(equation.rootNumber(), 0);
  }
  @Test
  public void testZero()
  {
    Equation equation = new Equation(0,0,0);
    Assert.assertEquals(equation.rootNumber(), -1);
  }
}

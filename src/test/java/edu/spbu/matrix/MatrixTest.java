package edu.spbu.matrix;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MatrixTest
{
  /**
   * ожидается 4 таких теста
   */
/*  @Test
  public void test_toString() throws Exception
  {
    Matrix m1 = new DenseMatrix(".\\src\\main\\java\\edu\\spbu\\matrix\\m1.txt");

    BufferedReader reader = new BufferedReader (new FileReader(".\\src\\main\\java\\edu\\spbu\\matrix\\m1.txt"));
    String s = new String();
    while (reader.ready())
      s=s.concat(reader.readLine()+"\n");
    s=s.substring(0,s.length()-1);
    assertEquals(s,m1.toString());
  }*/

/*  @Test
  public void test_toString() throws Exception
  {
    Matrix m1 = new DenseMatrix(".\\src\\main\\java\\edu\\spbu\\matrix\\m1.txt");

    BufferedReader reader = new BufferedReader (new FileReader(".\\src\\main\\java\\edu\\spbu\\matrix\\m1.txt"));
    String s = new String(),line;
    while (reader.ready())
    {
      line=reader.readLine();
      boolean flag=false;
      char[] str=line.toCharArray();
      for (int i=0;i<str.length;i++)
        if(str[i]!=' '||i+1<str.length&&str[i]==' '&&str[i+1]!=' ')
        {
          s=s.concat(String.valueOf(str[i]));
          flag=true;
        }

      if (flag)
        s=s.concat("\n");
    }
    s=s.substring(0,s.length()-1);
    assertEquals(s,m1.toString());
  }*/
  @Test
  public void test_constructor() throws Exception
  {
    DenseMatrix m1= new DenseMatrix();
    m1.lines=5;
    m1.columns=7;
    m1.matrix= new double[][]{{1, 5, 76, 4, 19, -25, 31},
                              {23, 7, 29, 64, 91, 55, 23},
                              {8, 1, 84, 0, 47, 2, 1},
                              {16, -73, 57, 6, 1, 58, 3},
                              {43, 0, -5, -6, 12, 0, -26}};
    Matrix m2= new DenseMatrix(".\\src\\main\\java\\edu\\spbu\\matrix\\m1.txt");
    assertEquals(m1,m2);
  }

  @Test
  public void test_toString() throws Exception
  {
    Matrix m1 = new DenseMatrix(".\\src\\main\\java\\edu\\spbu\\matrix\\m1.txt"),
           m2 = new DenseMatrix(".\\src\\main\\java\\edu\\spbu\\matrix\\m2.txt");
    String s= "1 5 76 4 19 -25 31\n" +
              "23 7 29 64 91 55 23\n" +
              "8 1 84 0 47 2 1\n" +
              "16 -73 57 6 1 58 3\n" +
              "43 0 -5 -6 12 0 -26";
    assertEquals(s,m1.toString());
    s="6 7 0 22 50\n" +
      "0 92 -1 14 11\n" +
      "-32 57 0 77 3\n" +
      "2 1 34 37 65\n" +
      "9 5 4 86 0\n" +
      "-7 93 2 4 1\n" +
      "0 -8 13 -41 1";
    assertEquals(s,m2.toString());
  }

  @Test
  public void test_equals() throws Exception
  {
    boolean test1,test2;
    Matrix m1 = new DenseMatrix(".\\src\\main\\java\\edu\\spbu\\matrix\\m1.txt");
    Matrix m2 = new DenseMatrix(".\\src\\main\\java\\edu\\spbu\\matrix\\m2.txt");
    test1=m1.equals(m2);
    m1 = new DenseMatrix(".\\src\\main\\java\\edu\\spbu\\matrix\\m1.txt");
    m2 = new DenseMatrix(".\\src\\main\\java\\edu\\spbu\\matrix\\m1.txt");
    test2=m2.equals(m1);
    assertTrue(!test1&&test2);
  }

  @Test
  public void mulDD() throws Exception
  {
    Matrix m1 = new DenseMatrix(".\\src\\main\\java\\edu\\spbu\\matrix\\m1.txt");
    Matrix m2 = new DenseMatrix(".\\src\\main\\java\\edu\\spbu\\matrix\\m2.txt");
    Matrix expected = new DenseMatrix(".\\src\\main\\java\\edu\\spbu\\matrix\\result.txt");
    assertEquals(expected, m1.mul(m2));
  }
}

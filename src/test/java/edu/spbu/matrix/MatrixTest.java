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

  @Test
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

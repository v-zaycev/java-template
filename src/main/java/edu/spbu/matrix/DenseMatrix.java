package edu.spbu.matrix;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Плотная матрица
 */
public class DenseMatrix implements Matrix
{
  double[][] matrix;
  int lines, columns;
  /**
   * загружает матрицу из файла
   * @param fileName
   */
  /*
  public DenseMatrix(String fileName) throws Exception
  {
    BufferedReader reader = new BufferedReader (new FileReader(fileName));
    String s;
    ArrayList<double[]> matrix= new ArrayList<double[]>();
    double[] current_string;
    int i=0;

    while (reader.ready())
    {
      s=reader.readLine();
      String[] elements=s.split(" ");
      current_string=new double[elements.length];
      for(int j=0;j<elements.length;j++)
        current_string[j]=Double.parseDouble(elements[j]);
      matrix.add(current_string);
      i++;
    }

    lines=i;
    columns= matrix.get(0).length;
    this.matrix=new double[i][];
    for (int j=0;j< matrix.size();j++)
      this.matrix[j]=matrix.get(j);
    reader.close();
  }
  */

  public DenseMatrix(String fileName) throws Exception
  {
    BufferedReader reader = new BufferedReader (new FileReader(fileName));
    String s, number= new String();
    ArrayList<String> elements;
    ArrayList<double[]> matrix= new ArrayList<double[]>();
    double[] current_string;
    int i=0;

    while (reader.ready())
    {
      s=reader.readLine();
      elements=new ArrayList<String>();
      char[] str=s.toCharArray();
      for (char j: str)
      {
        if (j!=' ')
          number=number.concat(String.valueOf(j));
        else
          if (number.length()>0)
          {
            elements.add(number);
            number=new String();
          }
      }
      if (number.length()>0)
      {
        elements.add(number);
        number=new String();
      }
      if (elements.size()>0)
      {
        current_string=new double[elements.size()];
        for(int j=0;j<elements.size();j++)
          current_string[j]=Double.parseDouble(elements.get(j));
        matrix.add(current_string);
        i++;
      }
    }

    lines=i;
    columns= matrix.get(0).length;
    this.matrix=new double[i][];
    for (int j=0;j< matrix.size();j++)
      this.matrix[j]=matrix.get(j);
    reader.close();
  }


  public DenseMatrix(int lines, int columns)
  {
    this.lines=lines;
    this.columns=columns;
    this.matrix=new double[lines][columns];
  }

  public DenseMatrix () {}
  /**
   * однопоточное умнджение матриц
   * должно поддерживаться для всех 4-х вариантов
   *
   * @param o
   * @return
   */
  @Override public Matrix mul(Matrix o)
  {
    if (getClass()!=o.getClass()||lines!=((DenseMatrix) o).columns)
      return null;

    double[][] transposed = new double[((DenseMatrix) o).columns][((DenseMatrix) o).lines];
    for(int i=0;i<((DenseMatrix) o).lines;i++)
      for(int j=0;j<((DenseMatrix) o).columns;j++)
        transposed[j][i]=((DenseMatrix) o).matrix[i][j];

    DenseMatrix matrix=new DenseMatrix(lines, ((DenseMatrix) o).columns);
    for(int i=0;i<matrix.lines;i++)
      for(int j=0;j<matrix.columns;j++)
        for (int k=0;k<columns;k++)
          matrix.matrix[i][j]+=this.matrix[i][k]*transposed[j][k];

    return matrix;
  }

  /**
   * многопоточное умножение матриц
   *
   * @param o
   * @return
   */
  @Override public Matrix dmul(Matrix o)
  {
    return null;
  }

  /**
   * спавнивает с обоими вариантами
   * @param o
   * @return
   */
  @Override public boolean equals(Object o)
  {
    if (this==o)
      return true;
    if (getClass()!=o.getClass())
      return false;

    if (lines!=((DenseMatrix) o).lines||columns!=((DenseMatrix) o).columns)
      return false;

    for (int i=0;i<lines;i++)
      for(int j=0;j<columns;j++)
        if (matrix[i][j]!=((DenseMatrix) o).matrix[i][j])
          return false;

    return true;
  }

  @Override public String toString()
  {
    DecimalFormat format = new DecimalFormat("#");
    String s= new String();
    for (double[] i:matrix)
    {
      for (double j : i)
      {
        if (j-(int)j==0)
          s=s.concat(format.format(j)+" ");
        else
          s=s.concat(j+" ");
      }
      s=s.substring(0,s.length()-1).concat("\n");
    }
    s=s.substring(0,s.length()-1);
        return s;
  }

  public static void main (String[] args) throws Exception
  {
    DenseMatrix m1= new DenseMatrix(".\\src\\main\\java\\edu\\spbu\\matrix\\m1.txt");
    DenseMatrix m2= new DenseMatrix(".\\src\\main\\java\\edu\\spbu\\matrix\\m2.txt");
    System.out.println(m1.mul(m2).toString());
  }
}

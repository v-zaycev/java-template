package edu.spbu.matrix;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * Плотная матрица
 */
public class DenseMatrix implements Matrix
{
  double[][] matrix;
  public int lines, columns;
  /**
   * загружает матрицу из файла
   * @param fileName
   */

  public DenseMatrix(String fileName) throws Exception
  {
    BufferedReader reader = new BufferedReader (new FileReader(fileName));
    String elements[], s;
    ArrayList<double[]> matrix= new ArrayList<>();
    double[] current_string;
    int i=0;

    while (reader.ready())
    {
      s=reader.readLine();
      elements=s.split(" ");
      if (elements.length>0)
      {
        current_string=new double[elements.length];
        for(int j=0;j<elements.length;j++)
          current_string[j]=Double.parseDouble(elements[j]);
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
    if (o instanceof DenseMatrix)
    {
      if (lines!=((DenseMatrix) o).columns)
        return null;

      DenseMatrix matrix=new DenseMatrix(lines, ((DenseMatrix) o).columns);
      for(int i=0;i<lines;i++)
        for(int j=0;j<columns;j++)
          for (int k=0;k<((DenseMatrix) o).columns;k++)
            matrix.matrix[i][k]+=this.matrix[i][j]*((DenseMatrix) o).matrix[j][k];

      return matrix;
    }
    if (o instanceof SparseMatrix)
    {
      SparseMatrix so=(SparseMatrix)o;
      if (lines!=so.columns)
        return null;

      DenseMatrix matrix=new DenseMatrix(lines, so.columns);
      DenseMatrix transposed=new DenseMatrix(columns,lines);
      for (int i=0;i<lines;i++)
        for (int j=0;j<columns;j++)
          transposed.matrix[j][i]=this.matrix[i][j];

      int k=0;

      for (int i=0;i<so.values.length;i++)
      {
        if (so.line_begin_indexes[k+1]==i)
          k++;
        for (int j=0;j<lines;j++)
          matrix.matrix[j][so.val_columns[i]]+=so.values[i]*transposed.matrix[k][j];
      }
      return matrix;
    }
    return null;
  }


  public class Line_mult  implements Runnable
  {
    int m;
    DenseMatrix d;
    DenseMatrix res;
    Line_mult (int i,DenseMatrix res,DenseMatrix d)
    {
      m=i;
      this.d=d;
      this.res=res;
    }

    @Override
    public void run()
    {
      for (int i=0;i<columns;i++)
        for (int j=0;j<d.columns;j++)
          res.matrix[m][j]+=matrix[m][i]*d.matrix[i][j];
      return;
    }
  }
  /**
   * многопоточное умножение матриц
   *
   * @param o
   * @return
   */
  @Override public Matrix dmul(Matrix o) throws InterruptedException
  {
    DenseMatrix d=(DenseMatrix) o;
    DenseMatrix result=new DenseMatrix(lines,d.columns);
    ExecutorService pool= Executors.newWorkStealingPool();
    for (int i=0;i<lines;i++)
    {
      pool.submit(new Line_mult(i,result,d));
    }

    pool.shutdown();
    pool.awaitTermination(100,TimeUnit.SECONDS);
    return result;
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
    if (o instanceof DenseMatrix)
    {
      if (lines!=((DenseMatrix) o).lines||columns!=((DenseMatrix) o).columns)
        return false;

      for (int i=0;i<lines;i++)
        for(int j=0;j<columns;j++)
          if (matrix[i][j]!=((DenseMatrix) o).matrix[i][j])
            return false;

      return true;
    }
    if (o instanceof SparseMatrix)
      return o.equals(this);
    return false;
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
    System.out.println(m1.dmul(m2).toString());
  }
}

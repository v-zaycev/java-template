package edu.spbu.matrix;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Разряженная матрица
 */

public class SparseMatrix implements Matrix
{
  double values[];
  int val_columns[];
  int lines, columns;
  int line_begin_indexes[];
  /**
   * загружает матрицу из файла
   * @param fileName
   */
  public SparseMatrix(String fileName) throws Exception
  {
    BufferedReader reader = new BufferedReader (new FileReader(fileName));
    String s, values[];
    ArrayList<Double>values_d= new ArrayList<>();
    ArrayList<Integer>val_columns = new ArrayList<>();
    ArrayList<Integer>line_begin_indexes=new ArrayList<>();
    int k;
    line_begin_indexes.add(0);
    while (reader.ready())
    {
      s=reader.readLine();
      values=s.split(" ");
      k=0;
      for (String i:values)
      {
        if (Double.parseDouble(i)!=0)
        {
          values_d.add(Double.parseDouble(i));
          val_columns.add(k);
        }
        k++;
      }
      line_begin_indexes.add(values_d.size());
      columns=values.length;
      lines++;
    }
    this.values=new double[values_d.size()];
    this.val_columns=new int[values_d.size()];
    this.line_begin_indexes= new int[line_begin_indexes.size()];
    for (int i=0;i<values_d.size();i++)
    {
      this.values[i]=values_d.get(i);
      this.val_columns[i]=val_columns.get(i);
    }
    for (int i=0;i<lines+1;i++)
      this.line_begin_indexes[i]=line_begin_indexes.get(i);

  }

  public SparseMatrix(){}
  /**
   * однопоточное умнджение матриц
   * должно поддерживаться для всех 4-х вариантов
   *
   * @param o
   * @return
   */
  @Override public Matrix mul(Matrix o)
  {
    if (o instanceof SparseMatrix)
    {
        SparseMatrix so= (SparseMatrix)o;
      if(so.lines==this.columns)
      {
        DenseMatrix mult_res=new DenseMatrix(lines,so.columns);

        int k=0;

        for (int i=0;i<values.length;i++)
        {
          if (k+1<lines&&i==line_begin_indexes[k+1])
            k++;
          int j=so.line_begin_indexes[val_columns[i]];
          while (j<so.line_begin_indexes[val_columns[i]+1])
            mult_res.matrix[k][so.val_columns[j]]+=values[i]*so.values[j++];
        }
        return  mult_res;
      }
      else
        return null;
    }
    if (o instanceof DenseMatrix)
    {
      if(((DenseMatrix) o).lines==columns)
      {
        DenseMatrix mult_res=new DenseMatrix(lines,((DenseMatrix) o).columns);
        int k=0;

        for (int i=0;i< values.length;i++)
        {
          if (k+1<lines&&i==line_begin_indexes[k+1])
            k++;
          for (int j=0;j<mult_res.columns;j++)
            mult_res.matrix[k][j]+=values[i]*((DenseMatrix) o).matrix[val_columns[i]][j];
        }
        return mult_res;
      }
      else
        return null;
    }
    return null;
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
    if (o instanceof SparseMatrix)
    {
      if(((SparseMatrix) o).columns==columns&&((SparseMatrix) o).lines==lines&&((SparseMatrix) o).values.length==values.length)
      {
        for(int i=0;i<values.length;i++)
          if (((SparseMatrix) o).values[i]!=values[i])
            return false;
          return true;
      }
      else
        return false;
    }
    if (o instanceof DenseMatrix)
    {
      if(((DenseMatrix) o).columns==columns&&((DenseMatrix) o).lines==lines)
      {
        int k=0,l=0;
        for (int i=0;i<((DenseMatrix) o).lines;i++)
          for (int j=0;j<((DenseMatrix) o).columns;j++)

            if(((DenseMatrix) o).matrix[i][j]!=0)
              if (l<values.length&&values[l]==((DenseMatrix) o).matrix[i][j])
                l++;
              else
                return false;
              else
                k++;
        if (l+k!=lines*columns)
          return false;
        return true;
      }
      else
        return false;
    }

    return false;
  }

  @Override public String toString()
  {
    DecimalFormat format = new DecimalFormat("#");
    String s=new String();
    int l=0,k=0;
    for (int i=0;i<lines;i++)
    {
      for (int j=0;j<columns;j++)
      {
        if (k+1<lines&&l==line_begin_indexes[k+1])
          k++;

        if(l<values.length&&val_columns[l]==j&&k==i)
        {
          if (values[l]-Math.floor(values[l])==0)
            s=s.concat(format.format(values[l])+" ");
          else
            s=s.concat(values[l]+" ");
          l++;
        }
        else
          s=s.concat("0 ");

      }
      s=s.substring(0,s.length()-1);
      s=s.concat("\n");
    }
    s=s.substring(0,s.length()-1);
    return s;
  }

  public static void main (String[] args) throws Exception
  {
    SparseMatrix m1= new SparseMatrix(".\\src\\main\\java\\edu\\spbu\\matrix\\m1.txt");
    SparseMatrix m2= new SparseMatrix(".\\src\\main\\java\\edu\\spbu\\matrix\\m2.txt");
    System.out.println(m1.mul(m2).toString());
  }
}

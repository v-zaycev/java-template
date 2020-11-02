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
  double[] values; //вектор всех значений в порядке строк
  int[] val_columns;//вектор соответствия значений столбцам
  int lines, columns;
  int[] line_begin_indexes; //индексы начал строк в общем векторе
  /**
   * загружает матрицу из файла
   * @param fileName
   */
  public SparseMatrix(String fileName) throws Exception
  {
    BufferedReader reader = new BufferedReader (new FileReader(fileName));
    String s, number= new String();
    ArrayList<String> elements;
    ArrayList<double[]> matrix= new ArrayList<>();
    double[] current_string;
    int i=0,n=0;

    while (reader.ready())
    {
      s=reader.readLine();
      elements=new ArrayList<>();
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
        {
          current_string[j]=Double.parseDouble(elements.get(j));
          if (current_string[j]!=0)
            n++;
        }
        matrix.add(current_string);
        i++;
      }
    }

    lines=i;
    columns= matrix.get(0).length;
    line_begin_indexes=new int[lines];
    values= new double[n];
    val_columns= new int[n];
    n=0;
    for (int j=0;j<lines;j++)
    {
      line_begin_indexes[j]=n;
      for (int k=0;k<columns;k++)
        if (matrix.get(j)[k]!=0)
        {
          values[n]=matrix.get(j)[k];
          val_columns[n]=k;
          n++;
        }
    }

  }

  public SparseMatrix(int lines, int columns, int nonzero)
  {
    this.lines=lines;
    this.columns=columns;
    values= new double[nonzero];
    val_columns= new int[nonzero];
    line_begin_indexes = new int[lines];
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
      if(((SparseMatrix) o).lines==this.columns)
      {
        DenseMatrix mult_res=new DenseMatrix(lines,((SparseMatrix) o).columns);
        SparseMatrix transposed= new SparseMatrix(((SparseMatrix) o).columns, ((SparseMatrix)o).lines, ((SparseMatrix)o).values.length);
        int[] vector = new int[transposed.columns+1];
        int k=0;

     /*   //транспонируем вторую матрицу
        for (int i=0;i<transposed.val_columns.length;i++) //подсчёт не 0 в столбцах
          vector[((SparseMatrix) o).val_columns[i]+1]++;
        for (int i=1;i<transposed.lines;i++) //определение начал столбцов в векторе значений
        {
          transposed.line_begin_indexes[i]=transposed.line_begin_indexes[i-1]+vector[i];
          vector[i]=0;
        }
        for (int i=0;i<((SparseMatrix)o).values.length;i++)  //перевод значений из строкового представления в столбцовое
        {
          if (k+1<((SparseMatrix)o).lines&&i==((SparseMatrix)o).line_begin_indexes[k+1])
            k++;
          transposed.values[transposed.line_begin_indexes[((SparseMatrix)o).val_columns[i]]+vector[((SparseMatrix)o).val_columns[i]]]=((SparseMatrix)o).values[i];
          transposed.val_columns[transposed.line_begin_indexes[((SparseMatrix)o).val_columns[i]]+(vector[((SparseMatrix)o).val_columns[i]]++)]=k;
        }
     */
        k=0;
        //проходим по первому вектору и для его значений проходим по кусочкам второго, для которых возможно произведение
        for (int i=0;i<values.length;i++)
        {
          if (k+1<lines&&i==line_begin_indexes[k+1])
            k++;
          int j=((SparseMatrix) o).line_begin_indexes[val_columns[i]];
          while (val_columns[i]+1<((SparseMatrix) o).line_begin_indexes.length&&j<((SparseMatrix) o).line_begin_indexes[val_columns[i]+1]||val_columns[i]+1==((SparseMatrix) o).line_begin_indexes.length&&j<((SparseMatrix) o).values.length)
            mult_res.matrix[k][((SparseMatrix) o).val_columns[j]]+=values[i]*((SparseMatrix) o).values[j++];
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
        DenseMatrix transposed = new DenseMatrix(((DenseMatrix) o).columns, ((DenseMatrix) o).lines);

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
              if (l<values.length&&((DenseMatrix) o).matrix[i][j]==values[l])
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
          if (values[l]-(int)values[l]==0)
            s=s.concat(format.format(values[l])+" ");
          else
            s=s.concat(values[l]+" ");
          l++;
        }
        else
        {
          s=s.concat("0 ");
        }

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

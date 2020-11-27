package edu.spbu;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Random;
import java.util.stream.Collectors;
import edu.spbu.matrix.*;

public class MatrixGenerator
{
  public static final int SEED1 = 1;
  public static final int SEED2 = 2;
  public static final int EMPTY_ROW_FRACTION = 10;

  public static final String MATRIX1_NAME = "m1.txt";
  public static final String MATRIX2_NAME = "m2.txt";
  public static final int SIZE = 4000;

  private final int emptyRowFraction;
  private final int size;
  private final String emptyRow;
  private final Random rnd;
  private final String file;

  public MatrixGenerator(int seed, int emptyRowFraction, String file, int size)
  {
    this.emptyRowFraction = emptyRowFraction;
    this.size = size;
    this.file = file;
    rnd = new Random(seed);
    emptyRow = Collections.nCopies(size, "0").stream().collect(Collectors.joining(" "));
  }

  public static void main(String args[]) throws Exception
  {
    try
    {
      new MatrixGenerator(SEED1, EMPTY_ROW_FRACTION, MATRIX1_NAME, SIZE).generate();
      new MatrixGenerator(SEED2, EMPTY_ROW_FRACTION, MATRIX2_NAME, SIZE).generate();
      testPerformance();
    }
    catch (IOException e)
    {
      System.out.println("Fail to generate matrix file: " + e);
    }
  }

  private static void testPerformance() throws Exception
  {
    // Uncomment the code to Test your library

    System.out.println("Starting loading dense matrices");
    DenseMatrix m1 = new DenseMatrix(MATRIX1_NAME);
    System.out.println("1 loaded");
    DenseMatrix m2 = new DenseMatrix(MATRIX2_NAME);
    System.out.println("2 loaded");
    long start = System.currentTimeMillis();
    m1.dmul(m2);
    System.out.println("Dense Matrix time: " +(System.currentTimeMillis() - start));

    System.out.println("Starting loading sparse matrices");
    SparseMatrix m11 = new SparseMatrix(MATRIX1_NAME);
    System.out.println("1 loaded");
    SparseMatrix m22 = new SparseMatrix(MATRIX2_NAME);
    System.out.println("2 loaded");
    start = System.currentTimeMillis();
    m11.mul(m22);
    System.out.println("Sparse Matrix time: " +(System.currentTimeMillis() - start));

  }

  public void generate() throws IOException
  {
    PrintWriter out = new PrintWriter(new FileWriter(file));
    for (int i = 0; i < size; i++)
    {
      // only 1/emptyRowFraction will have non 0 values
      if (rnd.nextInt(emptyRowFraction) == 0)
        out.println(generateRow());
      else
        out.println(emptyRow);
    }
    out.close();
  }

  private String generateRow()
  {
    return rnd.ints(0, emptyRowFraction).limit(size).mapToObj(r -> (r == 0) ? "" + rnd.nextInt(10000) : "0")
            .collect(Collectors.joining(" "));
  }

}

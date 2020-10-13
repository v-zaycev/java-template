package edu.spbu.sort;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
//check
/**
 * Created by artemaliev on 07/09/15.
 */
public class IntSort
{
  public static void merge (int[] main_array, int[] copy_array, int left, int right)
  {
    if (left<right)
    {
      int middle = (left + right) / 2;
      merge(main_array, copy_array, left, middle);
      merge(main_array, copy_array, middle+1, right);

      int i = left, k = left, j = middle+1;
      while (i <= middle && j <= right)
        if (copy_array[i] < copy_array[j])
          main_array[k++]=copy_array[i++];
        else
          main_array[k++]=copy_array[j++];
      while (i<=middle)
        main_array[k++]=copy_array[i++];
      while (j<=right)
        main_array[k++]=copy_array[j++];

      for (int a=left;a<right+1;a++)
      {
        copy_array[a]=main_array[a];
      }
    }
  }

  public static void sort (int[] array)
  {
    int[] array_copy=array.clone();
    merge(array,array_copy,0,array.length-1);
  }

  public static void sort (List<Integer> list)
  {
    int list_size = list.size();
    int[] main_array, copy_array;
    main_array = new int[list_size];
    copy_array = new int[list_size];
    for (int i=0;i<list_size;i++)
    {
      copy_array[i]=list.get(i);
      main_array[i]=list.get(i);
    }
    merge(main_array,copy_array,0,list_size-1);
    list.clear();
    for (int i:main_array)
      list.add(i);
  }

  public static void main (String[] args)
  {
    int[] array={1,7,3,54,6,2,4,7,9,41,12, 57,11};
    sort(array);
    for (int j : array) System.out.print(j+" ");
  }
}


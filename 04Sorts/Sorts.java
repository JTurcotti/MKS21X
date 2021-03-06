import java.util.Arrays;
import java.lang.reflect.*;
import java.lang.annotation.*;
import java.util.Random;

@Retention(RetentionPolicy.RUNTIME)
@interface SortAlgorithm {}

public class Sorts {

    public static String name(){
	return "01.Turcotti.Joshua";
    }

    public static void swap(int[] data, int a, int b) {
	int temp = data[a];
	data[a] = data[b];
	data[b] = temp;
    }

    public static boolean isOrdered(int[] data) {
	for (int i=data.length-1; i>0; i--) if (data[i]<data[i-1]) return false;
	return true;
    }

    @SortAlgorithm
    public static void selectionSort(int[] data) {
	for (int i=0; i+1<data.length; i++) {
	    int min = i;
	    for (int j=i; j<data.length; j++) if (data[j]<data[min]) min=j;
	    swap(data, i, min);
	}
    }

    @SortAlgorithm
    public static void insertionSort(int[] data) {
	for (int i=0; i<data.length; i++) {
	    int j=i;
	    while (j>0 && data[j]<data[j-1]) swap(data, j, --j);
	}
    }

    @SortAlgorithm
    public static void bubbleSort(int[] data) {
	for (int j=data.length-1; j>0; j--) for (int i=0; i<j; i++) if (data[i]>data[i+1]) swap(data, i,i+1);
    }


    public static void mergeSort(int[] data) {
	

    public static long getRuntime(Method algorithm, int size) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
	int[] arr = new int[size];
	Random r = new Random();
	for (int i=0; i<size; i++) arr[i]=r.nextInt();
	long start = System.currentTimeMillis();
	algorithm.invoke(null, arr);
	long runtime = System.currentTimeMillis() - start;
	if (isOrdered(arr)) return runtime; else return -1L;
    }

    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
	for (Method me: Sorts.class.getDeclaredMethods()) {
	    if (me.getAnnotation(SortAlgorithm.class)!=null) {
		long runtime = getRuntime(me, 10000);
		System.out.println(me.getName() + ":      " + runtime + " ms");
	    }
	}
    }
}
		
	       
	    
	    

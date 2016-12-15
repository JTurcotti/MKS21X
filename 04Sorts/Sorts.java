import java.util.Arrays;

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
	    


    public static void main(String[] args) {
	int[] arr = new int[] {1, 2, 3, 6, 5, 4, 7, 4, 7, 3, 5};
	selectionSort(arr);
	System.out.println(isOrdered(arr));
	insertionSort(arr);
	System.out.println(isOrdered(arr));
    }
}
		
	       
	    
	    

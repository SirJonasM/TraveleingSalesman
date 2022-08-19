import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class test {

    public static void main(String [] args) throws FileNotFoundException {
        Punkt [] array2 = TSP.readFile();
        Scanner scanner = new Scanner(TSP.OPTIMUMTOURFILE);
        int [] array = new int[array2.length];
        while (!scanner.next().equals("TOUR_SECTION")){
        }
        int zahl = scanner.nextInt();
        int counter = 0;
        while(zahl!=-1){
            array[counter] = zahl;
            counter++;
            zahl = scanner.nextInt();
        }
        Punkt [] array3 = new Punkt[array2.length];
        for(int i = 0;i<array.length;i++){
            for (int n = 0; n<array.length;n++){
                if(array2[n].id+1 == array[i]){
                    array3[i] = array2[n];
                }
            }
        }
        for (Punkt punkt : array3) {
            for (int i = 0; i < array3.length; i++) {
                punkt.distances.put(array3[i].id, TSP.getAbstand(punkt, array3[i]));
            }
        }
        Permutation permutation = new Permutation(array3);
        System.out.println(permutation);


    }
}

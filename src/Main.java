import model.LargeInt;
import model.LargeNumber;

public class Main {
    public static void main(String[] args) {
        LargeNumber largeNumber1 = new LargeInt(12340);
        LargeNumber largeNumber2 = new LargeInt("0010");

//        largeNumber1.subtractFrom(largeNumber2).subtractFrom(largeNumber2).subtractFrom(largeNumber2).subtractFrom(largeNumber2);

        largeNumber1.divideBy(largeNumber2).divideBy(largeNumber2).divideBy(largeNumber2).divideBy(largeNumber2).divideBy(largeNumber2);

        System.out.println(largeNumber1);
    }
}
public class TypeCastingExample {
    public static void main(String[] args) {
        double d = 25.75;
        int i = (int) d;
        System.out.println("Double value: " + d);
        System.out.println("After casting to int: " + i);
        int num = 50;
        double result = (double) num;
        System.out.println("Integer value: " + num);
        System.out.println("After casting to double: " + result);
    }
}
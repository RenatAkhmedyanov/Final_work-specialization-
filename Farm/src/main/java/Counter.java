public class Counter implements AutoCloseable {
    private static int count = 0;
    public static void add() {
        count++;
    }

    public static int getCount() {
        return count;
    }

    @Override
    public void close() throws Exception {
        System.out.println("Counter closed");
    }
}

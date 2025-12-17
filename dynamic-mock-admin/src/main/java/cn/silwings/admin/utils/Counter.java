package cn.silwings.admin.utils;

/**
 * @ClassName Counter
 * @Description
 * @Author Silwings
 * @Date 2023/8/8 15:45
 * @Since
 **/
public class Counter {

    private int count;

    private Counter(final int count) {
        this.count = count;
    }

    public int increment() {
        this.count++;
        return this.get();
    }

    public int add(final int num) {
        this.count += num;
        return this.get();
    }

    public int decrement() {
        this.count--;
        return this.get();
    }

    public int reduce(final int num) {
        this.count -= num;
        return this.get();
    }

    private int get() {
        return this.count;
    }

    public static Counter newInstance() {
        return newInstance(0);
    }

    public static Counter newInstance(final int start) {
        return new Counter(start);
    }
}
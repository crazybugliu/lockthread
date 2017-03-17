package test;

import java.util.Arrays;
import java.util.Random;

/**
 * 检测volatile在读取加载主存新值时， 是否一并加载别的变量
 * 结果是：不一定， 如这样的结果
 * W a=[64, 63, 95] x=1
 * R a=[0, 0, 0] x=1
 *
 * 或许 在Reader中，先直接读volatile变量x，会触发其他几个变量从主存中更新到本地内存，这样可以避免加重数据不一致
 */
public class VolatileSwapTest {

    static final Random random = new Random();

    public static void main(String[] args) {
        class Data {
            public int[] a = new int[3];
            public volatile Integer x = 0;

            @Override
            public String toString() {
                return "a=" + Arrays.toString(a) + " x=" + x;
            }
        }


        class Writer implements Runnable {
            private Data data;
            private int count = 0;

            public Writer(Data data) {
                this.data = data;
            }

            public void run() {
                while (true) {
                    data.a[0] = random.nextInt(100);
                    data.a[1] = random.nextInt(100);
                    data.a[2] = random.nextInt(100);
                    data.x = ++count;
                    System.out.println("\nW " + data);
                    if(count >= 100)
                        break;
                    try {
                        Thread.sleep(random.nextInt(100));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }


        class Reader implements Runnable {
            private Data data;

            public Reader(Data data) {
                this.data = data;
            }

            public void run() {
                while (true) {
                    System.out.println("R " + data);
                    if(data.x >= 100)
                        break;
                    try {
                        Thread.sleep(random.nextInt(100));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        Data data = new Data();

        new Thread(new Writer(data)).start();
        new Thread(new Reader(data)).start();

    }


}

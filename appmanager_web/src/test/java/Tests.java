import org.junit.Test;

import java.io.*;
import java.util.List;
import java.util.Vector;

/**
 * Created by Neo on 2017/1/11.
 */
public class Tests {

    public static void main(String[] args) {

        // 初始化一个list，放入5个元素
        final List<Integer> list = new Vector<>();
        for (int i = 0; i < 5; i++) {
            list.add(i);
        }

        // 线程一：通过Iterator遍历List
        new Thread(new Runnable() {

            @Override
            public void run() {
//                for (int i = 0; i < 10; i++) {
//                    System.out.println(list.get(0));
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
                    for (int item : list) {
                        System.out.println("遍历元素：" + item);
                        // 由于程序跑的太快，这里sleep了1秒来调慢程序的运行速度
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
            }
        }).start();

        // 线程二：remove一个元素
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 由于程序跑的太快，这里sleep了1秒来调慢程序的运行速度
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (list){
                    for (int i = 0; i < 4; i++) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        list.remove(0);
                    }
                }
                System.out.println("list.remove(4)");
            }
        }).start();
    }


    @Test
    public void downloadTest() {
        File source = new File("E:\\Software\\myeclipse-2015-stable-2.0-offline-installer-windows.exe");
        File target = new File("E:\\a.exe");

        try {
            InputStream is = new FileInputStream(source);
            RandomAccessFile os = new RandomAccessFile(target, "rw");

            long skip = 0;
            if (target.exists()) {
                skip = target.length();
                is.skip(skip);
                os.seek(skip);
            }
            byte[] bs = new byte[1024];
            int len = 0;
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }

            os.close();
            is.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

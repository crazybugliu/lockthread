package test.delayQueue;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DelayQueue使用场景举例：http://www.cnblogs.com/jobs/archive/2007/04/27/730255.html
 * a) 关闭空闲连接。服务器中，有很多客户端的连接，空闲一段时间之后需要关闭之。
 * b) 缓存。缓存中的对象，超过了空闲时间，需要从缓存中移出。
 * c) 任务超时处理。在网络协议滑动窗口请求应答式交互时，处理超时未响应的请求。
 */

public class Cache<K, V> {
    private static final Logger LOG = Logger.getLogger(Cache.class.getName());

    private ConcurrentMap<K, V> cacheObjMap = new ConcurrentHashMap<K, V>();

    private DelayQueue<DelayItem<Pair<K, V>>> q = new DelayQueue<>();

    private Thread daemonThread;

    public Cache() {
        Runnable daemonTask = this::daemonCheck;

        daemonThread = new Thread(daemonTask);
        daemonThread.setDaemon(true);
        daemonThread.setName("Cache Daemon");
        daemonThread.start();
    }

    // 超时对象处理
    private void daemonCheck() {
        if (LOG.isLoggable(Level.INFO))
            LOG.info("cache service started.");

        for (; ; ) {
            try {
                DelayItem<Pair<K, V>> delayItem = q.take();
                System.out.println("daemon take : " + delayItem);
                if (delayItem != null) {
                    Pair<K, V> pair = delayItem.getItem();
                    cacheObjMap.remove(pair.key, pair.value); // compare and remove
                }
            } catch (InterruptedException e) {
                if (LOG.isLoggable(Level.SEVERE))
                    LOG.log(Level.SEVERE, e.getMessage(), e);
                break;
            }
        }

        if (LOG.isLoggable(Level.INFO))
            LOG.info("cache service stopped.");
    }

    // 添加缓存对象
    public void put(K key, V value, long time, TimeUnit unit) {
        V oldValue = cacheObjMap.put(key, value);
        if (oldValue != null)
            q.remove(key);

        long nanoTime = TimeUnit.NANOSECONDS.convert(time, unit);
        q.put(new DelayItem<>(new Pair<>(key, value), nanoTime));
    }

    public V get(K key) {
        return cacheObjMap.get(key);
    }

    // 测试入口函数
    public static void main(String[] args) throws Exception {
        Cache<Integer, String> cache = new Cache<Integer, String>();
        cache.put(2, "aaaa", 40, TimeUnit.SECONDS);
        cache.put(1, "bbbb", 3, TimeUnit.SECONDS);

        Thread.sleep(1000 * 2);
        {
            String str = cache.get(1);
            System.out.println(str);
        }

        Thread.sleep(1000 * 2);
        {
            String str = cache.get(1);
            System.out.println(str);
        }
    }
}
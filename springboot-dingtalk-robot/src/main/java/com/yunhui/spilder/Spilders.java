package com.yunhui.spilder;

import com.yunhui.bean.Task;
import com.yunhui.bean.Word;
import com.yunhui.service.WordService;
import com.yunhui.utils.HtmlParseUtils;
import com.yunhui.utils.Requests;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class Spilders {

    @Autowired
    WordService wordService;
    private volatile boolean signal = false;
    private LinkedBlockingQueue<Task> queue = new LinkedBlockingQueue<>(1000);

    private ExecutorService spilderThreadPool = Executors.newFixedThreadPool(10);

    int limit = 100;

    {
        new Thread(() -> {
            while (!signal) {
                try {
                    Task task = queue.poll();
                    if (task == null) {
                        log.info("wait start spilderThreadPool...");
                        Thread.sleep(1000 * 60);
                        continue;
                    }
                    log.info("start spilderThreadPool...");
                    AtomicInteger atomicInteger = new AtomicInteger(0);
                    for (int i = 0; i < 10; i++) {
                        spilderThreadPool.execute(() -> {
                            while (true) {
                                Task take = queue.poll();
                                if (take == null) {
                                    log.info("queue poll is null,queue size:{}", queue.size());
                                    if (atomicInteger.incrementAndGet()>=limit) {
                                        log.info("queue empty exceed the limit 100,start shutdown threadpool");
                                        spilderThreadPool.shutdown();
                                        break;
                                    }
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        log.error("occur exception", e);
                                    }
                                    continue;
                                }
                                List<Word> words = HtmlParseUtils.parseWord(Requests.html(take.getUrl()), take.getType());
                                for (Word word : words) {
                                    int result = wordService.addWord(word);
                                    log.info("add word success,result:{},word:{}", result, word);
                                }
                            }
                        });
                    }
                    signal = true;
                } catch (InterruptedException e) {
                    log.error("occer exception", e);
                }
            }
        }).start();
    }


    public void putTaskToQueue(String url, Integer type, Integer lastPage) {
        for (Integer i = 0; i <= lastPage; i++) {
            queue.offer(new Task(url.replace("$page$", i.toString()), type));
            log.info("add Task to queue, current size is {}", queue.size());
        }
    }

}

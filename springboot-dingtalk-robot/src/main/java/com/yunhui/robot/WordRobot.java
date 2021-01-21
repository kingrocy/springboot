package com.yunhui.robot;

import com.yunhui.bean.Word;
import com.yunhui.bean.request.AtParam;
import com.yunhui.bean.request.CommonMsgRequestParam;
import com.yunhui.bean.request.MdParam;
import com.yunhui.service.WordService;
import com.yunhui.utils.MarkDownUtils;
import com.yunhui.utils.Requests;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Getter
@Setter
@Slf4j
@Component
public class WordRobot {

    @Autowired
    WordService wordService;
    /**
     * 5分钟丢一次单词
     */
    private Integer frequency = 5;

    public void sendWord() {
        Word word = wordService.randomWord();
        if (word != null) {
            Requests.dingtalk(CommonMsgRequestParam.markdown(new MdParam(
                    word.getWord(),
                    MarkDownUtils.builder()
                            .h1(word.getWord())
                            .wordH(word.getDesc())
                            .build()
            ), null));
            log.info("send randomWord,word:{}", word);
        }
    }

    @PostConstruct
    public void init() {
        Thread thread = new Thread(() -> {
            while (true) {
                sendWord();
                try {
                    Thread.sleep(frequency * 60 * 1000);
                } catch (InterruptedException e) {
                    log.error("word robot occur exception", e);
                }
            }
        });
        thread.start();
    }

}

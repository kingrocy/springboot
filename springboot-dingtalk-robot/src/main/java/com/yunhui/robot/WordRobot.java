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

/**
 * @Date : 2019-07-17 11:56
 * @Author : dushaoyun[dushaoyun@souche.com]
 * @CopyRight : DataTeam @ SouChe Inc
 * @Desc :
 */
@Getter
@Setter
@Slf4j
@Component
public class WordRobot {

    /**
     * 5分钟丢一次单词
     */
    private Integer frequency=5;

    @Autowired
    WordService wordService;

    @PostConstruct
    public void init(){
            Thread thread=new Thread(()->{
                while (true){
                    Word word = wordService.randomWord();
                    Requests.dingtalk(CommonMsgRequestParam.markdown(new MdParam(
                            word.getWord(),
                            MarkDownUtils.builder()
                                    .h1(word.getWord()+" @17755746651")
                                    .wordH(word.getDesc())
                                    .build()
                    ),AtParam.build().mobile("17755746651")));
                    log.info("send randomWord...");
                    try {
                        Thread.sleep(frequency*60*1000);
                    } catch (InterruptedException e) {
                        log.error("word robot occur exception", e);
                    }
                }
            });
            thread.setDaemon(true);
            thread.start();
    }

}

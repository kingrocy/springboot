package com.yunhui.controller;

import com.yunhui.bean.Chapter;
import com.yunhui.bean.ChapterContent;
import com.yunhui.mapper.ChapterContentMapper;
import com.yunhui.spilder.utils.HttpRequestUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * @Author: Yun
 * @Description:
 * @Date: Created in 2018-06-25 16:22
 */
public class ChapterCallable implements Callable<String> {

    private List<Chapter> list;

    private ChapterContentMapper chapterContentMapper;

    public ChapterCallable(List<Chapter> list, ChapterContentMapper chapterContentMapper) {
        this.list = list;
        this.chapterContentMapper = chapterContentMapper;
    }

    @Override
    public String call() throws Exception {
        long start = System.currentTimeMillis();
        for (Chapter chapter : list) {
            String content = HttpRequestUtil.doGet(chapter.getChapterUrl(), "gbk");
            Document document = Jsoup.parse(content);
            String text = document.select(".novel_content").html();
            ChapterContent chapterContent = new ChapterContent();
            chapterContent.setChapterContentDetail(text);
            chapterContent.setChapterId(chapter.getChapterId());
            try {
                chapterContentMapper.insertSelective(chapterContent);
            } catch (Exception e) {
                System.out.println("chapter:" + chapter.getChapterId() + "出错！！");
                e.printStackTrace();
            }
        }
        long end = System.currentTimeMillis();
        return Thread.currentThread().getName() + "结束任务,耗时:" + (end - start) / 1000.00 + "秒";
    }
}

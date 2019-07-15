package com.yunhui.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yunhui.bean.Chapter;
import com.yunhui.mapper.ChapterContentMapper;
import com.yunhui.mapper.ChapterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @Author: Yun
 * @Description:
 * @Date: Created in 2018-06-25 15:55
 */
@CrossOrigin
@RestController
public class CoreController {


    @Autowired
    ChapterMapper chapterMapper;

    @Autowired
    ChapterContentMapper chapterContentMapper;

    @GetMapping("/chaperSpilder")
    public void chapterContentSpilder(){
        List<Chapter> chapters=chapterMapper.listChapters(1L);

        int count=chapters.size();

        //每个线程爬取100章
        final int tasks=100;

        //线程总数
        final int threads=(count%100==0)?(count/100):(count/100+1);

        ExecutorService service= Executors.newFixedThreadPool(threads);

        List<Future<String>> futures=new ArrayList<>();

        for(int i=1;i<=threads;i++){
            int start=(i-1)*tasks;
            int end=start+tasks;
            if(i==threads){
                end=count;
            }
            Future<String> future = service.submit(new ChapterCallable(chapters.subList(start, end), chapterContentMapper));
            futures.add(future);
        }

        service.shutdown();

        while(service.isTerminated()){
            for(Future<String> future:futures){
                try {
                    System.out.println(future.get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @GetMapping("/listChapters")
    public PageInfo<Chapter> listChapters(@RequestParam(value = "pageIndex",defaultValue = "1")int pageIndex){
        PageHelper.startPage(pageIndex,50);
        List<Chapter> chapters = chapterMapper.listChapters(1L);
        PageInfo<Chapter> pageInfo=new PageInfo<>(chapters,6);
        return pageInfo;
    }

    @GetMapping("/getChapterDetail")
    public Chapter getChapterDetail(long chapterId){
        return chapterMapper.getChapterDetail(chapterId);
    }

}

package com.yunhui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Author: Yun
 * @Description:
 * @Date: Created in 2018-06-25 19:01
 */
@Controller
public class PageController {


    @GetMapping("/chapters")
    public String chapters() {
        return "chapters";
    }

    @GetMapping("details")
    public String details(Long chapterId) {
        return "chapterDetails";
    }
}

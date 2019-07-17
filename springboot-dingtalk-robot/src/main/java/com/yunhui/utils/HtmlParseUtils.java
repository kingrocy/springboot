package com.yunhui.utils;

import com.yunhui.bean.Word;
import com.yunhui.constant.YouDictConstant;
import com.yunhui.enums.WordTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Date : 2019-07-16 17:59
 * @Author : dushaoyun[dushaoyun@souche.com]
 * @CopyRight : DataTeam @ SouChe Inc
 * @Desc :
 */
@Slf4j
public class HtmlParseUtils {


    private static final String arrays []=new String[]{
            "vt.",
            "n.",
            "adj.",
            "adv.",
            "vi.",
            "aux.",
            "art.",
            "interj.",
            "prep.",
            "conj.",
            "sc.",
            "oc.",
            "num.",
            "int."
    };


    private static final String covert(String word){
        if(StringUtils.isEmpty(word)){
            log.error("word is null,word={}",word );
           return null;
        }
        String replace = word.trim().replace("[英语四级词汇]", "");
        for (String n : arrays) {
            replace=replace.replace(n, "\r\n"+n);
        }
        if(!replace.contains("adv.")&&replace.contains("v.")){
            replace=replace.replace("v.", "\r\nv.");
        }
        if(replace.startsWith("\r\n")){
            replace=replace.substring(2);
        }
        return replace;
    }

    public static List<Word> parseWord(String html,Integer type){
        Document document = Jsoup.parse(html);
        Elements elements = document.select(".row").select(".thumbnail");
        List<Word> list=new ArrayList<>();
        for (Element element : elements) {
            Word word=new Word();
            word.setPic(element.select("img").eq(0).attr("src"));
            word.setWord(element.select("a").text());
            word.setDesc(covert(element.select("p").text()));
            word.setType(type);
            list.add(word);
        }
        return list;
    }


    public static Integer getLastPage(String html){
        Document document = Jsoup.parse(html);
        Elements elements = document.select(".container").select(".yd-tags");
        String href= elements.select("a").last().attr("href");
        String str=href.substring(href.lastIndexOf("_")+1, href.lastIndexOf("."));
        if (!StringUtils.isEmpty(href)){
            return Integer.parseInt(str);
        }
        return 0;
    }


    public static void main(String[] args) {
        String word="\n" +
                "adj. 向内的；内部的；精神的；本质上的；熟悉的 \n" +
                "\n" +
                "ad\n" +
                "v. 向内；内心里 \n" +
                "n. 内部；内脏；密友 \n" +
                "n. (Inw\n" +
                "ard)人名；(英)英沃德 ";
        System.out.println(word.replace("he", "11"));

    }
}

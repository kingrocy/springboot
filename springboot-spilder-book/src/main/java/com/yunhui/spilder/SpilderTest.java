package com.yunhui.spilder;

import com.yunhui.Application;
import com.yunhui.bean.Book;
import com.yunhui.bean.Chapter;
import com.yunhui.mapper.BookMapper;
import com.yunhui.mapper.ChapterMapper;
import com.yunhui.spilder.utils.HttpRequestUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: Yun
 * @Description:
 * @Date: Created in 2018-06-25 15:22
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@EnableAutoConfiguration
public class SpilderTest {

    @Autowired
    BookMapper bookMapper;

    @Autowired
    ChapterMapper chapterMapper;

    @Test
    public void bookSpilder() {
        String content = HttpRequestUtil.doGet("http://www.163ks.info/files/article/html/0/318/index.html", "gb18030");
        Document document = Jsoup.parse(content);
        String bookName = document.select(".novel_name:eq(0)").text();
        String bookAuthor = document.select(".novel_info a").text();
        Book book = new Book();
        book.setBookName(bookName);
        book.setBookAuthor(bookAuthor);
        book.setBookUrl("http://www.163ks.info/files/article/html/0/318/index.html");
        bookMapper.insertSelective(book);
    }

    @Test
    public void chapterSpilder() {
        String url = "http://www.163ks.info/files/article/html/0/318/index.html";

        String content = HttpRequestUtil.doGet(url, "gb18030");

        Document document = Jsoup.parse(content);

        document.setBaseUri(url);

        Elements uls = document.select(".novel_list ul");

        for (Element ul : uls) {
            Elements elements = ul.select("li a");
            for (Element element : elements) {
                String title = element.text();
                String chapterUrl = element.absUrl("href");
                Chapter chapter = new Chapter();
                chapter.setChapterName(title);
                chapter.setChapterUrl(chapterUrl);
                chapter.setChapterBookId(1L);
                chapterMapper.insertSelective(chapter);
            }

        }
    }
}

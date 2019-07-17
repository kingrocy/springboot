import com.yunhui.Application;
import com.yunhui.bean.Word;
import com.yunhui.constant.YouDictConstant;
import com.yunhui.enums.WordTypeEnum;
import com.yunhui.service.WordService;
import com.yunhui.spilder.Spilders;
import com.yunhui.utils.HtmlParseUtils;
import com.yunhui.utils.Requests;
import org.junit.Test;
import org.junit.runner.Request;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Date : 2019-07-16 17:33
 * @Author : dushaoyun[dushaoyun@souche.com]
 * @CopyRight : DataTeam @ SouChe Inc
 * @Desc :
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes={Application.class})// 指定启动类
public class AppTest {

    @Autowired
    WordService wordService;

    @Autowired
    Spilders spilders;


    @Test
    public void test1(){
        Word word=new Word();
        word.setDesc("tette");
        word.setWord("test");
        word.setType(1);
        wordService.addWord(word);
    }


    @Test
    public void test2(){
        // 页数从0开始
        int [] frequencys=new int[]{1,2,3,4,5};
        for (int frequency : frequencys) {
            String baseUrl= YouDictConstant.getUrl(WordTypeEnum.FOURTH,frequency,null);
            String initUrl= YouDictConstant.getUrl(WordTypeEnum.FOURTH,frequency,0);
            String html = Requests.html(initUrl);
            spilders.putTaskToQueue(baseUrl, frequency, HtmlParseUtils.getLastPage(html));
        }
    }

}

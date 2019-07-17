package com.yunhui.constant;

import com.yunhui.enums.WordTypeEnum;

/**
 * @Date : 2019-07-16 09:56
 * @Author : dushaoyun[dushaoyun@souche.com]
 * @CopyRight : DataTeam @ SouChe Inc
 * @Desc :
 */
public class YouDictConstant {


    /**
     * 资源url
     * 第一个占位符 代表词典  1--四级  2--六级 3--英专4 4--英专8 5--考研 6--托福  7--雅思  8--GRE
     * 第二个占位符 代表词的使用频率  0--全部  1--高频 2--中高频 3--中频  4--中低频 5--低频
     * 第三个占位符 代表页码
     */
    private static final String RESOUCE_URL = "http://www.youdict.com/ciku/id_$type$_$frequency$_0_0_$page$.html";



    public static String getUrl(WordTypeEnum wordTypeEnum,Integer frequency,Integer page){
        String url=RESOUCE_URL;
        if(wordTypeEnum!=null){
            url=url.replace("$type$", wordTypeEnum.getKey().toString());
        }
        if(frequency!=null){
            url=url.replace("$frequency$", frequency.toString());
        }
        if(page!=null){
            url=url.replace("$page$", page.toString());
        }
        return url;
    }


}

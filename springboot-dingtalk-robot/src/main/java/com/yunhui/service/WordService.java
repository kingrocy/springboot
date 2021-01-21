package com.yunhui.service;

import com.yunhui.bean.Word;
import com.yunhui.mapper.WordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WordService {


    @Autowired
    WordMapper mapper;

    public int addWord(Word word) {
        return mapper.addWord(word);
    }

    public Word randomWord() {
        return mapper.randomWord();
    }

}

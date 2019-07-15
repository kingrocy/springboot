package com.yunhui.bean;

public class Chapter {
    private Long chapterId;

    private String chapterName;

    private String chapterUrl;

    private String chapterDeatil;

    private Long chapterBookId;

    public Long getChapterId() {
        return chapterId;
    }

    public void setChapterId(Long chapterId) {
        this.chapterId = chapterId;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName == null ? null : chapterName.trim();
    }

    public void setChapterUrl(String chapterUrl) {
        this.chapterUrl = chapterUrl;
    }

    public String getChapterUrl() {
        return chapterUrl;
    }

    public String getChapterDeatil() {
        return chapterDeatil;
    }

    public void setChapterDeatil(String chapterDeatil) {
        this.chapterDeatil = chapterDeatil;
    }

    public Long getChapterBookId() {
        return chapterBookId;
    }

    public void setChapterBookId(Long chapterBookId) {
        this.chapterBookId = chapterBookId;
    }
}
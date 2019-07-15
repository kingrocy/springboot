package com.yunhui.bean;

public class ChapterContent {

    private Long chapterContentId;

    private Long chapterId;

    private String chapterContentDetail;

    public Long getChapterContentId() {
        return chapterContentId;
    }

    public void setChapterContentId(Long chapterContentId) {
        this.chapterContentId = chapterContentId;
    }

    public Long getChapterId() {
        return chapterId;
    }

    public void setChapterId(Long chapterId) {
        this.chapterId = chapterId;
    }

    public String getChapterContentDetail() {
        return chapterContentDetail;
    }

    public void setChapterContentDetail(String chapterContentDetail) {
        this.chapterContentDetail = chapterContentDetail == null ? null : chapterContentDetail.trim();
    }
}
package com.yunhui.utils;

public class MarkDownUtils {


    private StringBuilder sb = new StringBuilder();

    private MarkDownUtils() {
    }

    public static MarkDownUtils builder() {
        return new MarkDownUtils();
    }

    public static void main(String[] args) {
        System.out.println("*");
    }

    public String build() {
        return sb.toString();
    }

    private MarkDownUtils h(String text, Integer num) {
        for (int i = 0; i < num; i++) {
            sb.append("#");
        }
        sb.append(" ");
        sb.append(text);
        sb.append("\n");
        return this;
    }

    public MarkDownUtils h1(String text) {
        return h(text, 1);
    }

    public MarkDownUtils h2(String text) {
        return h(text, 2);
    }

    public MarkDownUtils h3(String text) {
        return h(text, 3);
    }

    public MarkDownUtils h4(String text) {
        return h(text, 4);
    }

    public MarkDownUtils h5(String text) {
        return h(text, 5);
    }

    public MarkDownUtils ul(String text) {
        sb.append("- ");
        sb.append(text);
        sb.append("\n");
        return this;
    }

    public MarkDownUtils wordH(String word) {
        String[] split = word.split("\r\n");
        for (String s : split) {
            ul(s);
        }
        return this;
    }
}

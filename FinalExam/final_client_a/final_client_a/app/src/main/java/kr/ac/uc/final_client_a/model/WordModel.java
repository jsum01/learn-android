package kr.ac.uc.final_client_a.model;

import android.database.Cursor;

public class WordModel {
    public static final String COL_IDX = "idx";
    public static final String COL_ENG_WORD = "engWord";
    public static final String COL_KOR_WORD = "korWord";

    int idx;
    String engWord;
    String korWord;

    public WordModel() {
    }

    public WordModel(int idx, String engWord, String korWord) {
        this.idx = idx;
        this.engWord = engWord;
        this.korWord = korWord;
    }

    public WordModel(String engWord, String korWord) {
        this.engWord = engWord;
        this.korWord = korWord;
    }

    public static WordModel newInstance(Cursor c){
        int pos_idx = c.getColumnIndex(COL_IDX);
        int pos_eng = c.getColumnIndex(COL_ENG_WORD);
        int pos_kor = c.getColumnIndex(COL_KOR_WORD);
        return new WordModel(c.getInt(pos_idx), c.getString(pos_eng), c.getString(pos_kor));
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getEngWord() {
        return engWord;
    }

    public void setEngWord(String engWord) {
        this.engWord = engWord;
    }

    public String getKorWord() {
        return korWord;
    }

    public void setKorWord(String korWord) {
        this.korWord = korWord;
    }
}

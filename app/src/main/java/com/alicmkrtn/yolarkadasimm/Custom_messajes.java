package com.alicmkrtn.yolarkadasimm;
public class Custom_messajes {
    private String mad;
    private String mfoto;
    private String mid;
    public Custom_messajes(String vad, String vfoto,String vid)
    {
        mad = vad;
        mfoto = vfoto;
        mid =vid;
    }
    public String getad() {
        return mad;
    }
    public String getkalkis() {
        return mfoto;
    }
    public String getid() {
        return mid;
    }
}

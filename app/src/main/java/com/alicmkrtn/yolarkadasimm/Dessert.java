package com.alicmkrtn.yolarkadasimm;
public class Dessert {
    private String mad;
    private String mkalkis;
    private String mvaris;
    private String mfiyat;
    private String maciklama;
    private String murl;
    private String mtarih;
    private String mid;
    private String mcinsiyet;
    private String cinsiyetdegeri;
    public Dessert(String vad, String vkalkis,String vvaris, String vfiyat, String vaciklama,String vurl,String vtarih, String vid, String vcinsiyet,String cins)
    {
        mad = vad;
        mkalkis = vkalkis;
        mvaris = vvaris;
        mfiyat = vfiyat;
        maciklama = vaciklama;
        murl = vurl;
        mtarih = vtarih;
        mid = vid;
        mcinsiyet = vcinsiyet;
        cinsiyetdegeri = cins;
    }
    public String getad() {
        return mad;
    }
    public String getkalkis() {
        return mkalkis;
    }
    public String getvaris()
    {
        return mvaris;
    }
    public String getfiyat()
    {
        return mfiyat;
    }
    public String getaciklama()
    {
        return maciklama;
    }
    public String geturl()
    {
        return murl;
    }
    public String gettarih()
    {
        return mtarih;
    }
    public String getid()
    {
        return mid;
    }
    public String getcinsiyet()
    {
        return mcinsiyet;
    }
    public String getcinsiyetdegeri()
    {
        return cinsiyetdegeri;
    }
}

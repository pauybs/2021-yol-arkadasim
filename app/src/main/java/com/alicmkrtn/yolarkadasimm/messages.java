package com.alicmkrtn.yolarkadasimm;
public class messages {
    private String mmesaj;
    private String mgonderici;
    private String mtarih;
    public messages(String vmesaj, String vgonderici,String vtarih)
    {
        mmesaj = vmesaj;
        mgonderici = vgonderici;
        mtarih = vtarih;
    }
    public String getmesaj() {
        return mmesaj;
    }
    public String getgonderici() {
        return mgonderici;
    }
    public String gettarih()
    {
        return mtarih;
    }
}

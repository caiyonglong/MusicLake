package com.cyl.musicapi.qq;

/**
 * Created by D22434 on 2018/1/16.
 */

public class QQLyricInfo {
    /**
     * retcode : 0
     * code : 0
     * subcode : 0
     * lyric : W3RpOuS9oOi/mOimgeaIkeaAjuagtyAo44CK5aaI5aaI5YOP6Iqx5YS/5LiA5qC344CL55S16KeG5Ymn54mH5bC+5puyfOOAiuWmguaenOaIkeeIseS9oOOAi+eUteinhuWJp+aPkuabsildClthcjrolpvkuYvosKZdClthbDrmhI/lpJZdCltieTpdCltvZmZzZXQ6MF0KWzAwOjAwLjQ2XeS9oOi/mOimgeaIkeaAjuagtyAo44CK5aaI5aaI5YOP6Iqx5YS/5LiA5qC344CL55S16KeG5Ymn54mH5bC+5puyfOOAiuWmguaenOaIkeeIseS9oOOAi+eUteinhuWJp+aPkuabsikgLSDolpvkuYvosKYKWzAwOjAxLjkwXeivje+8muiWm+S5i+iwpgpbMDA6MDIuMDhd5puy77ya6Jab5LmL6LCmClswMDowMi40OF0KWzAwOjI0LjUzXeS9oOWBnOWcqOS6hui/meadoeaIkeS7rOeGn+aCieeahOihlwpbMDA6MjguNDNdClswMDozNC41M13miorkvaDlh4blpIflpb3nmoTlj7Dor43lhajlv7XkuIDpgY0KWzAwOjM4LjMxXQpbMDA6NDIuMzRd5oiR6L+Y5Zyo6YCe5by6IOivtOedgOiwjgpbMDA6NDUuODVdClswMDo0Ny4wMV3kuZ/msqHog73lipvpga7mjKEg5L2g5Y6755qE5pa55ZCRClswMDo1MS40Ml0KWzAwOjUyLjk0XeiHs+WwkeWIhuW8gOeahOaXtuWAmeaIkeiQveiQveWkp+aWuQpbMDA6NTkuNDJdClswMTowNC41N13miJHlkI7mnaXpg73kvJrpgInmi6nnu5Xov4fpgqPmnaHooZcKWzAxOjA4LjQzXQpbMDE6MTQuMzVd5Y+I5aSa5biM5pyb5Zyo5Y+m5LiA5p2h6KGX6IO96YGH6KeBClswMToxOC40Nl0KWzAxOjIyLjIxXeaAneW/teWcqOmAnuW8uiDkuI3ogq/lv5gKWzAxOjI1Ljk1XQpbMDE6MjYuNjNd5oCq5oiR5rKh6IO95Yqb6Lef6ZqPIOS9oOWOu+eahOaWueWQkQpbMDE6MzEuNTVdClswMTozMi43MF3oi6XotorniLHotorooqvliqgg6LaK6KaB6JC96JC95aSn5pa5ClswMTozOS4zNl0KWzAxOjQyLjA0XeS9oOi/mOimgeaIkeaAjuagtyDopoHmgI7moLcKWzAxOjQ2LjI4XQpbMDE6NDYuODld5L2g56qB54S25p2l55qE55+t5L+h5bCx5aSf5oiR5oKy5LykClswMTo1MS4xNl0KWzAxOjUxLjg2XeaIkeayoeiDveWKm+mBl+W/mCDkvaDkuI3nlKjmj5DphpLmiJEKWzAxOjU2LjQ0XQpbMDE6NTcuMDhd5ZOq5oCV57uT5bGA5bCx6L+Z5qC3ClswMjowMS4wOV0KWzAyOjAyLjA1XeaIkei/mOiDveaAjuagtyDog73mgI7moLcKWzAyOjA2LjIxXQpbMDI6MDYuODdd5pyA5ZCO6L+Y5LiN5piv6JC95b6X5oOF5Lq655qE56uL5Zy6ClswMjoxMS4zM10KWzAyOjExLjg4XeS9oOS7juadpeS4jeS8muaDsyDmiJHkvZXlv4Xov5nmoLcKWzAyOjIwLjA1XQpbMDI6NDQuNzJd5oiR5oWi5oWi55qE5Zue5Yiw6Ieq5bex55qE55Sf5rS75ZyIClswMjo0OC43MV0KWzAyOjU0LjM3XeS5n+W8gOWni+WPr+S7peaOpeinpuaWsOeahOS6uumAiQpbMDI6NTguNThdClswMzowMi4yNF3niLHkvaDliLDmnIDlkI4g5LiN55eb5LiN55eSClswMzowNi4xNV0KWzAzOjA3LjI2XeeVmeiogOWcqOiuoei+gyDosIHniLHov4fkuIDlnLoKWzAzOjExLjI1XQpbMDM6MTIuOTBd5oiR5Ymp5LiL5LiA5bygIOayoeWQjuaClOeahOaooeagtwpbMDM6MTkuMTVdClswMzoyMi4wNl3kvaDov5jopoHmiJHmgI7moLcg6KaB5oCO5qC3ClswMzoyNi4xNl0KWzAzOjI2Ljg1XeS9oOWNg+S4h+S4jeimgeWcqOaIkeWpmuekvOeahOeOsOWcugpbMDM6MzEuMTNdClswMzozMS45MV3miJHlkKzlrozkvaDniLHnmoTmrYwg5bCx5LiK5LqG6L2mClswMzozNi43N10KWzAzOjM3LjUzXeeIsei/h+S9oOW+iOWAvOW+lwpbMDM6NDAuOThdClswMzo0MS45M13miJHkuI3opoHkvaDmgI7moLcg5rKh5oCO5qC3ClswMzo0Ni4wNl0KWzAzOjQ2Ljk1XeaIkemZquS9oOi1sOeahOi3r+S9oOS4jeiDveW/mApbMDM6NTAuODldClswMzo1Mi4wNV3lm6DkuLrpgqPmmK/miJEg5pyA5b+r5LmQ55qE5pe25YWJClswNDowMC4wNF0KWzA0OjA0LjM0XeWQjuadpeaIkeeahOeUn+a0u+i/mOeul+eQhuaDswpbMDQ6MDguNTNdClswNDoxNC41MV3msqHkuLrkvaDokL3liLDlraTljZXnmoTkuIvlnLoKWzA0OjE4LjUwXQpbMDQ6MjIuMzZd5pyJ5LiA5aSp5pma5LiKIOaipuS4gOWcugpbMDQ6MjUuODhdClswNDoyNy4wNV3kvaDnmb3lj5Hoi43oi40g6K+05bim5oiR5rWB5rWqClswNDozMS41OV0KWzA0OjMzLjIzXeaIkei/mOaYr+ayoeeKueixqyDlsLHpmo/kvaDljrvlpKnloIIKWzA0OjQwLjA3XQpbMDQ6NDMuMzVd5LiN566h6IO95oCO5qC3IOaIkeiDvemZquS9oOWIsOWkqeS6rg==
     * trans :
     */

    private int retcode;
    private int code;
    private int subcode;
    private String lyric;
    private String trans;

    public int getRetcode() {
        return retcode;
    }

    public void setRetcode(int retcode) {
        this.retcode = retcode;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getSubcode() {
        return subcode;
    }

    public void setSubcode(int subcode) {
        this.subcode = subcode;
    }

    public String getLyric() {
        return lyric;
    }

    public void setLyric(String lyric) {
        this.lyric = lyric;
    }

    public String getTrans() {
        return trans;
    }

    public void setTrans(String trans) {
        this.trans = trans;
    }
}

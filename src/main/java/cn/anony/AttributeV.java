package cn.anony;

import cn.anony.annotations.ElementVersion;

/**
 * @author Cheng Yufei
 * @create 2024-02-18 14:56
 **/
public class AttributeV {



    @ElementVersion
    public static final String version = "";

    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

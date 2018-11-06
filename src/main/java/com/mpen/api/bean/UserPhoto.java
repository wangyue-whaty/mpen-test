package com.mpen.api.bean;

import java.io.Serializable;

/**
 * app2.0以及教师端涉及:用户头像 bean
 *
 */
public final class UserPhoto implements Serializable {

    private static final long serialVersionUID = -6063952401793903911L;

    private String loginId;
    // 用户头像
    private String photo;

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}

package com.mp.shared.common;

import java.io.Serializable;

/**
 * MP文件对应密钥
 * @author wangyue
 *
 */
public class MPFileKey implements Serializable, IsValid{

    private static final long serialVersionUID = 8615962221569420463L;
    
    public String fileName;
    public String secretData;
    
    public MPFileKey(String fileName, String secretData) {
        this.fileName = fileName;
        this.secretData = secretData;
    }
    
    @Override
    public boolean isValid() {
        return !(fileName == null || fileName.length() == 0 ||
                secretData == null || secretData.length() == 0 );
    }

    
}

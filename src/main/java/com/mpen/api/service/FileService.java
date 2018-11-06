/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.mp.shared.common.FileModule;
import com.mpen.api.bean.FileParam;
import com.mpen.api.bean.HomeworkResourceUrl;
import com.mpen.api.bean.UserSession;
import com.mpen.api.common.Constants.UploadType;

public interface FileService {
    /**
     * 上传文件.
     * 
     */
    String saveFile(MultipartFile file, String type, String uploadPath) throws IOException, Exception;

    List<FileModule> fileDownload(String path) throws Exception;

    boolean insertFile(FileParam param, String address) throws Exception;
    
    /**
     * 上传语音文件
     * @param fileParam
     * @param user
     * @param uploadType
     * @return
     * @throws IOException
     * @throws Exception
     */
    boolean uploadFile(FileParam fileParam, UserSession user, UploadType uploadType) throws IOException, Exception;

    List<HomeworkResourceUrl> saveFiles(FileParam fileParam, String path);

}

/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.mp.shared.common.FileModule;
import com.mp.shared.service.MpResourceDecoder;
import com.mpen.api.bean.FileParam;
import com.mpen.api.bean.HomeworkResourceUrl;
import com.mpen.api.bean.Hot;
import com.mpen.api.bean.Hot.Message;
import com.mpen.api.bean.Program;
import com.mpen.api.bean.UserSession;
import com.mpen.api.common.Constants;
import com.mpen.api.common.Constants.UploadType;
import com.mpen.api.domain.PersistenceFile;
import com.mpen.api.exception.SdkException;
import com.mpen.api.mapper.PersistenceFileMapper;
import com.mpen.api.service.FileService;
import com.mpen.api.service.MemCacheService;
import com.mpen.api.util.CommUtil;
import com.mpen.api.util.FileUtils;
import com.mpen.api.util.SpeechRecognition;

/**
 * App升级服务.
 *
 * @author zyt
 *
 */
@Component
public class FileServiceImpl implements FileService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileServiceImpl.class);
    @Autowired
    private PersistenceFileMapper persistenceFileMapper;
    @Autowired
    private MemCacheService memCacheService;

    @Override
    public String saveFile(MultipartFile file, String type, String uploadPath) throws IOException, Exception {
        // 有文件才上传
        final String fileName = file.getOriginalFilename();
        final String extensionName = FileUtils.getExtension(fileName);
        final String fileSavePath = FileUtils.uploadFile(file.getBytes(), fileName, type == null ? (uploadPath + extensionName +"/") : FileUtils.ROOTPATH, type);
        return fileSavePath;
    }

    @Override
    public List<FileModule> fileDownload(String path) throws Exception {
        final String savePath = FileUtils.getFilePartsSaveRealPath(path);
        path = FileUtils.getFileSaveRealPath(path, false);
        final List<FileModule> fileMoudles = MpResourceDecoder.getFileMoudles(path, savePath);
        if (fileMoudles == null) {
            throw new SdkException(Constants.GET_INFO_FAILURE);
        }
        fileMoudles.forEach((fileModule) -> {
            fileModule.url = FileUtils.getFullRequestPath(fileModule.url);
        });
        return fileMoudles;
    }

    @Override
    public boolean insertFile(FileParam param, String address) throws Exception {
        final String fileSavePath = saveFile(param.getFile(), param.getProgect() + param.getType(), null);
        final PersistenceFile file = new PersistenceFile(param, fileSavePath, address);
        persistenceFileMapper.insert(file);
        return true;
    }

    /**
     * 语音资源上传(小程序-->DIY有声书|拜年)
     */
    @Override
    public boolean uploadFile(FileParam fileParam, UserSession user, UploadType uploadType) throws IOException, Exception {
        final String filePath = saveFile(fileParam.getFile(), FileUtils.BOOK, null);
        // isDiyProgram=true : DIY有声书小程序执行
        if (UploadType.DAILY_PROGRAM == uploadType) {
            final int num = fileParam.getNum();
            final String uuid = fileParam.getUuid();
            // TODO：这些文件转换，混音的，都要提取出单独method，然后单元测试
            Constants.CACHE_THREAD_POOL.execute(() -> {
                try {
                    // 执行sh 命令
                    final String[] cmdStrings = { "sh /home/mpen/silk-v3-decoder-master/converter.sh "
                            + FileUtils.getFileSaveRealPath(filePath, false) + " wav" };
                    for (int idx = 0; idx < cmdStrings.length; idx++) {
                        doSilkV3Decoder(cmdStrings[idx]);
                    }
                    final List<Hot> list = Constants.hotAreas.get(fileParam.getId()).get(num - 1);
                    for (Hot hotArea : list) {
                        if (hotArea.num == Integer.valueOf(uuid.trim())) {
                            final Message message = new Message();
                            message.url = filePath.replace(".silk", ".wav");
                            try {
                                final String recognize = SpeechRecognition
                                    .recognize(FileUtils.getFileSaveRealPath(message.url, false)).trim();
                                message.text = recognize.endsWith("，")
                                    ? recognize.substring(0, recognize.length() - 1) : recognize;
                            } catch (Exception e) {
                            }
                            hotArea.map.put(user.getLoginId(), message);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
          // 拜年小程序执行
        } else if (UploadType.BAINIAN_2018 == uploadType){
            final String uuid = CommUtil.genRecordKey();
            final int id = Integer.valueOf(fileParam.getId());
            // 根据id得到图片语音等资源
            final Program program = Constants.programs.get(id);
            program.image = fileParam.getFilePath();
            program.name = fileParam.getDescription();
            program.audio = FileUtils.getFullRequestPath("/incoming/complete/" + uuid + ".wav");
            memCacheService.set(uuid, program, Constants.DEFAULT_CACHE_EXPIRATION);
            // TODO：这些文件转换，混音的，都要提取出单独method，然后单元测试
            Constants.CACHE_THREAD_POOL.execute(() -> {
                try {
                    // 执行sh 命令 (混音)
                    final String mp3Path = FileUtils.getFileSaveRealPath(filePath.replace(".silk", ".wav"), false);
                    final String comPath = "/root/johny/github/mpen-manager/incoming/complete/" + uuid + ".wav";
                    final String[] cmdStrings = { "sh /home/mpen/silk-v3-decoder-master/converter.sh "
                            + FileUtils.getFileSaveRealPath(filePath, false) + " wav", "/home/mpen/ffmpeg-3.4.1/ffmpeg -i " + mp3Path
                            + " -i /root/johny/github/mpen-manager/incoming/bainian/background" + id
                            + ".wav -filter_complex amix=inputs=2:duration=first:dropout_transition=2 -f wav "
                            + comPath };
                    for (int idx = 0; idx < cmdStrings.length; idx++) {
                        doSilkV3Decoder(cmdStrings[idx]);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        return true;
    }

    /**
     *  解码silk v3音频文件（类似微信的amr和aud文件、QQ的slk文件）并转换为其它格式（如MP3）
     */
    public void doSilkV3Decoder(String cmdString) throws Exception {
        Process proc = Runtime.getRuntime().exec(cmdString);
        String ls_1;
        BufferedReader bufferedReader = new BufferedReader(
            new InputStreamReader(proc.getInputStream()));
        while ((ls_1 = bufferedReader.readLine()) != null) {
            System.out.println(ls_1);
        }
        bufferedReader.close();
        proc.waitFor();
    }

    /**
     * 保存教师，学生上传资源文件
     */
    @Override
    public List<HomeworkResourceUrl> saveFiles(FileParam fileParam, String path) {
        final List<HomeworkResourceUrl> resourceUrls = new ArrayList<>();
        final MultipartFile[] files = fileParam.getFiles();
        if (files == null) {
            return null;
        }
        for (MultipartFile multipartFile : files) {
            final HomeworkResourceUrl resourceUrl = new HomeworkResourceUrl();
            try {
                final String originalFilename = multipartFile.getOriginalFilename();
                // 文件类型
                final String type = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
                final String fileSavePath = saveFile(multipartFile, path, null);
                // 文件路径
                final String fileSaveRealPath = FileUtils.getFullRequestPath(fileSavePath);
                resourceUrl.setUrl(fileSaveRealPath);
                resourceUrl.setType(type);
                resourceUrls.add(resourceUrl);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resourceUrls;
    }

}

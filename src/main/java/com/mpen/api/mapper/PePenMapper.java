/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.mpen.api.bean.DataAnalysisResult.Version;
import com.mpen.api.bean.PenInfo;
import com.mpen.api.domain.DdbPePen;

/**
 * PePen Mapper对象.
 * 
 * @author zyt
 *
 */
@Mapper
public interface PePenMapper {
    @Select("SELECT * FROM DDB_PE_PEN WHERE IDENTIFIACTION=#{identifiaction} LIMIT 1")
    DdbPePen getByIdentifiaction(String identifiaction);

    @Select("SELECT * FROM DDB_PE_PEN WHERE ID=#{id} LIMIT 1")
    DdbPePen getById(String id);
    
    /**
     * 笔激活 新增需修改字段TYPE,数据库表中已经存在该字段，无需修改表结构
     * @param pen
     */
    @Update("UPDATE DDB_PE_PEN SET CODE=#{code},NAME=#{name},IDENTIFIACTION=#{identifiaction},"
        + "FLAG_ACTIVIE=#{flagActivie},CREATE_DATETIME=#{createDatetime},MAC_ADDRESS=#{macAddress},"
        + "ISBIND=#{isBind},IS_TEST=#{isTest},ITEM=#{item},LABEL=#{label},APP_VERSION=#{appVersion},"
        + "ROM_VERSION=#{romVersion},PEN_ADMIT=#{penAdmit},PUBLIC_KEY=#{publicKey},ACTIVE_ADD=#{activeAdd},TYPE=#{type},"
        + "STORAGE_CAPACITY=#{storageCapacity} WHERE ID=#{id}")
    void update(DdbPePen pen);

    /**
     * 笔激活 新增字段TYPE,数据库表中已经存在该字段，无需修改表结构
     * 之前没有区分linux与android笔，所以没有添加该字段
     * @param pen
     */
    @Insert("INSERT INTO DDB_PE_PEN (ID,CODE,NAME,IDENTIFIACTION,FLAG_ACTIVIE,CREATE_DATETIME,"
        + "MAC_ADDRESS,PUBLIC_KEY,ACTIVE_ADD,TYPE,STORAGE_CAPACITY) VALUES (#{id},#{code},#{name},#{identifiaction},"
        + "#{flagActivie},#{createDatetime}," + "#{macAddress},#{publicKey},#{activeAdd},#{type},#{storageCapacity})")
    void create(DdbPePen pen);

    @Delete("DELETE FROM DDB_PE_PEN WHERE IDENTIFIACTION=#{identifiaction}")
    void deleteByIdentifiaction(String identifiaction);

    @Update("UPDATE DDB_PE_PEN SET APP_VERSION=#{version} WHERE IDENTIFIACTION=#{identifiaction}")
    void updateAppVersionByIdentifiaction(@Param("identifiaction") String identifiaction,
        @Param("version") String version);

    @Update("UPDATE DDB_PE_PEN SET ROM_VERSION=#{version} WHERE IDENTIFIACTION=#{identifiaction}")
    void updateRomVersionByIdentifiaction(@Param("identifiaction") String identifiaction,
        @Param("version") String version);

    @Select("SELECT * FROM DDB_PE_PEN WHERE MAC_ADDRESS=#{mac} LIMIT 1")
    DdbPePen getByMac(String mac);

    @Select("SELECT * FROM DDB_PE_PEN WHERE MAC_ADDRESS LIKE #{mac} LIMIT 1")
    DdbPePen getByDefaultMac(String mac);

    @Select("SELECT APP_VERSION NAME,COUNT(*) VALUE FROM DDB_PE_PEN WHERE APP_VERSION IS NOT NULL "
        + "AND TRIM(APP_VERSION) !='' GROUP BY APP_VERSION ORDER BY COUNT(*) DESC LIMIT 5")
    List<Version> getAppVersion();

    @Select("SELECT ROM_VERSION NAME,COUNT(*) VALUE FROM DDB_PE_PEN WHERE ROM_VERSION IS NOT NULL "
        + "AND TRIM(ROM_VERSION) !='' GROUP BY ROM_VERSION ORDER BY COUNT(*) DESC LIMIT 5")
    List<Version> getRomVersion();
    
    @Select("SELECT FK_PEN_ID ID, DDB_PE_PEN.MAC_ADDRESS, DDB_PE_PEN.ISBIND, DDB_PE_PEN.ISTEACHER FROM DDB_PEN_SERIAL_NUMBER_RELATIONSHIP ,"
        + "DDB_PE_PEN WHERE SERIAL_NUMBER=#{serialNum} AND DDB_PEN_SERIAL_NUMBER_RELATIONSHIP.FK_PEN_ID = DDB_PE_PEN.ID LIMIT 1")
    DdbPePen getPenIdAndMac(String serialNum);
    
    /**
     * 查询所有教师笔的信息(目前只需要唯一标识和mac地址)
     * @return
     */
    @Select("SELECT IDENTIFIACTION,MAC_ADDRESS FROM DDB_PE_PEN WHERE ISTEACHER=1")
    List<DdbPePen> getValidTeacherPen();
    
    /**
     * 根据序列号查询笔信息
     * @param serialNum
     * @return
     */
    @Select("SELECT * FROM DDB_PE_PEN WHERE ID =(SELECT FK_PEN_ID FROM DDB_PEN_SERIAL_NUMBER_RELATIONSHIP "
            + "WHERE SERIAL_NUMBER=#{serialNum} AND IS_VALID='1') ")
    DdbPePen getPenInfoBySerialNum(String serialNum);
    
    /**
     * 根据序列号或者Mac地址获得笔信息
     * @return
     * @author sxg
     */
    @Select({"<script>",
            "SELECT * FROM DDB_PE_PEN ",
            "WHERE 1=1 ",
            "<when test='serialNum!=null'>",
            "AND ID = (SELECT FK_PEN_ID FROM DDB_PEN_SERIAL_NUMBER_RELATIONSHIP WHERE SERIAL_NUMBER= #{serialNum} AND IS_VALID='1') ",
            "</when>", 
            "<when test='macAddress!=null'>",
            "AND MAC_ADDRESS = #{macAddress} ",
            "</when>",
            "</script>"}
             )
    DdbPePen getPenInfoBySerialNumOrMacAddress(@Param("serialNum")String serialNum, @Param("macAddress")String macAddress);
    
    /**
     * 根据笔id查询绑定手机号
     * @param penId
     * @return
     */
    @Select("SELECT LOGIN_ID FROM DDB_PE_CUSTOM WHERE ID =(SELECT FK_CUSTOM_ID FROM DDB_PR_PEN_CUSTOM WHERE FK_PEN_ID=#{penId}"
            + " AND IS_VALID='1')")
    String getBindMobileByPenId(String penId);
    
    /**
     * 根据绑定手机号查询笔信息
     * @param bindMobile
     * @return
     */
    @Select("SELECT FK_PEN_ID FROM ddb_pr_pen_custom WHERE FK_CUSTOM_ID = (SELECT ID FROM ddb_pe_custom WHERE LOGIN_ID=#{bindMobile})")
    String getPenIdByMobile(String bindMobile);
    
}

/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.mapper;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

/**
 * DataSource 和 Mapper管理.
 * 
 * @author kai
 */
@Configuration
@EnableTransactionManagement
public class MapperFactory implements TransactionManagementConfigurer {

    @Autowired
    private DataSource dataSource;

    /**
     * 配置DataSource.
     *
     * @return SqlSessionFactory
     * @throws Exception
     *             异常信息
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);
        sqlSessionFactory.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);
        return sqlSessionFactory.getObject();
    }

    protected <T> MapperFactoryBean<T> getMapper(Class<T> mapperInterface) throws Exception {
        MapperFactoryBean<T> mapperFactoryBean = new MapperFactoryBean<T>();
        mapperFactoryBean.setSqlSessionFactory(sqlSessionFactory());
        mapperFactoryBean.setMapperInterface(mapperInterface);
        return mapperFactoryBean;
    }

    @Bean
    public MapperFactoryBean<ResourceBookMapper> resourceBookMapper() throws Exception {
        return getMapper(ResourceBookMapper.class);
    }

    @Bean
    public MapperFactoryBean<PeCustomMapper> peCustomMapper() throws Exception {
        return getMapper(PeCustomMapper.class);
    }

    @Bean
    public MapperFactoryBean<PePenMapper> pePenMapper() throws Exception {
        return getMapper(PePenMapper.class);
    }

    @Bean
    public MapperFactoryBean<PrPenCustomMapper> prPenCustomMapper() throws Exception {
        return getMapper(PrPenCustomMapper.class);
    }

    @Bean
    public MapperFactoryBean<EnumConstMapper> enumConstMapper() throws Exception {
        return getMapper(EnumConstMapper.class);
    }

    @Bean
    public MapperFactoryBean<PushRecordMapper> pushRecordMapper() throws Exception {
        return getMapper(PushRecordMapper.class);
    }

    @Bean
    public MapperFactoryBean<AppMapper> appMapper() throws Exception {
        return getMapper(AppMapper.class);
    }

    @Bean
    public MapperFactoryBean<RomUpdateMapper> romUpdateMapper() throws Exception {
        return getMapper(RomUpdateMapper.class);
    }

    @Bean
    public MapperFactoryBean<RomVersionMapper> romVersionMapper() throws Exception {
        return getMapper(RomVersionMapper.class);
    }

    @Bean
    public MapperFactoryBean<ResourceVideoMapper> resourceVideoMapper() throws Exception {
        return getMapper(ResourceVideoMapper.class);
    }

    @Bean
    public MapperFactoryBean<PageScopeMapper> pageScopeMapper() throws Exception {
        return getMapper(PageScopeMapper.class);
    }

    @Bean
    public MapperFactoryBean<RecordUserBookMapper> recordUserBookMapper() throws Exception {
        return getMapper(RecordUserBookMapper.class);
    }

    @Bean
    public MapperFactoryBean<PeLabelMapper> peLabelMapper() throws Exception {
        return getMapper(PeLabelMapper.class);
    }

    @Bean
    public MapperFactoryBean<ResourceBookCatalogMapper> resourceBookCatalogMapper() throws Exception {
        return getMapper(ResourceBookCatalogMapper.class);
    }

    @Bean
    public MapperFactoryBean<ActionRecordMapper> actionRecordMapper() throws Exception {
        return getMapper(ActionRecordMapper.class);
    }

    @Bean
    public MapperFactoryBean<SerialNumberMapper> serialNumberMapper() throws Exception {
        return getMapper(SerialNumberMapper.class);
    }

    @Bean
    public MapperFactoryBean<PenSerialNumberRelationshipMapper> penSerialNumberRelationshipMapper() throws Exception {
        return getMapper(PenSerialNumberRelationshipMapper.class);
    }

    @Bean
    public MapperFactoryBean<QuestionMapper> questionMapper() throws Exception {
        return getMapper(QuestionMapper.class);
    }

    @Bean
    public MapperFactoryBean<QuestionItemMapper> questionItemMapper() throws Exception {
        return getMapper(QuestionItemMapper.class);
    }

    @Bean
    public MapperFactoryBean<MobileAppMapper> mobileAppMapper() throws Exception {
        return getMapper(MobileAppMapper.class);
    }

    @Bean
    public MapperFactoryBean<PageCodeMapper> pageCodeMapper() throws Exception {
        return getMapper(PageCodeMapper.class);
    }

    @Bean
    public MapperFactoryBean<PenReadingLimitMapper> penReadingLimitMapper() throws Exception {
        return getMapper(PenReadingLimitMapper.class);
    }

    @Bean
    public MapperFactoryBean<ResourceBookPrintMapper> resourceBookPrintMapper() throws Exception {
        return getMapper(ResourceBookPrintMapper.class);
    }

    @Bean
    public MapperFactoryBean<BookDetailMapper> bookDetailMapper() throws Exception {
        return getMapper(BookDetailMapper.class);
    }

    @Bean
    public MapperFactoryBean<LearnWordStructureDetailMapper> learnWordStructureDetailMapper() throws Exception {
        return getMapper(LearnWordStructureDetailMapper.class);
    }

    @Bean
    public MapperFactoryBean<PageDetailMapper> pageDetailMapper() throws Exception {
        return getMapper(PageDetailMapper.class);
    }

    @Bean
    public MapperFactoryBean<PenCmdMapper> penCmdMapper() throws Exception {
        return getMapper(PenCmdMapper.class);
    }

    @Bean
    public MapperFactoryBean<BookCoreDetailMapper> bookCoreDetailMapper() throws Exception {
        return getMapper(BookCoreDetailMapper.class);
    }

    @Bean
    public MapperFactoryBean<OralTestDetailMapper> oralTestDetailMapper() throws Exception {
        return getMapper(OralTestDetailMapper.class);
    }

    @Bean
    public MapperFactoryBean<PersistenceFileMapper> persistenceFileMapper() throws Exception {
        return getMapper(PersistenceFileMapper.class);
    }
    
    /*
     * PC端版本更新生成PCVersionUpdateMapper bean
     */
    @Bean
    public MapperFactoryBean<PCVersionUpdateMapper> pcVersionUpdateMapper() throws Exception {
        return getMapper(PCVersionUpdateMapper.class);
    }
    
    /**
     * 阅读分级存储考试详情
     * @return
     * @throws Exception
     */
    @Bean
    public MapperFactoryBean<RecordExamDetailMapper> recordExamDetailMapper() throws Exception {
        return getMapper(RecordExamDetailMapper.class);
    }
   
    @Bean
    public MapperFactoryBean<DdbOralTestBookMapper> DdbOralTestBookMapper() throws Exception {
        return getMapper(DdbOralTestBookMapper.class);
    }
    @Bean
    public MapperFactoryBean<DdbUserClassRelaMapper> DdbUserClassRelaMapper() throws Exception {
        return getMapper(DdbUserClassRelaMapper.class);
    }
    @Bean
    public MapperFactoryBean<DdbUserClassMapper> DdbUserClassMapper() throws Exception {
        return getMapper(DdbUserClassMapper.class);
    }
    @Bean
    public MapperFactoryBean<DdbUserHomeworkMapper> DdbUserHomeworkMapper() throws Exception {
        return getMapper(DdbUserHomeworkMapper.class);
    }
    @Bean
    public MapperFactoryBean<DdbUserHomeworkStateMapper> DdbUserHomeworkStateMapper() throws Exception {
        return getMapper(DdbUserHomeworkStateMapper.class);
    }
    @Bean
    public MapperFactoryBean<DdbLearnLogDayTraceMapper> DdbLearnLogDayTraceMapper() throws Exception {
        return getMapper(DdbLearnLogDayTraceMapper.class);
    }
    @Bean
    public MapperFactoryBean<DdbLearnLogBookSumTraceMapper> DdbLearnLogBookSumTraceMapper() throws Exception {
        return getMapper(DdbLearnLogBookSumTraceMapper.class);
    }
    @Bean
    public MapperFactoryBean<DdbLearnLogBookDetailTraceMapper> DdbLearnLogBookDetailTraceMapper() throws Exception {
        return getMapper(DdbLearnLogBookDetailTraceMapper.class);
    }
    @Bean
    public MapperFactoryBean<DdbLearnLogBookTraceMapper> DdbLearnLogBookTraceMapper() throws Exception {
        return getMapper(DdbLearnLogBookTraceMapper.class);
    }
    @Bean
    public MapperFactoryBean<DdbUserCommentsMapper> DdbUserCommentsMapper() throws Exception {
        return getMapper(DdbUserCommentsMapper.class);
    }
    @Bean
    public MapperFactoryBean<DdbUserMedalRecordMapper> DdbUserMedalRecordMapper() throws Exception {
        return getMapper(DdbUserMedalRecordMapper.class);
    }
    @Bean
    public MapperFactoryBean<DdbUserMedalDictionaryMapper> DdbUserMedalDictionaryMapper() throws Exception {
        return getMapper(DdbUserMedalDictionaryMapper.class);
    }
    @Bean
    public MapperFactoryBean<DdbUserDynamicRecordMapper> DdbUserDynamicRecordMapper() throws Exception {
        return getMapper(DdbUserDynamicRecordMapper.class);
    }
    
    /**
     * 教师笔版本信息
     * @return
     * @throws Exception
     */
    @Bean
    public MapperFactoryBean<DdbPenDetailMapper> ddbPenDetailMapper() throws Exception {
        return getMapper(DdbPenDetailMapper.class);
    }
    
    @Bean
    public MapperFactoryBean<ReadingLevelResourceMapper> readingLevelResourceMapper() throws Exception {
        return getMapper(ReadingLevelResourceMapper.class);
    }
    @Bean
    public MapperFactoryBean<DdbUserIntegralRecordMapper> DdbUserIntegralRecordMapper() throws Exception {
        return getMapper(DdbUserIntegralRecordMapper.class);
    }
    @Bean
    public MapperFactoryBean<DdbUserPraiseRelationshipMapper> DdbUserPraiseRelationshipMapper() throws Exception {
        return getMapper(DdbUserPraiseRelationshipMapper.class);
    }
    @Bean
    public MapperFactoryBean<DdbUserRelationshipMapper> DdbUserRelationshipMapper() throws Exception {
        return getMapper(DdbUserRelationshipMapper.class);
    }
    @Bean
    public MapperFactoryBean<DdbObtainIntegralInstructionMapper> DdbObtainIntegralInstructionMapper() throws Exception {
        return getMapper(DdbObtainIntegralInstructionMapper.class);
    }
    @Bean
    public MapperFactoryBean<DdbUserMessageMapper> DdbUserMessageMapper() throws Exception {
        return getMapper(DdbUserMessageMapper.class);
    }
    @Bean
    public MapperFactoryBean<DdbUserDynamicPraiseMapper> DdbUserDynamicPraiseMapper() throws Exception {
        return getMapper(DdbUserDynamicPraiseMapper.class);
    }
    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }
    
    
}
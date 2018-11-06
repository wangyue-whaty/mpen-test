/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.mapper.db;

import java.util.ArrayList;
import java.util.List;

public class DataSourceConfig {
    boolean isEmbedded = true;
    String databaseUrl;
    String databaseUsername;
    String databasePassword;
    Boolean useUtcTimeZone = false;

    List<String> hsqldbScriptList = new ArrayList<>();

    public Boolean getUseUtcTimeZone() {
        return useUtcTimeZone;
    }

    public void setUseUtcTimeZone(Boolean useUtcTimeZone) {
        this.useUtcTimeZone = useUtcTimeZone;
    }

    public DataSourceConfig withUtcTimeZone(final Boolean utcTimeZone) {
        this.useUtcTimeZone = utcTimeZone;
        return this;
    }

    public List<String> getHsqldbScriptList() {
        return hsqldbScriptList;
    }

    public void setHsqldbScriptList(List<String> hsqldbScriptList) {
        this.hsqldbScriptList = hsqldbScriptList;
    }

    public DataSourceConfig addHsqldbScriptList(String hsqldbScript) {
        hsqldbScriptList.add(hsqldbScript);
        return this;
    }

    public Boolean getIsEmbedded() {
        return isEmbedded;
    }

    public void setIsEmbedded(Boolean isEmbedded) {
        this.isEmbedded = isEmbedded;
    }

    public DataSourceConfig withIsEmbedded(Boolean isEmbedded) {
        this.isEmbedded = isEmbedded;
        return this;
    }

    public String getDatabaseUrl() {
        return databaseUrl;
    }

    public void setDatabaseUrl(String databaseUrl) {
        this.databaseUrl = databaseUrl;
    }

    public DataSourceConfig withDatabaseUrl(String databaseUrl) {
        this.databaseUrl = databaseUrl;
        return this;
    }

    public String getDatabaseUsername() {
        return databaseUsername;
    }

    public void setDatabaseUsername(String databaseUsername) {
        this.databaseUsername = databaseUsername;
    }

    public DataSourceConfig withDatabaseUsername(String databaseUsername) {
        this.databaseUsername = databaseUsername;
        return this;
    }

    public String getDatabasePassword() {
        return databasePassword;
    }

    public void setDatabasePassword(String databasePassword) {
        this.databasePassword = databasePassword;
    }

    public DataSourceConfig withDatabasePassword(String databasePassword) {
        this.databasePassword = databasePassword;
        return this;
    }

}

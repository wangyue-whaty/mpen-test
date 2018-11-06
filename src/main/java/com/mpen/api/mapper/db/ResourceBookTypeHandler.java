package com.mpen.api.mapper.db;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.mp.shared.common.FullBookInfo.Type;

/**
 * TODO 自定义枚举类型转换器.
 * 
 * @author zyt
 *
 */
public class ResourceBookTypeHandler extends BaseTypeHandler<Type> {
    private Class<Type> type;
    private final Type[] types;

    public ResourceBookTypeHandler(Class<Type> type) {
        if (type == null)
            throw new IllegalArgumentException("Type argument cannot be null");
        this.type = type;
        this.types = type.getEnumConstants();
        if (this.types == null)
            throw new IllegalArgumentException(type.getSimpleName() + " does not represent an enum type.");
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Type parameter, JdbcType jdbcType)
        throws SQLException {
        ps.setString(i, parameter.getName());
    }

    @Override
    public Type getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String str = rs.getString(columnName);
        if (rs.wasNull()) {
            return null;
        } else {
            return getType(str);
        }
    }

    @Override
    public Type getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String str = rs.getString(columnIndex);
        if (rs.wasNull()) {
            return null;
        } else {
            return getType(str);
        }
    }

    @Override
    public Type getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String str = cs.getString(columnIndex);
        if (cs.wasNull()) {
            return null;
        } else {
            return getType(str);
        }
    }

    private Type getType(String name) {
        for (Type status : types) {
            if (status.getName().equals(name)) {
                return status;
            }
        }
        throw new IllegalArgumentException("未知的枚举类型：" + name + ",请核对" + type.getSimpleName());
    }

}

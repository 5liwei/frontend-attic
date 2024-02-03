package com.frontendAttic.mappers;

import org.apache.ibatis.annotations.Param;

public interface CommonMapper {

    Integer updateCount(
            @Param("tableName") String tableName,
            @Param("readCount") Integer readCount,
            @Param("collectCount") Integer collectCount,
            @Param("keyId") Integer keyId);
}

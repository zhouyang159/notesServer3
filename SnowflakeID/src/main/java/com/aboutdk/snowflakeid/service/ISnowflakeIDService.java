package com.aboutdk.snowflakeid.service;

public interface ISnowflakeIDService {


   int addID(long id, String creator, String action);
}

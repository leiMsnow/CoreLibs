package com.gsty.corelibs.db;

import java.util.List;

/**
 * 数据库操作接口
 *
 * @param <T> 数据类型
 */
interface IDaoHelper<T extends Object> {
    long addData(T bean);

    void addDataAll(Iterable<T> entities);

    boolean hasKey(String id);

    long getTotalCount();

    T getDataById(String id);

    List<T> getDataAll();

    void deleteData(String id);

    void deleteAll();
}
package com.gsty.corelibs.db;

import android.text.TextUtils;

import java.util.List;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.query.QueryBuilder;
import de.greenrobot.dao.query.WhereCondition;


/**
 * 数据库操作类
 * Created by zhangleilei on 15/7/30.
 */
public abstract class BaseDaoHelper<D extends AbstractDao, T extends Object> implements IDaoHelper<T> {

    protected D tableDao;

    protected abstract T getDataInfoById(String id);

    protected abstract boolean hasKeyById(String id);

    protected BaseDaoHelper(D dao) {
        try {
            tableDao = dao;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public long addData(T bean) {
        if (tableDao != null && bean != null) {
            return tableDao.insertOrReplace(bean);
        }
        return -1;
    }

    @Override
    public void addDataAll(Iterable<T> entities) {
        if (tableDao != null && entities != null) {
            tableDao.insertOrReplaceInTx(entities);
        }
    }

    @Override
    public void deleteData(String id) {
        if (tableDao != null && !TextUtils.isEmpty(id)) {
            tableDao.deleteByKey(id);
        }
    }

    @Override
    public T getDataById(String id) {
        if (tableDao != null && !TextUtils.isEmpty(id)) {
            return getDataInfoById(id);
        }
        return null;
    }

    @Override
    public List<T> getDataAll() {
        if (tableDao != null) {
            return tableDao.loadAll();
        }
        return null;
    }

    @Override
    public boolean hasKey(String id) {
        if (tableDao == null) {
            return false;
        }

        if (TextUtils.isEmpty(id)) {
            return false;
        }

        return hasKeyById(id);
    }

    @Override
    public long getTotalCount() {
        if (tableDao == null) {
            return 0;
        }
        QueryBuilder<T> qb = tableDao.queryBuilder();
        return qb.buildCount().count();
    }

    @Override
    public void deleteAll() {
        if (tableDao != null) {
            tableDao.deleteAll();
        }
    }
}

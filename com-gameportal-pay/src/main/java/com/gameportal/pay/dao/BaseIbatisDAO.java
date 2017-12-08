package com.gameportal.pay.dao;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.ibatis.SqlMapClientCallback;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.gameportal.pay.model.BaseEntity;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapExecutor;

public abstract class BaseIbatisDAO extends SqlMapClientDaoSupport {

	/**
	 * Logger for this class
	 */
	private static final Log log = LogFactory.getLog(BaseIbatisDAO.class);

	public abstract Class getEntityClass();

	/**
	 * @param primaryKey
	 * @return
	 */
	public Object findById(Integer primaryKey) {
		Object object = getSqlMapClientTemplate().queryForObject(
				getFindByPrimaryKeyQuery(), primaryKey);
		// this.set
		return object;
	}

	public boolean delete(Integer id) {
		return getSqlMapClientTemplate().delete(getDeleteQuery(), id) > 0 ? true
				: false;
	}

	public boolean delete(BaseEntity entity) {
		return getSqlMapClientTemplate().delete(getDeleteQuery(),
				entity.getID()) > 0 ? true : false;
	}

	public Serializable save(BaseEntity entity) {
		prepareObjectForSaveOrUpdate(entity);
		Object primaryKey = getSqlMapClientTemplate().insert(getInsertQuery(),
				entity);
		return (Serializable) entity;
	}

	public boolean update(BaseEntity entity) {
		try {
			prepareObjectForSaveOrUpdate(entity);
			Object primaryKey = getSqlMapClientTemplate().update(
					getUpdateQuery(), entity);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}

	public List queryForPager(String statementName, Map values, int startNo,
			int pagaSize) {
		List list = new ArrayList();
		if (pagaSize == 0)
			list = getSqlMapClientTemplate()
					.queryForList(statementName, values);
		else
			list = getSqlMapClientTemplate().queryForList(statementName,
					values, startNo, pagaSize);
		return list;
	}

	public List queryForPager(Map values, int startNo, int pagaSize) {

		List list = queryForPager(getSelectQuery(), values, startNo, pagaSize);
		return list;
	}

	public long getRecordCount(String statementName, Map values) {

		long totalCount = (Long) this.queryForObject(statementName, values);

		return totalCount;
	}

	public long getRecordCount(Map values) {

		long totalCount = (Long) this.queryForObject(getCountQuery(), values);

		return totalCount;
	}

	public Object queryForObject(String statementName, Map values) {
		return this.getSqlMapClientTemplate().queryForObject(statementName,
				values);
	}

	public boolean updateCheckRusultList(final String statementName,
			final List updateData) {
		try {
			getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
				public Object doInSqlMapClient(SqlMapExecutor executor)
						throws SQLException {
					executor.startBatch();
					for (Iterator i2 = updateData.iterator(); i2.hasNext();) {

						executor.update(statementName, i2.next());
					}
					executor.executeBatch();
					return null;
				}
			});
			return true;
		} catch (Exception e) {
			log.error("[updateCheckRusultList Exception:]", e);
			return false;
		}

	}

	/**
	 * 用于子类覆盖,在insert,update之前调用
	 * 
	 * @param o
	 */
	protected void prepareObjectForSaveOrUpdate(Object o) {
	}

	public String getSimpleName() {
		return getEntityClass().getSimpleName();
	}

	public String getFindByPrimaryKeyQuery() {
		return getSimpleName() + ".getById";
	}

	public String getInsertQuery() {
		return getSimpleName() + ".insert";
	}

	public String getUpdateQuery() {
		return getSimpleName() + ".update";
	}

	public String getDeleteQuery() {
		return getSimpleName() + ".delete";
	}

	public String getSelectQuery() {
		return getSimpleName() + ".pageSelect";
	}

	public String getCountQuery() {
		return getSimpleName() + ".count";
	}

	/**
	 * 
	 * @Title: setSqlMapClientForAutowired
	 * @Description: TODO(dao中注入sqlMapClient)
	 * @param @param sqlMapClient 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	@Autowired
	@Qualifier("sqlMapClient")
	private void setSqlMapClientForAutowired(SqlMapClient sqlMapClient) {
		super.setSqlMapClient(sqlMapClient);
	}
}

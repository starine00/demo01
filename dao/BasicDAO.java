package com.mhl.dao;


import com.mhl.utils.JDBCUtilsByDruid;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/*
开发BasicDAO 是其他DAO的父类
 */
public class BasicDAO<T> {//泛型指定具体的类型
    private QueryRunner qr = new QueryRunner();

    //开发一个通用的dml操作 针对任意的表
    public int update(String sql, Object... parameters) {
        Connection connection = null;
        try {
            connection = JDBCUtilsByDruid.getConnection();
            int update = qr.update(connection, sql, parameters);
            return update;
        } catch (SQLException e) {
            throw new RuntimeException(e);//将编译异常 转化为 运行异常 方便使用
        } finally {
            JDBCUtilsByDruid.close(null, null, connection);
        }
    }

    //返回多个对象(即查询结果是多行的) 针对任意表

    /**
     * @param sql        sql语句，可以有？
     * @param clazz      传入一个类的Class对象 比如Actor.class
     * @param parameters 传入？的具体的值 可以是多个
     * @return 根据Actor.class返回对应的ArrayList集合
     */
    public List<T> queryMulti(String sql, Class<T> clazz, Object... parameters) {
        Connection connection = null;
        try {
            connection = JDBCUtilsByDruid.getConnection();
            return qr.query(connection, sql, new BeanListHandler<T>(clazz), parameters);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtilsByDruid.close(null, null, connection);
        }
    }

    //查询单行结果的通用方法 针对任意表
    public T querysingle(String sql,Class<T> clazz,Object...parameters){
        Connection connection = null;
        try {
            connection = JDBCUtilsByDruid.getConnection();
            return qr.query(connection,sql,new BeanHandler<T>(clazz),parameters);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtilsByDruid.close(null, null, connection);
        }
    }
    //查询单行单列的方法 即返回单值的方法
    public Object queryScalar(String sql,Object...parameters){
        Connection connection = null;
        try {
            connection = JDBCUtilsByDruid.getConnection();
            return qr.query(connection,sql,new ScalarHandler(),parameters);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtilsByDruid.close(null, null, connection);
        }
    }
}

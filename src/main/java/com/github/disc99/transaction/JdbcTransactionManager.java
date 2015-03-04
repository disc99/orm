package com.github.disc99.transaction;

import java.sql.Connection;
import java.sql.SQLException;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.InvalidTransactionException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

public class JdbcTransactionManager implements TransactionManager {

    public Connection getConnection() throws SystemException {
        try {
            return ConnectionHandler.getConnection();
        } catch (SQLException e) {
            throw new SystemException(e.toString());
        }
    }

    public void closeConnection() throws SystemException {
        try {
            ConnectionHandler.closeConnection();
        } catch (SQLException e) {
            throw new SystemException(e.toString());
        }
    }

    @Override
    public void begin() throws NotSupportedException, SystemException {
        try {
            ConnectionHandler.begin();
        } catch (SQLException e) {
            throw new SystemException(e.toString());
        }
    }

    @Override
    public void commit() throws RollbackException, HeuristicMixedException, HeuristicRollbackException,
            SecurityException, IllegalStateException, SystemException {
        try {
            ConnectionHandler.commit();
        } catch (SQLException e) {
            throw new SystemException(e.toString());
        }
    }

    @Override
    public int getStatus() throws SystemException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Transaction getTransaction() throws SystemException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void resume(Transaction tobj) throws InvalidTransactionException, IllegalStateException, SystemException {
        // TODO Auto-generated method stub

    }

    @Override
    public void rollback() throws IllegalStateException, SecurityException, SystemException {
        try {
            ConnectionHandler.rollback();
        } catch (SQLException e) {
            throw new SystemException(e.toString());
        }
    }

    @Override
    public void setRollbackOnly() throws IllegalStateException, SystemException {
        // TODO Auto-generated method stub

    }

    @Override
    public void setTransactionTimeout(int seconds) throws SystemException {
        // TODO Auto-generated method stub

    }

    @Override
    public Transaction suspend() throws SystemException {
        // TODO Auto-generated method stub
        return null;
    }

}

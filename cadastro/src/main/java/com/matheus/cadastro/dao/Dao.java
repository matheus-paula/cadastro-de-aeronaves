package com.matheus.cadastro.dao;

import java.io.Serializable;


import org.hibernate.query.Query;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.springframework.stereotype.Repository;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.matheus.cadastro.model.*;

import com.matheus.cadastro.dao.HibernateControl;

@SuppressWarnings("all")

@Repository
abstract class Dao implements Serializable {
	protected static HibernateControl control;
	protected Session session;
	protected Exception exception;


	protected void commitTransaction(Transaction transaction) {
		if (transaction.getStatus() != TransactionStatus.COMMITTED)
			transaction.commit();
		
	}

	public Dao() {
		if (control == null)
			control = new HibernateControl();
		session = control.getSession();
	}

	@Override
	protected void finalize() throws Throwable {
		endSession();
	}


	protected void endSession() {
		session.flush();
		session.close();
	}

	protected void rollback(Transaction transaction, Exception exception) {
		transaction.rollback();
		exception.printStackTrace();
		this.exception = exception;
	}

	public Object readOneObject(Query<?> query) {
		Transaction transaction = session.getTransaction();
		Object object = null;
		try {
			object = query.uniqueResult();
		} catch (Exception e) {
			rollback(transaction, e);
		}
		return object;
	}

	public Object readManyObjects(Query<?> query) {
		Transaction transaction = session.getTransaction();
		Object object = null;
		try {
			object = query.list();
		} catch (Exception e) {
			rollback(transaction, e);
		}
		return object;
	}

	public CommitStatus save(Object object) {
		Transaction transaction = session.beginTransaction();
		CommitStatus status = new CommitStatus();
		try {
			session.save(object);
			commitTransaction(transaction);
			status.setCommited(true);
		} catch (Exception e) {
			rollback(transaction, e);
			status.setException(e);
		}

		return status;
	}

	public CommitStatus saveAndClose(Object object) {
		Transaction transaction = session.beginTransaction();
		CommitStatus status = new CommitStatus();
		try {
			session.save(object);
			commitTransaction(transaction);
			status.setCommited(true);
			session.close();
		} catch (Exception e) {
			rollback(transaction, e);
			status.setException(e);
		}

		return status;
	}
	
	public CommitStatus update(Object object) {
		Transaction transaction = session.beginTransaction();
		CommitStatus status = new CommitStatus();
		try {
			session.update(object);
			commitTransaction(transaction);
			status.setCommited(true);
		} catch (Exception e) {
			rollback(transaction, e);
			status.setException(e);
		}

		return status;
	}

	public CommitStatus delete(Object object) {
		Transaction transaction = session.beginTransaction();
		CommitStatus status = new CommitStatus();
		try {
			session.delete(object);
			commitTransaction(transaction);
			status.setCommited(true);
		} catch (Exception e) {
			rollback(transaction, e);
			status.setException(e);
		}

		return status;
	}

	public synchronized Exception getException() {
		return exception;
	}
}
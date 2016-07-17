package com.ttnd.reap.dao.Impl;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ttnd.reap.dao.IRemainingBadgesDao;
import com.ttnd.reap.pojo.EmployeeDetails;
import com.ttnd.reap.pojo.ReceivedBadges;
import com.ttnd.reap.pojo.RemainingBadges;

@SuppressWarnings("deprecation")
@Repository
public class RemainingBadgesDaoImpl implements IRemainingBadgesDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(EmployeeDetails employeeDetails) {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		try {
			RemainingBadges remainingBadges = new RemainingBadges();
			remainingBadges.setEmployeeDetails(employeeDetails);
			session.save(remainingBadges);
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		}
		session.close();
	}

	@Override
	public RemainingBadges getRemainingBadgesOfEmployee(EmployeeDetails employeeDetails) {
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(RemainingBadges.class)
				.add(Restrictions.eq("employeeDetails", employeeDetails));
		return (RemainingBadges) criteria.uniqueResult();
	}

	@Override
	public boolean updateGold(int employeeId) {
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(RemainingBadges.class).createCriteria("employeeDetails")
				.add(Restrictions.eq("id", employeeId));
		ReceivedBadges receivedBadges = (ReceivedBadges) criteria.uniqueResult();

		int val = receivedBadges.getGold();
		if (val == 0)
			return false;
		receivedBadges.setGold(val - 1);
		Transaction transaction = session.beginTransaction();
		try {
			session.update(receivedBadges);
			transaction.commit();
			session.close();
			return true;
		} catch (Exception e) {
			transaction.rollback();
			// e.printStackTrace();
			session.close();
			return false;
		}
	}

	@Override
	public boolean updateSilver(int employeeId) {
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(RemainingBadges.class).createCriteria("employeeDetails")
				.add(Restrictions.eq("id", employeeId));
		ReceivedBadges receivedBadges = (ReceivedBadges) criteria.uniqueResult();

		int val = receivedBadges.getSilver();
		if (val == 0)
			return false;
		receivedBadges.setSilver(val - 1);
		Transaction transaction = session.beginTransaction();
		try {
			session.update(receivedBadges);
			transaction.commit();
			session.close();
			return true;
		} catch (Exception e) {
			transaction.rollback();
			// e.printStackTrace();
			session.close();
			return false;
		}
	}

	@Override
	public boolean updateBronze(int employeeId) {
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(RemainingBadges.class).createCriteria("employeeDetails")
				.add(Restrictions.eq("id", employeeId));
		ReceivedBadges receivedBadges = (ReceivedBadges) criteria.uniqueResult();

		int val = receivedBadges.getBronze();
		if (val == 0)
			return false;
		receivedBadges.setBronze(val - 1);
		Transaction transaction = session.beginTransaction();
		try {
			session.update(receivedBadges);
			transaction.commit();
			session.close();
			return true;
		} catch (Exception e) {
			transaction.rollback();
			// e.printStackTrace();
			session.close();
			return false;
		}
	}

}

package com.invoice.util;



import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

/**
 * @author Arshad
 *
 */
public class HibernateUtil {
	private static final SessionFactory sessionFactory;
	static Logger logger = Logger.getLogger(HibernateUtil.class);
	static {
		try {
			sessionFactory = new AnnotationConfiguration().configure()
					.buildSessionFactory();
		} catch (Throwable ex) {
			logger.error("Initial SessionFactory creation failed.",ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
}
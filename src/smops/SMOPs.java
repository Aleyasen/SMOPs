/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smops;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import smops.hibernate.Business;

/**
 *
 * @author Aale
 */
public class SMOPs {

    /**
     * @param args the command line arguments
     */
    private static SessionFactory sessionFactory;

    public static void main(String[] args) {
        sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        Business b = new Business();
        session.beginTransaction();
        session.save(b);
        session.getTransaction().commit();

    }

}

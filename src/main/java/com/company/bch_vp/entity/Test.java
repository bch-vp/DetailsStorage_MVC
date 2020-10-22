package com.company.bch_vp.entity;

import com.company.bch_vp.service.DetailInfoService;
import com.company.bch_vp.service.DetailService;
import com.company.bch_vp.service.ProjectService;
import com.company.bch_vp.service.impl.DetailInfoServiceImpl;
import com.company.bch_vp.service.impl.DetailServiceImpl;
import com.company.bch_vp.service.impl.ProjectServiceImpl;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTransactionManager;

import java.nio.channels.SeekableByteChannel;

public class Test {
    public static void main(String[] args) {

//        DetailInfoServiceImpl detailInfoServiceImpl=new DetailInfoServiceImpl() ;
//        ProjectServiceImpl projectServiceImpl=new ProjectServiceImpl() ;
//        DetailServiceImpl detailServiceImpl=new DetailServiceImpl();
//
//        Detail detail_1=new Detail("detail_1",100);
//        Long idDetail_1=detailServiceImpl.saveDetail(detail_1).getId();
//
//        Detail detail_2=new Detail("detail_2",200);
//        Long idDetail_2=detailServiceImpl.saveDetail(detail_2).getId();
//
//        Project project=new Project("prpject_1");
//        Long idProject=projectServiceImpl.saveProject(project).getId();
//
//        detailInfoServiceImpl.addDetail(30,idDetail_1,idProject);
//        detailInfoServiceImpl.addDetail(20,idDetail_2,idProject);






        Project project=new Project("prpject_1");
        Detail detail=new Detail("detail_1");

        Session session=HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction=session.beginTransaction();

        session.persist(project);
        session.persist(detail);

        DetailInfo detailInfo =new DetailInfo(30,detail,project);


        session.persist(detailInfo);

       transaction.commit();
        session.close();

//
//
//
//
//        Project project1=new Project("prpject_2");
//        Detail detail1=new Detail("detail_2");
//
//        Session session1=HibernateSessionFactoryUtil.getSessionFactory().openSession();
//        Transaction transaction1=session1.beginTransaction();
//
//        session1.persist(project1);
//        session1.persist(detail1);
//
//        DetailInfo detailInfo1 =new DetailInfo(30,detail1,project1);
//
//
//        session1.persist(detailInfo1);
//
//        transaction1.commit();
//        session1.close();

    }
}

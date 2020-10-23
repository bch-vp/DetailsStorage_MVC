package com.company.bch_vp.service.impl.detailServiceImplTest;

import com.company.bch_vp.entity.Detail;
import com.company.bch_vp.entity.DetailInfo;
import com.company.bch_vp.entity.Project;
import com.company.bch_vp.service.impl.DetailInfoServiceImpl;
import com.company.bch_vp.service.impl.DetailServiceImpl;
import com.company.bch_vp.service.impl.ProjectServiceImpl;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(properties ="classpath:application.properties")
public class TestDeleteDetail {

    @Autowired
    private DetailServiceImpl detailServiceImpl;
    @Autowired
    private DetailInfoServiceImpl detailInfoServiceImpl;
    @Autowired
    private ProjectServiceImpl projectServiceImpl;

    @Before
    @Transactional
    public void fillDB(){
        Detail detail_1=new Detail("detail_1", "type","production",100, (double)40, "storage");
        Long idDetail_1=detailServiceImpl.saveDetail(detail_1).getId();

        Detail detail_2=new Detail("detail_2", "type","production",200, (double)40, "storage");
        Long idDetail_2=detailServiceImpl.saveDetail(detail_2).getId();

        Project project=new Project("prpject_1","type" , 1,"storage");
        Long idProject=projectServiceImpl.saveProject(project).getId();

        detailInfoServiceImpl.addDetail(30, idDetail_1, idProject);
        detailInfoServiceImpl.addDetail(20, idDetail_2, idProject);
    }

    @Test
    @Transactional
    public void test1(){
        //delete 1 detail from 2
        //1 project which contains 2 details
        //2 this details also contains this 1 project
        // (in DB we have 2 details,1 project)
        detailServiceImpl.deleteDetailById((long)1);

        List<Detail> detailList = detailServiceImpl.findAll();
        List<DetailInfo> detailsInfo =detailInfoServiceImpl.findAll();
        List<Project> projects =projectServiceImpl.findAll();

        Assert.assertEquals(200,(int)detailServiceImpl.findDetailById((long)2).getQuantityOfAvailable());
        Assert.assertEquals(200,(int)detailServiceImpl.findDetailById((long)2).getQuantityOfAll());
        Assert.assertEquals(1, detailServiceImpl.findAll().size());
        Assert.assertEquals(0,detailInfoServiceImpl.findAll().size());
        Assert.assertEquals(0,projectServiceImpl.findAll().size());
    }

    @Test
    @Transactional
    public void test2(){
        //delete project which contains 1 details
        //this 1 detail contain only 1 this project
        // (in DB we have 3 details,3 projects)

        //add one project_2 and detail_3 to DB and join them together
        Detail detail_3 = new Detail("detail_3","type","production",300, (double)40, "storage");
        Long idDetail_3 = detailServiceImpl.saveDetail(detail_3).getId();
        Project project_3 = new Project("prpject_3","type" , 1,"storage");
        Long idProject_3 = projectServiceImpl.saveProject(project_3).getId();
        detailInfoServiceImpl.addDetail(60, idDetail_3, idProject_3);

        List<DetailInfo> detailsInfo =detailInfoServiceImpl.findAll();

        detailServiceImpl.deleteDetailById((long)3);
        //delete
        List<DetailInfo> detailsInfo1 =detailInfoServiceImpl.findAll();

//        List<Detail> detailList = detailServiceImpl.findAll();
//        List<DetailInfo> detailsInfo =detailInfoServiceImpl.findAll();
//        List<Project> projects =projectServiceImpl.findAll();

        Assert.assertEquals(2, detailServiceImpl.findAll().size());
        Assert.assertEquals(2,detailInfoServiceImpl.findAll().size());
        Assert.assertEquals(1,projectServiceImpl.findAll().size());
    }
}

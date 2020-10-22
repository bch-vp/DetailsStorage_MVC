package com.company.bch_vp.controller;

import com.company.bch_vp.entity.Detail;
import com.company.bch_vp.entity.DetailInfo;
import com.company.bch_vp.entity.Project;
import com.company.bch_vp.service.impl.DetailInfoServiceImpl;
import com.company.bch_vp.service.impl.DetailServiceImpl;
import com.company.bch_vp.service.impl.ProjectServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;
import java.util.List;

@Controller
public class ProjectsController {
    @Autowired
    private DetailServiceImpl detailServiceImpl;
    @Autowired
    private DetailInfoServiceImpl detailInfoServiceImpl;
    @Autowired
    private ProjectServiceImpl projectServiceImpl;

//    @PersistenceContext
//    private EntityManager entityManger;
//
//    public void fillDetailss(Model model) {
//        detailServiceImpl.saveDetail(new Detail("det1","aeg","faefa",100,(double)43,"Aeg"));
//        //detailServiceImpl.saveDetail(new Detail("det2","aeg","faefa",50,(double)43,"Aeg"));
//        projectServiceImpl.saveProject(new Project("pr1","agfe",33,"aegaff"));
//        projectServiceImpl.saveProject(new Project("pr2","agee",3,"aegf"));
//        projectServiceImpl.saveProject(new Project("pr3","aafge",5,"agegf"));
//        detailInfoServiceImpl.addDetail(20,(long)1,(long)1);
//        detailInfoServiceImpl.addDetail(30,(long)1,(long)2);
//        detailInfoServiceImpl.addDetail(40,(long)1,(long)3);
//    }

    @GetMapping(value = "/projects")
    public String showProjects(Model model) {
     //  fillDetailss(model);
        List<DetailInfo> list=detailInfoServiceImpl.findAll();
        List<Project> listpro=projectServiceImpl.findAll();

        model.addAttribute("projects",projectServiceImpl.findAll());
        return "projects";
    }

    @PostMapping(value = "/projects", params = "id")
    public String deleteDetail(Long id,Model model){
        List<DetailInfo> list=detailInfoServiceImpl.findAll();
        String s=list.get(0).getProject().getProjectName();
        projectServiceImpl.deleteProjectById(id);
        return showProjects(model);
    }
}

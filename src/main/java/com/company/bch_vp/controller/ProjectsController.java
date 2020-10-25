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
    @Autowired
    private EntityManager entityManager;


    @GetMapping(value = "/projects")
    public String showProjects(Model model) {
        entityManager.clear();
        model.addAttribute("projects",projectServiceImpl.findAll());
        return "projects";
    }

    @PostMapping(value = "/projects", params = "id")
    public String deleteProject(Long id,Model model){
        projectServiceImpl.deleteProjectById(id);
        return showProjects(model);
    }

    @PostMapping(value = "/projects", params = {"idDetail","idProject"})
    public String deleteProjectInDetail(Long idDetail, Long idProject,Model model){
        projectServiceImpl.deleteDetailInProject(idDetail, idProject);
        return showProjects(model);
    }
}

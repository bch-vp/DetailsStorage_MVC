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
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
    public String deleteDetailInProject(Long idDetail, Long idProject,Model model){
        projectServiceImpl.deleteDetailInProject(idDetail, idProject);
        return showProjects(model);
    }

    @PostMapping(value = "/projects", params = {"quantity","idDetail","idProject","button"})
    public String addQuantityOfDetailInProject(@RequestParam(required = false) Integer quantity,
                                               Long idDetail,
                                               Long idProject,
                                               String button,
                                               Model model){
        if(button.equals("add") && quantity!=null && quantity>0 && detailServiceImpl.findDetailById(idDetail).getQuantityOfAvailable()>=quantity){
            detailInfoServiceImpl.addQuantityOfDetailsInProject(quantity, idDetail, idProject);
        }
        else if(button.equals("subtract") && quantity!=null && quantity>0 && detailInfoServiceImpl.findById(idDetail,idProject).getQuantityDetailsUsed()>=quantity){
            detailInfoServiceImpl.subtractQuantityOfDetailsInProject(quantity, idDetail, idProject);
        }
        else if(quantity==null){
            model.addAttribute("errorAddQuantityOfDetailInProject_quantityIsNull","Is required");
            model.addAttribute("errorAddQuantityOfDetailInProject_idDetail",idDetail);
            model.addAttribute("errorAddQuantityOfDetailInProject_idProject",idProject);
        }
        else{ // if quantity==null && getQuantityOfAvailable() < quantity
            model.addAttribute("errorAddQuantityOfDetailInProject_quantityIsNotCorrect","Quantity isn't correct");
            model.addAttribute("errorAddQuantityOfDetailInProject_idDetail",idDetail);
            model.addAttribute("errorAddQuantityOfDetailInProject_idProject",idProject);
        }
        return showProjects(model);
    }


    @PostMapping(value = "/projects", params = {"idProjectForEdit"})
    public String formEditProject(@RequestParam (name = "idProjectForEdit") Long idProject,Model model){
        model.addAttribute("project",projectServiceImpl.findById(idProject));
        return "editProject";
    }

    @PostMapping(value = "/projects", params = {"idProjectForEdit","projectName","type", "quantity", "storage"})
    public String editProject(@RequestParam(name = "idProjectForEdit", required = true) Long idProject,
                                 @RequestParam(name = "projectName", required = false) String projectName,
                                 @RequestParam(name = "type", required = false) String type,
                                 @RequestParam(name = "quantity", required = false) Integer quantity,
                                 @RequestParam(name = "storage", required = false) String storage,
                                 Model model) {
        Project project=projectServiceImpl.findById(idProject);
        if(!projectName.isEmpty()){
            project.setProjectName(projectName);
        }
        if(!type.isEmpty()){
            project.setType(type);
        }
        if (quantity!=null) {
            project.setQuantity(quantity);
        }
        if(!storage.isEmpty()){
            project.setStorage(storage);
        }
        projectServiceImpl.sendChangesImmediately();
        return showProjects(model);
    }
}

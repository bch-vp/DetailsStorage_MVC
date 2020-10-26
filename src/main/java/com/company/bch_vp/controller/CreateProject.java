package com.company.bch_vp.controller;

import com.company.bch_vp.entity.DetailForm;
import com.company.bch_vp.entity.DetailMap;
import com.company.bch_vp.entity.Project;
import com.company.bch_vp.service.impl.DetailInfoServiceImpl;
import com.company.bch_vp.service.impl.DetailServiceImpl;
import com.company.bch_vp.service.impl.ProjectServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class CreateProject {
    @Autowired
    private DetailServiceImpl detailServiceImpl;
    @Autowired
    private DetailInfoServiceImpl detailInfoServiceImpl;
    @Autowired
    private ProjectServiceImpl projectServiceImpl;
    @Autowired
    private EntityManager entityManager;

    @GetMapping("/createProject")
    public String createeeProject(Model model){
        entityManager.clear();
        model.addAttribute("project", new Project());
        model.addAttribute("details", detailServiceImpl.findAll());
        DetailMap detailMap = new DetailMap();
        for (int i = 0; i < detailServiceImpl.findAll().size(); i++) {
            detailMap.addDetail(new DetailForm());
        }
        model.addAttribute("detailMap", detailMap);
        return "createProject";
    }

    @PostMapping("/createProject")
    public String createeProject(@Valid Project project, Errors errors, DetailMap detailMap, Model model){
        entityManager.clear();
        deleteAllNullFields(detailMap);
        if(!checkIsQuantityInFormWasCorrect(detailMap)) {
            model.addAttribute("quantityErrorInForm", "Quantity filled not correct");
        }else if(detailMap.getDetails().isEmpty()) {
            model.addAttribute("quantityError", "Quantity is required (at least 1)");
        }else if(!errors.hasErrors()){
            Long idProject=projectServiceImpl.saveProject(project).getId();
            detailMap.getDetails()
                    .stream()
                    .forEach(detailForm->{
                        detailInfoServiceImpl.addDetail(detailForm.getQuantity(),detailForm.getId(),idProject);
                    });
            model.addAttribute("projects", projectServiceImpl.findAll());
            return "projects";
        }
        model.addAttribute("details", detailServiceImpl.findAll());
       return "createProject";
    }

    private boolean checkIsQuantityInFormWasCorrect(DetailMap detailMap){
        for(DetailForm detailForm: detailMap.getDetails()){
            Integer quantityForm=detailForm.getQuantity();
            Integer quantityOfAvailableInDetail=detailServiceImpl.findDetailById(detailForm.getId()).getQuantityOfAvailable();
            if(quantityForm>quantityOfAvailableInDetail){
                return false;
            }
        }
        return true;
    }

    private DetailMap deleteAllNullFields(DetailMap detailMap){
        detailMap.setDetails(detailMap.getDetails()
                .stream()
                .filter(detail-> detail.getQuantity()!=null)
                .collect(Collectors.toList()));
        return detailMap;
    }
}

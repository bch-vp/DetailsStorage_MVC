package com.company.bch_vp.controller;

import com.company.bch_vp.entity.Detail;
import com.company.bch_vp.entity.Project;
import com.company.bch_vp.service.impl.DetailInfoServiceImpl;
import com.company.bch_vp.service.impl.DetailServiceImpl;
import com.company.bch_vp.service.impl.ProjectServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;

@Controller
public class DetailsController {
    @Autowired
    private DetailServiceImpl detailServiceImpl;
    @Autowired
    private DetailInfoServiceImpl detailInfoServiceImpl;
    @Autowired
    private ProjectServiceImpl projectServiceImpl;
//
//    @GetMapping("/fill")
//    public String fillDetails(Model model) {
//        detailServiceImpl.saveDetail(new Detail("det1","aeg","faefa",50,(double)43,"Aeg"));
//        //detailServiceImpl.saveDetail(new Detail("det2","aeg","faefa",50,(double)43,"Aeg"));
//        projectServiceImpl.saveProject(new Project("pr1","age",30,"aegf"));
//        projectServiceImpl.saveProject(new Project("pr2","age",30,"aegf"));
//        detailInfoServiceImpl.addDetail(20,(long)1,(long)1);
//        detailInfoServiceImpl.addDetail(20,(long)1,(long)2);
//
//        return showDetails(model);
//    }

    @GetMapping("/")
    public String showDetails(Model model) {
        model.addAttribute("detail",new Detail());
        model.addAttribute("details",detailServiceImpl.findAll());
        return "details";
    }

    @PostMapping(value = "/")
    public String addDetail(@Valid Detail detail, Errors errors, Model model){
        if(!errors.hasErrors()){
            detail.setQuantityOfAvailable(detail.getQuantityOfAll());//should fill because, this field is null
            detailServiceImpl.saveDetail(detail);
        }
        model.addAttribute("details",detailServiceImpl.findAll());
        return "details";
    }

    @PostMapping(value = "/", params = "idDetail")
    public String deleteDetail(Long idDetail,Model model){
        detailServiceImpl.deleteDetailById(idDetail);
        return showDetails(model);
    }

    @PostMapping(value = "/",params ={ "quantity","idDetail"})
    public String addQuantityToDetail(Long idDetail,@RequestParam(required = false) Integer quantity,Model model){
        if(quantity!=null) {
            detailServiceImpl.addQuantityOfDetails(idDetail, quantity);
        }
        else{
            model.addAttribute("error","Is required");
            model.addAttribute("errorId",idDetail);
        }
        return showDetails(model);
    }

    @PostMapping(value = "/", params = {"idDetail","idProject"})
    public String deleteProject(Long idDetail, Long idProject,Model model){
        detailInfoServiceImpl.deleteProjectInDetail(idDetail, idProject);
        return showDetails(model);
    }
}

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
import java.util.Map;

@Controller
public class DetailsController {
    @Autowired
    private DetailServiceImpl detailServiceImpl;
    @Autowired
    private DetailInfoServiceImpl detailInfoServiceImpl;
    @Autowired
    private ProjectServiceImpl projectServiceImpl;
    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping("/")
    public String showDetails(Model model) {
        entityManager.clear();
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
        if(quantity!=null && quantity>0) {
            detailServiceImpl.addQuantityOfDetails(idDetail, quantity);
        }
        else{
            model.addAttribute("errorAddQuantityToDetail","Is required");
            model.addAttribute("errorIdAddQuantityToDetail",idDetail);
        }
        return showDetails(model);
    }

    @PostMapping(value = "/", params = {"quantity","idDetail","idProject","button"})
    public String addQuantityOfUsedDetailInProject(@RequestParam(required = false) Integer quantity,
                                               Long idDetail,
                                               Long idProject,
                                               String button,
                                               Model model){
        if(button.equals("add") && quantity!=null && quantity>0 && detailServiceImpl.findDetailById(idDetail).getQuantityOfAvailable()>=quantity){
            detailInfoServiceImpl.addQuantityOfDetailsInProject(quantity, idDetail, idProject);
        }
        else if(button.equals("subtract") && quantity!=null && detailInfoServiceImpl.findById(idDetail,idProject).getQuantityDetailsUsed()>=quantity){
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
        return showDetails(model);
    }

    @PostMapping(value = "/", params = {"idDetail","idProject"})
    public String deleteDetailInProject(Long idDetail, Long idProject,Model model){
        detailInfoServiceImpl.deleteProjectInDetail(idDetail, idProject);
        return showDetails(model);
    }

    @GetMapping(value="/editDetail", params = {"idDetailForEdit"})
    public String formEditDetail(@RequestParam("idDetailForEdit") Long idDetail, Model model){
        model.addAttribute("detail", detailServiceImpl.findDetailById(idDetail));
        return "editDetail";
    }

    @GetMapping(value="/editDetail")
    public String editDetail(@RequestParam Map<String,Object> map, Model model){
        //?
        return "editDetail";
    }


}

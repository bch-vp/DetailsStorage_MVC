package com.company.bch_vp.controller;

import com.company.bch_vp.entity.Detail;
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
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @GetMapping( value="/")
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

    @PostMapping(value = "/", params = {"idDetailForDelete"})
    public String deleteDetail(@RequestParam (name = "idDetailForDelete") Long idDetail,Model model){
        detailServiceImpl.deleteDetailById(idDetail);
        return showDetails(model);
    }


    @PostMapping(value = "/", params = {"idDetailForEdit"})
    public String formEditDetail(@RequestParam (name = "idDetailForEdit") Long idDetail,Model model){
        model.addAttribute("detail",detailServiceImpl.findDetailById(idDetail));
        return "editDetail";
    }

    @PostMapping(value = "/", params = {"idDetailForEdit","detailName", "type", "production", "price", "storage"})
    public String editDetail(@RequestParam(name = "idDetailForEdit", required = true) Long idDetail,
                             @RequestParam(name = "detailName", required = false) String detailName,
                             @RequestParam(name = "type", required = false) String type,
                             @RequestParam(name = "production", required = false) String production,
                             @RequestParam(name = "price", required = false) Double price,
                             @RequestParam(name = "storage", required = false) String storage,
                             Model model) {
        Detail detail=detailServiceImpl.findDetailById(idDetail);
        if(!detailName.isEmpty()){
            detail.setDetailName(detailName);
        }
        if(!type.isEmpty()){
            detail.setType(type);
        }
        if (!production.isEmpty()) {
            detail.setProduction(production);
        }
        if(price!=null){
            detail.setPrice(price);
        }
        if(!storage.isEmpty()){
            detail.setStorage(storage);
        }
        detailServiceImpl.sendChangesImmediately();
        return showDetails(model);
    }

    @PostMapping(value = "/", params = {"idDetailFor_AddProjectInDetail"})
    public String formAddProjectInDetail(@RequestParam (name = "idDetailFor_AddProjectInDetail") Long idDetail,Model model){
        List<Project> projects=projectServiceImpl.findAll();
        Detail detail=detailServiceImpl.findDetailById(idDetail);

        //delete projects which detail already contain
        detail.getDetailsInfo()
                .stream()
                .forEach(detailInfo -> {
                    if(projects.contains(detailInfo.getProject())){
                        projects.remove(detailInfo.getProject());
                    }
                });

        DetailMap detailMap = new DetailMap();
        int n=projectServiceImpl.findAll().size()-detailServiceImpl.findDetailById(idDetail).getDetailsInfo().size();
        for (int i = 0; i < n; i++) {
            detailMap.addDetail(new DetailForm());
        }

        model.addAttribute("detailMap", detailMap);
        model.addAttribute("detail",detail);
        model.addAttribute("projects",projects);
        return "addProjectInDetail";
    }

    @PostMapping(value = "/",params = {"idDetailFor_AddProjectInDetail","add"})
    public String addProjectInDetail(@RequestParam (name = "idDetailFor_AddProjectInDetail",required = true) Long idDetail,
                                     @RequestParam (name = "add",required = true) String add,
                                     DetailMap detailMap,
                                     Model model){
        entityManager.clear();
        Detail detail=detailServiceImpl.findDetailById(idDetail);
        deleteAllNullFields(detailMap);

        //rewrite if minus
        for (DetailForm detailForm : detailMap.getDetails())
            if (detailForm.getQuantity() <= 0) {
                model.addAttribute("errorQuantity", "Quantity filled not correct");
                return formAddProjectInDetail(idDetail, model);
            }
        ///

        int quantityOfDetails= detailMap.getDetails()
                .stream()
                .mapToInt(DetailForm::getQuantity)
                .sum();
        if(quantityOfDetails<=detail.getQuantityOfAvailable() && !detailMap.getDetails().isEmpty()){
            detailMap.getDetails()
                    .stream()
                    .forEach(detailForm -> {
                        detailInfoServiceImpl.addDetail(detailForm.getQuantity(),idDetail,detailForm.getId());
                    });
            return showDetails(model);
        }
        //else
        model.addAttribute("errorQuantity","Quantity filled not correct");
        return formAddProjectInDetail(idDetail,model);
    }






    private DetailMap deleteAllNullFields(DetailMap detailMap){
        detailMap.setDetails(detailMap.getDetails()
                .stream()
                .filter(detail-> detail.getQuantity()!=null)
                .collect(Collectors.toList()));
        return detailMap;
    }


    @PostMapping(value = "/",params ={"quantity","idDetail"})
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

}

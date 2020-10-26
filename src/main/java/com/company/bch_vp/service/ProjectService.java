package com.company.bch_vp.service;

import com.company.bch_vp.entity.Project;

import java.util.List;

public interface ProjectService {
    Project saveProject(Project project);
    void deleteProjectById(Long id);
    Project getByName(String name);
    List<Project> findAll();
    Project findById(Long id);
    void deleteDetailInProject(Long idDetail, Long idProject);
    void sendChangesImmediately();
}

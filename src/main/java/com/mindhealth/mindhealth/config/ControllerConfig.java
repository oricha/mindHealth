package com.mindhealth.mindhealth.config;

import com.mindhealth.mindhealth.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;


@ControllerAdvice
@RequiredArgsConstructor
public class ControllerConfig {

    private final CategoryService categoryService;

    @InitBinder
    public void initBinder(final WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @ModelAttribute
    public void sharedModel(Model model) {
        model.addAttribute("navCategories", categoryService.findAll());
    }

}

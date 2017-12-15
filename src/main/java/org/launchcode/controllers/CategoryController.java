package org.launchcode.controllers;

import org.launchcode.models.Category;
import org.launchcode.models.Cheese;
import org.launchcode.models.data.CategoryDao;
import org.launchcode.models.data.CheeseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("category")
public class CategoryController {
    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private CheeseDao cheeseDao;

    @RequestMapping("")
    public String index(Model model) {

        model.addAttribute("categories", categoryDao.findAll());
        model.addAttribute("title", "My Categories");

        return "category/index";
    }
    @RequestMapping(value ="add", method = RequestMethod.GET)
    public String add(Model model){
        model.addAttribute("title", "Add Category");
        model.addAttribute(new Category());

        return "category/add";
    }
    @RequestMapping(value="add", method=RequestMethod.POST)
    public String add(Model model,
                      @ModelAttribute @Valid Category category, Errors errors){
        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Category");
            return "category/add";
        }
        categoryDao.save(category);
        return "redirect:";


    }
//Bonus Mission now working.
    @RequestMapping(value = "{category}", method = RequestMethod.GET)
    public String byCategory(Model model, @PathVariable("category") Category category) {
        model.addAttribute("cheeses", cheeseDao.findAll());
        model.addAttribute("category", category.getId());
        model.addAttribute("title", category.getName()+" Cheeses");

        return "cheese/index";
    }

}



package org.launchcode.controllers;

import org.launchcode.models.Cheese;
import org.launchcode.models.Category;
import  org.launchcode.controllers.CategoryController;
import org.launchcode.models.data.CategoryDao;
import org.launchcode.models.data.CheeseDao;
import org.omg.CORBA.INTERNAL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;

/**
 * Created by LaunchCode
 */
@Controller
@RequestMapping("cheese")
public class CheeseController {

    @Autowired
    private CheeseDao cheeseDao;

    @Autowired
    private CategoryDao categoryDao;

    // Request path: /cheese
    @RequestMapping(value = "")
    public String index(Model model) {

        model.addAttribute("cheeses", cheeseDao.findAll());
        model.addAttribute("title", "My Cheeses");

        return "cheese/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayAddCheeseForm(Model model) {
        model.addAttribute("title", "Add Cheese");
        model.addAttribute(new Cheese());
        model.addAttribute("categories", categoryDao.findAll());
        return "cheese/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddCheeseForm(
            @ModelAttribute @Valid Cheese newCheese,
            Errors errors,
            @RequestParam int categoryId,
            Model model) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Cheese");
            return "cheese/add";
        }
        Category cat = categoryDao.findOne(categoryId);
        newCheese.setCategory(cat);
        cheeseDao.save(newCheese);
        return "redirect:";

    }
    @RequestMapping(value = "remove", method = RequestMethod.GET)
    public String displayRemoveCheeseForm(Model model) {
        model.addAttribute("cheeses", cheeseDao.findAll());
        model.addAttribute("title", "Remove Cheese");
        return "cheese/remove";
    }

    @RequestMapping(value = "remove", method = RequestMethod.POST)
    public String processRemoveCheeseForm(@RequestParam int[] cheeseIds) {

        for (int cheeseId : cheeseIds) {
            cheeseDao.delete(cheeseId);
        }

        return "redirect:";
    }
    @RequestMapping(value = "edit/{cheeseId}", method = RequestMethod.GET)
    public String displayEditForm(Model model, @PathVariable int cheeseId) {
        Cheese aCheese = cheeseDao.findOne(cheeseId);
        model.addAttribute("title", "Edit Cheese "+aCheese.getName()+"(id="+aCheese.getId());
        model.addAttribute("cheese", aCheese);
        model.addAttribute("categories", categoryDao.findAll());
        return "cheese/edit";

    }
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public String processEditForm(Model model, int cheeseId, String name, String description, int categoryId) {

            Cheese aCheese = cheeseDao.findOne(cheeseId);
            Category cat = categoryDao.findOne(categoryId);
            aCheese.setName(name);
            aCheese.setDescription(description);
            aCheese.setCategory(cat);
            cheeseDao.save(aCheese);
            return "redirect:/cheese/";

    }

}

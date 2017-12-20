package org.launchcode.controllers;

import org.launchcode.models.Cheese;
import org.launchcode.models.data.CheeseDao;
import org.launchcode.models.Menu;
import org.launchcode.models.data.MenuDao;
import org.launchcode.models.forms.AddMenuItemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "menu")
public class MenuController {
    @Autowired
    private MenuDao menuDao;

    @Autowired
    private CheeseDao cheeseDao;



    @RequestMapping("")
    public String index(Model model) {

        model.addAttribute("menus", menuDao.findAll());
        model.addAttribute("title", "My Menus");

        return "menu/index";
    }
    @RequestMapping(value ="add", method = RequestMethod.GET)
    public String add(Model model){
        model.addAttribute("title", "Add Menu");
        model.addAttribute(new Menu());

        return "menu/add";

    }
    @RequestMapping(value="add", method=RequestMethod.POST)
    public String add(Model model,
                      @ModelAttribute @Valid Menu menu, Errors errors){
        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Menu");
            return "menu/add";
        }
        menuDao.save(menu);
        return "redirect:view/" + menu.getId();
    }
    @RequestMapping(value = "view/{menu}", method = RequestMethod.GET)
    public String viewMenu(Model model, @PathVariable("menu") Menu menu) {
        model.addAttribute("cheeses", menu.getCheeses());
        model.addAttribute("menu", menu);
        model.addAttribute("title", menu.getName()+" Menu");

        return "menu/view";
    }
    @RequestMapping(value = "add-item/{menu}", method=RequestMethod.GET)
    public String addItem(Model model, @PathVariable("menu") int menuId) {

        Menu menu = menuDao.findOne(menuId);

        AddMenuItemForm form = new AddMenuItemForm(menu,cheeseDao.findAll());

        model.addAttribute("form", form);
        model.addAttribute("title", "Add item to " + menu.getName() + " Menu");

        return "menu/add-item";

    }
    @RequestMapping(value = "add-item", method = RequestMethod.POST)
    public String addtem(Model model,
                          @ModelAttribute @Valid AddMenuItemForm form,
                          Errors errors) {
        Cheese newCheese = cheeseDao.findOne(form.getCheeseId());
        Menu targetMenu = menuDao.findOne(form.getMenuId());

        targetMenu.addItem(newCheese);
        menuDao.save(targetMenu);

        return "redirect:/menu/view/" +targetMenu.getId();
    }
}



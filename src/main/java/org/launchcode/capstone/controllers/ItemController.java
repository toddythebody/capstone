package org.launchcode.capstone.controllers;

import org.launchcode.capstone.models.Item;
import org.launchcode.capstone.models.User;
import org.launchcode.capstone.models.data.ItemDao;
import org.launchcode.capstone.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Objects;

@Controller
@RequestMapping(value = "item")
public class ItemController {

    @Autowired
    private ItemDao itemDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private HttpServletResponse response;

    @Autowired
    private HttpServletRequest request;

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String addDisplay(Model model, @ModelAttribute Item item) {
        model.addAttribute("title", "iWants");
        return "item/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String addProcess(Model model, @ModelAttribute @Valid Item item, Errors errors) {
        if (errors.hasErrors()) {
            model.addAttribute("title", "iWants");
            return "item/add";
        }
        User user = userDao.findByName(Objects.requireNonNull(WebUtils.getCookie(request, "name")).getValue());
        item.setUser(user);
        itemDao.save(item);
        return "redirect:/user/";
    }

    @RequestMapping(value = "edit/{itemId}", method = RequestMethod.GET)
    public String editDisplay(Model model, @PathVariable int itemId) {
        Item item = itemDao.findById(itemId).get();
        model.addAttribute("title", "iWants");
        model.addAttribute(item);
        return "item/edit";
    }

    @RequestMapping(value = "edit/{itemId}", method = RequestMethod.POST)
    public String editProcess(Model model, @PathVariable int itemId, @ModelAttribute @Valid Item item, Errors errors) {
        if (errors.hasErrors()) {
            model.addAttribute("title", "iWants");
            return "item/edit";
        }
        Item newItem = itemDao.findById(itemId).get();
        newItem.setName(item.getName());
        newItem.setDescription(item.getDescription());
        itemDao.save(newItem);
        return "redirect:/user/";
    }

    @RequestMapping(value = "remove/{itemId}")
    public String remove(@PathVariable int itemId) {
        itemDao.delete(itemDao.findById(itemId).get());
        return "redirect:/user/";
    }

}

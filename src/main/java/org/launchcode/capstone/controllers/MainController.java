package org.launchcode.capstone.controllers;

import org.launchcode.capstone.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class MainController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;


    @RequestMapping(value = "")
    public String homepage(Model model) {
        if (WebUtils.getCookie(request, "name") != null) {
            return "redirect:/user";
        }
        model.addAttribute("title", "iWants");
        return "main/index";
    }

}

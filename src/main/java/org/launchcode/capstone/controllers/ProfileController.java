package org.launchcode.capstone.controllers;

import org.launchcode.capstone.models.Profile;
import org.launchcode.capstone.models.User;
import org.launchcode.capstone.models.data.ProfileDao;
import org.launchcode.capstone.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "profile")
public class ProfileController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private ProfileDao profileDao;

    @Autowired
    private HttpServletRequest request;

    private Cookie cookie;

    @RequestMapping(value = "")
    public String index(Model model) {
        String name = WebUtils.getCookie(request, "name").getValue();
        User user = userDao.findByName(name);
        if (user.getProfile() != null) {
            model.addAttribute(user.getProfile());
        }
        model.addAttribute("title", "iWants");
        return "profile/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String addDisplay(Model model, @ModelAttribute Profile profile) {
        model.addAttribute("title", "iWants");
        return "profile/add";
    }
}

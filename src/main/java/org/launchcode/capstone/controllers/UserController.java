package org.launchcode.capstone.controllers;

import org.launchcode.capstone.models.User;
import org.launchcode.capstone.models.data.UserDao;
import org.launchcode.capstone.models.faces.Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping(value = "user")
public class UserController {

    @Autowired
    private UserDao userDao;

    private Encoder encoder;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpSession session;

    @RequestMapping(value = "")
    public String index(Model model) {
        if (session.getAttribute("user") == "null") {
            return "redirect:/user/login";
        }
        User user = (User) session.getAttribute("user");
        model.addAttribute("name", user.getName());
        model.addAttribute("title", "Hello World");
        return "user/index";
    }

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String loginDisplay(Model model, @ModelAttribute User user) {
        model.addAttribute("title", "Login");
        return "user/login";
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String loginProcess(Model model, @ModelAttribute @Valid User user, Errors errors) throws Exception {
        if (errors.hasErrors()) {
            model.addAttribute("title", "Login");
            return "user/login";
        }
        for (User user1 : userDao.findAll()) {
            if (user1.getName().equals(user.getName())) {
                if (Encoder.match(user.getName(), user.getPassword(), user1.getPassword())) {
                    session = request.getSession(true);
                    session.setAttribute("user", user1);
                    return "redirect:";
                } else {
                    model.addAttribute("title", "Login");
                    model.addAttribute("error", "Wrong Password");
                    return "user/login";
                }
            }
        }
        model.addAttribute("title", "Login");
        model.addAttribute("error", "User doesn't exist");
        return "user/login";
    }

    @RequestMapping(value = "register", method = RequestMethod.GET)
    public String registerDisplay(Model model, @ModelAttribute User user) {
        model.addAttribute("title", "Register");
        return "user/register";
    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
    public String registerProcess(Model model, @ModelAttribute @Valid User user, Errors errors,
                                  @RequestParam String verify) throws Exception {
        if (errors.hasErrors()) {
            model.addAttribute("title", "Register");
            return "user/register";
        }
        for (User user1 : userDao.findAll()) {
            if (user1.getName().equals(user.getName())) {
                model.addAttribute("title", "Register");
                model.addAttribute("error", "User Exists");
                return "user/register";
            }
            if (!user.getEmail().isEmpty() && !user1.getEmail().isEmpty()) {
                if (user.getEmail().equals(user1.getEmail())) {
                    model.addAttribute("title", "Register");
                    model.addAttribute("error", "Email Exists");
                    return "user/register";
                }
            }
        }
        if (!user.getPassword().equals(verify)) {
            model.addAttribute("title", "Register");
            model.addAttribute("verify", "Password does not match");
            return "user/register";
        }
        user.setPassword(Encoder.encode(user.getName(), user.getPassword()));
        userDao.save(user);
        session = request.getSession(true);
        session.setAttribute("user", user);
        return "redirect:";
    }

    @RequestMapping(value = "logout")
    public String logout() {
        session.setAttribute("user", "null");
        session = request.getSession(false);
        return "redirect:/user/login";
    }

}

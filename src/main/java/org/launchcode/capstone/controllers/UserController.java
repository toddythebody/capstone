package org.launchcode.capstone.controllers;

import org.launchcode.capstone.models.Item;
import org.launchcode.capstone.models.User;
import org.launchcode.capstone.models.data.ItemDao;
import org.launchcode.capstone.models.data.UserDao;
import org.launchcode.capstone.models.security.Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "user")
public class UserController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private ItemDao itemDao;

    private Encoder encoder;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    private Cookie cookie;

    @RequestMapping(value = "")
    public String index(Model model) {
        String name = WebUtils.getCookie(request, "name").getValue();
        User user = userDao.findByName(name);
        model.addAttribute("title", "Hello World");
        model.addAttribute("name", name);
        model.addAttribute(user);
        return "user/index";
    }

    @RequestMapping(value = "/{name}")
    public String profile(Model model, @PathVariable String name) {
        User user = userDao.findByName(name);
        model.addAttribute("title", name + "'s profile");
        model.addAttribute("name", name);
        model.addAttribute(user);
        return "user/friend";
    }

    @RequestMapping(value = "purchase/{itemId}")
    public String profile(@PathVariable int itemId) {
        Item item = itemDao.findById(itemId).get();
        User user = item.getUser();
        if (item.isPurchased()) {
            item.setPurchased(false);
        } else {
            item.setPurchased(true);
        }
        itemDao.save(item);
        return "redirect:/user/" + user.getName();
    }

    @RequestMapping(value = "friends")
    public String friends(Model model) {
        User user = userDao.findByName(WebUtils.getCookie(request, "name").getValue());
        List<User> friends = user.getFriends();
        model.addAttribute("title", "Following");
        model.addAttribute(user);
        return "user/friends";
    }

    @RequestMapping(value = "search")
    public String search(Model model, @RequestParam String search) {
        if (search.equals("")) {
            return "redirect:/user/friends";
        }
        List<User> userList = new ArrayList<>();
        User logged_user = userDao.findByName(WebUtils.getCookie(request, "name").getValue());
        for (User user : userDao.findAll()) {
            if (user.getName().toLowerCase().contains(search.toLowerCase())) {
                if (!logged_user.equals(user) && !logged_user.getFriends().contains(user)) {
                    userList.add(user);
                }
            }
        }
        model.addAttribute(userList);
        model.addAttribute("title", "Search: " + search);
        return "user/search";
    }

    @RequestMapping(value = "add/{name}")
    public String addFriend(@PathVariable String name) {
        User user = userDao.findByName(WebUtils.getCookie(request, "name").getValue());
        User friend = userDao.findByName(name);
        if (!user.equals(friend) && !user.getFriends().contains(friend)) {
            user.addFriend(friend);
            userDao.save(user);
        }
        return "redirect:/user/friends";
    }

    @RequestMapping(value = "remove/{name}")
    public String removeFriend(@PathVariable String name) {
        User user = userDao.findByName(WebUtils.getCookie(request, "name").getValue());
        User friend = userDao.findByName(name);
        if (!user.equals(friend) && user.getFriends().contains(friend)) {
            user.removeFriend(friend);
            userDao.save(user);
        }
        return "redirect:/user/friends";
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
            if (user1.getName().toLowerCase().equals(user.getName().toLowerCase())) {
                if (Encoder.match(user.getName(), user.getPassword(), user1.getPassword())) {
                    cookie = new Cookie("name", user1.getName());
                    cookie.setMaxAge(60*60*24*30);
                    cookie.setPath("/");
                    response.addCookie(cookie);
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
            if (user1.getName().toLowerCase().equals(user.getName().toLowerCase())) {
                model.addAttribute("title", "Register");
                model.addAttribute("error", "User Exists");
                return "user/register";
            }
            if (!user.getEmail().isEmpty() && !user1.getEmail().isEmpty()) {
                if (user.getEmail().toLowerCase().equals(user1.getEmail().toLowerCase())) {
                    model.addAttribute("title", "Register");
                    model.addAttribute("error", "Email Exists");
                    return "user/register";
                }
            }
        }
        if (!user.getPassword().equals(verify)) {
            model.addAttribute("title", "Register");
            model.addAttribute("error", "Password does not match");
            return "user/register";
        }
        user.setPassword(Encoder.encode(user.getName(), user.getPassword()));
        userDao.save(user);
        cookie = new Cookie("name", user.getName());
        cookie.setMaxAge(60*60*24*30);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "redirect:";
    }

    @RequestMapping(value = "logout")
    public String logout() {
        cookie = new Cookie("name", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "redirect:";
    }

}

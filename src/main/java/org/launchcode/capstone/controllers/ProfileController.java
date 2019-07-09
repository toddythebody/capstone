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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.WebUtils;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

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
    public String index(Model model) throws IOException {
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

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String addProcess(Model model, @RequestParam("pic") MultipartFile pic) throws IOException {
        if (!pic.isEmpty()) {
            BufferedImage src = ImageIO.read(new ByteArrayInputStream(pic.getBytes()));
            int user_id = userDao.findByName(WebUtils.getCookie(request, "name").getValue()).getId();
            File destination = new File("src/main/resources/static/img/profile_pic/" + user_id + ".jpg");
            ImageIO.write(src, "jpg", destination);
        }
        return "redirect:/profile";
    }
}

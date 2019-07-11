package org.launchcode.capstone.controllers;

import org.launchcode.capstone.models.Profile;
import org.launchcode.capstone.models.User;
import org.launchcode.capstone.models.data.ProfileDao;
import org.launchcode.capstone.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.WebUtils;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;

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
        if (user.getProfile() != null && user.getProfile().getPic() != null) {
            String pic_base64 = new String(Base64.getEncoder().encode(user.getProfile().getPic()), "UTF-8");
            model.addAttribute("pic", pic_base64);
        }
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
    public String addProcess(Model model, @RequestParam("file") MultipartFile file, @ModelAttribute @Valid Profile profile, Errors errors) throws IOException {
        if (errors.hasErrors()) {
            model.addAttribute("title", "iWants");
            return "profile/add";
        }
        User user = userDao.findByName(WebUtils.getCookie(request, "name").getValue());
        profile.setUser(user);
        if (!file.isEmpty()) {
            byte[] pic = file.getBytes();
            profile.setPic(pic);
        }
        profileDao.save(profile);
        return "redirect:/profile";
    }

    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public String editDisplay(Model model) {
        User user = userDao.findByName(WebUtils.getCookie(request, "name").getValue());
        Profile profile = user.getProfile();

        model.addAttribute("title", "iWants");
        model.addAttribute(profile);
        return "profile/edit";
    }
}

package com.exemple.jarsoft.controller;

import com.exemple.jarsoft.domain.Banner;
import com.exemple.jarsoft.domain.Category;
import com.exemple.jarsoft.domain.Request;
import com.exemple.jarsoft.repos.BannerRepos;
import com.exemple.jarsoft.repos.CategoryRepos;
import com.exemple.jarsoft.repos.RequestRepos;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;

@Controller
public class BannerController {
    private final BannerRepos bannerRepos;
    private final CategoryRepos categoryRepos;
    private final RequestRepos requestRepos;


    public BannerController(BannerRepos bannerRepos, CategoryRepos categoryRepos, RequestRepos requestRepos) {
        this.bannerRepos = bannerRepos;
        this.categoryRepos = categoryRepos;
        this.requestRepos = requestRepos;
    }

    @GetMapping("/banner")
    public String getBanner(Model model) {
        model.addAttribute("banners", bannerRepos.findByIsDeleted(false));
        return "banner";
    }

    @GetMapping("/createBanner")
    public String newBanner(Model model) {
        model.addAttribute("banner", new Banner());
        model.addAttribute("categories", categoryRepos.findAll());
        return "createBanner";
    }

    @PostMapping("/createBanner")
    public String createBanner(Banner banner, Model model) throws UnknownHostException {
        Banner foundBanner = bannerRepos.findByNameAndCategory(banner.getName(), banner.getCategory());

        if (foundBanner == null) {
            banner.setDeleted(false);
            bannerRepos.save(banner);

            requests(banner);

            return "redirect:/banner";
        }

        model.addAttribute("categories", categoryRepos.findAll());
        model.addAttribute("message", "Банер с таким именем в категории существует!");
        return "createBanner";
    }

    @GetMapping("/editBanner")
    public String getEditBanner(@RequestParam Integer id, Model model) {
        model.addAttribute("categories", categoryRepos.findAll());
        model.addAttribute("banner", bannerRepos.getOne(id));
        return "editBanner";
    }

    @PostMapping("/editBanner/{banner_id}")
    public String editBanner(@PathVariable("banner_id") Integer id,
                             @RequestParam(required = false) String name,
                             @RequestParam(required = false) Double price,
                             @RequestParam(required = false) Integer categoryId,
                             @RequestParam(required = false) String text, Model model) throws UnknownHostException {
        Banner banner = bannerRepos.findByNameAndCategory(name, categoryRepos.getOne(categoryId));

        if (banner != null) {
            model.addAttribute("message", "Баннер с именем " + name + " в категории " +
                    categoryRepos.getOne(categoryId).getName() + " существует!");
            return "banner/{banner_id}";
        }

        Banner thisBanner = bannerRepos.getOne(id);

        if (name != null && !name.isEmpty()) {
            thisBanner.setName(name);
        }

        if (price != null) {
            thisBanner.setPrice(price);
        }

        if (categoryId != null) {
            thisBanner.setCategory(categoryRepos.getOne(categoryId));
        }

        if (text != null && !text.isEmpty()) {
            thisBanner.setText(text);
        }

        requests(thisBanner);

        return "redirect:/banner";
    }


    @PostMapping("/deleteBanner/{banner_id}")
    public String deleteBanner(@PathVariable("banner_id") Integer id) throws UnknownHostException {
        Banner banner = bannerRepos.getOne(id);
        banner.setDeleted(true);
        bannerRepos.save(banner);

        requests(banner);

        return "redirect:/banner";
    }

    private void requests(Banner banner) throws UnknownHostException {
        String ip = InetAddress.getLocalHost().toString();
        LocalDateTime ldt = LocalDateTime.now();

        Request request = new Request(banner, ip, ldt);
        requestRepos.save(request);
    }
}

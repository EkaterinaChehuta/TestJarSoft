package com.exemple.jarsoft.controller;

import com.exemple.jarsoft.domain.Banner;
import com.exemple.jarsoft.domain.Request;
import com.exemple.jarsoft.repos.BannerRepos;
import com.exemple.jarsoft.repos.CategoryRepos;
import com.exemple.jarsoft.repos.RequestRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.UnknownHostException;
import java.util.List;

@Controller
public class BannerController {
    private final BannerRepos bannerRepos;
    private final CategoryRepos categoryRepos;

    @Autowired
    public BannerController(BannerRepos bannerRepos, CategoryRepos categoryRepos) {
        this.bannerRepos = bannerRepos;
        this.categoryRepos = categoryRepos;
    }

    @GetMapping("/banner")
    public String getBanner(Model model) {
        model.addAttribute("banners", bannerRepos.findByIsDeleted(false));
        return "banner";
    }

    @GetMapping("/banner/filter")
    public String getFilterBanner(@RequestParam(required = false) String filter, Model model) {
        List<Banner> filteredBanners = bannerRepos.findByNameContainingAndIsDeleted(filter, false);

        model.addAttribute("banners", filteredBanners);
        return "banner";
    }

    @GetMapping("/createBanner")
    public String newBanner(Model model) {
        model.addAttribute("banner", new Banner());
        model.addAttribute("categories", categoryRepos.findByIsDeleted(false));
        return "createBanner";
    }

    @PostMapping("/createBanner")
    public String createBanner(Banner banner, Model model) {
        Banner foundBanner = bannerRepos.findByNameAndCategory(banner.getName(), banner.getCategory());

        if (foundBanner == null) {
            banner.setDeleted(false);
            bannerRepos.save(banner);

            return "redirect:/banner";
        }

        model.addAttribute("categories", categoryRepos.findByIsDeleted(false));
        model.addAttribute("message", "Банер с именем " + banner.getName() + " в категории " +
                banner.getCategory().getName() + " существует!");
        return "createBanner";
    }

    @GetMapping("/editBanner/{banner_id}")
    public String getEditBanner(@PathVariable("banner_id") Integer id, Model model) {
        model.addAttribute("categories", categoryRepos.findByIsDeleted(false));
        model.addAttribute("banner", bannerRepos.getOne(id));
        return "editBanner";
    }

    @PostMapping("/editBanner/{banner_id}")
    public String editBanner(@PathVariable("banner_id") Integer id,
                             @RequestParam(required = false) String name,
                             @RequestParam(required = false) Double price,
                             @RequestParam(required = false) Integer categoryId,
                             @RequestParam(required = false) String text, Model model) throws UnknownHostException {
        Banner thisBanner = bannerRepos.getOne(id);

        if (name != null && !name.isEmpty()) {
            Banner banner = bannerRepos.findByNameAndCategory(name, thisBanner.getCategory());

            if (banner == null) {
                if (categoryId != null) {
                    banner = bannerRepos.findByNameAndCategory(name, categoryRepos.getOne(categoryId));

                    if (banner == null) {
                        thisBanner.setCategory(categoryRepos.getOne(categoryId));
                    } else {
                        model.addAttribute("message", "Баннер с именем " + name + " в категории " +
                                categoryRepos.getOne(categoryId).getName() + " существует!");
                        return getEditBanner(id, model);
                    }
                }

                thisBanner.setName(name);
            } else {
                model.addAttribute("message", "Баннер с именем " + name + " в категории " +
                        thisBanner.getCategory().getName() + " существует!");
                return getEditBanner(id, model);
            }
        } else {
            if (categoryId != null) {
                Banner banner = bannerRepos.findByNameAndCategory(thisBanner.getName(), categoryRepos.getOne(categoryId));

                if (banner == null) {
                    thisBanner.setCategory(categoryRepos.getOne(categoryId));
                } else {
                    model.addAttribute("message", "Баннер с именем " + thisBanner.getName() + " в категории " +
                            categoryRepos.getOne(categoryId).getName() + " существует!");
                    return getEditBanner(id, model);
                }
            }
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

        bannerRepos.save(thisBanner);

        return "redirect:/banner";
    }


    @PostMapping("/deleteBanner/{banner_id}")
    public String deleteBanner(@PathVariable("banner_id") Integer id) {
        Banner banner = bannerRepos.getOne(id);
        banner.setDeleted(true);
        bannerRepos.save(banner);

        return "redirect:/banner";
    }
}

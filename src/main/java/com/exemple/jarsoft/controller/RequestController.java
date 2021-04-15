package com.exemple.jarsoft.controller;

import com.exemple.jarsoft.domain.Banner;
import com.exemple.jarsoft.domain.Request;
import com.exemple.jarsoft.repos.BannerRepos;
import com.exemple.jarsoft.repos.CategoryRepos;
import com.exemple.jarsoft.repos.RequestRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Controller
public class RequestController {
    private final BannerRepos bannerRepos;
    private final CategoryRepos categoryRepos;
    private final RequestRepos requestRepos;

    @Autowired
    public RequestController(BannerRepos bannerRepos, CategoryRepos categoryRepos, RequestRepos requestRepos) {
        this.bannerRepos = bannerRepos;
        this.categoryRepos = categoryRepos;
        this.requestRepos = requestRepos;
    }

    @GetMapping("/bid")
    @ResponseBody
    public String textBanner(HttpServletRequest req,
                             @RequestParam String category) {
        String ipAddress = req.getRemoteAddr();
        LocalDateTime dateTime = LocalDateTime.now();
        String userAgent = req.getHeader("User-Agent");

        List<Banner> banners = bannerRepos.findByCategoryAndIsDeleted(categoryRepos.findByRequestIdAndIsDeleted(category, false), false);

        if (banners.size() == 0) {
            Request request = new Request(null, userAgent, ipAddress, dateTime);
            requestRepos.save(request);

            return HttpStatus.NO_CONTENT.toString();
        }

        Banner banner = banners.stream().max(Comparator.comparing(Banner::getPrice)).get();

        Request request = new Request(banner, userAgent, ipAddress, dateTime);
        requestRepos.save(request);

        return banner.getText();
    }
}

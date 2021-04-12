package com.exemple.jarsoft.controller;

import com.exemple.jarsoft.domain.Banner;
import com.exemple.jarsoft.domain.Category;
import com.exemple.jarsoft.repos.BannerRepos;
import com.exemple.jarsoft.repos.CategoryRepos;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CategoryController {
    private final CategoryRepos categoryRepos;
    private final BannerRepos bannerRepos;

    public CategoryController(CategoryRepos categoryRepos, BannerRepos bannerRepos) {
        this.categoryRepos = categoryRepos;
        this.bannerRepos = bannerRepos;
    }

    @GetMapping("/category")
    public String getCategory(Model model) {
        model.addAttribute("categories", categoryRepos.findByIsDeleted(false));
        return "category";
    }

    @GetMapping("/createCategory")
    public String newCategory(Model model) {
        model.addAttribute("category", new Category());
        return "createCategory";
    }

    @PostMapping("/createCategory")
    public String createCategory(Category category, Model model) {
        List<Category> foundCategory = categoryRepos.findByNameOrRequestId(category.getName(), category.getRequestId());

        if (foundCategory.size() != 0) {
            model.addAttribute("message", "Категория с таким именем или идентификатором существует!");
            return "createCategory";
        }

        category.setDeleted(false);
        categoryRepos.save(category);
        return "redirect:/category";
    }

    @GetMapping("/editCategory/{category_id}")
    public String getEditCategory(@PathVariable("category_id") Integer id, Model model) {
        model.addAttribute("category", categoryRepos.getOne(id));
        return "editCategory";
    }

    @PostMapping("/editCategory/{category_id}")
    public String editCategory(@PathVariable("category_id") Integer id,
                               @RequestParam(required = false) String name,
                               @RequestParam(required = false) String requestId, Model model) {
        List<Category> foundCategory = categoryRepos.findByNameOrRequestId(name, requestId);

        if (foundCategory.size() != 0) {
            model.addAttribute("message", "Категория с таким именем существует!");
            return "editCategory/{category_id}";
        }

        Category thisCategory = categoryRepos.getOne(id);

        if (name != null && !name.isEmpty()) {
            thisCategory.setName(name);
        }

        if (requestId != null && !requestId.isEmpty()) {
            thisCategory.setRequestId(requestId);
        }

        categoryRepos.save(thisCategory);

        return "redirect:/category";
    }

    @PostMapping("/deleteCategory/{category_id}")
    public String deleteCategory(@PathVariable("category_id") Integer id, Model model) {
        Category category = categoryRepos.getOne(id);
        List<Banner> bannerList = bannerRepos.findByCategoryAndIsDeleted(category, false);

        if(bannerList.size() == 0){
            category.setDeleted(true);
            categoryRepos.save(category);
            return "redirect:/category";
        }

        model.addAttribute("message", "Категория не может быть удалена, так как имеет не удаленные баннеры!");
        model.addAttribute("bannersList", bannerList);
        return "editCategory/{category_id}";
    }
}

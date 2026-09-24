package vn.iotstar.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.iotstar.dto.ProductDTO;
import vn.iotstar.service.ProductService;
import vn.iotstar.service.UserService;

@Controller @RequiredArgsConstructor @RequestMapping("/products")
public class ProductController {
    private final ProductService products; private final UserService users;
    @GetMapping public String list(@RequestParam(defaultValue = "") String keyword, @RequestParam(defaultValue = "0") int page, Model model) {
        model.addAttribute("products", products.search(keyword, page, 5)); model.addAttribute("keyword", keyword); return "products/list";
    }
    @GetMapping("/new") public String form(Model model) { model.addAttribute("productDTO", new ProductDTO()); model.addAttribute("users", users.search("", 0, 100).getContent()); return "products/form"; }
    @PostMapping("/save") public String save(@Valid @ModelAttribute ProductDTO dto, BindingResult result, @RequestParam(required = false) MultipartFile image, Model model, RedirectAttributes redirect) {
        if (result.hasErrors()) { model.addAttribute("users", users.search("", 0, 100).getContent()); return "products/form"; }
        products.save(dto, image); redirect.addFlashAttribute("success", "Đã thêm sản phẩm."); return "redirect:/products";
    }
    @PostMapping("/delete/{id}") public String delete(@PathVariable Long id, RedirectAttributes redirect) { products.delete(id); redirect.addFlashAttribute("success", "Đã xóa sản phẩm."); return "redirect:/products"; }
}
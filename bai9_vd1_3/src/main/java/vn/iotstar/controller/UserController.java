package vn.iotstar.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.iotstar.service.UserService;

@Controller @RequiredArgsConstructor @RequestMapping("/users")
public class UserController {
    private final UserService users;
    @GetMapping public String list(@RequestParam(defaultValue = "") String keyword, @RequestParam(defaultValue = "0") int page, Model model) {
        model.addAttribute("users", users.search(keyword, page, 5)); model.addAttribute("keyword", keyword); return "users/list";
    }
}
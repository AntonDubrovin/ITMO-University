package ru.itmo.wp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.itmo.wp.form.NoticeCredentials;
import ru.itmo.wp.service.NoticeService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class AddNoticePage extends Page {
    private final NoticeService noticeService;

    public AddNoticePage(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @PostMapping("/addNotice")
    public String addNotice(@Valid @ModelAttribute("noticeForm") NoticeCredentials noticeForm,
                                BindingResult bindingResult,
                                Model model) {
        if (bindingResult.hasErrors()) {
            return "AddNoticePage";
        }
        if (model.getAttribute("user") == null) {
            return "redirect:/";
        }
        noticeService.addNotice(noticeForm);
        return "redirect:/";
    }

    @GetMapping("/addNotice")
    public String addNotice(Model model, HttpSession httpSession) {
        if (getUser(httpSession) == null) {
            return "redirect:/enter";
        }

        model.addAttribute("noticeForm", new NoticeCredentials());
        return "AddNoticePage";
    }
}

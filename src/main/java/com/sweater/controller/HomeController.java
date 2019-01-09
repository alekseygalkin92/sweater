package com.sweater.controller;

import com.sweater.domain.Message;
import com.sweater.repos.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private MessageRepo messageRepo;

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("messageList", messageRepo.findAll());
        model.addAttribute("message", new Message());
        return "home";
    }

    @PostMapping("/home")
    public String addMessage(@ModelAttribute Message message, Model model) {
        messageRepo.save(message);

        model.addAttribute("messageList", messageRepo.findAll());
        model.addAttribute("message", new Message());

        return "home";
    }

    @PostMapping("/filter")
    public String filter(@RequestParam String filter, Model model) {
        Iterable<Message> messages;

        if (filter != null && !filter.isEmpty()) {
            messages = messageRepo.findByTag(filter);
        } else {
            messages = messageRepo.findAll();
        }

        model.addAttribute("messageList", messages);
        model.addAttribute("message", new Message());

        return "home";
    }
}

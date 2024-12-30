package com.stdev.futurebox.controller;

import com.stdev.futurebox.service.FutureBoxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/future-boxes")
@RequiredArgsConstructor
public class FutureBoxController {

    private final FutureBoxService futureBoxService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("futureBoxes", futureBoxService.findAll());
        return "future-boxes/futureBoxList";
    }

    @GetMapping("/{futureBoxId}")
    public String futureBox(@PathVariable Long futureBoxId, Model model) {
        model.addAttribute("futureBox", futureBoxService.findById(futureBoxId));
        return "future-boxes/futureBox";
    }

}

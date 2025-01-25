package com.stdev.futurebox.controller;

import com.stdev.futurebox.domain.FuturePerfume;
import com.stdev.futurebox.dto.FuturePerfumeCreateForm;
import com.stdev.futurebox.service.FutureBoxService;
import com.stdev.futurebox.service.FuturePerfumeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/future-perfume")
@RequiredArgsConstructor
public class FuturePerfumeController {

    private final FuturePerfumeService futurePerfumeService;
    private final FutureBoxService futureBoxService;

    @GetMapping("/new")
    public String createForm(@RequestParam Long boxId, Model model) {
        model.addAttribute("boxId", boxId);
        return "future-perfume/createPerfumeForm";
    }

    @PostMapping("/new")
    public String create(@RequestParam Long boxId, @ModelAttribute FuturePerfumeCreateForm form) {
        FuturePerfume perfume = form.toEntity();
        perfume.setBoxId(boxId);
        futurePerfumeService.save(perfume);
        return "redirect:/future-boxes/" + boxId;
    }

    @GetMapping("/{id}")
    public String perfume(@PathVariable Long id, Model model) {
        FuturePerfume perfume = futurePerfumeService.findById(id);
        model.addAttribute("perfume", perfume);
        return "future-perfume/perfume";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam Long id, @RequestParam Long boxId) {
        futurePerfumeService.delete(id);
        return "redirect:/future-boxes/" + boxId;
    }
} 
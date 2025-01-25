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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        return "future-perfume/futurePerfume";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        FuturePerfume perfume = futurePerfumeService.findById(id);
        if (perfume == null) {
            throw new IllegalArgumentException("해당 ID의 FuturePerfume이 존재하지 않습니다: " + id);
        }
        
        Long boxId = perfume.getBoxId();
        futurePerfumeService.delete(id);
        redirectAttributes.addFlashAttribute("message", "Future Perfume deleted successfully.");
        return "redirect:/future-boxes/" + boxId;
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        FuturePerfume perfume = futurePerfumeService.findById(id);
        model.addAttribute("perfume", perfume);
        return "future-perfume/editPerfumeForm";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id, @ModelAttribute FuturePerfumeCreateForm form) {
        FuturePerfume perfume = form.toEntity();
        perfume.setId(id);
        futurePerfumeService.update(perfume);
        return "redirect:/future-perfume/" + perfume.getId();
    }
} 
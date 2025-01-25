package com.stdev.futurebox.controller;

import com.stdev.futurebox.domain.FutureTarot;
import com.stdev.futurebox.dto.FutureTarotCreateForm;
import com.stdev.futurebox.service.FutureTarotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/future-tarot")
@RequiredArgsConstructor
public class FutureTarotController {

    private final FutureTarotService futureTarotService;

    @GetMapping("/new")
    public String createForm(@RequestParam Long boxId, Model model) {
        model.addAttribute("boxId", boxId);
        return "future-tarot/createTarotForm";
    }

    @PostMapping("/new")
    public String create(@RequestParam Long boxId, @ModelAttribute FutureTarotCreateForm form) {
        FutureTarot tarot = form.toEntity();
        tarot.setBoxId(boxId);
        futureTarotService.save(tarot);
        return "redirect:/future-boxes/" + boxId;
    }

    @GetMapping("/{id}")
    public String tarot(@PathVariable Long id, Model model) {
        FutureTarot tarot = futureTarotService.findById(id);
        model.addAttribute("tarot", tarot);
        return "future-tarot/futureTarot";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        FutureTarot tarot = futureTarotService.findById(id);
        if (tarot == null) {
            throw new IllegalArgumentException("해당 ID의 FutureTarot가 존재하지 않습니다: " + id);
        }
        
        Long boxId = tarot.getBoxId();
        futureTarotService.delete(id);
        redirectAttributes.addFlashAttribute("message", "Future Tarot deleted successfully.");
        return "redirect:/future-boxes/" + boxId;
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        FutureTarot tarot = futureTarotService.findById(id);
        model.addAttribute("tarot", tarot);
        return "future-tarot/editTarotForm";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id, @ModelAttribute FutureTarotCreateForm form) {
        FutureTarot tarot = form.toEntity();
        futureTarotService.update(id, tarot);
        return "redirect:/future-tarot/" + id;
    }
} 
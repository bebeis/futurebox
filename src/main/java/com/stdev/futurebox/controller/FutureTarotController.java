package com.stdev.futurebox.controller;

import com.stdev.futurebox.domain.FutureTarot;
import com.stdev.futurebox.dto.FutureTarotCreateForm;
import com.stdev.futurebox.service.FutureBoxService;
import com.stdev.futurebox.service.FutureTarotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/future-tarot")
@RequiredArgsConstructor
public class FutureTarotController {

    private final FutureTarotService futureTarotService;
    private final FutureBoxService futureBoxService;

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
        return "future-tarot/tarot";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam Long id, @RequestParam Long boxId) {
        futureTarotService.delete(id);
        return "redirect:/future-boxes/" + boxId;
    }
} 
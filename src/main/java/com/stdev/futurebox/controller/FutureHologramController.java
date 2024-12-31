package com.stdev.futurebox.controller;

import com.stdev.futurebox.domain.FutureHologram;
import com.stdev.futurebox.dto.FutureHologramCreateForm;
import com.stdev.futurebox.repository.AccessLogRepository;
import com.stdev.futurebox.service.FutureHologramService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/future-hologram")
@Slf4j
@RequiredArgsConstructor
public class FutureHologramController {
    private final FutureHologramService futureHologramService;
    private final AccessLogRepository accessLogRepository;

    @GetMapping("/{futureHologramId}")
    public String futureHologram(@PathVariable Long futureHologramId, Model model, HttpServletRequest request) {
        FutureHologram futureHologram = futureHologramService.findById(futureHologramId);
        model.addAttribute("hologram", futureHologram);
        String ipAddress = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        accessLogRepository.saveLog("HOLOGRAM", futureHologramId, ipAddress, userAgent);
        log.info("Admin viewed future hologram id: {} from ip: {}, userAgent: {}", futureHologramId, ipAddress, userAgent);
        return "future-hologram/futureHologram";
    }

    @GetMapping("/new")
    public String createForm() {
        return "future-hologram/createHologramForm";
    }

    @PostMapping("/new")
    public String create(FutureHologramCreateForm form, Model model, RedirectAttributes redirectAttributes) {
        FutureHologram hologram = form.toEntity();
        futureHologramService.create(hologram);
        model.addAttribute("hologram", hologram);
        redirectAttributes.addFlashAttribute("message", "Future Hologram created successfully.");
        redirectAttributes.addAttribute("futureHologramId", hologram.getId());
        return "redirect:/future-hologram/{futureHologramId}";
    }

    @GetMapping("/edit/{futureHologramId}")
    public String editForm(@PathVariable Long futureHologramId, Model model) {
        FutureHologram futureHologram = futureHologramService.findById(futureHologramId);
        model.addAttribute("hologram", futureHologram);
        return "future-hologram/editHologramForm";
    }

    @PostMapping("/edit/{futureHologramId}")
    public String edit(@PathVariable Long futureHologramId, Model model, FutureHologramCreateForm form, RedirectAttributes redirectAttributes) {
        FutureHologram hologram = form.toEntity();
        hologram.setId(futureHologramId);
        futureHologramService.update(hologram);
        model.addAttribute("hologram", hologram);
        redirectAttributes.addFlashAttribute("message", "Future Hologram updated successfully.");
        redirectAttributes.addAttribute("futureHologramId", hologram.getId());
        return "redirect:/future-hologram/{futureHologramId}";
    }

    @PostMapping("/delete/{futureHologramId}")
    public String delete(@PathVariable Long futureHologramId, RedirectAttributes redirectAttributes) {
        FutureHologram futureHologram = futureHologramService.findById(futureHologramId);
        futureHologramService.delete(futureHologramId);
        redirectAttributes.addFlashAttribute("message", "Future Hologram deleted successfully.");
        redirectAttributes.addAttribute("futureBoxId", futureHologram.getBoxId());
        return "redirect:/future-boxes/{futureBoxId}";
    }
}

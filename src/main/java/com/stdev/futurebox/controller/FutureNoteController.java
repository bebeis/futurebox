package com.stdev.futurebox.controller;

import com.stdev.futurebox.domain.FutureNote;
import com.stdev.futurebox.dto.FutureNoteCreateForm;
import com.stdev.futurebox.repository.AccessLogRepository;
import com.stdev.futurebox.service.FutureNoteService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/future-note")
@Slf4j
@RequiredArgsConstructor
public class FutureNoteController {

    private final FutureNoteService futureNoteService;
    private final AccessLogRepository accessLogRepository;

    @GetMapping("/{futureNoteId}")
    public String futureNote(@PathVariable Long futureNoteId, Model model, HttpServletRequest request) {
        FutureNote futureNote = futureNoteService.findById(futureNoteId);
        model.addAttribute("note", futureNote);
        String ipAddress = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        accessLogRepository.saveLog("NOTE", futureNoteId, ipAddress, userAgent);
        log.info("Admin viewed future note id: {} from ip: {}, userAgent: {}", futureNoteId, ipAddress, userAgent);
        return "future-note/futureNote";
    }

    @GetMapping("/new")
    public String createForm() {
        return "future-note/createNoteForm";
    }

    @PostMapping("/new")
    public String create(@ModelAttribute FutureNoteCreateForm form, Model model, RedirectAttributes redirectAttributes)  {
        FutureNote futureNote = form.toEntity();
        futureNoteService.create(futureNote);
        model.addAttribute("note", futureNote);
        redirectAttributes.addFlashAttribute("message", "Future Note created successfully.");
        redirectAttributes.addAttribute("futureNoteId", futureNote.getId());
        return "redirect:/future-note/{futureNoteId}";
    }

    @GetMapping("/edit/{futureNoteId}")
    public String editForm(@PathVariable Long futureNoteId, Model model) {
        FutureNote futureNote = futureNoteService.findById(futureNoteId);
        model.addAttribute("note", futureNote);
        return "future-note/editNoteForm";
    }

    @PostMapping("/edit/{futureNoteId}")
    public String edit(@PathVariable Long futureNoteId, FutureNoteCreateForm form, Model model, RedirectAttributes redirectAttributes) {
        FutureNote futureNote = form.toEntity();
        futureNote.setId(futureNoteId);
        futureNoteService.update(futureNote);
        model.addAttribute("note", futureNote);
        redirectAttributes.addFlashAttribute("message", "Future Note updated successfully.");
        redirectAttributes.addAttribute("futureNoteId", futureNote.getId());
        return "redirect:/future-note/{futureNoteId}";
    }

    @PostMapping("/delete/{futureNoteId}")
    public String delete(@PathVariable Long futureNoteId, RedirectAttributes redirectAttributes) {
        FutureNote futureNote = futureNoteService.findById(futureNoteId);
        futureNoteService.delete(futureNote.getId());
        redirectAttributes.addFlashAttribute("message", "Future Note deleted successfully.");
        redirectAttributes.addAttribute("futureBoxId", futureNote.getBoxId());
        return "redirect:/future-boxes/{futureBoxId}";
    }
}

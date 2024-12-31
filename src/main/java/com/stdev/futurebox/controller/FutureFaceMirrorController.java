package com.stdev.futurebox.controller;

import com.stdev.futurebox.domain.FutureFaceMirror;
import com.stdev.futurebox.dto.FutureFaceMirrorCreateForm;
import com.stdev.futurebox.repository.AccessLogRepository;
import com.stdev.futurebox.service.FutureFaceMirrorService;
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
@RequestMapping("/future-face-mirror")
@Slf4j
@RequiredArgsConstructor
public class FutureFaceMirrorController {

    private final FutureFaceMirrorService futureFaceMirrorService;
    private final AccessLogRepository accessLogRepository;

    @GetMapping("/{futureFaceMirrorId}")
    public String futureFaceMirror(@PathVariable Long futureFaceMirrorId, Model model, HttpServletRequest request) {
        FutureFaceMirror futureFaceMirror = futureFaceMirrorService.findById(futureFaceMirrorId);
        model.addAttribute("faceMirror", futureFaceMirror);
        String ipAddress = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        accessLogRepository.saveLog("FACE_MIRROR", futureFaceMirrorId, ipAddress, userAgent);
        log.info("관리자가 미래의 얼굴 거울 id: {}를 조회하였습니다. ip: {}, userAgent: {}", futureFaceMirrorId, ipAddress, userAgent);
        return "future-face-mirror/futureFaceMirror";
    }

    @GetMapping("/new")
    public String createForm() {
        return "future-face-mirror/createFaceMirrorForm";
    }

    @PostMapping("/new")
    public String create(FutureFaceMirrorCreateForm form, Model model, RedirectAttributes redirectAttributes) {
        FutureFaceMirror faceMirror = form.toEntity();
        futureFaceMirrorService.create(faceMirror);
        model.addAttribute("faceMirror", faceMirror);
        redirectAttributes.addFlashAttribute("message", "Future Face Mirror created successfully.");
        redirectAttributes.addAttribute("futureFaceMirrorId", faceMirror.getId());
        return "redirect:/future-face-mirror/{futureFaceMirrorId}";
    }

    @GetMapping("/edit/{futureFaceMirrorId}")
    public String editForm(@PathVariable Long futureFaceMirrorId, Model model) {
        FutureFaceMirror futureFaceMirror = futureFaceMirrorService.findById(futureFaceMirrorId);
        model.addAttribute("faceMirror", futureFaceMirror);
        return "future-face-mirror/editFaceMirrorForm";
    }

    @PostMapping("/edit/{futureFaceMirrorId}")
    public String edit(@PathVariable Long futureFaceMirrorId, FutureFaceMirrorCreateForm form, Model model, RedirectAttributes redirectAttributes) {
        FutureFaceMirror faceMirror = form.toEntity();
        faceMirror.setId(futureFaceMirrorId);
        futureFaceMirrorService.update(faceMirror);
        model.addAttribute("faceMirror", faceMirror);
        redirectAttributes.addFlashAttribute("message", "Future Face Mirror updated successfully.");
        redirectAttributes.addAttribute("futureFaceMirrorId", faceMirror.getId());
        return "redirect:/future-face-mirror/{futureFaceMirrorId}";
    }

    @PostMapping("/delete/{futureFaceMirrorId}")
    public String delete(@PathVariable Long futureFaceMirrorId, RedirectAttributes redirectAttributes) {
        FutureFaceMirror futureFaceMirror = futureFaceMirrorService.findById(futureFaceMirrorId);
        futureFaceMirrorService.delete(futureFaceMirrorId);
        redirectAttributes.addFlashAttribute("message", "Future Face Mirror deleted successfully.");
        redirectAttributes.addAttribute("futureBoxId", futureFaceMirror.getBoxId());
        return "redirect:/future-boxes/{futureBoxId}";
    }


}

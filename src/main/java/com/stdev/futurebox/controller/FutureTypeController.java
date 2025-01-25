package com.stdev.futurebox.controller;

import com.stdev.futurebox.domain.FutureGifticon;
import com.stdev.futurebox.dto.FutureGifticonCreateForm;
import com.stdev.futurebox.service.FutureTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/future-types")
@RequiredArgsConstructor
public class FutureTypeController {

    private final FutureTypeService futureTypeService;

    @GetMapping("")
    public String types(Model model) {
        model.addAttribute("gifticons", futureTypeService.findFutureGifticonsAll());
        return "future-types/types";
    }


    @GetMapping("/gifticons/new")
    public String createGifticonForm() {
        return "future-types/createGifticonForm";
    }

    @PostMapping("/gifticons/new")
    public String createGifticon(@ModelAttribute FutureGifticonCreateForm gifticon, 
                               RedirectAttributes redirectAttributes) {
        futureTypeService.createFutureGifticon(gifticon.toEntity());
        redirectAttributes.addFlashAttribute("message", "기프티콘 타입이 생성되었습니다.");
        return "redirect:/future-types";
    }

    @GetMapping("/gifticons/edit/{id}")
    public String editGifticonForm(@PathVariable Long id, Model model) {
        model.addAttribute("gifticon", futureTypeService.findGifticonById(id));
        return "future-types/editGifticonForm";
    }

    @PostMapping("/gifticons/edit/{id}")
    public String editGifticon(@PathVariable Long id, 
                             @ModelAttribute FutureGifticonCreateForm form, 
                             RedirectAttributes redirectAttributes) {
        FutureGifticon gifticon = form.toEntity();
        gifticon.setId(id);
        futureTypeService.updateFutureGifticon(gifticon);
        redirectAttributes.addFlashAttribute("message", "기프티콘 타입이 수정되었습니다.");
        return "redirect:/future-types";
    }

    @DeleteMapping("/gifticons/{id}")
    public ResponseEntity<Void> deleteGifticon(@PathVariable Long id) {
        futureTypeService.deleteGifticonById(id);
        return ResponseEntity.ok().build();
    }

}

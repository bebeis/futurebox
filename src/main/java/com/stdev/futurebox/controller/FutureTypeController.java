package com.stdev.futurebox.controller;

import com.stdev.futurebox.domain.FutureGifticon;
import com.stdev.futurebox.domain.FutureInvention;
import com.stdev.futurebox.domain.FutureMovie;
import com.stdev.futurebox.dto.FutureGifticonCreateForm;
import com.stdev.futurebox.dto.FutureInventionCreateForm;
import com.stdev.futurebox.dto.FutureMovieCreateForm;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/future-types")
@RequiredArgsConstructor
public class FutureTypeController {

    private final FutureTypeService futureTypeService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("movies", futureTypeService.findFutureMoviesAll());
        model.addAttribute("gifticons", futureTypeService.findFutureGifticonsAll());
        model.addAttribute("inventions", futureTypeService.findFutureInventionsAll());
        return "future-types/futureTypeList";
    }

    @GetMapping("/movies/new")
    public String createMovieForm() {
        return "future-types/createMovieForm";
    }

    @PostMapping("/movies/new")
    public String createMovie(@ModelAttribute FutureMovieCreateForm movie, RedirectAttributes redirectAttributes) {
        futureTypeService.createFutureMovie(movie.toEntity());
        redirectAttributes.addFlashAttribute("message", "영화 타입이 생성되었습니다.");
        return "redirect:/future-types";
    }

    @GetMapping("/movies/edit/{id}")
    public String editMovieForm(@PathVariable Long id, Model model) {
        model.addAttribute("movie", futureTypeService.findMovieById(id));
        return "future-types/editMovieForm";
    }

    @PostMapping("/movies/edit/{id}")
    public String editMovie(@PathVariable Long id, @ModelAttribute FutureMovie movie, RedirectAttributes redirectAttributes) {
        movie.setId(id);
        futureTypeService.updateFutureMovie(movie);
        redirectAttributes.addFlashAttribute("message", "영화 타입이 수정되었습니다.");
        return "redirect:/future-types";
    }

    @DeleteMapping("/movies/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        futureTypeService.deleteMovieById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/gifticons/new")
    public String createGifticonForm() {
        return "future-types/createGifticonForm";
    }

    @PostMapping("/gifticons/new")
    public String createGifticon(@ModelAttribute FutureGifticonCreateForm gifticon, RedirectAttributes redirectAttributes) {
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
    public String editGifticon(@PathVariable Long id, @ModelAttribute FutureGifticon gifticon, RedirectAttributes redirectAttributes) {
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

    @GetMapping("/inventions/new")
    public String createInventionForm() {
        return "future-types/createInventionForm";
    }

    @PostMapping("/inventions/new")
    public String createInvention(@ModelAttribute FutureInventionCreateForm invention, RedirectAttributes redirectAttributes) {
        futureTypeService.createFutureInvention(invention.toEntity());
        redirectAttributes.addFlashAttribute("message", "발명품 타입이 생성되었습니다.");
        return "redirect:/future-types";
    }

    @GetMapping("/inventions/edit/{id}")
    public String editInventionForm(@PathVariable Long id, Model model) {
        model.addAttribute("invention", futureTypeService.findInventionById(id));
        return "future-types/editInventionForm";
    }

    @PostMapping("/inventions/edit/{id}")
    public String editInvention(@PathVariable Long id, @ModelAttribute FutureInvention invention, RedirectAttributes redirectAttributes) {
        invention.setId(id);
        futureTypeService.updateFutureInvention(invention);
        redirectAttributes.addFlashAttribute("message", "발명품 타입이 수정되었습니다.");
        return "redirect:/future-types";
    }

    @DeleteMapping("/inventions/{id}")
    public ResponseEntity<Void> deleteInvention(@PathVariable Long id) {
        futureTypeService.deleteInventionById(id);
        return ResponseEntity.ok().build();
    }
}

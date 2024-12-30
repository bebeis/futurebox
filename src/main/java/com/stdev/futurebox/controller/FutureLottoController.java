package com.stdev.futurebox.controller;


import com.stdev.futurebox.domain.FutureLotto;
import com.stdev.futurebox.dto.FutureLottoCreateForm;
import com.stdev.futurebox.service.FutureLottoService;
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
@RequestMapping("/future-lotto")
@Slf4j
@RequiredArgsConstructor
public class FutureLottoController {

    private final FutureLottoService futureLottoService;

    @GetMapping("/{futureLottoId}")
    public String futureLotto(@PathVariable Long futureLottoId, Model model) {
        log.info("futureLottoId = {}", futureLottoId);
        FutureLotto futureLotto = futureLottoService.findById(futureLottoId);
        model.addAttribute("lotto", futureLotto);
        return "future-lotto/futureLotto";
    }

    @GetMapping("/new")
    public String createForm() {
        return "future-lotto/createLottoForm";
    }

    @PostMapping("/new")
    public String create(FutureLottoCreateForm form, Model model, RedirectAttributes redirectAttributes) {
        FutureLotto futureLotto = form.toEntity();
        futureLottoService.create(futureLotto);
        model.addAttribute("lotto", futureLotto);
        redirectAttributes.addFlashAttribute("message", "Future Lotto created successfully.");
        redirectAttributes.addAttribute("futureLottoId", futureLotto.getId());
        log.info("return to futureLotto ID = {}", futureLotto.getId());
        return "redirect:/future-lotto/{futureLottoId}";
    }

    @GetMapping("/edit/{futureLottoId}")
    public String editForm(@PathVariable Long futureLottoId, Model model) {
        FutureLotto futureLotto = futureLottoService.findById(futureLottoId);
        model.addAttribute("lotto", futureLotto);
        return "future-lotto/editLottoForm";
    }

    @PostMapping("/edit/{futureLottoId}")
    public String edit(@PathVariable Long futureLottoId, FutureLottoCreateForm form, Model model, RedirectAttributes redirectAttributes) {
        FutureLotto futureLotto = form.toEntity();
        futureLotto.setId(futureLottoId);
        futureLottoService.update(futureLotto);
        model.addAttribute("lotto", futureLotto);
        redirectAttributes.addFlashAttribute("message", "Future Lotto updated successfully.");
        redirectAttributes.addAttribute("futureLottoId", futureLotto.getId());
        return "redirect:/future-lotto/{futureLottoId}";
    }

    @PostMapping("/delete/{futureLottoId}")
    public String delete(@PathVariable Long futureLottoId, RedirectAttributes redirectAttributes) {
        FutureLotto futureLotto = futureLottoService.findById(futureLottoId);
        futureLottoService.delete(futureLottoId);
        redirectAttributes.addFlashAttribute("message", "Future Lotto deleted successfully.");
        redirectAttributes.addAttribute("futureBoxId", futureLotto.getBoxId());
        return "redirect:/future-boxes/{futureBoxId}";
    }
}

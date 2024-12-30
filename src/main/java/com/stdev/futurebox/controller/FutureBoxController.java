package com.stdev.futurebox.controller;

import com.stdev.futurebox.domain.FutureBox;
import com.stdev.futurebox.dto.FutureBoxCreateForm;
import com.stdev.futurebox.service.FutureBoxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
@RequestMapping("/future-boxes")
@RequiredArgsConstructor
public class FutureBoxController {

    private final FutureBoxService futureBoxService;

    @GetMapping
    public String list(@RequestParam(defaultValue = "1") int page,
                      @RequestParam(defaultValue = "created_at") String sortField,
                      @RequestParam(defaultValue = "DESC") String sortDirection,
                      Model model) {
        List<FutureBox> allBoxes = futureBoxService.findAll(sortField, sortDirection);
        
        int pageSize = 20;
        int totalItems = allBoxes.size();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);
        
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, totalItems);
        
        List<FutureBox> pagedResults = allBoxes.subList(start, end);
        
        model.addAttribute("futureBoxes", pagedResults);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("reverseSortDirection", sortDirection.equals("ASC") ? "DESC" : "ASC");
        return "future-boxes/futureBoxList";
    }

    @GetMapping("/{futureBoxId}")
    public String futureBox(@PathVariable Long futureBoxId, Model model) {
        model.addAttribute("futureBox", futureBoxService.findById(futureBoxId));
        return "future-boxes/futureBox";
    }

    @GetMapping("/new")
    public String createForm() {
        return "future-boxes/createFutureBoxForm";
    }

    @PostMapping("/new")
    public String create(@ModelAttribute FutureBoxCreateForm form, RedirectAttributes redirectAttributes) {
        FutureBox formEntity = form.toEntity();
        futureBoxService.create(formEntity);
        redirectAttributes.addAttribute("futureBoxId", formEntity.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/future-boxes/{futureBoxId}";
    }

    @GetMapping("/search")
    public String search(@RequestParam(required = false) String searchType,
                         @RequestParam(required = false) String keyword,
                         @RequestParam(required = false) Boolean isOpened,
                         @RequestParam(defaultValue = "1") int page,
                         Model model) {
        List<FutureBox> searchResults = new ArrayList<>();
        
        if (searchType != null && keyword != null && !keyword.trim().isEmpty()) {
            switch (searchType) {
                case "id":
                    try {
                        Long id = Long.parseLong(keyword);
                        FutureBox box = futureBoxService.findById(id);
                        if (box != null) {
                            searchResults.add(box);
                        }
                    } catch (NumberFormatException e) {
                        // ID가 숫자 형식이 아닌 경우 무시
                        log.warn("ID가 숫자 형식이 아닙니다.");
                    }
                    break;
                case "uuid":
                    try {
                        UUID uuid = UUID.fromString(keyword);
                        FutureBox box = futureBoxService.findByUuid(uuid);
                        if (box != null) {
                            searchResults.add(box);
                        }
                    } catch (IllegalArgumentException e) {
                        // UUID 형식이 아닌 경우 무시
                        log.warn("UUID 형식이 아닙니다.");
                    }
                    break;
                case "sender":
                    searchResults = futureBoxService.findBySender(keyword);
                    break;
                case "receiver":
                    searchResults = futureBoxService.findByReceiver(keyword);
                    break;
                
            }
        } else {
            searchResults = futureBoxService.findAll();
        }
        
        // 개봉 여부로 필터링
        if (isOpened != null) {
            searchResults = searchResults.stream()
                    .filter(box -> box.getOpen().equals(isOpened))
                    .collect(Collectors.toList());
        }
        
        // 페이징 처리
        int pageSize = 20;
        int totalItems = searchResults.size();
        int totalPages = Math.max(1, (int) Math.ceil((double) totalItems / pageSize));
        
        // 현재 페이지가 총 페이지 수를 초과하지 않도록 조정
        page = Math.min(page, totalPages);
        
        List<FutureBox> pagedResults;
        if (totalItems > 0) {
            int start = (page - 1) * pageSize;
            int end = Math.min(start + pageSize, totalItems);
            pagedResults = searchResults.subList(start, end);
        } else {
            pagedResults = new ArrayList<>();
        }
        
        model.addAttribute("searchType", searchType);
        model.addAttribute("keyword", keyword);
        model.addAttribute("isOpened", isOpened);
        model.addAttribute("futureBoxes", pagedResults);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        return "future-boxes/futureBoxList";
    }

    @PostMapping("/delete")
    public String deleteSelected(@RequestParam("selectedIds") List<Long> ids, RedirectAttributes redirectAttributes) {
        for (Long id : ids) {
            futureBoxService.deleteById(id);
        }
        redirectAttributes.addAttribute("deleteStatus", true);
        return "redirect:/future-boxes";
    }

}

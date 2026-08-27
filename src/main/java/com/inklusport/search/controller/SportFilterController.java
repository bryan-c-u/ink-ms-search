git adpackage com.inklusport.search.controller;

import com.inklusport.search.dto.PageResponse;
import com.inklusport.search.dto.SportFilterResponse;
import com.inklusport.search.service.SportSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/search/sports")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class SportFilterController {

    private final SportSearchService sportSearchService;

    @GetMapping
    public ResponseEntity<PageResponse<SportFilterResponse>> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String difficulty,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        return ResponseEntity.ok(sportSearchService.search(name, difficulty, isActive, sortBy, direction, page, size));
    }
}

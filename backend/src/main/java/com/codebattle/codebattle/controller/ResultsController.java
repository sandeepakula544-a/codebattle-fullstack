package com.codebattle.codebattle.controller;

import com.codebattle.codebattle.dto.ResultsResponse;
import com.codebattle.codebattle.service.ResultsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/results")
public class ResultsController {

    private final ResultsService resultsService;

    public ResultsController(ResultsService resultsService) {
        this.resultsService = resultsService;
    }

    @GetMapping("/{roomCode}")
    public ResponseEntity<ResultsResponse> getResults(@PathVariable String roomCode) {
        return ResponseEntity.ok(resultsService.getResults(roomCode.toUpperCase()));
    }
}

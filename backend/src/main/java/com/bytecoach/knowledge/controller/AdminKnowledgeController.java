package com.bytecoach.knowledge.controller;

import com.bytecoach.common.api.Result;
import com.bytecoach.knowledge.dto.KnowledgeImportRequest;
import com.bytecoach.knowledge.vo.KnowledgeDocVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/knowledge")
@RequiredArgsConstructor
public class AdminKnowledgeController {

    private final com.bytecoach.knowledge.service.KnowledgeService knowledgeService;

    @PostMapping("/import")
    public Result<KnowledgeDocVO> importDoc(@Valid @RequestBody KnowledgeImportRequest request) {
        return Result.success(knowledgeService.importSeed(request));
    }

    @PostMapping("/rechunk/{docId}")
    public Result<KnowledgeDocVO> rechunk(@PathVariable Long docId) {
        return Result.success(knowledgeService.rechunk(docId));
    }

    @PostMapping("/reindex/{docId}")
    public Result<KnowledgeDocVO> reindex(@PathVariable Long docId) {
        return Result.success(knowledgeService.reindex(docId));
    }
}

package com.bytecoach.knowledge.controller;

import com.bytecoach.common.api.Result;
import com.bytecoach.knowledge.dto.KnowledgeImportRequest;
import com.bytecoach.knowledge.vo.KnowledgeDocVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/knowledge")
public class AdminKnowledgeController {

    @PostMapping("/import")
    public Result<KnowledgeDocVO> importDoc(@Valid @RequestBody KnowledgeImportRequest request) {
        return Result.success(KnowledgeDocVO.builder()
                .id(3L)
                .title(request.getTitle())
                .status("draft")
                .summary("文档导入成功，待切分和索引。")
                .build());
    }

    @PostMapping("/rechunk/{docId}")
    public Result<String> rechunk(@PathVariable Long docId) {
        return Result.success("doc " + docId + " rechunk triggered");
    }

    @PostMapping("/reindex/{docId}")
    public Result<String> reindex(@PathVariable Long docId) {
        return Result.success("doc " + docId + " reindex triggered");
    }
}

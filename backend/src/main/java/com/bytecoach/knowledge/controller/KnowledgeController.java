package com.bytecoach.knowledge.controller;

import com.bytecoach.common.api.Result;
import com.bytecoach.knowledge.dto.KnowledgeSearchRequest;
import com.bytecoach.knowledge.vo.KnowledgeDocVO;
import com.bytecoach.knowledge.vo.KnowledgeSearchVO;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/knowledge")
public class KnowledgeController {

    @GetMapping("/list")
    public Result<List<KnowledgeDocVO>> list() {
        return Result.success(List.of(
                KnowledgeDocVO.builder().id(1L).title("Spring 核心笔记").status("vectorized").summary("IOC/AOP/MVC 速查").build(),
                KnowledgeDocVO.builder().id(2L).title("JVM 面试手册").status("parsed").summary("类加载、GC、内存模型").build()
        ));
    }

    @PostMapping("/search")
    public Result<KnowledgeSearchVO> search(@Valid @RequestBody KnowledgeSearchRequest request) {
        return Result.success(KnowledgeSearchVO.builder()
                .query(request.getQuery())
                .references(List.of(
                        KnowledgeSearchVO.Reference.builder()
                                .docId(1L)
                                .chunkId(101L)
                                .docTitle("Spring 核心笔记")
                                .snippet("AOP 的底层实现通常依赖 JDK 动态代理或 CGLIB。")
                                .build()
                ))
                .build());
    }
}


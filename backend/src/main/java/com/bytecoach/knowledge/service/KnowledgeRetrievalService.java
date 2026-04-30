package com.bytecoach.knowledge.service;

import com.bytecoach.knowledge.vo.KnowledgeSearchVO;
import java.util.List;

public interface KnowledgeRetrievalService {
    List<KnowledgeSearchVO.Reference> searchReferences(String query, int limit);
}

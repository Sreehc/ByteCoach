package com.bytecoach.wrong.service;

import com.bytecoach.wrong.dto.WrongMasteryUpdateRequest;
import com.bytecoach.wrong.vo.WrongQuestionVO;
import java.util.List;

public interface WrongService {

    List<WrongQuestionVO> list(Long userId);

    WrongQuestionVO detail(Long userId, Long id);

    void updateMastery(Long userId, Long id, WrongMasteryUpdateRequest request);

    void delete(Long userId, Long id);
}

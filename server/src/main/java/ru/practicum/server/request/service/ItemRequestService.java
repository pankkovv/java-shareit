package ru.practicum.server.request.service;

import ru.practicum.server.request.dto.ItemRequestDto;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface ItemRequestService {
    ItemRequestDto addRequest(Long userId, ItemRequestDto itemRequestDto);

    List<ItemRequestDto> getRequest(Long userId);

    List<ItemRequestDto> getRequestAll(Long userId, Integer from, Integer size);

    ItemRequestDto getRequestId(Long userId, Long requestId);

}

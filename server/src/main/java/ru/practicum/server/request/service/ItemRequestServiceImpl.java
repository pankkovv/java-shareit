package ru.practicum.server.request.service;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.server.exception.NotStateException;
import ru.practicum.server.messages.ExceptionMessages;
import ru.practicum.server.messages.LogMessages;
import ru.practicum.server.exception.NotRequestException;
import ru.practicum.server.item.mapper.ItemMap;
import ru.practicum.server.item.repository.ItemRepository;
import ru.practicum.server.request.dto.ItemRequestDto;
import ru.practicum.server.request.mapper.ItemRequestMap;
import ru.practicum.server.request.repository.ItemRequestRepository;
import ru.practicum.server.user.model.User;
import ru.practicum.server.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@NoArgsConstructor
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService {
    @Autowired
    private ItemRequestRepository itemRequestRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserService userService;

    @Override
    public ItemRequestDto addRequest(Long userId, ItemRequestDto itemRequestDto) {
        User user = validationExistUser(userId);
        itemRequestDto.setRequestor(userId);
        if (itemRequestDto.getCreated() == null) {
            itemRequestDto.setCreated(LocalDateTime.now());
        }
        log.debug(LogMessages.ADD_REQUEST.label, itemRequestDto);
        return ItemRequestMap.mapToItemRequestDto(itemRequestRepository.save(ItemRequestMap.mapToItemRequest(itemRequestDto, user)));
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemRequestDto> getRequest(Long userId) {
        validationExistUser(userId);
        List<ItemRequestDto> listItemRequestDto = ItemRequestMap.mapToItemRequestDto(itemRequestRepository.findItemRequestByRequestor_Id(userId));
        for (ItemRequestDto item : listItemRequestDto) {
            item.setItems(ItemMap.mapToItemDto(itemRepository.findItemByRequest_Id(item.getId())));
        }
        log.debug(LogMessages.GET_REQUEST.label, userId);
        return listItemRequestDto;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemRequestDto> getRequestAll(Long userId, Integer from, Integer size) {
        if (from == null || size == null) {
            return List.of();
        } else if (from >= 0 && size > 0) {
            Pageable page = PageRequest.of(from / size, size);
            validationExistUser(userId);
            List<ItemRequestDto> listItemRequestDto = ItemRequestMap.mapToItemRequestDto(itemRequestRepository.findItemRequestByIdNotOrderByCreatedDesc(userId, page));
            for (ItemRequestDto item : listItemRequestDto) {
                item.setItems(ItemMap.mapToItemDto(itemRepository.findItemByRequest_Id(item.getId())));
            }
            log.debug(LogMessages.GET_REQUEST_ALL.label, userId);
            return listItemRequestDto;
        } else {
            throw new NotStateException(ExceptionMessages.FROM_NOT_POSITIVE.label);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public ItemRequestDto getRequestId(Long userId, Long requestId) {
        validationExistUser(userId);
        ItemRequestDto itemRequestDto = ItemRequestMap.mapToItemRequestDto(itemRequestRepository.findById(requestId).orElseThrow(() -> new NotRequestException(ExceptionMessages.NOT_FOUND_REQUEST.label)));
        itemRequestDto.setItems(ItemMap.mapToItemDto(itemRepository.findItemByRequest_Id(requestId)));
        log.debug(LogMessages.GET_REQUEST_ID.label, requestId);
        return itemRequestDto;
    }

    User validationExistUser(Long userId) {
        return userService.findById(userId);
    }
}

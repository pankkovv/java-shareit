package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotRequestException;
import ru.practicum.shareit.exception.NotStateException;
import ru.practicum.shareit.item.mapper.ItemMap;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.messages.ExceptionMessages;
import ru.practicum.shareit.messages.LogMessages;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMap;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;
    private final UserService userService;

    @Override
    public ItemRequestDto addRequest(Long userId, ItemRequestDto itemRequestDto) {
        User user = validationExistUser(userId);
        itemRequestDto.setRequestor(userId);
        itemRequestDto.setCreated(LocalDateTime.now());
        log.debug(LogMessages.ADD_REQUEST.label, itemRequestDto);
        return ItemRequestMap.mapToItemRequestDto(itemRequestRepository.save(ItemRequestMap.mapToItemRequest(itemRequestDto, user)));
    }

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

    @Override
    public List<ItemRequestDto> getRequestAll(Long userId, Integer from, Integer size) {
        if (from == null || size == null) {
            return List.of();
        } else if (from >= 0) {
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

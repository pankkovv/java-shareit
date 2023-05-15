package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.mapper.ItemMap;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMap;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.mapper.UserMap;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService{
    private final ItemRequestRepository itemRequestRepository;
    private final UserService userService;
    private final ItemRepository itemRepository;
    @Override
    public ItemRequestDto addRequest(Long userId, ItemRequestDto itemRequestDto) {
        User user = UserMap.mapToUser(userService.getByUserId(userId));
        itemRequestDto.setRequestor(userId);
        itemRequestDto.setStart(LocalDateTime.now());
        return ItemRequestMap.mapToItemRequestDto(itemRequestRepository.save(ItemRequestMap.mapToItemRequest(itemRequestDto, user)));
    }

    @Override
    public List<ItemRequestDto> getRequest(Long userId) {
        List<ItemRequestDto> listItemRequest = ItemRequestMap.mapToUserDto(itemRequestRepository.findItemRequestByRequestor_Id(userId));
        for(ItemRequestDto item : listItemRequest){
            item.setResponse(ItemMap.mapToItemResponseDto(itemRepository.findItemByRequest_Id(item.getId())));
        }
         return listItemRequest;
    }

    @Override
    public List<ItemRequestDto> getRequestAll(Long userId, Integer from, Integer size) {
        Pageable page = PageRequest.of(from > 0 ? from / size : 0, size);
        return ItemRequestMap.mapToUserDto(itemRequestRepository.findItemRequestByIdNotOrderByStartDesc(userId, page));
    }

    @Override
    public ItemRequestDto getRequestId(Long requestId) {
        ItemRequestDto itemRequestDto = ItemRequestMap.mapToItemRequestDto(itemRequestRepository.findById(requestId).orElse(null));
        itemRequestDto.setResponse(ItemMap.mapToItemResponseDto(itemRepository.findItemByRequest_Id(requestId)));
        return itemRequestDto;
    }

}

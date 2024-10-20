package com.smirnov.services;

import com.smirnov.dto.request.RequestCreateDTO;
import com.smirnov.dto.response.RequestDTO;
import com.smirnov.entity.Request;
import com.smirnov.entity.User;
import com.smirnov.exception.EntityNotFoundException;
import com.smirnov.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.smirnov.enums.RequestStatus.ACCEPTED;
import static com.smirnov.enums.RequestStatus.DRAFT;
import static com.smirnov.enums.RequestStatus.REJECTED;
import static com.smirnov.enums.RequestStatus.SENT;
import static org.springframework.data.domain.Sort.by;

/**
 * Сервисный слой для работы с заявками.
 */
@Service
@Transactional
public class RequestService {
    private final RequestRepository requestRepository;
    private final UserService userService;

    /**
     * Количество элементов для пагинации
     */
    private final byte size;

    public RequestService(RequestRepository requestRepository, UserService userService, @Value("${page.size}") byte size) {
        this.requestRepository = requestRepository;
        this.userService = userService;
        this.size = size;
    }

    /**
     * Отправляет заявку на рассмотрение по ее идентификатору.
     */
    public void submitRequestReview(Long id) {
        Request request = requestRepository.findByIdAndStatus(id, DRAFT).orElseThrow(() -> new EntityNotFoundException(Request.class, id));
        request.setStatus(SENT);
    }

    /**
     * Принимает заявку по ее идентификатору.
     *
     * @param id Идентификатор заявки.
     */
    public void acceptRequest(Long id) {
        Request request = requestRepository.findByIdAndStatus(id, SENT).orElseThrow(() -> new EntityNotFoundException(Request.class, id));
        request.setStatus(ACCEPTED);
    }

    /**
     * Отклоняет заявку по ее идентификатору.
     *
     * @param id Идентификатор заявки.
     */
    public void rejectRequest(Long id) {
        Request request = requestRepository.findByIdAndStatus(id, SENT).orElseThrow(() -> new EntityNotFoundException(Request.class, id));
        request.setStatus(REJECTED);
    }

    public RequestDTO getRequest(Long id) {
        Request request = requestRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Request.class, id));
        return mapRequestDTO(request);
    }

    /**
     * Создает новую заявку.
     * @param requestCreateDTO информация о заявке
     * @return Идентификатор созданной заявки
     */
    public Long createRequest(RequestCreateDTO requestCreateDTO) {
        final User user = userService.getUserById(requestCreateDTO.getUserId());
        Request request = new Request();
        request.setMessage(requestCreateDTO.getMessage());
        request.setUser(user);
        request.setStatus(DRAFT);
        request.setCreatedAt(LocalDateTime.now());
        return requestRepository.save(request).getId();
    }

    /**
     * Возвращает частично список заявок.
     *
     * @return Список заявок
     */
    public Page<RequestDTO> getPageRequest(Pageable pageable, Sort.Direction sorting) {
        return requestRepository.findAll(PageRequest.of(pageable.getPageNumber(), size,
                        by(sorting,  Request.Fields.createdAt)))
                .map(this::mapRequestDTO);
    }

    private RequestDTO mapRequestDTO(Request request){
        return RequestDTO.builder()
                .userLogin(request.getUser().getLogin())
                .message(request.getMessage())
                .status(request.getStatus().toString())
                .createdAt(request.getCreatedAt())
                .build();

    }
}

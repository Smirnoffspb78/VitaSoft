package com.smirnov.services;

import com.smirnov.dto.create.RequestCreateDTO;
import com.smirnov.dto.get.RequestDTO;
import com.smirnov.entity.Request;
import com.smirnov.entity.User;
import com.smirnov.entity.UserRole;
import com.smirnov.enums.RequestStatus;
import com.smirnov.enums.UserRight;
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
import static com.smirnov.enums.UserRight.ROLE_OPERATOR;
import static com.smirnov.enums.UserRight.ROLE_USER;
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
    public void submitRequestReview(Long id, Integer userId) {
        requestRepository.findByIdAndStatusAndUser_id(id, DRAFT, userId)
                .orElseThrow(() -> new EntityNotFoundException(Request.class, id))
                .setStatus(SENT);
    }

    /**
     * Принимает заявку по ее идентификатору.
     *
     * @param id Идентификатор заявки.
     */
    public void acceptRequest(Long id) {
        getRequestIdStatus(id, SENT).setStatus(ACCEPTED);
    }

    /**
     * Отклоняет заявку по ее идентификатору.
     *
     * @param id Идентификатор заявки.
     */
    public void rejectRequest(Long id) {
        getRequestIdStatus(id, SENT).setStatus(REJECTED);
    }

    /**
     * Возвращает заявку по ее идентификатору.
     *
     * @param id Идентификатор заявки
     * @return Заявка
     */
    @Transactional(readOnly = true)
    public RequestDTO getRequest(Long id) {
        Request request = requestRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Request.class, id));
        return mapRequestDTO(request, ROLE_OPERATOR);
    }

    /**
     * Создает новую заявку.
     *
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
     * Редактирует заявку по ее идентификатору.
     *
     * @param id               Идентификатор заявки
     * @param requestCreateDTO заявка
     */
    public void editDraftRequest(Long id, RequestCreateDTO requestCreateDTO) {
        requestRepository.findByIdAndStatusAndUser_id(id, DRAFT, requestCreateDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException(Request.class, id))
                .setMessage(requestCreateDTO.getMessage());
    }

    /**
     * Возвращает список заявок, отправленных на рассмотрение.
     *
     * @return Список заявок
     */
    @Transactional(readOnly = true)
    public Page<RequestDTO> getPageRequest(int pageNumber, Sort.Direction sorting, String name) {
        Page<Request> requestPage = name == null ? requestRepository.findByStatus(SENT, PageRequest.of(pageNumber, size,
                by(sorting, Request.Fields.createdAt))) :
                requestRepository.findByStatusAndUser_nameContainingIgnoreCase(SENT, name, PageRequest.of(pageNumber, size,
                        by(sorting, Request.Fields.createdAt)));
        return requestPage.map(request -> mapRequestDTO(request, ROLE_OPERATOR));
    }

    /**
     * Возвращает частично заявки пользователя по его идентификатору.
     *
     * @return Список заявок
     */
    @Transactional(readOnly = true)
    public Page<RequestDTO> getAllByUser(int pageNumber, Sort.Direction sorting, Integer userId) {
        return requestRepository
                .findByUser_id(userId, PageRequest.of(pageNumber, size,
                        by(sorting, Request.Fields.createdAt)))
                .map(request -> mapRequestDTO(request, ROLE_USER));
    }

    private RequestDTO mapRequestDTO(Request request, UserRight userRight) {
        User user = request.getUser();
        return RequestDTO.builder()
                .userLogin(user.getLogin())
                .userName(user.getName())
                .message(userRight.getTextRequest(request.getMessage()))
                .status(request.getStatus().toString())
                .createdAt(request.getCreatedAt())
                .build();

    }

    private Request getRequestIdStatus(Long id, RequestStatus requestStatus) {
        return requestRepository.findByIdAndStatus(id, requestStatus)
                .orElseThrow(() -> new EntityNotFoundException(Request.class, id));
    }
}

package com.smirnov.controller;

import com.smirnov.dto.create.RequestCreateDTO;
import com.smirnov.dto.get.RequestDTO;
import com.smirnov.services.RequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static java.util.Objects.isNull;

/**
 * Контроллер для работы с заявками.
 */
@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
@Slf4j
public class RequestController {

    /**
     * Сервисный слой для работы с заявками.
     */
    private final RequestService requestService;


    /**
     * Позволяет посмотреть информацию о заявке по ее идентификатору.
     *
     * @param id Идентификатор заявки
     * @return Заявка
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_OPERATOR')")
    public RequestDTO watchRequest(@PathVariable("id") Long id) {
        log.info("GET: /requests/{}", id);
        RequestDTO requestDTO = requestService.getRequest(id);
        log.info("{}. Получена заявка с id {}.", HttpStatus.OK, id);
        return requestDTO;
    }

    /**
     * Возвращает страницу с заявками, которые направлены на рассмотрение.
     * Уровень доступа:
     * - OPERATOR
     *
     * @return Список DTO записей
     */
    @GetMapping
    @PreAuthorize("hasRole('ROLE_OPERATOR')")
    public Page<RequestDTO> getPageRequest(@RequestParam("pageNumber") int pageNumber,
                                           @RequestParam("sorting") Sort.Direction sorting,
                                           @RequestParam(value = "name", required = false) String name) {
        if (isNull(name)) {
            log.info("GET: /requests?pageNumber={}&sorting={}", pageNumber, sorting);
        } else {
            log.info("GET: /requests?pageNumber={}&sorting={}&name={}", pageNumber, sorting, name);
        }
        Page<RequestDTO> pageRequest = requestService.getPageRequest(pageNumber, sorting, name);
        log.info("{}. Получена страница № {} с заявками на рассмотрение.", HttpStatus.OK, pageNumber);
        return pageRequest;
    }

    /**
     * Возвращает все заявки пользователя по его идентификатору.
     * Уровень доступа:
     * -USER
     *
     * @return Список DTO записей.
     */
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ROLE_USER') and authentication.principal.id == #userId")
    public Page<RequestDTO> getPageByUser(@PathVariable("userId") Integer userId,
                                          @RequestParam("pageNumber") int pageNumber,
                                          @RequestParam("sorting") Sort.Direction sorting) {
        log.info("GET: /requests/user/{}?pageNumber={}&sorting={}", userId, pageNumber, sorting);
        Page<RequestDTO> pageRequest = requestService.getAllByUser(pageNumber, sorting, userId);
        log.info("{}. Получена страница c заявками № {} пользователя с id {}.", HttpStatus.OK, pageNumber, userId);
        return pageRequest;
    }

    /**
     * Создает новую заявку
     * Уровень доступа:
     * - USER
     *
     * @param requestCreateDTO заявка
     * @return Номер новой заявки
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_USER') and authentication.principal.id == #requestCreateDTO.userId")
    public Long createRequest(@RequestBody @Valid RequestCreateDTO requestCreateDTO) {
        log.info("POST: /requests");
        Long requestId = requestService.createRequest(requestCreateDTO);
        log.info("{}. Заявка с id {} создана.", HttpStatus.CREATED, requestId);
        return requestId;
    }

    /**
     * Отправляет заявку на рассмотрение по ее идентификатору.
     * Уровень доступа:
     * - USER, чей id совпадает с userId в заявке
     *
     * @param id Идентификатор заявки.
     */
    @PutMapping("/{id}/submit-review")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_USER') and authentication.principal.id == #userId")
    public void submitReview(@PathVariable("id") Long id, @RequestParam(name = "userId") Integer userId) {
        log.info("POST: /requests/{}/submit-review/?userid={}", id, userId);
        requestService.submitRequestReview(id, userId);
        log.info("{}. Заявка с id {} отправлена на рассмотрение.", HttpStatus.NO_CONTENT, id);
    }

    /**
     * Принимает заявку по ее идентификатору.
     * Уровень доступа:
     * - OPERATOR
     *
     * @param id Идентификатор заявки.
     */
    @PutMapping(value = "/{id}/accept")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_OPERATOR')")
    public void acceptRequest(@PathVariable("id") Long id) {
        log.info("POST: /requests/{}/accept", id);
        requestService.acceptRequest(id);
        log.info("{}. Заявка с id {} принята", HttpStatus.NO_CONTENT, id);
    }

    /**
     * Отклоняет заявку по ее идентификатору.
     * Уровень доступа:
     * - OPERATOR
     *
     * @param id Идентификатор заявки.
     */
    @PutMapping(value = "/{id}/reject")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_OPERATOR')")
    public void rejectRequest(@PathVariable("id") Long id) {
        log.info("POST: /requests/{}/reject", id);
        requestService.rejectRequest(id);
        log.info("{}. Заявка с id {} отклонена", HttpStatus.NO_CONTENT, id);
    }

    /**
     * Редактирует заявку пользователя.
     * Уровень доступа:
     * - USER, чей id совпадает с userId в заявке
     *
     * @param id               Идентификатор заявки
     * @param requestCreateDTO Новая заявка
     */
    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_USER') and authentication.principal.id == #requestCreateDTO.userId")
    public void editDraftRequest(@PathVariable("id") Long id, @RequestBody @Valid RequestCreateDTO requestCreateDTO) {
        log.info("POST: /requests/{}", id);
        requestService.editDraftRequest(id, requestCreateDTO);
        log.info("{}. Заявка с id {} изменена", HttpStatus.NO_CONTENT, id);
    }
}

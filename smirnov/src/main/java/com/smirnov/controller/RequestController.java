package com.smirnov.controller;

import com.smirnov.dto.request.RequestCreateDTO;
import com.smirnov.dto.response.RequestDTO;
import com.smirnov.services.RequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
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
     * Позволяет посмотреть информацию о заявке.
     *
     * @param id Идентификатор заявки
     * @return Заявка
     */
    @GetMapping(value = "/{id}")
    public RequestDTO watchRequest(@PathVariable("id") Long id) {
        log.info("POST: /{}", id);
        RequestDTO requestDTO = requestService.getRequest(id);
        log.info("{}. Получена заявка с id {}", HttpStatus.OK, id);
        return requestDTO;
    }

    /**
     * Возвращает страницу с заявками.
     *
     * @return Список DTO записей
     */
    @GetMapping
    public Page<RequestDTO> getPageRequest(@RequestParam("page") int page,
                                           @RequestParam("sorting") Sort.Direction sorting,
                                           Pageable pageable) {
        log.info("GET: /file?page={}", page);
        Page<RequestDTO> pageFiles = requestService.getPageRequest(pageable, sorting);
        log.info("{}. Получена страница {} размером {} элементов", HttpStatus.OK, pageable.getPageNumber(), pageable.getPageSize());
        return pageFiles;
    }

    /**
     * Создает новую заявку
     *
     * @param requestCreateDTO заявка
     * @param userId           Идентификатор пользователя
     * @return Номер новой заявки
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long createRequest(@RequestBody @Valid RequestCreateDTO requestCreateDTO,
                              @RequestParam(name = "userId") Integer userId) {
        log.info("POST: /requests?userId={}", userId);
        Long requestId = requestService.createRequest(requestCreateDTO);
        log.info("{}. Заявка с id {} создана", HttpStatus.CREATED, requestId);
        return requestId;
    }

    /**
     * Отправляет заявку на рассмотрение.
     *
     * @param id Идентификатор заявки.
     */
    @PutMapping(value = "/{id}/submit-review")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void submitReview(@PathVariable("id") Long id) {
        log.info("POST: /requests/{}/submit-review", id);
        requestService.submitRequestReview(id);
        log.info("{}. Заявка с id {} отправлена на рассмотрение", HttpStatus.NO_CONTENT, id);
    }

    /**
     * Отправляет заявку на рассмотрение.
     *
     * @param id Идентификатор заявки.
     */
    @PutMapping(value = "/{id}/accept")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void acceptRequest(@PathVariable("id") Long id) {
        log.info("POST: /requests/{}/accept", id);
        requestService.acceptRequest(id);
        log.info("{}. Заявка с id {} принята", HttpStatus.NO_CONTENT, id);
    }

    /**
     * Отправляет заявку на рассмотрение.
     *
     * @param id Идентификатор заявки.
     */
    @PutMapping(value = "/{id}/reject")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void rejectRequest(@PathVariable("id") Long id) {
        log.info("POST: /requests/{}/reject", id);
        requestService.acceptRequest(id);
        log.info("Заявка с id {} отклонена", id);
    }
}

package com.calculate.controller;

import com.calculate.common.ErrorResource;
import com.calculate.domain.Payment;
import com.calculate.dto.DivisionPaymentDto;
import com.calculate.dto.PaymentDto;
import com.calculate.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final ModelMapper modelMapper;

    private final String room = "X-ROOM-ID";
    private final String user = "X-USER-ID";

    @PostMapping
    public ResponseEntity createPayment(@RequestHeader(room) @NotEmpty String roomId, @RequestHeader(user) @Min(0) Long userId, @RequestBody @Valid PaymentDto.request paymentDto, Errors errors) {
        if (errors.hasErrors()) {
            return badRequest(errors);
        }

        Payment createdPayment = paymentService.createPayment(paymentDto.getTotalAmount(), paymentDto.getDivisionCnt(),userId, roomId );

        WebMvcLinkBuilder webMvcLinkBuilder = linkTo(PaymentController.class).slash(createdPayment.getToken());
        URI createUri =webMvcLinkBuilder.toUri();


        Map<String, String> map = new HashMap<>();
        map.put("token", createdPayment.getToken());


        return ResponseEntity.created(createUri).body(map);
    }

    @PutMapping("/{token}")
    public ResponseEntity payment(@RequestHeader(room) @NotEmpty String roomId, @RequestHeader(user) @Min(0) Long userId, @PathVariable String token) {
        int payment = paymentService.payment(token, roomId, userId);
        Map<String, Integer>  map = new HashMap<>();
        map.put("amount", payment);
        return ResponseEntity.ok(map);
    }

    @GetMapping("/{token}")
    public ResponseEntity getPayment(@RequestHeader(user) @Min(0) Long userId, @PathVariable String token) {

        Payment payment = paymentService.findPayment(token, userId);

        PaymentDto.response response = modelMapper.map(payment, PaymentDto.response.class);

        response.setDivisionPaymentDtos(payment.getDivisionPayments().stream()
                .map(divisionPayment -> modelMapper.map(divisionPayment, DivisionPaymentDto.Response.class))
                .collect(Collectors.toList()));

        return ResponseEntity.ok(response);
    }

    private ResponseEntity badRequest(Errors errors) {
        return ResponseEntity.badRequest().body(new ErrorResource(errors));
    }
}

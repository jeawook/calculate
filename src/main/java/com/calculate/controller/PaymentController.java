package com.calculate.controller;

import com.calculate.common.ErrorResource;
import com.calculate.domain.Payment;
import com.calculate.dto.PaymentDto;
import com.calculate.resource.PaymentResource;
import com.calculate.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

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
    public ResponseEntity createPayment(@RequestHeader(room) String roomId, @RequestHeader(user) Integer userId, @RequestBody @Valid PaymentDto paymentDto, Errors errors) {
        if (errors.hasErrors()) {
            return badRequest(errors);
        }
        if (roomId == null || userId == null) {
            return ResponseEntity.badRequest().build();
        }

        Payment createdPayment = paymentService.createPayment(paymentDto.getTotalAmount(), paymentDto.getDivisionCnt(),userId, roomId );

        WebMvcLinkBuilder webMvcLinkBuilder = linkTo(PaymentController.class).slash(createdPayment.getToken());
        URI createUri =webMvcLinkBuilder.toUri();

        //PaymentResource paymentResource =new PaymentResource(payment);
        Map<String, String> map = new HashMap<>();
        map.put("token", createdPayment.getToken());
/*        EntityModel<Map<String, String>> entityModel = new EntityModel<>(map);
        entityModel.add(linkTo(PaymentController.class).withRel("query-payment"));
        entityModel.add(webMvcLinkBuilder.withRel("update-payment"));
        entityModel.add(new Link("/docs/index.html#resources-payment-create").withRel("profile"));*/

        return ResponseEntity.created(createUri).body(map);
    }

    /*@GetMapping
    public ResponseEntity getPayment(@RequestHeader(room) String roomId, @RequestHeader(user) Integer userId, @RequestBody String token) {

    }*/

    private ResponseEntity badRequest(Errors errors) {
        return ResponseEntity.badRequest().body(new ErrorResource(errors));
    }
}

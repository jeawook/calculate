package com.calculate.resource;

import com.calculate.controller.PaymentController;
import com.calculate.domain.Payment;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class PaymentResource extends EntityModel<Payment> {


    public PaymentResource(Payment payment, Link... links) {
        super(payment, links);
        add(linkTo(PaymentController.class).slash(payment.getToken()).withSelfRel());
    }
}

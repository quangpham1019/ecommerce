package org.ecommerce.user.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/csrf")
public class CsrfController {

    @PostMapping(name = "getCsrf")
    @ResponseStatus(HttpStatus.OK)
    public void csrfToken(){
    }
}
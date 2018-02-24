package org.staydigital.services.banking.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FormController {

    @RequestMapping(value = "/form")
    public String index() {
        return "index.html";
    }
}

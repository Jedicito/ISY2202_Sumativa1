package com.duoc.seguridadcalidad;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    /** [Pública] Página de login. El POST /login lo procesa Spring Security. */
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}

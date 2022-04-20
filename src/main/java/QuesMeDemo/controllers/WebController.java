package QuesMeDemo.controllers;

import QuesMeDemo.entities.AdminEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class WebController {
    private static List<AdminEntity> persons = new ArrayList<AdminEntity>();

    static {
        persons.add(new AdminEntity("QuesMe", "Global Admin", "87654321"));
        persons.add(new AdminEntity("IntelliJ IDEA", "IDE2021", "202132"));
    }

    // ​​​​​​​
    // Вводится (inject) из application.properties.
    @Value("${welcome.message}")
    private String message;

    @Value("${error.message}")
    private String errorMessage;

    @RequestMapping(value = { "/", "/ttt" }, method = RequestMethod.GET)
    public String ttt(Model model) {

        model.addAttribute("message", message);

        return "ttt";
    }
}

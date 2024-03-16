package hexlet.code.controller;

import hexlet.code.dto.BasePage;
import io.javalin.http.Context;

import java.util.Collections;

public class MainController {
    public static void index(Context context) {
        var page = new BasePage();
        page.setFlash(context.consumeSessionAttribute("flash"));
        context.render("index.jte", Collections.singletonMap("page", page));
    }
}

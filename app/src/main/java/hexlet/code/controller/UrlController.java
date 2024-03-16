package hexlet.code.controller;

import hexlet.code.dto.url.UrlPage;
import hexlet.code.dto.url.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collections;

public class UrlController {
    public static void index(Context context) throws SQLException {
        var urls = UrlRepository.getEntities();
        var page = new UrlsPage(urls);
        page.setFlash(context.consumeSessionAttribute("flash"));
        context.render("urls/index.jte", Collections.singletonMap("page", page));
    }

    public static void create(Context context) throws SQLException {
        var urlParam = context.formParam("url");
        URL url = null;
        try {
            url = new URL(urlParam);
            String uri = url.getProtocol() + "://" + url.getAuthority();
            var urlDb = UrlRepository.findByName(uri);
            if (urlDb.isEmpty()) {
                //Можно создавать в бд
                var entity = new Url(uri, new Timestamp(System.currentTimeMillis()));
                UrlRepository.save(entity);
                context.sessionAttribute("flash", "Страница успешно создана");
                context.redirect(NamedRoutes.urlsPath());
            } else {
                //В БД уже есть данные, нужно flash отдать
                context.sessionAttribute("flash", "Страница уже существует");
                context.redirect(NamedRoutes.rootPath());
            }
        } catch (MalformedURLException e) {
            context.sessionAttribute("flash", "Некорректный URL");
            context.redirect(NamedRoutes.rootPath());
        }
    }

    public static void show(Context context) throws SQLException {
        var idParam = context.pathParamAsClass("id", Long.class).get();
        //NotFoundException не обрабатывает кириллицу. Нужно явно установить кодироку
        context.contentType("text/html; charset=utf-8");
        var url = UrlRepository.findById(idParam)
                .orElseThrow(() -> new NotFoundResponse("Страница с ID=" + idParam + " не существует"));
        var page = new UrlPage(url);
        context.render("urls/show.jte", Collections.singletonMap("page", page));
    }
}

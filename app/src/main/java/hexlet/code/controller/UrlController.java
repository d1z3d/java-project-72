package hexlet.code.controller;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import hexlet.code.dto.url.UrlPage;
import hexlet.code.dto.url.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.HtmlUtils;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlController {
    public static void index(Context context) throws SQLException {
        var urls = UrlRepository.getLeads();
        var page = new UrlsPage(urls);
        page.setFlash(context.consumeSessionAttribute("flash"));
        context.contentType("text/html; charset=utf-8");
        context.render("urls/index.jte", Collections.singletonMap("page", page));
    }

    public static void create(Context context) throws SQLException {
        var urlParam = context.formParam("url");
        URL url = null;
        try {
            url = new URL(Objects.requireNonNull(urlParam).trim());
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
        } catch (Exception e) {
            context.sessionAttribute("flash", "Некорректный URL");
            context.redirect(NamedRoutes.rootPath());
        }
    }

    public static void show(Context context) throws SQLException {
        var idParam = context.pathParamAsClass("id", Long.class).get();
        //NotFoundException не обрабатывает кириллицу. Нужно явно установить кодироку
        var url = UrlRepository.findById(idParam)
                .orElseThrow(() -> new NotFoundResponse("Страница с ID=" + idParam + " не существует"));
        var urlChecks = UrlCheckRepository.getEntitiesByUrlId(url.getId());
        var page = new UrlPage(url, urlChecks);
        context.contentType("text/html; charset=utf-8");
        context.render("urls/show.jte", Collections.singletonMap("page", page));
    }

    public static void createCheck(Context context) throws SQLException, UnirestException, UnsupportedEncodingException {
        var idParam = context.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.findById(idParam)
                .orElseThrow(() -> new NotFoundResponse("Not found"));
        HttpResponse<String> response = Unirest.get(url.getName())
                .header("Content-Type", "text/html; charset=utf-8")
                .asString();

        String body = response.getBody();

        String title = new String(HtmlUtils.getValueOfNodeByTag("title", body).getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
        String h1 = HtmlUtils.getValueOfNodeByTag("h1", body);
        String description = HtmlUtils.getValueOfNodeByTag("description", body);

        UrlCheck urlCheck = new UrlCheck(response.getCode(), title, h1, description, url.getId(), new Timestamp(System.currentTimeMillis()));
        UrlCheckRepository.save(urlCheck);
        context.redirect(NamedRoutes.urlPath(url.getId()));
    }
}

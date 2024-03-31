package hexlet.code.controller;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import hexlet.code.dto.url.UrlPage;
import hexlet.code.dto.url.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.http.NotFoundResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.URI;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Objects;

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
            url = new URI(Objects.requireNonNull(urlParam).trim()).toURL();
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
                context.contentType("text/html; charset=utf-8");
                context.redirect(NamedRoutes.rootPath(), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            context.sessionAttribute("flash", "Некорректный URL");
            context.contentType("text/html; charset=utf-8");
            context.redirect(NamedRoutes.rootPath(), HttpStatus.BAD_REQUEST);
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

    public static void createCheck(Context context) throws SQLException, UnirestException {
        var idParam = context.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.findById(idParam)
                .orElseThrow(() -> new NotFoundResponse("Not found"));
        HttpResponse<String> response = Unirest.get(url.getName())
                .asString();

        Document document = Jsoup.parse(response.getBody());
        String title = document.title();
        String h1 = getValueByTag("h1", document);
        String description = getDescription(document);

        UrlCheck urlCheck = new UrlCheck(response.getCode(), title, h1,
                description, url.getId(), new Timestamp(System.currentTimeMillis()));
        UrlCheckRepository.save(urlCheck);
        context.contentType("text/html; charset=utf-8");
        context.redirect(NamedRoutes.urlPath(url.getId()));
    }

    private static String getValueByTag(String tag, Document document) {
        Element element = document.selectFirst(tag);
        return element != null ? element.text() : "";
    }
    private static String getDescription(Document document) {
        var elements = document.getElementsByTag("meta");
        String result = "";
        for (Element element : elements) {
            if (element.attr("name").equals("description")) {
                result = element.attr("content");
            }
        }
        return result;
    }
}

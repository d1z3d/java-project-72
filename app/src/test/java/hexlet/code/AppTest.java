package hexlet.code;

import hexlet.code.model.Url;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
import io.javalin.testtools.JavalinTest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class AppTest {
    private String name;
    private Url url;
    private Javalin app;
    private MockWebServer mockWebServer;


    @BeforeEach
    public final void setUp() throws Exception {
        String html1 = TestUtils.getDataFromFile(TestUtils.getFixturePath("html",
                "exampleWithRequiredFields.html"));
        String html2 = TestUtils.getDataFromFile(TestUtils.getFixturePath("html",
                "exampleWithoutRequiredFields.html"));
        String html3 = TestUtils.getDataFromFile(TestUtils.getFixturePath("html",
                "exampleWithoutTitle.html"));
        String html4 = TestUtils.getDataFromFile(TestUtils.getFixturePath("html",
                "exampleWithoutDescription.html"));
        String html5 = TestUtils.getDataFromFile(TestUtils.getFixturePath("html",
                "exampleWithoutH1.html"));
        app = App.getApp();
        mockWebServer = new MockWebServer();
        MockResponse response1 = new MockResponse()
                .setResponseCode(HttpStatus.OK.getCode())
                .setBody(html1);
        MockResponse response2 = new MockResponse()
                .setResponseCode(HttpStatus.OK.getCode())
                .setBody(html2);
        MockResponse response3 = new MockResponse()
                .setResponseCode(HttpStatus.OK.getCode())
                .setBody(html3);
        MockResponse response4 = new MockResponse()
                .setResponseCode(HttpStatus.OK.getCode())
                .setBody(html4);
        MockResponse response5 = new MockResponse()
                .setResponseCode(HttpStatus.OK.getCode())
                .setBody(html5);
        mockWebServer.enqueue(response1);
        mockWebServer.enqueue(response2);
        mockWebServer.enqueue(response3);
        mockWebServer.enqueue(response4);
        mockWebServer.enqueue(response5);
        mockWebServer.start();
        name = String.format("http://%s:%s", mockWebServer.getHostName(), mockWebServer.getPort());
        url = new Url(name);
    }

    @AfterEach
    public final void turnDown() throws IOException {
        app.stop();
        mockWebServer.shutdown();
    }

    @Test
    public void getRootTest() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.rootPath());
            assertThat(response.code()).isEqualTo(HttpStatus.OK.getCode());
            assertThat(response.body().string()).contains("Анализатор страниц");
            response.close();
        });
    }

    @Test
    public void getUrlsTest() throws SQLException {
        UrlRepository.save(url);
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.urlsPath());
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("1", url.getName());
        });
    }

    @Test
    public void createUrlTest() {
        JavalinTest.test(app, (server, client) -> {
            var urlParam = "url=" + name;
            client.post(NamedRoutes.urlsPath(), urlParam);
            var response = client.get(NamedRoutes.urlsPath());
            assertThat(response.code()).isEqualTo(HttpStatus.OK.getCode());
            assertThat(response.body().string()).contains(name);

            response = client.post(NamedRoutes.urlsPath(), urlParam);
            assertThat(response.code()).isEqualTo(HttpStatus.CONFLICT.getCode());

            response = client.post(NamedRoutes.urlsPath(), "test");
            assertThat(response.code()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());
            response.close();
        });
    }

    @Test
    public void createUrlCheckTest() throws SQLException {
        UrlRepository.save(url);
        JavalinTest.test(app, (server, client) -> {
            client.post(NamedRoutes.urlCreateCheckPath(url.getId()));
            var response = client.get(NamedRoutes.urlPath(url.getId()));
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("title1",
                    "description1",
                    "first h1");

            client.post(NamedRoutes.urlCreateCheckPath(url.getId()));
            response = client.get(NamedRoutes.urlPath(url.getId()));
            assertThat(response.code()).isEqualTo(200);
            var lastChecks = UrlCheckRepository.getLastChecks();
            var check = lastChecks.get(url.getId());
            assertThat(check.getTitle()).isEmpty();
            assertThat(check.getDescription()).isEmpty();
            assertThat(check.getH1()).isEmpty();

            client.post(NamedRoutes.urlCreateCheckPath(url.getId()));
            response = client.get(NamedRoutes.urlPath(url.getId()));
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("description3", "third h1");
            lastChecks = UrlCheckRepository.getLastChecks();
            check = lastChecks.get(url.getId());
            assertThat(check.getTitle()).isEmpty();

            client.post(NamedRoutes.urlCreateCheckPath(url.getId()));
            response = client.get(NamedRoutes.urlPath(url.getId()));
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("title4", "fourth h1");
            lastChecks = UrlCheckRepository.getLastChecks();
            check = lastChecks.get(url.getId());
            assertThat(check.getDescription()).isEmpty();

            client.post(NamedRoutes.urlCreateCheckPath(url.getId()));
            response = client.get(NamedRoutes.urlPath(url.getId()));
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("title5", "description5");
            lastChecks = UrlCheckRepository.getLastChecks();
            check = lastChecks.get(url.getId());
            assertThat(check.getH1()).isEmpty();

            response.close();
        });
    }
}

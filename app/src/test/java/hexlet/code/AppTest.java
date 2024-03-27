package hexlet.code;

import hexlet.code.model.Url;
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
import java.sql.Timestamp;

import static org.assertj.core.api.Assertions.assertThat;

public class AppTest {
    private String name;
    private String html;
    private Url url;
    private Javalin app;
    private MockWebServer mockWebServer;


    @BeforeEach
    public final void setUp() throws Exception {
        html = TestUtils.getDataFromFile(TestUtils.getFixturePath("html", "example.html"));
        app = App.getApp();
        mockWebServer = new MockWebServer();
        MockResponse response = new MockResponse()
                .setResponseCode(HttpStatus.OK.getCode())
                .setBody(html);
        mockWebServer.enqueue(response);
        mockWebServer.start();
        name = String.format("http://%s:%s", mockWebServer.getHostName(), mockWebServer.getPort());
        url = new Url(name, new Timestamp(System.currentTimeMillis()));
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
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("Анализатор страниц");
            response.close();
        });
    }

    @Test
    public void createUrlTest() {
        JavalinTest.test(app, (server, client) -> {
            var urlParam = "url=" + name;
            client.post(NamedRoutes.urlsPath(), urlParam);
            var response = client.get(NamedRoutes.urlsPath());
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains(name);
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
            assertThat(response.body().string()).contains("test");
            response.close();
        });
    }
}

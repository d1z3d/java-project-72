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
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Timestamp;

import static org.assertj.core.api.Assertions.assertThat;

public class AppTest {
    private static final Integer PORT = 3543;
    private static final String NAME = "http://localhost:" + PORT;
    private static String html;
    private static Javalin app;
    private static MockWebServer mockWebServer;


    @BeforeAll
    public static void setUp() throws Exception {
        html = TestUtils.getDataFromFile(TestUtils.getFixturePath("html", "example.html"));
        app = App.getApp();
        mockWebServer = new MockWebServer();
        MockResponse response = new MockResponse()
                .setResponseCode(HttpStatus.OK.getCode())
                .setBody(html);
        mockWebServer.enqueue(response);
        mockWebServer.start(PORT);
    }

    @AfterAll
    public static void turnDown() throws IOException {
        app.stop();
        mockWebServer.shutdown();
    }

    @Test
    public void test() throws Exception {
        JavalinTest.test(app, (server, client) -> {
            Url url = new Url(NAME, new Timestamp(System.currentTimeMillis()));
            UrlRepository.save(url);
            var response1 = client.post(NamedRoutes.urlCreateCheckPath(url.getId()));
            var checks = UrlCheckRepository.getEntitiesByUrlId(url.getId());
            assertThat(200).isEqualTo(checks.get(0).getStatusCode());
            assertThat("test").isEqualTo(checks.get(0).getDescription());
        });
        mockWebServer.shutdown();
        app.stop();
    }
}

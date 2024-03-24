package hexlet.code.repository;

/*import hexlet.code.App;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;*/

public class UrlCheckRepositoryTest {

    /*@BeforeEach
    public final void setUp() throws SQLException, IOException {
        App.getApp();
    }

    @Test
    public void test() throws SQLException {
        var actual1 = new ArrayList<UrlCheck>();
        var expected1 = UrlCheckRepository.getEntitiesByUrlId(1L);
        assertIterableEquals(actual1, expected1);

        var url = new Url("https://example.com", new Timestamp(System.currentTimeMillis()));
        UrlRepository.save(url);
        var urlCheck = new UrlCheck(200, "check1", "check2",
                "check3", url.getId(), new Timestamp(System.currentTimeMillis()));
        UrlCheckRepository.save(urlCheck);
        var actual2 = new ArrayList<UrlCheck>();
        actual2.add(urlCheck);
        var expected2 = UrlCheckRepository.getEntitiesByUrlId(url.getId());
        assertIterableEquals(actual2, expected2);
    }*/
}

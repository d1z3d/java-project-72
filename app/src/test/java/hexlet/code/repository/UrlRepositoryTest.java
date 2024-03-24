package hexlet.code.repository;

/*import hexlet.code.App;
import hexlet.code.model.Url;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;*/

public class UrlRepositoryTest {

    /*private Url url;

    @BeforeEach
    public final void setUp() throws SQLException, IOException {
        url = new Url("https://example.com", new Timestamp(System.currentTimeMillis()));
        App.getApp();
    }

    @Test
    public void test() throws SQLException {
        var actual1 = Optional.empty();
        var actual2 = new ArrayList<Url>();
        var expected1 = UrlRepository.findById(1L);
        var expected2 = UrlRepository.findByName(url.getName());
        var expected3 = UrlRepository.getEntities();
        var expected4 = UrlRepository.getLeads();
        assertThat(actual1).isEqualTo(expected1);
        assertThat(actual1).isEqualTo(expected2);
        assertIterableEquals(actual2, expected3);
        assertIterableEquals(actual2, expected4);

        UrlRepository.save(url);
        var actual3 = List.of(url);
        var expected5 = UrlRepository.findById(url.getId()).get();
        var expected6 = UrlRepository.findByName(url.getName()).get();
        var expected7 = UrlRepository.getEntities();
        var expected8 = UrlRepository.getLeads();
        assertThat(url).isEqualTo(expected5);
        assertThat(url).isEqualTo(expected6);
        assertIterableEquals(actual3, expected7);
        url.setStatusCode(0);
        assertIterableEquals(actual3, expected8);
    }*/
}

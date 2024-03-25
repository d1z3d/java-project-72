package hexlet.code.repository;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import hexlet.code.TestUtils;
import hexlet.code.model.Url;
import io.javalin.Javalin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

public class UrlRepositoryTest {

    private Javalin app;
    private Url url;

    @BeforeEach
    public final void setUp() throws Exception {
        url = new Url("https://example.com", new Timestamp(System.currentTimeMillis()));
        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(System.getenv()
                .getOrDefault("JDBC_DATABASE_URL", "jdbc:h2:mem:project;DB_CLOSE_DELAY=-1;"));
        var dataSource = new HikariDataSource(hikariConfig);
        var sql = TestUtils.getDataFromFile(TestUtils.getFixturePath("sql", "schema.sql"));

        //log.info(sql);
        try (var connection = dataSource.getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.execute();
        }
        BaseRepository.dataSource = dataSource;
    }

    @AfterEach
    public final void turnDown() {
        BaseRepository.dataSource.close();
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
    }

    @Test
    public void findByIdTest() throws SQLException {
        Optional<Url> actual1 = Optional.empty();
        Optional<Url> expected1 = UrlRepository.findById(1L);
        assertThat(actual1).isEqualTo(expected1);

        UrlRepository.save(url);
        Url expected2 = UrlRepository.findById(url.getId()).get();
        assertThat(url).isEqualTo(expected2);
    }

    @Test
    public void saveTest() throws SQLException {
        UrlRepository.save(url);
        assertThat(url.getId()).isEqualTo(1);
    }

    @Test
    public void findByNameTest() throws SQLException {
        Optional<Url> actual1 = Optional.empty();
        Optional<Url> expected1 = UrlRepository.findByName(url.getName());
        assertThat(actual1).isEqualTo(expected1);

        UrlRepository.save(url);
        Url expected2 = UrlRepository.findByName(url.getName()).get();
        assertThat(url).isEqualTo(expected2);
    }

    @Test
    public void getEntitiesTest() throws SQLException {
        List<Url> actual = new ArrayList<>();
        List<Url> expected1 = UrlRepository.getEntities();
        assertIterableEquals(actual, expected1);

        UrlRepository.save(url);
        actual.add(url);
        List<Url> expected2 = UrlRepository.getEntities();
        assertIterableEquals(actual, expected2);
    }

    @Test
    public void getLeadsTest() throws SQLException {
        List<Url> actual = new ArrayList<>();
        List<Url> expected1 = UrlRepository.getLeads();
        assertIterableEquals(actual, expected1);

        UrlRepository.save(url);
        url.setStatusCode(0);
        actual.add(url);
        List<Url> expected2 = UrlRepository.getLeads();

        assertIterableEquals(expected2, actual);
    }
}

package me.decentos.repository;

import me.decentos.model.Option;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OptionRepositoryTest {

    @Autowired
    OptionRepository repository;

    @Test
    public void findFirstOption() {
        Option option = repository.findById(1).orElseThrow(RuntimeException::new);
        assertThat(option).isNotNull();
    }

}
package com.group3.vaccinemaps.repository

import com.group3.vaccinemaps.entity.User
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.util.*

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest() {
    @Autowired
    private lateinit var repository: UserRepository

    @Test
    fun insertUser() {
        repository.findById(100)
    }

    @Test
    fun existByEmailTest() {
        val exist = repository.existsByEmail("bagus")
        Assertions.assertThat(exist).isFalse
    }
}
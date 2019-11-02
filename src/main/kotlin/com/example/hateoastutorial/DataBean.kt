package com.example.hateoastutorial

import com.example.hateoastutorial.model.Role
import com.example.hateoastutorial.model.User
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*
import kotlin.collections.ArrayList

object DataBean {

    lateinit var roles: ArrayList<Role>
    lateinit var users: ArrayList<User>
}

@Configuration
class BeanConfiguration {

    @Bean
    fun dataBean(): DataBean {
        return DataBean.also {
            it.roles = arrayListOf(
                    Role("admin"),
                    Role("user")
            )
            it.users = arrayListOf<User>(
                    User("Peter", Random().nextInt(100), listOf(Role("admin"))),
                    User("Jens", Random().nextInt(100), listOf(Role("user"))),
                    User("Maik", Random().nextInt(100), listOf(Role("user"))),
                    User("Martin", Random().nextInt(100), listOf(Role("user"))),
                    User("Jonathan", Random().nextInt(100), listOf(Role("user")))
            )
        }
    }
}
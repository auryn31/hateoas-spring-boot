package com.example.hateoastutorial

import com.example.hateoastutorial.model.User
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class UserController(val dataBean: DataBean) {


    @GetMapping("/users")
    fun allUsers() : CollectionModel<EntityModel<User>> {
        return CollectionModel(
                dataBean.users.map(this::generateEntityModelFromUser),
                linkTo(methodOn(UserController::class.java).allUsers()).withSelfRel()
        )
    }


    @GetMapping("/users/{id}")
    fun specificUser(@PathVariable id: Int): ResponseEntity<EntityModel<User>> {
        if(dataBean.users.filter { it.id == id }.isEmpty()){
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
        return ResponseEntity(EntityModel(dataBean.users.filter { it.id == id }.first(),
                linkTo(methodOn(UserController::class.java).specificUser(id)).withSelfRel(),
                linkTo(methodOn(UserController::class.java).allUsers()).withRel("users")), HttpStatus.OK)
    }

    private fun generateEntityModelFromUser(user: User): EntityModel<User>{
        return EntityModel(user,
                linkTo(methodOn(UserController::class.java).specificUser(user.id)).withSelfRel())
    }
}
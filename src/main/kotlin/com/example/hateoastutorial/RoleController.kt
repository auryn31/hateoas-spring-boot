package com.example.hateoastutorial

import com.example.hateoastutorial.model.Role
import com.example.hateoastutorial.model.User
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class RoleController(val dataBean: DataBean) {

    @GetMapping("/role/{rolename}")
    fun specificRole(@PathVariable rolename: String): ResponseEntity<EntityModel<Role>> {
        if(dataBean.roles.filter { it.name == rolename }.isEmpty()){
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
        return ResponseEntity(EntityModel(dataBean.roles.filter { it.name == rolename }.first(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RoleController::class.java).specificRole(rolename)).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RoleController::class.java).allRoles()).withRel("roles"),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RoleController::class.java).usersWithRole(rolename)).withRel("users_with_role")
                ), HttpStatus.OK)
    }

    @GetMapping("/userswithrole/{rolename}")
    fun usersWithRole(@PathVariable rolename: String): ResponseEntity<CollectionModel<EntityModel<User>>> {
        val role = Role(rolename)
        if(dataBean.users.filter { it.roles.contains(role) }.isEmpty()){
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
        val usersWithRole = dataBean.users.filter { it.roles.contains(role) }.map {
            EntityModel(it, WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController::class.java).specificUser(it.id)).withSelfRel())
        }
        return ResponseEntity(CollectionModel(usersWithRole,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RoleController::class.java).allRoles()).withRel("all_roles")), HttpStatus.OK)
    }

    @GetMapping("/roles")
    fun allRoles() : CollectionModel<EntityModel<Role>> {
        return CollectionModel(
                dataBean.roles.map(this::generateEntityModelFromRole),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RoleController::class.java).allRoles()).withSelfRel()
        )
    }

    private fun generateEntityModelFromRole(role: Role): EntityModel<Role>{
        return EntityModel(role,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RoleController::class.java).specificRole(role.name)).withRel("role_details"),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RoleController::class.java).usersWithRole(role.name)).withRel("users_with_role"))
    }
}
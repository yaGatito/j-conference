package com.epam.jconference.controller.assembler;

import com.epam.jconference.controller.UserController;
import com.epam.jconference.controller.model.UserModel;
import com.epam.jconference.dto.UserDto;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserAssembler extends RepresentationModelAssemblerSupport<UserDto, UserModel> {

    private static final String GET_REL = "get_user";
    private static final String CREATE_REL = "create_user";
    private static final String UPDATE_REL = "update_user";
    private static final String LOGIN_REL = "login_user";
    private static final String LOGOUT_REL = "logout_user";
    private static final String PROFILE_REL = "profile_user";

    public UserAssembler() {
        super(UserController.class, UserModel.class);
    }

    @Override
    public UserModel toModel(UserDto entity) {
        UserModel userModel = new UserModel(entity);

        Link get = linkTo(methodOn(UserController.class).getById(entity.getId())).withRel(GET_REL).withType(HttpMethod.GET.toString());
        Link create = linkTo(methodOn(UserController.class).create(entity)).withRel(CREATE_REL).withType(HttpMethod.POST.toString());
        Link update = linkTo(methodOn(UserController.class).update(entity)).withRel(UPDATE_REL).withType(HttpMethod.PUT.toString());
        Link login = linkTo(methodOn(UserController.class).login(entity)).withRel(LOGIN_REL).withType(HttpMethod.POST.toString());
        Link logout = linkTo(methodOn(UserController.class).logout()).withRel(LOGOUT_REL).withType(HttpMethod.POST.toString());
        Link profile = linkTo(methodOn(UserController.class).profile()).withRel(PROFILE_REL).withType(HttpMethod.GET.toString());

        userModel.add(get, create, update, login, logout, profile);
        return userModel;
    }
}

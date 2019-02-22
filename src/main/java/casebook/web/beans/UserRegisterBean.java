package casebook.web.beans;

import casebook.domain.models.binding.UserRegisterBindingModel;
import casebook.domain.models.service.UserServiceModel;
import casebook.service.UserService;
import org.modelmapper.ModelMapper;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;

@Named
@RequestScoped
public class UserRegisterBean {
    private UserRegisterBindingModel userRegisterBindingModel;
    private UserService userService;
    private ModelMapper modelMapper;

    public UserRegisterBean() {
    }

    @Inject
    public UserRegisterBean(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }
    @PostConstruct
    public void init(){
        this.userRegisterBindingModel = new UserRegisterBindingModel();
    }

    public UserRegisterBindingModel getUserRegisterBindingModel() {
        return this.userRegisterBindingModel;
    }

    public void setUserRegisterBindingModel(UserRegisterBindingModel userRegisterBindingModel) {
        this.userRegisterBindingModel = userRegisterBindingModel;
    }

    public void register() throws IOException {
        if (!userRegisterBindingModel.getPassword()
                .equals(userRegisterBindingModel.getConfirmPassword())){
            throw new IllegalArgumentException("Password do not match");
        }
        if (!this.userService.registerUser(this.modelMapper.map(this.userRegisterBindingModel, UserServiceModel.class))){
            throw new IllegalArgumentException("Something wrong!");
        }

        FacesContext.getCurrentInstance().getExternalContext()
                .redirect("/login");
    }
}

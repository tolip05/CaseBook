package casebook.web.beans;

import casebook.domain.models.binding.UserLoginBindingModel;
import casebook.domain.models.service.UserServiceModel;
import casebook.service.UserService;
import org.modelmapper.ModelMapper;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Named
@RequestScoped
public class UserLoginBean {
    private UserLoginBindingModel userLoginBindingModel;
    private UserService userService;
    private ModelMapper modelMapper;

    public UserLoginBean() {
    }

    @Inject
    public UserLoginBean(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @PostConstruct
    public void init(){
        this.userLoginBindingModel = new UserLoginBindingModel();
    }

    public UserLoginBindingModel getUserLoginBindingModel() {
        return this.userLoginBindingModel;
    }

    public void setUserLoginBindingModel(UserLoginBindingModel userLoginBindingModel) {
        this.userLoginBindingModel = userLoginBindingModel;
    }
    public void login() throws IOException {

        UserServiceModel userServiceModel = this.userService
                .loginUser(this.modelMapper.map(this.userLoginBindingModel,UserServiceModel.class));
           if (userServiceModel == null){
               throw new IllegalArgumentException("Something went wrong!");
           }
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(true);
           session.setAttribute("username",userServiceModel.getUsername());
           session.setAttribute("id",userServiceModel.getId());
        FacesContext.getCurrentInstance().getExternalContext()
                .redirect("/home");
    }
}

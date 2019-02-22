package casebook.web.beans;

import casebook.domain.models.service.UserServiceModel;
import casebook.domain.models.view.UserHomeViewModel;
import casebook.service.UserService;
import org.modelmapper.ModelMapper;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Named
@RequestScoped
public class HomeBean {
private List<UserHomeViewModel> models;
private UserService userService;
private ModelMapper modelMapper;

    public HomeBean() {
    }

    @Inject
    public HomeBean(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.initModels();
    }

    private void initModels() {
        String username = (String) ((HttpSession)FacesContext.getCurrentInstance()
                .getExternalContext().getSession(false))
                .getAttribute("username");

        String id = (String) ((HttpSession)FacesContext.getCurrentInstance()
                .getExternalContext().getSession(false))
                .getAttribute("id");

        UserServiceModel loggedUser = this.userService.getById(id);
     this.models =
             this.userService.getAllUsers()
             .stream()
             .filter(u -> !u.getUsername().equals(username)
             && !loggedUser.getFriends().stream()
             .map(f -> f.getUsername())
             .collect(Collectors.toList())
                     .contains(u.getUsername()))
             .map(u -> this.modelMapper.map(u,UserHomeViewModel.class))
             .collect(Collectors.toList());


    }


    public List<UserHomeViewModel> getModels() {
        return this.models;
    }

    public void setModels(List<UserHomeViewModel> models) {
        this.models = models;
    }

    public void addFriend(String id) throws IOException {
        UserServiceModel loggedUser = this.userService.getById((String) ((HttpSession)FacesContext.getCurrentInstance()
                .getExternalContext().getSession(false))
                .getAttribute("id"));
        UserServiceModel friendUser = this.userService.getById(id);
        loggedUser.getFriends().add(friendUser);
        friendUser.getFriends().add(loggedUser);

        if (!this.userService.addFriend(loggedUser)){
            throw new IllegalArgumentException("Something went wrong!");
        }

        if (!this.userService.addFriend(friendUser)){
            throw new IllegalArgumentException("Something went wrong!");
        }
        FacesContext.getCurrentInstance()
                .getExternalContext().redirect("/home");
    }
}

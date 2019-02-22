package casebook.web.beans;

import casebook.domain.models.service.UserServiceModel;
import casebook.domain.models.view.UserFriendsViewModel;
import casebook.domain.models.view.UserHomeViewModel;
import casebook.domain.models.view.UserProfileViewModel;
import casebook.service.UserService;
import org.modelmapper.ModelMapper;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Named
@RequestScoped
public class UserFriendsBean {
    private List<UserFriendsViewModel> models;
    private UserService userService;
    private ModelMapper modelMapper;

    public UserFriendsBean() {
    }

    @Inject
    public UserFriendsBean(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.initModels();
    }

    private void initModels() {

        String id = (String) ((HttpSession)FacesContext.getCurrentInstance()
                .getExternalContext().getSession(false))
                .getAttribute("id");
        UserServiceModel userServiceModel = this.userService.getById(id);
        this.models = userServiceModel.getFriends()
                .stream().map(f -> this.modelMapper
                        .map(f, UserFriendsViewModel.class))
                .collect(Collectors.toList());

    }

    public List<UserFriendsViewModel> getModels() {
        return this.models;
    }

    public void setModels(List<UserFriendsViewModel> models) {
        this.models = models;
    }
    public void removeFriend(String id) throws IOException {
        UserServiceModel loggedUser = this.userService.getById((String) ((HttpSession)FacesContext.getCurrentInstance()
                .getExternalContext().getSession(false))
                .getAttribute("id"));
        UserServiceModel friendUser = this.userService.getById(id);
        List<UserServiceModel> listRemoved = new ArrayList<>();
        for (UserServiceModel friend : loggedUser.getFriends()) {
            if (!friend.getUsername().equals(friendUser.getUsername())){
                listRemoved.add(friend);
            }
        }
        loggedUser.setFriends(listRemoved);
        List<UserServiceModel> friendRemove = new ArrayList<>();
        for (UserServiceModel userServiceModel : friendUser.getFriends()) {
            if (!userServiceModel.getUsername().equals(loggedUser.getUsername())){
                friendRemove.add(userServiceModel);
            }
        }


        friendUser.setFriends(friendRemove);

        if (!this.userService.removeFriend(loggedUser)){
            throw new IllegalArgumentException("Something went wrong!");
        }

        if (!this.userService.removeFriend(friendUser)){
            throw new IllegalArgumentException("Something went wrong!");
        }
        FacesContext.getCurrentInstance()
                .getExternalContext().redirect("/home");
    }

}

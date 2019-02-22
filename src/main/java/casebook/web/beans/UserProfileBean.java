package casebook.web.beans;

import casebook.domain.models.service.UserServiceModel;
import casebook.domain.models.view.UserProfileViewModel;
import casebook.service.UserService;
import org.modelmapper.ModelMapper;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

@Named
@RequestScoped
public class UserProfileBean {
    private UserProfileViewModel userProfileViewModel;
    private UserService userService;
    private ModelMapper modelMapper;

    public UserProfileBean() {
    }

    @Inject
    public UserProfileBean(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @PostConstruct
    public void init() {
        this.userProfileViewModel = new UserProfileViewModel();
//        HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
//                .getExternalContext().getSession(true);
        String id = FacesContext.getCurrentInstance().getExternalContext()
                .getRequestParameterMap().get("id");
        UserServiceModel userServiceModel =
                this.userService.getById(id);
        if (userServiceModel == null){
            throw new IllegalArgumentException("Somethig went wrong!");
        }
        this.userProfileViewModel =
                this.modelMapper
                        .map(userServiceModel,
                                UserProfileViewModel.class);
    }

    public UserProfileViewModel getUserProfileViewModel() {
        return this.userProfileViewModel;
    }

    public void setUserProfileViewModel(UserProfileViewModel userProfileViewModel) {
        this.userProfileViewModel = userProfileViewModel;
    }
}

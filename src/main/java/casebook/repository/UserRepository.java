package casebook.repository;

import casebook.domain.entities.User;
import casebook.domain.models.service.UserServiceModel;

public interface UserRepository extends GenericRepository<User, String> {

    User findByName(String name);


}

package at.technikum.drivingschool.bookingappbackend.dto.response;

import at.technikum.drivingschool.bookingappbackend.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserListResponse {
    private List<UserInfoResponse> users;

    public UserListResponse() {
    }

    public void initWith(List<User> origUsers) {
        users = new ArrayList<>();
        for (User origUser: origUsers) {
            UserInfoResponse user = new UserInfoResponse();
            user.setId(origUser.getId());
            user.setUsername(origUser.getUsername());
            user.setEmail(origUser.getEmail());

            List<String> roles = origUser.getRoles().stream()
                    .map(item -> item.getName().name())
                    .collect(Collectors.toList());

            user.setRoles(roles);

            users.add(user);
        }
    }

    public UserListResponse(List<UserInfoResponse> users) {
        this.users = users;
    }

    public List<UserInfoResponse> getUsers() {
        return users;
    }

    public void setUsers(List<UserInfoResponse> users) {
        this.users = users;
    }
}

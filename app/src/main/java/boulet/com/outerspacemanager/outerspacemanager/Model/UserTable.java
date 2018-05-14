package boulet.com.outerspacemanager.outerspacemanager.Model;

import boulet.com.outerspacemanager.outerspacemanager.Model.UserResponse;

/**
 * Created by bouleta on 26/02/2018.
 */
public class UserTable {
    private UserResponse[] users;

    public UserResponse[] getUsers() {
        return users;
    }

    public void setUsers(UserResponse[] users) {
        this.users = users;
    }

}
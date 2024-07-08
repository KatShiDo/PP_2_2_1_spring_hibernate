package hiber.service;

import hiber.model.User;

import java.util.List;

public interface UserService {
    void add(User user);
    List<User> listUsers();
    void cleanAllTables();
    User findByCar(String model, int series);
}

package com.epam.gwt.client.services;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryAccountService implements AccountService {

    private final Map<String, String> users;

    public InMemoryAccountService() {
        users = new HashMap<>();
        users.put("vasya", "123");
        users.put("admin", "admin");
    }

    @Override
    public boolean login(String userName, String password) {
        return Optional.ofNullable(users.get(userName))
                       .filter(p -> p.equals(password))
                       .isPresent();
    }
}

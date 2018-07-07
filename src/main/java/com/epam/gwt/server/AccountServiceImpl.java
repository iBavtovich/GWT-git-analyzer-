package com.epam.gwt.server;

import com.epam.gwt.client.services.AccountService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AccountServiceImpl extends RemoteServiceServlet implements AccountService {
    private final Map<String, String> users;

    public AccountServiceImpl() {
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

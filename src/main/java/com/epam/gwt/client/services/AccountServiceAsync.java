package com.epam.gwt.client.services;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AccountServiceAsync {

    void login(String userName, String password, AsyncCallback<Boolean> async);

}

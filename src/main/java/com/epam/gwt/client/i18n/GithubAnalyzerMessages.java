package com.epam.gwt.client.i18n;

import com.google.gwt.i18n.client.Messages;

public interface GithubAnalyzerMessages extends Messages {

    @DefaultMessage("Successfully logged in: {0}")
    String successfulLogin(String name);
}

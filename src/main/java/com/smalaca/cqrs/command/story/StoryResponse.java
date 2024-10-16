package com.smalaca.cqrs.command.story;

public class StoryResponse {
    private boolean storyDoesNotExist;
    private boolean userDoesNotExist;

    public void storyDoesNotExist(boolean storyExist) {
        this.storyDoesNotExist = storyExist;
    }

    public boolean doesNotStoryExist() {
        return storyDoesNotExist;
    }

    public void userDoesNotExist(boolean userDoesNotExist) {
        this.userDoesNotExist = userDoesNotExist;
    }

    public boolean isUserDoesNotExist() {
        return userDoesNotExist;
    }
}

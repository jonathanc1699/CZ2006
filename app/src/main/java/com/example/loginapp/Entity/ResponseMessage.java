package com.example.loginapp.Entity;

/**
 * This class implements the ResponseMessage entity with the attributes textMessage and isUser
 *
 * @author Goh Shan Ying, Jonathan Chang, Lee Xuanhui, Luke Chin Peng Hao, Lynn Masillamoni, Russell Leung
 */
public class ResponseMessage {
    String textMessage;
    boolean isUser;

    /**
     * Constructor for ResponseMessage.
     * @param textMessage text that is going to be displayed in the speech bubble for the user or chatbot
     * @param isUser boolean value to know if the message is sent by the user or the chatbot so when the speech bubble is displayed in the chat it'll be on the correct side
     */
    public ResponseMessage(String textMessage, boolean isUser) {
        this.textMessage = textMessage;
        this.isUser = isUser;
    }

    /**
     * Get text that is going to be displayed in the speech bubble for the user or chatbot
     * @return text message to be displayed in chatbot
     */
    public String getTextMessage() {
        return textMessage;
    }

    /**
     * Identify if message is sent by the user
     * @return true if message is sent by the user, false message is not sent by the user
     */
    public boolean isUser() {
        return isUser;
    }

    /**
     * Set user
     * @param user user
     */
    public void setUser(boolean user) {
        isUser = user;
    }
}

package com.example.loginapp.Control;

import android.util.Log;

/**
 * This class implements the EnableDeletedUser controller where relevant emails will be sent to user.
 * This class is used in DisableAdminPage and EnableAdminPage class.
 * These class will either call EnableDeletedUser class to send a delete email or enable email to user.
 *
 * @author Goh Shan Ying, Jonathan Chang, Lee Xuanhui, Luke Chin Peng Hao, Lynn Masillamoni, Russell Leung
 */


public class EnableDeletedUser {

    /**
     * send email to deleted user
     * @param useremail deleted user's email
     * @param username deleted user's full name
     */
    public void sendDeleteEmail(String useremail,String username)
    {
        String senderemail = "cz2006sickgowhere@gmail.com";
        String recepientemail=useremail;// fetch user's email
        System.out.println("manage to fetch user email");
        Thread sender = new Thread(new Runnable() {
            public void run() {
                try {
                    GMailSender sender = new GMailSender("cz2006sickgowhere@gmail.com", "123456sickgowhere");
                    sender.sendMail("SickGoWhere account has been deleted",
                            "Dear "+ username+",\n"+"Your SickGoWhere account has been deleted " +
                                    "due to violation of the Code of Conduct that have been set.\n" +
                                    "\n" +
                                    "If you require more clarification, " +
                                    "send us an email at cz2006sickgowhere@gmail.com\n"+
                                    "Sorry for any inconvenience caused. Thank you.\n" +
                                    "\nBest Regards,\nSickGoWhere Team.",
                            senderemail, recepientemail);

                } catch (Exception e) {
                    Log.e("mylog", "Error: " + e.getMessage());
                }
            }
        });
        sender.start();

    }

    /**
     * send email to enabled user
     * @param useremail enabled user's email
     * @param username enabled user's full name
     */
    //send enable email format
    public void sendEnableEmail(String useremail,String username)
    {
        String senderemail = "cz2006sickgowhere@gmail.com";
        String recepientemail=useremail;// fetch user's email
        System.out.println("manage to fetch user email");
        Thread sender = new Thread(new Runnable() {
            public void run() {
                try {
                    GMailSender sender = new GMailSender("cz2006sickgowhere@gmail.com", "123456sickgowhere");
                    sender.sendMail("SickGoWhere account has been enabled",
                            "Dear "+ username+",\n"+"Your SickGoWhere account has been enabled " +
                                    "If you require more clarification, " +
                                    "send us an email at cz2006sickgowhere@gmail.com\n"+
                                    "Sorry for any inconvenience caused. Thank you.\n" +
                                    "\nBest Regards,\nSickGoWhere Team.",
                            senderemail, recepientemail);

                } catch (Exception e) {
                    Log.e("mylog", "Error: " + e.getMessage());
                }
            }
        });
        sender.start();

    }
}

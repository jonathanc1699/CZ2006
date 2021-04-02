package com.example.loginapp.Control;

import android.util.Log;

public class EnableDeletedUser {


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

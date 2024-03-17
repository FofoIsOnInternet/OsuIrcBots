/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package main;

import autohostbot.AutoHostBot;
import com.oopsjpeg.osu4j.exception.OsuAPIException;
import java.net.MalformedURLException;

/**
 *
 * @author faust
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws OsuAPIException, MalformedURLException {
        // TODO code application logic here
        AutoHostBot bot = new AutoHostBot(4.20f, 5.55f,300);
        bot.run();
    }
}

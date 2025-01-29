/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package main;

import autohostbot.AutoHostBot;

/**
 *
 * @author faust
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        AutoHostBot bot = new AutoHostBot(4.20f, 5.55f,300);
        bot.run();
    }
}

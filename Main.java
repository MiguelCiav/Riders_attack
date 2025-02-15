/*
 * PROCESOS:
 * - Despachador
 * - Usuario
 * - Rider
 * 
 * RECURSOS CRÍTICOS:
 * - Cantidad de riders disponibles
 * - 
 * 
 * 
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java Main <input_file>");
            return;
        }

        String inputFileName = args[0];
        try (BufferedReader br = new BufferedReader(new FileReader(inputFileName))) {
            int maxUsers = Integer.parseInt(br.readLine().trim());
            String[] bipbipData = br.readLine().split(",");
            String[] rideryData = br.readLine().split(",");
            String[] yummyData = br.readLine().split(",");

            int bipbip = Integer.parseInt(bipbipData[1].trim());
            int ridery = Integer.parseInt(rideryData[1].trim());
            int yummy = Integer.parseInt(yummyData[1].trim());

            Dispatcher dispatcher = new Dispatcher(bipbip, ridery, yummy, maxUsers);
            User[] users = new User[maxUsers];

            for (int i = 0; i < maxUsers; i++) {
                users[i] = new User(i, dispatcher);
            }

            for (int i = 0; i < maxUsers; i++) {
                (new Thread(users[i])).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
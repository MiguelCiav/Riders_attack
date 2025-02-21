import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    private static int getCantRiders(String[] line1, String[] line2, String[] line3, String app) {
        int cantRiders = 0;
        if (line1[0].toUpperCase().equals(app)) {
            cantRiders = Integer.parseInt(line1[1].trim());
        } else if (line2[0].toUpperCase().equals(app)){
            cantRiders = Integer.parseInt(line2[1].trim());
        } else {
            cantRiders = Integer.parseInt(line3[1].trim());
        }

        return cantRiders;
    }
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java Main <input_file>");
            return;
        }

        String inputFileName = args[0];
        try (BufferedReader br = new BufferedReader(new FileReader(inputFileName))) {
            int maxUsers = Integer.parseInt(br.readLine().trim());
            String[] line1 = br.readLine().split(",");
            String[] line2 = br.readLine().split(",");
            String[] line3 = br.readLine().split(",");

            int bipbip = getCantRiders(line1, line2, line3, "BIPBIP");
            int ridery = getCantRiders(line1, line2, line3, "RIDERY");
            int yummy = getCantRiders(line1, line2, line3, "YUMMY");
            
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
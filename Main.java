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

public class Main {
    private static final int MAX_USERS = 100;
    private static final int MAX_RIDERS = 20;
    public static void main(String[] args) {
        Dispatcher dispatcher = new Dispatcher(MAX_RIDERS);
        User[] users = new User[MAX_USERS];

        for(int i = 0; i < MAX_USERS; i++) {
            users[i] = new User(i, dispatcher);
        }

        for(int i = 0; i < MAX_USERS; i++) {
            (new Thread(users[i])).start();
        }
    }
}
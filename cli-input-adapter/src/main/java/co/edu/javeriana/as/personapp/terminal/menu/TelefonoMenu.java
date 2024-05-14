package co.edu.javeriana.as.personapp.terminal.menu;

import java.util.InputMismatchException;
import java.util.Scanner;

import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.terminal.adapter.PhoneInputAdapterCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PhoneMenu {

    private static final int OPTION_RETURN_TO_MODULES = 0;
    private static final int PERSISTENCE_MARIADB = 1;
    private static final int PERSISTENCE_MONGODB = 2;

    private static final int OPTION_RETURN_TO_PERSISTENCE_ENGINE = 0;
    private static final int OPTION_VIEW_ALL = 1;
    private static final int OPTION_CREATE = 2;
    private static final int OPTION_EDIT = 3;
    private static final int OPTION_DELETE = 4;

    public void startMenu(PhoneInputAdapterCli phoneInputAdapterCli, Scanner keyboard) {
        boolean isValid = false;
        do {
            try {
                showPersistenceEngineMenu();
                int option = readOption(keyboard);
                switch (option) {
                    case OPTION_RETURN_TO_MODULES:
                        isValid = true;
                        break;
                    case PERSISTENCE_MARIADB:
                        phoneInputAdapterCli.setPersonOutputPortInjection("MARIA");
                        optionsMenu(phoneInputAdapterCli, keyboard);
                        break;
                    case PERSISTENCE_MONGODB:
                        phoneInputAdapterCli.setPersonOutputPortInjection("MONGO");
                        optionsMenu(phoneInputAdapterCli, keyboard);
                        break;
                    default:
                        log.warn("The chosen option is not valid.");
                }
            } catch (InvalidOptionException e) {
                log.warn(e.getMessage());
            }
        } while (!isValid);
    }

    private void optionsMenu(PhoneInputAdapterCli phoneInputAdapterCli, Scanner keyboard) {
        boolean isValid = false;
        do {
            try {
                showOptionsMenu();
                int option = readOption(keyboard);
                switch (option) {
                    case OPTION_RETURN_TO_PERSISTENCE_ENGINE:
                        isValid = true;
                        break;
                    case OPTION_VIEW_ALL:
                        phoneInputAdapterCli.history();
                        break;
                    case OPTION_CREATE:
                        phoneInputAdapterCli.create(keyboard);
                        keyboard = new Scanner(System.in);
                        break;
                    case OPTION_EDIT:
                        phoneInputAdapterCli.edit(keyboard);
                        keyboard = new Scanner(System.in);
                        break;
                    case OPTION_DELETE:
                        phoneInputAdapterCli.delete(keyboard);
                        keyboard = new Scanner(System.in);
                        break;
                    default:
                        log.warn("The chosen option is not valid.");
                }
            } catch (InputMismatchException e) {
                log.warn("Only numbers are allowed.");
                keyboard.next(); // Clear the invalid input before retrying
            }
        } while (!isValid);
    }

    private void showOptionsMenu() {
        System.out.println("----------------------");
        System.out.println(OPTION_VIEW_ALL + " to view all phones");
        System.out.println(OPTION_CREATE + " to create a phone");
        System.out.println(OPTION_EDIT + " to edit a phone");
        System.out.println(OPTION_DELETE + " to delete a phone");

        System.out.println(OPTION_RETURN_TO_PERSISTENCE_ENGINE + " to return");
    }

    private void showPersistenceEngineMenu() {
        System.out.println("----------------------");
        System.out.println(PERSISTENCE_MARIADB + " for MariaDB");
        System.out.println(PERSISTENCE_MONGODB + " for MongoDB");
        System.out.println(OPTION_RETURN_TO_MODULES + " to return");
    }

    private int readOption(Scanner keyboard) {
        try {
            System.out.print("Enter an option: ");
            return keyboard.nextInt();
        } catch (InputMismatchException e) {
            log.warn("Only numbers are allowed.");
            keyboard.next(); // Clear the erroneous input
            return readOption(keyboard);
        }
    }
}

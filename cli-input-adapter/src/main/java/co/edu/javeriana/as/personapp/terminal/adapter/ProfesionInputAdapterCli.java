package co.edu.javeriana.as.personapp.terminal.adapter;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.ProfessionInputPort;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.ProfessionUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.terminal.mapper.ProfesionMapperCli;
import co.edu.javeriana.as.personapp.terminal.model.ProfesionModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class ProfessionInputAdapterCli {

    @Autowired
    @Qualifier("professionOutputAdapterMaria")
    private ProfessionOutputPort professionOutputPortMaria;

    @Autowired
    @Qualifier("professionOutputAdapterMongo")
    private ProfessionOutputPort professionOutputPortMongo;

    @Autowired
    private ProfesionMapperCli professionMapperCli;

    ProfessionInputPort professionInputPort;

    public void setProfessionOutputPortInjection(String dbOption) throws InvalidOptionException {
        if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
            professionInputPort = new ProfessionUseCase(professionOutputPortMaria);
        } else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
            professionInputPort = new ProfessionUseCase(professionOutputPortMongo);
        } else {
            throw new InvalidOptionException("Invalid database option: " + dbOption);
        }
    }

    public void history() {
        log.info("Fetching history of ProfessionEntity in Input Adapter");
        professionInputPort.findAll().stream()
                .map(professionMapperCli::fromDomainToAdapterCli)
                .forEach(System.out::println);
    }

    public void create(Scanner sc) {
        log.info("Creating ProfessionEntity in Input Adapter");
        sc = new Scanner(System.in);
        System.out.println("Enter new ID: ");
        String identification = sc.nextLine();
        System.out.println("Enter new name: ");
        String name = sc.nextLine();
        System.out.println("Enter new description: ");
        String description = sc.nextLine();
        Integer id = Integer.parseInt(identification);
        Profession profession = new Profession(id, name, description, null);
        profession = professionInputPort.create(profession);
        System.out.println(professionMapperCli.fromDomainToAdapterCli(profession));
    }

    public void edit(Scanner sc) {
        log.info("Editing ProfessionEntity in Input Adapter");
        try {
            sc = new Scanner(System.in);
            System.out.println("Current ID: ");
            String old_identification = sc.nextLine();
            System.out.println("New ID: ");
            String identification = sc.nextLine();
            System.out.println("New name: ");
            String name = sc.nextLine();
            System.out.println("New description: ");
            String description = sc.nextLine();
            Integer id = Integer.parseInt(identification);
            Profession profession = new Profession(id, name, description, null);
            profession = professionInputPort.edit(String.valueOf(Integer.parseInt(old_identification)), profession);
            System.out.println(professionMapperCli.fromDomainToAdapterCli(profession));
        } catch (NoExistException e) {
            log.warn(e.getMessage());
        }
    }

    public void delete(Scanner sc) {
        log.info("Deleting ProfessionEntity in Input Adapter");
        try {
            sc = new Scanner(System.in);
            System.out.println("Current ID: ");
            String old_identification = sc.nextLine();
            System.out.println(professionInputPort.drop(String.valueOf(Integer.parseInt(old_identification))));
        } catch (NoExistException e) {
            log.warn(e.getMessage());
        }
    }
}

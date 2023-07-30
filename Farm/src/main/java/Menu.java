import javax.swing.*;
import java.io.*;
import java.sql.SQLOutput;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Menu {
    private static final String PATH = "data.txt";

    private List<Animal> animals = new ArrayList<>();
    private Scanner scn = new Scanner(System.in);

    public void mainMenu() {
        while (true) {
            try {
                System.out.println("=".repeat(32) + "\n" +
                        "| Добро пожаловать в питомник! | \n" +
                        "=".repeat(32));
                System.out.println("Животных в питомнике на данный момент: " + Counter.getCount());
                System.out.println("Выберите необходимый пункт меню: \n" +
                        "1 - Добавить новое животное\n" +
                        "2 - Вывести тип животного\n" +
                        "3 - Вывести список всех животных\n" +
                        "4 - Вывести список команд животного\n" +
                        "5 - Обучить животное новой команде\n" +
                        "0 - Выход из программы");
                int input = scn.nextInt();
                switch (input) {
                    case 1 -> addNewAnimal();
                    case 2 -> System.out.println("Вид животного: " + showKindOfAnimal());
                    case 3 -> showAllAnimals();
                    case 4 -> System.out.println(showAvailableCommands());
                    case 5 -> trainNewCommand();
                    case 0 -> {
                        System.out.println("До новых встреч!");
                        System.exit(0);
                    }
                    default -> System.out.println("Выбран некорректный пункт меню!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Некорректный ввод!");
                scn.nextLine();
            }
        }
    }

    private void addNewAnimal() {
        while (true) {
            try {
                System.out.println("=".repeat(32) + "\n" +
                        "| Добавление нового животного | \n" +
                        "=".repeat(32));
                System.out.println("Кого хотите добавить? \n" +
                        "1 - Собаку\n" +
                        "2 - Кошку\n" +
                        "3 - Хомяка\n" +
                        "4 - Лошадь\n" +
                        "5 - Верблюда\n" +
                        "6 - Осла\n" +
                        "0 - Выйти в главное меню");
                int inputClass = scn.nextInt();
                if (inputClass < 0 || inputClass > 6) {
                    System.out.println("Неверный выбор!");
                    addNewAnimal();
                }
                if (inputClass == 0) {
                    mainMenu();
                }
                scn.nextLine();
                System.out.println("Введите имя: ");
                String name = scn.nextLine();
                System.out.println("Введите команды: ");
                String commands = scn.nextLine();
                LocalDate birthday = null;
                boolean correctBirthday = false;
                while (!correctBirthday) {
                    System.out.println("Введите дату рождения(гггг-мм-дд): ");
                    String inputDate = scn.nextLine();
                    try {
                        birthday = LocalDate.parse(inputDate);
                        correctBirthday = true;
                    } catch (DateTimeParseException e) {
                        System.out.println("Неверный формат ввода! Введите дату рождения по шаблону: гггг-мм-дд");
                    }
                }
                Animal animal = null;
                switch (inputClass) {
                    case 1 -> animal = new Dog(name, commands, birthday);
                    case 2 -> animal = new Cat(name, commands, birthday);
                    case 3 -> animal = new Hamster(name, commands, birthday);
                    case 4 -> animal = new Horse(name, commands, birthday);
                    case 5 -> animal = new Camel(name, commands, birthday);
                    case 6 -> animal = new Donkey(name, commands, birthday);
                    default -> {
                        System.out.println("Некорректный выбор");
                        addNewAnimal();
                    }
                }
                animals.add(animal);
                System.out.println("Животное добавлено!");
                saveData();
                try (Counter counter = new Counter()){
                    counter.add();
                } catch (Exception e) {
                    System.out.println("Ресурс не закрыт");
                }
            } catch (InputMismatchException e) {
                System.out.println("Некорректный ввод!");
                scn.nextLine();
            }
        }
    }

    private String showKindOfAnimal() {
        System.out.println("Введите имя животного: ");
        scn.nextLine();
        String input = scn.nextLine();
        for (Animal animal : animals) {
            if (input.equals(animal.getName())) {
                return animal.getClass().getSimpleName();
            }
        }
        return "Животное не найдено";
    }

    private void showAllAnimals() {
        for (Animal animal : animals) {
            System.out.println(animal);
        }
    }

    private String showAvailableCommands() {
        System.out.println("Введите имя животного: ");
        scn.nextLine();
        String input = scn.nextLine();
        for (Animal animal : animals) {
            if (input.equals(animal.getName())) {
                return "Команды животного: " + animal.getCommands();
            }
        }
        return "Животное не найдено";
    }

    private void trainNewCommand() {
        System.out.println("Введите имя животного: ");
        scn.nextLine();
        String input = scn.nextLine();
        boolean flag = false;
        for (Animal animal : animals) {
            if (input.equals(animal.getName())) {
                System.out.println("Введите новую команду: ");
                input = scn.nextLine();
                animal.setCommands(input);
                flag = true;
                saveData();
            }
        }
        if (!flag) {
            System.out.println("Животное не найдено.");
        }
    }
    private void saveData() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(PATH))) {
            for (Animal animal : animals) {
                String kind = animal.getClass().getSimpleName();
                String name = animal.getName();
                String commands = animal.getCommands();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String birthday = animal.getBirthday().format(formatter);
                String line = kind + " | " + name + " | " + commands + " | " + birthday + "\n";
                bw.write(line);
            }
            System.out.println("Изменения внесены в базу.");
        } catch (IOException e) {
            System.out.println("Ошибка при внесении изменений!");
        }
    }
    public void loadData() {
        try (BufferedReader br = new BufferedReader(new FileReader(PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(" \\| ");
                String kind = data[0];
                String name = data[1];
                String commands = data[2];
                LocalDate birthday = LocalDate.parse(data[3]);
                Animal animal = null;
                switch (kind) {
                    case "Dog" -> animal = new Dog(name, commands, birthday);
                    case "Cat" -> animal = new Cat(name, commands, birthday);
                    case "Hamster" -> animal = new Hamster(name, commands, birthday);
                    case "Horse" -> animal = new Horse(name, commands, birthday);
                    case "Camel" -> animal = new Camel(name, commands, birthday);
                    case "Donkey" -> animal = new Donkey(name, commands, birthday);
                }
                animals.add(animal);
                Counter.add();
            }
            System.out.println("База данных загружена");
        } catch (IOException e) {
            System.out.println("Ошибка при чтении данных!");
        }
    }
}

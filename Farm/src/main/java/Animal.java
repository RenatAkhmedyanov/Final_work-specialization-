import java.time.LocalDate;

public class Animal {
    private String name;
    private String commands;
    private LocalDate birthday;

    public Animal(String name, String commands, LocalDate birthday) {
        this.name = name;
        this.commands = commands;
        this.birthday = birthday;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommands() {
        return commands;
    }

    public void setCommands(String commands) {
        this.commands = this.commands + ", " + commands;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return  "Kind: " + getClass().getSimpleName() + "\n" +
                "Name: " + name + "\n" +
                "Commands: " + commands + "\n" +
                "Birthday: " + birthday + "\n" +
                "-".repeat(30);
    }
}

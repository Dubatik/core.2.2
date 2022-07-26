import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Main {

    private static List<Person> generateListPeople() {
        List<Person> persons = new ArrayList<>();

        List<String> names = Arrays.asList("Jack", "Connor", "Harry", "George", "Samuel", "John");
        List<String> families = Arrays.asList("Evans", "Young", "Harris", "Wilson", "Davies", "Adamson", "Brown");

        for (int i = 0; i < 10_000_000; i++) {
            persons.add(new Person(
                    names.get(new Random().nextInt(names.size())),
                    families.get(new Random().nextInt(families.size())),
                    new Random().nextInt(100),
                    Sex.values()[new Random().nextInt(Sex.values().length)],
                    Education.values()[new Random().nextInt(Education.values().length)]));
        }

        return persons;
    }

    private static <T> void printList(String title, List<T> list) {
        System.out.println(title);
        for (T obj : list) {
            System.out.println(obj);
        }
        System.out.println();
    }

    private static Predicate<Person> filterPerson(final Sex sex,
                                                  final int ageFrom,
                                                  final int ageTo,
                                                  final Education education) {
        return person -> person.getSex().equals(sex)
                && person.getAge() >= ageFrom
                && person.getAge() < ageTo
                && person.getEducation().equals(education);
    }

    public static void main(String[] args) {
        List<Person> persons = generateListPeople();

        long c = persons.stream()
                .filter(person -> person.getAge() < 18)
                .count();

        List<String> conscripts = persons.stream()
                .filter(person -> person.getSex() == Sex.MAN)
                .filter(person -> person.getAge() >= 18)
                .filter(person -> person.getAge() < 27)
                .map(Person::getFamily)
                .collect(Collectors.toList());

        List<Person> hardworkingPeople = persons.stream()
                .filter(filterPerson(Sex.MAN, 18, 65, Education.HIGHER)
                        .or(filterPerson(Sex.WOMAN, 18, 60, Education.HIGHER)))
                .sorted(Comparator.comparing(Person::getFamily))
                .collect(Collectors.toList());

        System.out.println("Количество несовершеннолетних: " + c);
        printList("Список фамилий призывников: ", conscripts);
        printList("Список работоспособных людей с высшим образованием, отсортированный по фамилии: ", hardworkingPeople);
    }
}
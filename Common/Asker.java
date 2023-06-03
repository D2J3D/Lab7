package Common;

import Common.core.*;

import java.io.Serializable;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Asker implements Serializable {
    private final Scanner sc;

    public Asker(Scanner sc){
        this.sc = sc;
    }

    public SpaceMarine ask(){
        System.out.print("Введите имя: ");
        String name = sc.next();
        System.out.println("Введите здоровье: ");
        Float health = Float.parseFloat(sc.next());
        Coordinates coordinates = getCoordinates(0);
        Chapter chapter = getChapter(0);
        System.out.print("Введите astartesCategory (DREADNOUGHT/TERMINATOR/APOTHECARY): ");
        AstartesCategory astartesCategory = AstartesCategory.valueOf(sc.next());
        System.out.print("Введите weaponType (BOLT_PISTOL/COMBI_FLAMER/MISSILE_LAUNCHER): ");
        Weapon weapon = Weapon.valueOf(sc.next());
        System.out.print("Введите meleeWeapon (CHAIN_SWORD/CHAIN_AXE/MANREAPER/POWER_BLADE/POWER_FIST): ");
        MeleeWeapon meleeWeapon = MeleeWeapon.valueOf(sc.next());
        return new SpaceMarine(name, coordinates, health, astartesCategory, weapon, meleeWeapon, chapter);
    }

    public Coordinates getCoordinates(int currentAttempt){
        int x = 0, y = 0;
        try{
            System.out.print("Введите x: ");
            x = Integer.parseInt(sc.next());
            System.out.print("Введите y: ");
            y = Integer.parseInt(sc.next());
        } catch (InputMismatchException e){
            System.out.println("x - целое число, попробуйте ввести его корректно");
            if (currentAttempt < 2){
                getCoordinates(currentAttempt++);
            }
        }
        return new Coordinates(x, y);
    }

    public Chapter getChapter(int currentAttempt){
        System.out.print("Введите chapterName: ");
        String chapterName = sc.next();
        System.out.print("Введите parentLegion: ");
        String parentLegion = sc.next();
        Integer marinesCount = 25;
        try{
            System.out.print("Введите marinesCount: ");
            marinesCount = Integer.parseInt(sc.next());
        } catch (InputMismatchException e){
            System.out.println("marinesCount - целое число, от 0 до 1000, попробуйте ввести его корректно");
            if (currentAttempt < 2){
                getCoordinates(currentAttempt++);
            }
        }
        System.out.print("Введите world: ");
        String world = sc.next();
        return new Chapter(chapterName, parentLegion, marinesCount, world);
    }
}

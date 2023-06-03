package Server.db_utils;

import Common.core.*;
import Server.collection_utils.ServerInvoker;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DBConnector {
    static final String DB_URL = "jdbc:postgresql://localhost:5432/dbmylabwork7";
    static final String USER = "postgres";
    static final String PASSWORD = "1";
    private final Connection conn;

    public DBConnector() throws SQLException {
        this.conn = connectDB();
    }

    public Connection connectDB() throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        return connection;
    }

    public List<SpaceMarine> fillUserCollection(Integer collectionOwnerId) throws SQLException {

        List<SpaceMarine> spaceMarineSynList = Collections.synchronizedList(new LinkedList<>());
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT id, name, coordinatesId, chapterId, collectionId, health, astartesCategory, weaponType, meleeWeapon FROM SpaceMarine WHERE collectionId = '" + collectionOwnerId + "'");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {

            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            float health = resultSet.getFloat("health");
            AstartesCategory astartesCategory = AstartesCategory.valueOf(resultSet.getString("astartesCategory"));
            Weapon weapon = Weapon.valueOf(resultSet.getString("weaponType"));
            MeleeWeapon meleeWeapon = MeleeWeapon.valueOf(resultSet.getString("meleeWeapon"));
            Coordinates coordinates = getCoordinates(resultSet.getInt("coordinatesId"));
            Chapter chapter = getChapter(resultSet.getInt("chapterId"));
            int collectionId = resultSet.getInt("collectionId");
            SpaceMarine spaceMarine = new SpaceMarine(id, collectionId, name, coordinates, health, astartesCategory, weapon, meleeWeapon, chapter);
            spaceMarineSynList.add(spaceMarine);
        }
        return spaceMarineSynList;
    }

    public Coordinates getCoordinates(int coordinatesId) throws SQLException {
        int x = 0, y = 0;
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT x, y FROM Coordinates WHERE coordinatesId = " + coordinatesId);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            x = resultSet.getInt("x");
            y = resultSet.getInt("y");
        }
        return new Coordinates(x, y);
    }

    public int setCoordinatesId(Coordinates coordinates) throws SQLException {
        PreparedStatement preparedStatement1 = conn.prepareStatement("SELECT COUNT(1) FROM coordinates WHERE x = " + coordinates.getX());
        PreparedStatement preparedStatement2 = conn.prepareStatement("SELECT COUNT(1) FROM coordinates WHERE y = " + coordinates.getY());
        ResultSet resultSet1 = preparedStatement1.executeQuery();

        int amountOfCoordinatesWithXSet = 0;
        while(resultSet1.next()){
            amountOfCoordinatesWithXSet = resultSet1.getInt("count");
        }
        ResultSet resultSet2 = preparedStatement2.executeQuery();
        int amountOfCoordinatesWithYSet = resultSet2.getInt("count");
        while(resultSet2.next()){
            amountOfCoordinatesWithYSet = resultSet2.getInt("count");
        }
        if (amountOfCoordinatesWithXSet + amountOfCoordinatesWithYSet == 2){
            PreparedStatement preparedStatement4 = conn.prepareStatement("SELECT coordinatesId FROM coordinates WHERE x = " + coordinates.getX());
            ResultSet resultSet5 = preparedStatement4.executeQuery();
            int ans = 1;
            while (resultSet5.next()){
                ans = resultSet5.getInt("coordinatesId");
            }
            return ans;

        }else{
            PreparedStatement preparedStatement3 = conn.prepareStatement("INSERT INTO coordinates VALUES (DEFAULT, " + coordinates.getX() + ", " + coordinates.getY() + ")");
            preparedStatement3.executeQuery();
            PreparedStatement newPreparedStatement = conn.prepareStatement("SELECT coordinatesid FROM coordinates WHERE x = " + coordinates.getX());
            ResultSet resultSet5 = newPreparedStatement.executeQuery();
            int ans = 1;
            while (resultSet5.next()){
                 ans = resultSet5.getInt("coordinatesId");
            }
            return ans;
        }

    }
    public Chapter getChapter(int chapterId) throws SQLException {
        String chapterName = "mars1";
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT name FROM Chapter WHERE chapterId = '" + chapterId+"'");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            chapterName = resultSet.getString("name");
        }
        return new Chapter(chapterName);
    }

    public void removeSpaceMarine(Integer spaceMarineId) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("DELETE FROM SpaceMarine WHERE id = '" + spaceMarineId+"'");
        preparedStatement.execute();
    }

    public void addSpaceMarine(SpaceMarine spaceMarine, Integer collectionId) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO spacemarine VALUES (DEFAULT, '" + spaceMarine.getName() + "', '" + spaceMarine.getHealth() + "', '" + spaceMarine.getAstartesCategory() + "', '" + spaceMarine.getWeaponType() + "', '" + spaceMarine.getMeleeWeapon() + "'," + setCoordinatesId(spaceMarine.getCoordinates()) + "," + 1 + ")");
        preparedStatement.execute();
    }

    public int setChapterId(Chapter chapter) throws SQLException {
        PreparedStatement preparedStatement1 = conn.prepareStatement("SELECT COUNT(1) FROM Chapter WHERE name = '" + chapter.getName()+"'");
        ResultSet resultSet1 = preparedStatement1.executeQuery();
        int amountOfChapterWithNameSet = 0;
        while(resultSet1.next()){
            amountOfChapterWithNameSet = resultSet1.getInt("count");
        }
        if (amountOfChapterWithNameSet == 1){
            PreparedStatement preparedStatement4 = conn.prepareStatement("SELECT chapterId FROM Chapter WHERE chapterId = '" + chapter.getName()+"'");
            ResultSet resultSet3 = preparedStatement4.executeQuery();
            return resultSet3.getInt("chapterId");
        }else{
            PreparedStatement preparedStatement3 = conn.prepareStatement("INSERT INTO Chapter VALUES (DEFAULT, '" + chapter.getName()+"', '" + chapter.getParentLegion() + "', '" + chapter.getMarinesCount() + "', '" + chapter.getWorld() + "')");
            ResultSet resultSet2 = preparedStatement3.executeQuery();
            return resultSet2.getInt("chapterId");
        }
    }

    public void registerUser(String login, String password) throws SQLException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-224");

        // digest() method is called
        // to calculate message digest of the input string
        // returned as array of byte
        byte[] messageDigest = md.digest(password.getBytes());

        // Convert byte array into signum representation
        BigInteger no = new BigInteger(1, messageDigest);

        // Convert message digest into hex value
        String hashPassword = no.toString(16);

        // Add preceding 0s to make it 32 bit
        while (hashPassword.length() < 32) {
            hashPassword = "0" + hashPassword;
        }
        PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO \"user\" VALUES (DEFAULT, '"+login+"', '"+hashPassword+"' )");
        preparedStatement.execute();
    }

    public Integer getUserId(String login, String password) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT userId FROM \"user\" WHERE userLogin='" + login + "'");
        ResultSet resultSet = preparedStatement.executeQuery();
        Integer userId = null;
        while(resultSet.next()){
            userId = resultSet.getInt("userId");
        }
        return userId;
    }

    public Integer loginUser(String login, String password) throws SQLException, NoSuchAlgorithmException {
        PreparedStatement checkLoginExistence = conn.prepareStatement("SELECT COUNT(1) FROM \"user\" WHERE userLogin = '" + login +"'");
        ResultSet resultSet1 = checkLoginExistence.executeQuery();
        int userAmount = 0;
        while(resultSet1.next()){
            userAmount = resultSet1.getInt("count");
        }
        if (userAmount > 0){
            System.out.println("Пользователь найден");
            PreparedStatement checkPassword = conn.prepareStatement("SELECT userPassword FROM \"user\" WHERE userLogin = '"+login+"'");
            ResultSet resultSet2 = checkPassword.executeQuery();
            String inputPassword = "";
            while(resultSet2.next()){
                inputPassword = resultSet2.getString("userPassword");
            }

            MessageDigest md = MessageDigest.getInstance("SHA-224");

            // digest() method is called
            // to calculate message digest of the input string
            // returned as array of byte
            byte[] messageDigest = md.digest(password.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashPassword = no.toString(16);

            // Add preceding 0s to make it 32 bit
            while (hashPassword.length() < 32) {
                hashPassword = "0" + hashPassword;
            }

            if (inputPassword.equals(hashPassword)){
                System.out.println("Авторизация пользователя " + login + " пройдена");
                PreparedStatement getUserId = conn.prepareStatement("SELECT userId FROM \"user\" WHERE userLogin = '" + login + "'");
                ResultSet resultSet = getUserId.executeQuery();
                Integer userId = null;
                while (resultSet.next()){
                    userId = resultSet.getInt("userId");
                }
                return userId;
            }
            else{
                System.out.println("Неправильный пароль");
                return -1;
            }
        } else{
            System.out.println("Пользователь с логином " + login + " не найден");
            return -1;
        }
    }
    public ArrayList<Integer> getUserCollections(String login, String password) throws SQLException {
        int userId = this.getUserId(login, password);
        PreparedStatement getUserCollections = conn.prepareStatement("SELECT collectionid FROM spacemarines WHERE userid = " +userId+ "");
        ResultSet resultSet = getUserCollections.executeQuery();
        ArrayList<Integer> userCollectionIds = new ArrayList<>();
        while (resultSet.next()){
            int collectionId = resultSet.getInt("userId");
            userCollectionIds.add(collectionId);
        }
        return userCollectionIds;
    }

}

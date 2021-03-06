package jdbcdemo.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import jdbcdemo.domain.Person;

public class PersonRepository {

	Connection connection;
	
	private String createTableSql = "CREATE TABLE person("
			+ "id INT GENERATED BY DEFAULT AS IDENTITY,"
			+ "name VARCHAR(20),"
			+ "surname VARCHAR(50),"
			+ "age INT"
			+ ")";
	
	private String insertSql = "INSERT INTO person(name,surname,age) VALUES (?,?,?)";
	
	private String selectAllSql = "SELECT * FROM person";
	
	Statement createTable;
	PreparedStatement insert;
	PreparedStatement selectAll;
	
	public PersonRepository(){
		
		try {
			connection = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/workdb");
			createTable = connection.createStatement();
			insert = connection.prepareStatement(insertSql);
			selectAll = connection.prepareStatement(selectAllSql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<Person> getAll(){
		List<Person> result = new ArrayList<Person>();
		try {
			ResultSet rs = selectAll.executeQuery();
			while(rs.next()){
				Person p = new Person();
				p.setId(rs.getInt("id"));
				p.setName(rs.getString("name"));
				p.setSurname(rs.getString("surname"));
				p.setAge(rs.getInt("age"));
				result.add(p);
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public void add(Person person){
		try{
			insert.setString(1, person.getName());
			insert.setString(2, person.getSurname());
			insert.setInt(3, person.getAge());
			insert.executeUpdate();
		}catch(SQLException ex){
			ex.printStackTrace();
		}
	}
	
	public void createTable(){
		try {
			
			ResultSet rs = connection.getMetaData().getTables(null, null, null, null);
			boolean tableExists = false;
			while(rs.next()){
				if(rs.getString("TABLE_NAME").equalsIgnoreCase("person")){
					tableExists=true;
					break;
				}
			}
			if(!tableExists)
				createTable.executeUpdate(createTableSql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
}

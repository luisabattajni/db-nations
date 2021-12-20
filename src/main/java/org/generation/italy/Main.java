package org.generation.italy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {

	private final static String URL = "jdbc:mysql://localhost:3306/db-nations";
	private final static String USER = "root";
	private final static String PASSWORD = "fancypass_BC!!09";
	
	
	public static void main(String[] args) {
		try(Connection con = DriverManager.getConnection(URL, USER, PASSWORD)){
			
			String query = "SELECT c.country_id AS `id_nazione`, c.name AS `nome_nazione`, r.name AS `nome_regione`, c2.name AS `nome_continente`\n"
					+ "FROM countries c \n"
					+ "INNER JOIN regions r ON c.region_id = r.region_id \n"
					+ "INNER JOIN continents c2 ON r.continent_id = c2.continent_id \n"
					+ "ORDER BY c.name;";
			
			try(PreparedStatement ps = con.prepareStatement(query)){
				
				
				try(ResultSet rs = ps.executeQuery()){
					while(rs.next()) {
						int countryId = rs.getInt(1);
						String countryName = rs.getString(2);
						String regionName = rs.getString(3);
						String continentName = rs.getString(4);
						
//						System.out.print("Id nazione: " + countryId + " ");
//						System.out.println("Nome nazione: " + countryName + " ");
//						System.out.print("Nome zona: " + regionName + " ");
//						System.out.println("Nome continente: " + continentName);
						
						System.out.println(countryId + " - " + countryName + " - " + regionName + " - " + continentName);
					}
				}
				
			}

	} catch(SQLException e) {

		e.printStackTrace();
	}
	}

}

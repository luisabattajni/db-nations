package org.generation.italy;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

	private final static String URL = "jdbc:mysql://localhost:3306/db-nations";
	private final static String USER = "root";
	private final static String PASSWORD = "fancypass_BC!!09";

	public static void main(String[] args) {
		// open scanner
		Scanner scan = new Scanner(System.in);

		try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {

			System.out.print("Inserisci una stringa da ricercare: ");
			String findThis = scan.nextLine();

			String query = "SELECT c.country_id AS `id_nazione`, c.name AS `nome_nazione`, r.name AS `nome_regione`, c2.name AS `nome_continente`\n"
					+ "FROM countries c \n" + "INNER JOIN regions r ON c.region_id = r.region_id \n"
					+ "INNER JOIN continents c2 ON r.continent_id = c2.continent_id \n" + "WHERE c.name LIKE ? \n"
					+ "ORDER BY c.name;";

			try (PreparedStatement ps = con.prepareStatement(query)) {
				ps.setString(1, "%" + findThis + "%");

				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						int countryId = rs.getInt(1);
						String countryName = rs.getString(2);
						String regionName = rs.getString(3);
						String continentName = rs.getString(4);

						System.out
								.println(countryId + " - " + countryName + " - " + regionName + " - " + continentName);

					}
				}

			}

			System.out.println("Inserisci uno degli ID trovati:");
			int inputId = Integer.parseInt(scan.nextLine());
			System.out.println("Ulteriori dettagli:");
//			System.out.println("\n");
			System.out.print("Lingue parlate: ");

			String sql = "SELECT `language`\n" + "FROM languages l \n"
					+ "INNER JOIN country_languages cl ON cl.language_id = l.language_id \n"
					+ "INNER JOIN countries c ON cl.country_id = c.country_id\n" + "WHERE c.country_id = ? \n"
					+ "ORDER BY `language`;";

			try (PreparedStatement psLingue = con.prepareStatement(sql)) {
				psLingue.setInt(1, inputId);

				try (ResultSet rsLingue = psLingue.executeQuery()) {
					while (rsLingue.next()) {

						String lingue = rsLingue.getString(1);

						System.out.print(lingue + "; ");

					}
				}
			}
			System.out.println("\n");
			System.out.println("Statistiche più recenti:");

			String sql2 = "SELECT cs.`year`, cs.population, cs.gdp \n" + "FROM country_stats cs\n"
					+ "INNER JOIN countries c ON cs.country_id = c.country_id \n" + "WHERE c.country_id = ? \n"
					+ "ORDER BY cs.`year` DESC\n" + "LIMIT 1;";

			try (PreparedStatement psStats = con.prepareStatement(sql2)) {
				psStats.setInt(1, inputId);

				try (ResultSet rsStats = psStats.executeQuery()) {
					while (rsStats.next()) {

						int year = rsStats.getInt(1);
						int population = rsStats.getInt(2);
						BigDecimal gdp = rsStats.getBigDecimal(3);

						System.out.println("Anno: " + year);
						System.out.println("Popolazione: " + population);
						System.out.println("GDP: " + gdp);

					}
				}
			}

		} catch (SQLException e) {

			System.out.println("Errore");
			System.out.println(e.getMessage());
		}
		// close scanner
		scan.close();
	}

}

package personal.jdbc.orm;

import personal.jdbc.orm.dao.DatabaseManager;

import java.io.PrintStream;
import java.util.Scanner;


public class Main {
	private static final Scanner in = new Scanner(System.in);
	private static final PrintStream out = System.out;

	public static void main(String[] args) {
		DatabaseManager dbm = new DatabaseManager();
	}
}



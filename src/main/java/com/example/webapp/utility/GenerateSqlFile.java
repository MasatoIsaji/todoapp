package com.example.webapp.utility;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * テスト時に使用する大量データを生成するクラス
 */
public class GenerateSqlFile {

	public static void main(String[] args) throws IOException {
		Scanner sc = new Scanner(System.in);

		System.out.println("""
				*************************
				*	SQLダミーデータ作成ツール *
				*************************
								""");

		System.out.print("作成するユーザーの数(MAX49)：");
		int userLength = sc.nextInt();

		System.out.print("作成するToDoの数：");
		int todoLength = sc.nextInt();

		// resouces直下に出力(ファイルが存在しる場合は上書きされる)
		String filePath = "./src/main/resources/sql_queries.sql";

		BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));

		// SQL文字列を生成
		String insertUserSql = generateDummySql(userLength);
		String insertToDoSql = generateToDoSql(todoLength);

		// ファイルに書き込み
		writer.write(insertUserSql); // ユーザーデータ
		writer.newLine(); // 改行
		writer.write(insertToDoSql); // ToDoデータ
		writer.close();

		System.out.println("SQL File completed!");
		System.out.println("FilePath： " + filePath);
		System.out.println();
		System.out.println("作成したファイルはsql_queries.sqlです。");
		System.out.println("SpringBoot起動時に実行させる場合、data.sqlにリネームしてください。");

	}

	private static String generateToDoSql(int todoLength) {
		String query = "";
		for (int i = 1; i <= todoLength; i++) {
			String todoNumber = "ToDo" + i;
			query += "INSERT INTO todos (todo,username,status,detail,created_at,updated_at)VALUES('" + todoNumber
					+ "','admin,false,'テストToDo',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);\n";
			// 作成したToDoを出力
			System.out.println("ToDo：" + todoNumber);
		}
		return query;

	}

	private static String generateDummySql(int userLength) {
		// 管理者を追加
		String query = "INSERT INTO authentications (username, password, authority)VALUES('admin','$2a$10$lNH4dLsCH4/g7aZZq14QG.PvnC7rkeN395ZWanW/hTOi5k6y009mm','ADMIN');\n";
		query += "-- password：userpass\n";

		for (int i = 1; i <= userLength; i++) {
			// primery keyのusernameを可変させる
			String username = "user" + i;

			// クエリ作成
			query += "INSERT INTO authentications (username, password, authority) VALUES('"
					+ username + "','$2a$10$/jar9xXQ6lrnVjLvLGv5BepFkLnGIO49RrGx42p2i.1hQt1BZ/7E2','USER');\n";

			// 作成したusernameを出力
			System.out.println("username：" + username + " created.");
		}

		return query;
	}

}

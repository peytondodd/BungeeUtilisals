package com.dbsoftware.bungeeutilisals.api;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;

public class MySQL {
	
	private static MySQL instance = new MySQL();
	
	public static MySQL getInstance(){
		return instance;
	}
	
	public select select(){
		return new select();
	}
	
	public insert insert(){
		return new insert();
	}
	
	public update update(){
		return new update();
	}
	
	public delete delete(){
		return new delete();
	}
	
	
	public class delete {
		
		private String table, where, equals;
		
		public delete table(String table) {
			this.table = table;
			return this;
		}
		
		public delete where(String where) {
			this.where = where;
			return this;
		}

		public delete equals(String equals) {
			this.equals = equals;
			return this;
		}

		public void delete() {
			try {
				PreparedStatement preparedStatement;
				StringBuilder sb = new StringBuilder();

				sb.append("DELETE FROM `");
				sb.append(table);
				sb.append("` WHERE `");
				sb.append(where);
				sb.append("` = ?;");

				preparedStatement = BungeeUtilisals.getInstance().getDatabaseManager().getConnection().prepareStatement(sb.toString());
				preparedStatement.setString(1, equals);
				preparedStatement.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public enum UpdateType{SET}
	
	public class update {
		
		private UpdateType type;
		private String column, table, where, equals, to;

		public update type(UpdateType type) {
			this.type = type;
			return this;
		}
		
		public update to(String to){
			this.to = to;
			return this;
		}

		public update table(String table) {
			this.table = table;
			return this;
		}

		public update column(String column) {
			this.column = column;
			return this;
		}

		public update where(String where) {
			this.where = where;
			return this;
		}

		public update equals(String equals) {
			this.equals = equals;
			return this;
		}

		public void update() {
			try {
				PreparedStatement preparedStatement;
				StringBuilder sb = new StringBuilder();

				sb.append("UPDATE `");
				sb.append(table);
				sb.append("` " + type.toString() + " `");

				sb.append(column);
				sb.append("` = ? ");
				sb.append(" WHERE `");
				sb.append(where);
				sb.append("` = ?;");

				preparedStatement = BungeeUtilisals.getInstance().getDatabaseManager().getConnection().prepareStatement(sb.toString());
				preparedStatement.setString(1, to);
				preparedStatement.setString(2, equals);
				preparedStatement.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public class insert {
		
		public insertsingle single(){
			return new insertsingle();
		}
		
		public class insertsingle {
			
			private String table,column,value;
			
			public insertsingle table(String table){
				this.table = table;
				return this;
			}
			
			public insertsingle column(String column){
				this.column = column;
				return this;
			}
			
			public insertsingle value(String value){
				this.value = value;
				return this;
			}
			
			public void insert(){
				try {
					PreparedStatement preparedStatement;
					
					StringBuilder sb = new StringBuilder();
					sb.append("INSERT INTO ");
					sb.append(table);
					sb.append("(");
					sb.append("`" + column + "`");
					sb.append(") VALUES (");
					sb.append("'" + value + "'");
					sb.append(");");
											
					preparedStatement = BungeeUtilisals.getInstance().getDatabaseManager().getConnection().prepareStatement(sb.toString());
					preparedStatement.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		private String table;
		private List<String> columns;
		private List<Object> values;
		
		public insert table(String table){
			this.table = table;
			return this;
		}
		
		public insert columns(List<String> columns){
			this.columns = columns;
			return this;
		}
		
		public insert values(List<Object> values){
			this.values = values;
			return this;
		}
		
		public void insert(){
			try {
				PreparedStatement preparedStatement;
				String cfirst = columns.get(0);
				columns.remove(cfirst);
				
				Object vfirst = values.get(0);
				values.remove(vfirst);
				
				StringBuilder sb = new StringBuilder();
				sb.append("INSERT INTO ");
				sb.append(table);
				sb.append("(");
				sb.append("`" + cfirst + "`");
				for(String column : columns){
					sb.append(", `" + column + "`");
				}
				sb.append(") VALUES (");
				sb.append("'" + vfirst + "'");
				for(Object value : values){
					sb.append(", '" + value + "'");
				}
				sb.append(");");
										
				preparedStatement = BungeeUtilisals.getInstance().getDatabaseManager().getConnection().prepareStatement(sb.toString());
				preparedStatement.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public enum WhereType{EQUALS("="),HIGHER(">"),LOWER("<"),LOWEROREQUALS("<="),HIGHEROREQUALS(">="),LOWERORHIGHER("<>");
		private String sql;
		WhereType(String sql){
			this.sql = sql;
		}
		public String getSQL(){
			return sql;
		}
	}
	public class select {
		
		private String column,table,where,equals;
		private WhereType type;
		
		public select table(String table){
			this.table = table;
			return this;
		}
		
		public select column(String column){
			this.column = column;
			return this;
		}
		
		public select where(String where){
			this.where = where;
			return this;
		}
		
		public select wheretype(WhereType type){
			this.type = type;
			return this;
		}
		
		public select wherereq(String equals){
			this.equals = equals;
			return this;
		}
		
		public ResultSet select(){
			try {
				PreparedStatement preparedStatement;
				StringBuilder sb = new StringBuilder();
				
				if(column.equals("*")){
					sb.append("SELECT ");
					sb.append(column);
					sb.append(" FROM `");
				} else {
					sb.append("SELECT `");
					sb.append(column);
					sb.append("` FROM `");
				}
				sb.append(table);
				if(where != null){
					sb.append("` WHERE `");
					sb.append(where);
					
					sb.append("` ");
					sb.append(type.getSQL());
					sb.append(" ?;");
				} else {
					sb.append("`;");
				}

				
				preparedStatement = BungeeUtilisals.getInstance().getDatabaseManager().getConnection().prepareStatement(sb.toString());
				
				if(equals != null){
					preparedStatement.setString(1, equals);
				}
				preparedStatement.executeQuery();
				
				return preparedStatement.getResultSet();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return null;
		}
	}
}
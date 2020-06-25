package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import model.CrudStuff;

public class CrudDaoStuff implements CrudStuffDao {

	private CrudDaoStuff() {

	}

	private static class SingletonHelper {
		private static final CrudDaoStuff INSTANCE = new CrudDaoStuff();
	}

	public static CrudDaoStuff getInstance() {
		return SingletonHelper.INSTANCE;
	}

	@Override
	public Optional<CrudStuff> find(String id) throws SQLException {
		String sql = "SELECT CRUD_ID,NAME, DESCRIPTION, QUANTITY,LOCATION From TABLECRUD Where CRUD_ID=?";
		int CRUD_ID = 0;
		int QUANTITY = 0;
		String NAME = " ";
		String DESCRIPTION = " ";
		String LOCATION = " ";
		Connection conn = DataSourceFactory.getConnection();
		PreparedStatement statement = conn.prepareStatement(sql);
		statement.setString(1, id);
		ResultSet resultSet = statement.executeQuery();
		if (resultSet.next()) {
			CRUD_ID = resultSet.getInt("CRUD_ID");
			NAME = resultSet.getString("NAME");
			DESCRIPTION = resultSet.getString("DESCRIPTION");
			QUANTITY = resultSet.getInt("QUANTITY");
			LOCATION = resultSet.getString("LOCATION");
		}
		return Optional.of(new CrudStuff(CRUD_ID, NAME, DESCRIPTION, QUANTITY, LOCATION));
	}

	@Override
	public List<CrudStuff> findAll() throws SQLException {
		List<CrudStuff> listStuff = new ArrayList<>();
		String sql = "SELECT CRUD_ID,NAME, DESCRIPTION, QUANTITY,LOCATION From TABLECRUD Where CRUD_ID=?";
		Connection conn = DataSourceFactory.getConnection();
		Statement statement = conn.createStatement();
		ResultSet resultSet = statement.executeQuery(sql);
		while (resultSet.next()) {
			int id = resultSet.getInt("CRUD_ID");
			String name = resultSet.getString("NAME");
			String description = resultSet.getString("DESCRIPTION");
			int quantity = resultSet.getInt("QUANTITY");
			String location = resultSet.getString("LOCATION");
			CrudStuff stuff = new CrudStuff(id, name, description, quantity, location);
			listStuff.add(stuff);
		}

		return listStuff;
	}

	@Override
	public boolean save(CrudStuff o) throws SQLException {
		String sql = "INSERT into TABLECRUD(NAME,DESCRIPTION,QUANTITY,LOCATION) VALUES( ?, ?, ?, ?) ";
		boolean rowInserted = false;
		Connection conn = DataSourceFactory.getConnection();
		PreparedStatement statement = conn.prepareStatement(sql);
		statement.setString(1, o.getName());
		statement.setString(2, o.getDescription());
		statement.setInt(3, o.getQuantity());
		statement.setString(4, o.getLocation());
		rowInserted = statement.executeUpdate() > 0;

		return rowInserted;
	}

	@Override
	public boolean update(CrudStuff o) throws SQLException {

		String sql = "UPDATE TABLECRUD SET NAME =?, DESCRIPTION=?, QUANTITY=?, LOCATION=?";
		sql += "  WHERE CRUD_ID=?";
		boolean rowUpdateted = false;
		Connection conn = DataSourceFactory.getConnection();
		PreparedStatement statement = conn.prepareStatement(sql);
		statement.setString(1, o.getName());
		statement.setString(2, o.getDescription());
		statement.setInt(3, o.getQuantity());
		statement.setString(4, o.getLocation());
		rowUpdateted = statement.executeUpdate() > 0;

		return rowUpdateted;
	}

	@Override
	public boolean delete(CrudStuff o) throws SQLException {

		String sql = "DELETE FROM TABLECRUD where CRUD_ID=?";
		boolean rowDeleted = false;
		Connection conn = DataSourceFactory.getConnection();
		PreparedStatement statement = conn.prepareStatement(sql);
		statement.setInt(1, o.getId());
		rowDeleted = statement.executeUpdate() > 0;
		return rowDeleted;
	}

}

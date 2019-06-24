package it.polito.tdp.poweroutages.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import it.polito.tdp.poweroutages.model.Nerc;

public class PowerOutagesDAO {
	
	public List<Nerc> loadAllNercs(Map<Integer, Nerc>idMap) {

		String sql = "SELECT id, value FROM nerc";
		List<Nerc> nercList = new ArrayList<>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				if(idMap.get(res.getInt("id"))==null) {
				Nerc n = new Nerc(res.getInt("id"), res.getString("value"));
				idMap.put(n.getId(), n);
				nercList.add(n);
			}}
			

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return nercList;
	}
	public List<Nerc> getVicini(Map<Integer,Nerc> idMap, Nerc nerc){
		

		String sql = "SELECT nerc_one FROM nercrelations WHERE nerc_two =?";
		List<Nerc> nercList = new ArrayList<>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, nerc.getId());
			ResultSet res = st.executeQuery();

			while (res.next()) {
				
				Nerc n = idMap.get(res.getInt("nerc_one"));
				if(n==null) {
					System.out.println("Errore");
					
				}
				else
				nercList.add(n);
			}
			

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return nercList;
		
	}
	public int getCorrelazione(Nerc n1, Nerc n2) {
		String sql = "SELECT DISTINCT YEAR(p1.date_event_began),  month(p1.date_event_began)\n" + 
				"FROM poweroutages AS p1, poweroutages AS p2 \n" + 
				"WHERE p1.nerc_id = ? AND p2.nerc_id = ? AND MONTH(p1.date_event_began)= MONTH(p2.date_event_began)\n" + 
				"AND YEAR(p1.date_event_began)=YEAR(p2.date_event_began)";
		int count=0;		
		try {
					Connection conn = ConnectDB.getConnection();
					PreparedStatement st = conn.prepareStatement(sql);
					st.setInt(1, n1.getId());
					st.setInt(2, n2.getId());
					

					ResultSet res = st.executeQuery();

					while (res.next()) {
					count ++;	
						
					}
					

					conn.close();

				} catch (SQLException e) {
					throw new RuntimeException(e);
				}

				return count;

	}
}

package pers.sivous.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class LoginService {
	//模拟数据库
	private static HashMap<String, String> map = new HashMap<>();
	static {
		map.put("1", "1");
		map.put("2", "2");
		map.put("3", "3");
	}
	
	public boolean Login(String username,String password) {
		Iterator it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Entry) it.next();
			String user = (String) entry.getKey();
			String pwd = (String) entry.getValue();
			if (user.equals(username)&&pwd.equals(password)) {
				return true;
			}		
		}
		return false;
	}
	
	public HashMap getMap() {
		return this.map;
	}
}

package main.java.com.inventory.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCUtil {

        private static final Properties properties = new Properties() ;

        static {
            try ( InputStream input = JDBCUtil.class.getClassLoader().getResourceAsStream("db.properties") ) {
                if(input != null)
                    properties.load(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static Connection getConnection() throws SQLException {
            String url = properties.getProperty("db.url") ;
            String root = properties.getProperty("db.username");
            String passWord = properties.getProperty("db.password") ;

            return DriverManager.getConnection(url, root, passWord);
        }

}

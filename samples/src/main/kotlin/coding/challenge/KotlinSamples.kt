package coding.challenge

import java.sql.DriverManager
import java.sql.DriverManager.println
import java.sql.ResultSet
import java.sql.SQLException

class KotlinSamples {








    fun someArray() {
        val ary = Array(Int.MAX_VALUE) { LongArray(Int.MAX_VALUE) }
        println(ary.size.toString())
    }













    fun someSql(username: String): ResultSet? {
        try {
            val c = DriverManager.getConnection("jdbc:sqlite:/tmp/my_database:127.0.0.1")
            val s = c.createStatement()

            return s.executeQuery("SELECT secret FROM Users " +
                    "WHERE (username = '$username' AND NOT role = 'admin')")
        }
        catch (e: SQLException) {

        }

        return null
    }
}

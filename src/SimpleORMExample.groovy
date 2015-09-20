import groovy.sql.*

class SimpleORMExample {
	
	static main(args) {
		
		println "Starting..."
		
		//connect to the DB
		def db = Sql.newInstance(
			url:"jdbc:mysql://localhost/mysql",
			user:"root", password:"",
			driver:"com.mysql.jdbc.Driver"
		)
		
		//create an instance of the orm
		def orm = new GSimpleORM(db)
		
		//Dynamic syntax (other clauses are optional Maps)
		def q1 = orm.select '*' from User//, [where:"host ='localhost'"]
		
		//Execute query
		def res = orm.query(q1)
		
		println "host: " + res[0].host
		
		//print out outputs
		res.each { println it.host}
		
		//Custom SQL
		orm.query('select count(*) nbOfUser from User').each {
			assert it['nbOfUser'] == res.size()
		}
		
		
		//Explicit syntax for inline query (but not static compile)
		orm.query(orm.select().from(User,[where:"user='admin'"])) each {
			println it/*.user.toUpperCase()*/
		}

		//Advanced query (every clause w'll be injected to the query)
		def q2 = orm.select 'user, host' from User, [where:"password is not null"], [groupBy: 'host'], [having:'count(host) > 0']
		println orm.query(q2)

		//Close connection
		orm.close()
		
		println "done"
		
	} //main()
	
}

//Model
class User {
	def host, user
	String toString() { "$host:$user"}
}
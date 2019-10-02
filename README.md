# GSimpleORM (Groovy Simple ORM)

This is a very Simple ORM (Object Relation Mapping) for Groovy language

Based on a snippet Groovy Sql at snipplr.com:
http://snipplr.com/view/10220/

GSimpleORM extends Sql using command-chains and Groovy's DSL capabilites to make writing SQL queries easy and readable, it doesn't use class Builder or syntax parser.

# Example
```groovy
//connect to the DB
def db = Sql.newInstance(
	url:"jdbc:mysql://localhost/mysql",
	user:"root", password:"",
	driver:"com.mysql.jdbc.Driver"
)

//create an instance of the orm
def orm = new GSimpleORM(db)

//Dynamic syntax (other clauses are optional Maps)
def sql1 = orm.select '*' from User//, [where:"host ='localhost'"]

//Execute query
def res = orm.query(sql1)

//print out outputs
res.each { println it.host}

//Custom SQL
orm.query('select count(*) nbOfUser from User').each {
	assert it['nbOfUser'] == res.size()
}

//Close connection
orm.close()

//User Model
class User {
def host, user
String toString() { "$host:$user"}
}
```

more usage examples in the source.
note: this example use "User" table database wich comes with mysql by default.

# License
GSimpleORM is licensed under the terms of the Apache License, Version 2.0

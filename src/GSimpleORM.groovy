import groovy.sql.Sql

class GSimpleORM {
	
	Sql db
	
		def GSimpleORM(url, user, password, driver){
			GSimpleORM(Sql.newInstance(url, user, password, driver))
		}
		
		def GSimpleORM(s){
		db = s
		// Coerce
		groovy.sql.GroovyRowResult.metaClass.coerce << {Class clazz ->
			def resultant = clazz.newInstance()
			def targetProps = [:]
			(resultant.properties.keySet() - ["metaClass","class"]).each({
				targetProps."${it.toLowerCase()}" = it
			})
			delegate.keySet().each {
				def property = targetProps."${it.toLowerCase().replaceAll('_','')}"
				if(property){
				  resultant."$property" = delegate."$it"
				}
			}
			return resultant
		  }
		
		  // GroovyRowResult.Transmogrify
		  groovy.sql.GroovyRowResult.metaClass.transmogrify {Class clazz ->
			delegate.coerce(clazz)
		  }
		  
		  // DataSet.Transmogrify
		  groovy.sql.DataSet.metaClass.transmogrify << {Class clazz ->
			  delegate.rows().collect {row ->
				  row.coerce(clazz)
			  }
		  }
		  
		  //Sql.metaClass.transmogrify
		  groovy.sql.Sql.metaClass.transmogrify << {String sql, Class clazz ->
			  delegate.rows(sql).collect {row ->
				  row.coerce(clazz)
			  }
		  }
		  	  
		  groovy.sql.Sql.metaClass.select << {...fields ->
			  [from:
				{ def table, ...clauses -> 
					String r = "select ${fields.join(',')} from ${table.name}"
						clauses*.each { clause, expr ->
							switch (clause){
								case 'orderBy': r += " order by $expr"; break 
								case 'groupBy': r += " group by $expr"; break 
								default: r += " $clause $expr"
							}
						}
					r
				}]
		  }
		  
	} //GSimpleORM()
	
		def select(...fields = '*'){
			db.select(fields)
		}
		def close(){
			db.close()
		}
		def query(sql){
			db.rows(sql)
		}

} //class GSimpleORM


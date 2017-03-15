/**
 * This class is generated by jOOQ
 */
package org.javaclub.opensource.demos.jooq;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(value    = "http://jooq.sourceforge.net",
                            comments = "This class is generated by jOOQ")
public class TestJooqFactory extends org.jooq.util.mysql.MySQLFactory {

	private static final long serialVersionUID = -341454567;

	/**
	 * Create a factory with a connection
	 *
	 * @param connection The connection to use with objects created from this factory
	 */
	public TestJooqFactory(java.sql.Connection connection) {
		super(connection);
	}

	/**
	 * Create a factory with a connection and a schema mapping
	 *
	 * @param connection The connection to use with objects created from this factory
	 * @param mapping The schema mapping to use with objects created from this factory
	 */
	public TestJooqFactory(java.sql.Connection connection, org.jooq.SchemaMapping mapping) {
		super(connection, mapping);
	}
}

/**
 * This class is generated by jOOQ
 */
package org.javaclub.opensource.demos.jooq;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(value    = "http://jooq.sourceforge.net",
                            comments = "This class is generated by jOOQ")
public class TestJooq extends org.jooq.impl.SchemaImpl {

	private static final long serialVersionUID = -1691562580;

	/**
	 * The singleton instance of test_jooq
	 */
	public static final TestJooq TEST_JOOQ = new TestJooq();

	/**
	 * No further instances allowed
	 */
	private TestJooq() {
		super("test_jooq");
	}

	@Override
	public final java.util.List<org.jooq.Table<?>> getTables() {
		return java.util.Arrays.<org.jooq.Table<?>>asList(
			org.javaclub.opensource.demos.jooq.tables.TAuthor.T_AUTHOR,
			org.javaclub.opensource.demos.jooq.tables.TBook.T_BOOK,
			org.javaclub.opensource.demos.jooq.tables.TBookStore.T_BOOK_STORE,
			org.javaclub.opensource.demos.jooq.tables.TBookToBookStore.T_BOOK_TO_BOOK_STORE,
			org.javaclub.opensource.demos.jooq.tables.TLanguage.T_LANGUAGE);
	}
}

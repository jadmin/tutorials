/**
 * This class is generated by jOOQ
 */
package org.javaclub.opensource.demos.jooq.tables.records;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(value    = "http://jooq.sourceforge.net",
                            comments = "This class is generated by jOOQ")
public class TBookStoreRecord extends org.jooq.impl.UpdatableRecordImpl<org.javaclub.opensource.demos.jooq.tables.records.TBookStoreRecord> {

	private static final long serialVersionUID = 1639858598;

	/**
	 * An uncommented item
	 */
	public void setName(java.lang.String value) {
		setValue(org.javaclub.opensource.demos.jooq.tables.TBookStore.NAME, value);
	}

	/**
	 * An uncommented item
	 */
	public java.lang.String getName() {
		return getValue(org.javaclub.opensource.demos.jooq.tables.TBookStore.NAME);
	}

	/**
	 * An uncommented item
	 */
	public java.util.List<org.javaclub.opensource.demos.jooq.tables.records.TBookToBookStoreRecord> fetchTBookToBookStoreList() throws java.sql.SQLException {
		return create()
			.selectFrom(org.javaclub.opensource.demos.jooq.tables.TBookToBookStore.T_BOOK_TO_BOOK_STORE)
			.where(org.javaclub.opensource.demos.jooq.tables.TBookToBookStore.BOOK_STORE_NAME.equal(getValue(org.javaclub.opensource.demos.jooq.tables.TBookStore.NAME)))
			.fetch();
	}

	/**
	 * Create a detached TBookStoreRecord
	 */
	public TBookStoreRecord() {
		super(org.javaclub.opensource.demos.jooq.tables.TBookStore.T_BOOK_STORE);
	}

	/**
	 * Create an attached TBookStoreRecord
	 */
	public TBookStoreRecord(org.jooq.Configuration configuration) {
		super(org.javaclub.opensource.demos.jooq.tables.TBookStore.T_BOOK_STORE, configuration);
	}
}

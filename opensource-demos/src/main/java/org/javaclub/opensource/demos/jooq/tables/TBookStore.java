/**
 * This class is generated by jOOQ
 */
package org.javaclub.opensource.demos.jooq.tables;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(value    = "http://jooq.sourceforge.net",
                            comments = "This class is generated by jOOQ")
public class TBookStore extends org.jooq.impl.UpdatableTableImpl<org.javaclub.opensource.demos.jooq.tables.records.TBookStoreRecord> {

	private static final long serialVersionUID = 1439054228;

	/**
	 * The singleton instance of t_book_store
	 */
	public static final org.javaclub.opensource.demos.jooq.tables.TBookStore T_BOOK_STORE = new org.javaclub.opensource.demos.jooq.tables.TBookStore();

	/**
	 * The class holding records for this type
	 */
	private static final java.lang.Class<org.javaclub.opensource.demos.jooq.tables.records.TBookStoreRecord> __RECORD_TYPE = org.javaclub.opensource.demos.jooq.tables.records.TBookStoreRecord.class;

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<org.javaclub.opensource.demos.jooq.tables.records.TBookStoreRecord> getRecordType() {
		return __RECORD_TYPE;
	}

	/**
	 * An uncommented item
	 */
	public static final org.jooq.TableField<org.javaclub.opensource.demos.jooq.tables.records.TBookStoreRecord, java.lang.String> NAME = new org.jooq.impl.TableFieldImpl<org.javaclub.opensource.demos.jooq.tables.records.TBookStoreRecord, java.lang.String>("name", org.jooq.impl.SQLDataType.VARCHAR, T_BOOK_STORE);

	/**
	 * No further instances allowed
	 */
	private TBookStore() {
		super("t_book_store", org.javaclub.opensource.demos.jooq.TestJooq.TEST_JOOQ);
	}

	@Override
	public org.jooq.UniqueKey<org.javaclub.opensource.demos.jooq.tables.records.TBookStoreRecord> getMainKey() {
		return org.javaclub.opensource.demos.jooq.Keys.KEY_t_book_store_name;
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.List<org.jooq.UniqueKey<org.javaclub.opensource.demos.jooq.tables.records.TBookStoreRecord>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<org.javaclub.opensource.demos.jooq.tables.records.TBookStoreRecord>>asList(org.javaclub.opensource.demos.jooq.Keys.KEY_t_book_store_name);
	}
}

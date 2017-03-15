package org.javaclub.opensource.demos.hibernate.onetoone;

import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

public class ExportDB {
	
	/**
	 * hibernate.cfg.xml配置文件在classpath的根目录中
	 * 
	 */
	public static void create() {
		// 读取hibernate.cfg.xml文件
		Configuration cfg = new Configuration().configure();
		SchemaExport export = new SchemaExport(cfg);
		export.create(true, true);
	}

	/**
	 * hibernateCfgXml可以是形如"/com/jsoft/util/hibernate.cfg.xml"
	 * 
	 * @param hibernateCfgXml hibernate配置文件的包路径
	 */
	public static void create(String hibernateCfgXml) {
		// 读取hibernate.cfg.xml文件
		Configuration cfg = new Configuration().configure(hibernateCfgXml);
		SchemaExport export = new SchemaExport(cfg);
		export.create(true, true);
	}

	public static void main(String[] args) {
		ExportDB.create("org/javaclub/opensource/demos/hibernate/onetoone/hibernate.cfg.xml");
	}

}

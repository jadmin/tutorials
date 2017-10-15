package com.github.javaclub.tutorial.hadoop;

import org.apache.hadoop.util.ProgramDriver;

import com.github.javaclub.tutorial.hadoop.temperature.MaxTemperature;
import com.github.javaclub.tutorial.hadoop.wordcount.WordCount;
import com.github.javaclub.tutorial.hadoop.wordcount.WordCountCaseSensitive;

/**
 * A description of an example program based on its class and a human-readable
 * description.
 */
public class ExampleDriver {

	public static void main(String argv[]) {
		int exitCode = -1;
		ProgramDriver pgd = new ProgramDriver();
		try {
			pgd.addClass("wordcount", WordCount.class, "统计单词频次");
			pgd.addClass("wordcountsensitive", WordCountCaseSensitive.class, "是否支持区分大小写和忽略单词分隔符");
			pgd.addClass("maxtemp", MaxTemperature.class, "计算最高气温");
			exitCode = pgd.run(argv);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
		System.exit(exitCode);
	}
}

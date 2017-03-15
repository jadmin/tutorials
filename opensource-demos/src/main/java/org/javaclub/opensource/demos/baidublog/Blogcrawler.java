/*
 * @(#)Blogcrawler.java	2011-10-10
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.baidublog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Baidu博客数据抓取及解析
 * 
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: Blogcrawler.java 2011-10-10 下午08:13:43 Exp $
 */
public class Blogcrawler {
	
	private String blogid;
	
	public Blogcrawler() {
		super();
	}
	
	public Blogcrawler(String blogid) {
		super();
		this.blogid = blogid;
	}
	

	public final String getBlogid() {
		return blogid;
	}

	public final void setBlogid(String blogid) {
		this.blogid = blogid;
	}

	/**
	 * 获取页面博文链接
	 * 
	 * @param html 网页源码
	 * @return 页面中的博文链接
	 */
	private List<String> getPostLinks(String html) {
		// 分析页面内容，取得页面中的文章链接
		String titleDivRegex = "<div class=\"tit\"><a href=\"/" + getBlogid() + "/blog/item/" 
							 + "[\\d\\D]*?\" target=\"_blank\">[\\d\\D]*?</div>";
		Pattern titleDivPattern = Pattern.compile(titleDivRegex);
		Matcher titleDivMatcher = titleDivPattern.matcher(html);
		List<String> posts = new ArrayList<String>();
		while (titleDivMatcher.find()) {
			String div = titleDivMatcher.group();
			String titleUrl = div.substring(div.indexOf("/"), div
					.indexOf("\" target"));
			posts.add("http://hi.baidu.com" + titleUrl);
		}
		return posts;
	}

	/**
	 * <p>
	 * 获取博客总页数 <br>
	 * 我的博客内容有16页，有上一页，下一页，尾页等这样的标志，如果博文少的话可能这些标志不会出现，请修改此方法
	 * 
	 * @param html 源码（最好是第一页）
	 * @return 博客总页数
	 */
	private int getTotalPages(String html) {
		String pageRegex = "<a href=\"/" + this.getBlogid() + "/blog/index/[\\d]+\">\\[尾页\\]</a>";
		Pattern pagePattern = Pattern.compile(pageRegex);
		Matcher pageMatcher = pagePattern.matcher(html);
		String totalPagesStr = null;
		int pages = 0;
		if (pageMatcher.find()) {
			String pagelink = pageMatcher.group();
			totalPagesStr = pagelink.replaceAll(
					"<a[\\s]href=\"/" + this.getBlogid() + 
					"/blog/index/", "").replaceAll(
					"\">\\[尾页\\]</a>", "");
			pages = Integer.parseInt(totalPagesStr);
		}
		return pages;
	}

	/**
	 * <p>
	 * 获取博客的所有博文的地址 <br>
	 * 没有对url进行编码处理，如果博客地址含中文，请对url进行处理
	 * 
	 * @param blogUrl 博客地址
	 * @return 所有博文地址，存放于栈中，使用的时候请使用pop方法取出元素，这样可以保证按照最先发表的博文最先处理
	 */
	public Stack<String> getAllPostLink(String blogUrl) {
		Stack<String> posts = new Stack<String>();
		// 1.下载第一页
		// String firstPageHtml = downloadPage(blogUrl + "/blog/index/0");
		String firstPageHtml = DownloadUtil.download(blogUrl + "/blog/index/0").getText();
		// 2.获取博文总页数
		int totalPages = getTotalPages(firstPageHtml);
		// 3.下载各摘要页
		posts.addAll(getPostLinks(firstPageHtml));
		if (totalPages < 1) {
			return posts;
		}
		for (int i = 1; i <= totalPages; i++) {
			String page = DownloadUtil.download(blogUrl + "/blog/index/" + i).getText();
			posts.addAll(getPostLinks(page));
		}
		return posts;
	}

	/**
	 * 解析博文，获取标题，发布时间，内容，分类等信息
	 * 
	 * @param postUrl 博文地址
	 * @return 封装了博文信息的BaiduHi
	 */
	public BaiduHi getBaiduHi(String postUrl) {
		String html = DownloadUtil.download(postUrl).getText();
		// /<div class="tit">
		String titleDivRegex = "<div[\\s]id=\"m_blog\"[\\s]class=\"modbox\"[\\s]style=\"overflow-x:hidden;\">[\\d\\D]*?<div[\\s]class=\"tit\">[\\d\\D]*?</div>[\\d\\D]*?<div[\\s]class=\"date\">";
		Pattern titleDivPattern = Pattern.compile(titleDivRegex);
		Matcher titleDivMatcher = titleDivPattern.matcher(html);
		String title = null;
		if (titleDivMatcher.find()) {
			title = titleDivMatcher
					.group()
					.replaceAll(
							"<div[\\s]id=\"m_blog\"[\\s]class=\"modbox\"[\\s]style=\"overflow-x:hidden;\">[\\d\\D]*?<div[\\s]class=\"tit\">",
							"")
					.replaceAll("</div>[\\d\\D]*?<div[\\s]class=\"date\">", "").trim();
		}
		String dateDivRegex = "<div[\\s]class=\"date\">.+?</div>";
		Pattern dateDivPattern = Pattern.compile(dateDivRegex);
		Matcher dateMatcher = dateDivPattern.matcher(html);
		String dateStr = null;
		Date postDate = null;
		if (dateMatcher.find()) {
			dateStr = dateMatcher.group().replaceAll(
					"<div[\\s]class=\"date\">", "").replaceAll("</div>", "")
					.trim();
			postDate = getDate(dateStr);
		}
		String textDivRegex = "<div[\\s]id=\"blog_text\"[\\s]class=\"cnt\"[\\s]+>[\\d\\D]*?</div>";
		Pattern textDivPattern = Pattern.compile(textDivRegex);
		Matcher textMatcher = textDivPattern.matcher(html);
		String text = null;
		if (textMatcher.find()) {
			text = textMatcher.group().replaceAll(
					"<div[\\s]id=\"blog_text\"[\\s]class=\"cnt\"[\\s]+>", "")
					.replaceAll("</div>", "").trim();
		}
		String categoriesRegex = "title=\"查看该分类中所有文章\">类别：.+?</a>";
		Pattern categoriesDivPattern = Pattern.compile(categoriesRegex);
		Matcher categoriesMatcher = categoriesDivPattern.matcher(html);
		String categories = null;
		if (categoriesMatcher.find()) {
			categories = categoriesMatcher.group().replaceAll(
					"title=\"查看该分类中所有文章\">类别：", "").replaceAll("</a>", "")
					.trim();
		}
		BaiduHi hi = new BaiduHi();
		hi.setTitle(title);
		hi.setContent(text);
		hi.setCategories(categories);
		hi.setDateCreated(postDate);
		return hi;
	}

	/**
	 * 解析博文中的日期格式返回Date类型
	 * 
	 * @param str 博文中的日期
	 * @return date类型日期
	 */
	@SuppressWarnings("deprecation")
	private Date getDate(String str) {
		// 2011年08月24日 星期三  12:59
		String yearStr = str.substring(0, str.indexOf("年")).trim();
		String monthStr = str.substring(str.indexOf("年"), str.indexOf("月"))
				.replace("年", "").trim();
		String dayStr = str.substring(str.indexOf("月"), str.indexOf("日"))
				.replace("月", "").trim();
		/*String timeStr = str.substring(str.indexOf("午")).replace("午", "").trim();*/
		String timeStr = str.substring(16).trim();
		String hourStr = timeStr.split(":")[0];
		String minutesStr = timeStr.split(":")[1];
		Date date = new Date();
		date.setYear(Integer.parseInt(yearStr) - 1900);
		date.setMonth(Integer.parseInt(monthStr) - 1);
		date.setDate(Integer.parseInt(dayStr));
		date.setHours(Integer.parseInt(hourStr));
		date.setMinutes(Integer.parseInt(minutesStr));
		/*if (str.contains("下午")) {
			date.setHours(Integer.parseInt(hourStr) + 12);
		} else {
			date.setHours(Integer.parseInt(hourStr));
		}
		date.setMinutes(Integer.parseInt(minutesStr));*/
		return date;
	}
}

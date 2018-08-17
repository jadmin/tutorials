/*
 * @(#)TextCheckTests.java	2018年8月17日
 *
 * Copyright (c) 2018. All Rights Reserved.
 *
 */

package com.github.javaclub.tests;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.javaclub.sword.core.Strings;
import com.github.javaclub.sword.core.Systems;
import com.github.javaclub.tests.excel.ExcelUtils;
import com.github.javaclub.tests.http.HttpClientUtils;

/**
 * TextCheckTests
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: TextCheckTests.java 2018年8月17日 下午6:22:20 Exp $
 */
public class TextCheckTests {
	
	static final String ND_ACCESS_KEY = "UwopA2StQJHMkfaiZYZO";
	static final String ND_TEXT_CHECK_URL = "http://api.fengkongcloud.com/v2/saas/anti_fraud/text";

	@Test
	public void test4kItemName() throws Exception {
		InputStream stream = new FileInputStream("/Users/chenzq/Documents/globalscanner/风控/商品标题过滤/本地生活商品.xlsx");
		List<List<String>> lines = ExcelUtils.readXlsxRows(stream);
		for (List<String> row : lines) {
			String col1 = row.get(1);
			String col2 = row.get(2);
			if(Strings.isNotBlank(col1)) {
				NDCheckResultDTO d1 = checkTextWIthND(col1);
				if(null != d1 && Objects.equals(2, d1.getCheckResult())) {
					Systems.out("ID={},NAME={},HIT_RULE={}", new Object[] { row.get(0), col1, d1.getDetail()});
					continue;
				}
			}
			if(Strings.isNotBlank(col2)) {
				NDCheckResultDTO d2 = checkTextWIthND(col2);
				if(null != d2 && Objects.equals(2, d2.getCheckResult())) {
					Systems.out("ID={},PRODUCT_DESC={},HIT_RULE={}", new Object[] { row.get(0), col2, d2.getDetail()});
					continue;
				}
			}
			Thread.sleep(30L);
		}
	}
	
	@Test
	public void testOneWord() {
		String text = "毛主席万岁，中国共产党万岁！";
		NDCheckResultDTO dto = checkTextWIthND(text);
		System.out.println(dto);
		System.out.println(dto.getResultDetail().getDescription());
	}
	
	private static String buildReqParam(String content) {
        JSONObject json = new JSONObject();
        json.put("accessKey", ND_ACCESS_KEY);
        JSONObject data = new JSONObject();
        data.put("tokenId", DigestUtils.md5Hex(content));
        json.put("type", "ECOM"); //电商场景
        data.put("text",content);
        json.put("data", data);
        return JSON.toJSONString(json);
    }
	
	public static NDCheckResultDTO checkTextWIthND(String content) {
        String json = buildReqParam(content);
        String reponseContent = HttpClientUtils.doPostJson(ND_TEXT_CHECK_URL, json);
        NDCheckResultDTO result = JSON.parseObject(reponseContent, NDCheckResultDTO.class);
        if (result.getCode() != 1100) { // 接口请求失败
            return null;
        }
        Integer checkReresult = handleCheckResult(result);
        result.setCheckResult(checkReresult);
        // System.out.println(result.getDetail());
        
        return result;
    }
	
	static Integer handleCheckResult(NDCheckResultDTO checkResultDTO ) {
        Integer checkResult = 2;
        if ("PASS".equals(checkResultDTO.getRiskLevel())) {
            checkResult = 0;
        } else if ("REVIEW".equals(checkResultDTO.getRiskLevel())) {
            checkResult = 1;
        } else if ("REJECT".equals(checkResultDTO.getRiskLevel())) {
            checkResult = 2;
        }
        return checkResult;

    }
	
}



class NDCheckResultDTO {
	
    private Integer code ;

    private String message;

    private String requestId;

    private Integer score;

    private Integer checkResult;

    private String riskLevel;

    private String detail;

    private NDResultDetail resultDetail;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getCheckResult() {
        return checkResult;
    }

    public void setCheckResult(Integer checkResult) {
        this.checkResult = checkResult;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public NDResultDetail getResultDetail() {
        return resultDetail;
    }

    public void setResultDetail(NDResultDetail resultDetail) {
        this.resultDetail = resultDetail;
    }

    @Override
    public String toString() {
        return "NDCheckResultDTO{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", requestId='" + requestId + '\'' +
                ", score=" + score +
                ", checkResult=" + checkResult +
                ", riskLevel='" + riskLevel + '\'' +
                ", detail='" + detail + '\'' +
                ", resultDetail=" + resultDetail +
                '}';
    }
}

class NDResultDetail {

    private Integer riskType;

    private String description;

    public Integer getRiskType() {
        return riskType;
    }

    public void setRiskType(Integer riskType) {
        this.riskType = riskType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "NDResultDetail{" +
                "riskType=" + riskType +
                ", description='" + description + '\'' +
                '}';
    }
}

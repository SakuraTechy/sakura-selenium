package com.sakura.handler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import com.sakura.base.TestStep;
import com.sakura.service.RunUnitService;
import com.sakura.util.ConfigUtil;
import com.sakura.util.Constants;
import com.sakura.util.ImageUtil;
import com.sakura.util.SeleniumUtil;

@SuppressWarnings("unused")
public class GetCodeActionHandler {
	static Logger log = Logger.getLogger(GetCodeActionHandler.class);

	/**
	 *
	 * <br>
	 * Web端获取图片验证码</br>
	 * 
	 * @param step
	 * @throws Exception
	 */
	public void webGetcode(TestStep step) throws Exception {
		Boolean state = true;
		outer : while (state) {
			try {
				String Python_Path = ConfigUtil.getProperty("Python_Path", Constants.CONFIG_APP);
				String Python_Run = System.getProperty("user.dir") + "/TestData/Code/getCode.py";
				String Python_Run_IMG = System.getProperty("user.dir") + "/TestData/Code/getCode.png";
				String Python_Run_IMG_CODE = System.getProperty("user.dir") + "/TestData/Code/getCode.txt";
				String code = "1234";

//				Thread.sleep(2000);
				step.setLocator(SeleniumUtil.parseStringHasEls(step.getUrl()));
				String src = SeleniumUtil.getElement(step).getAttribute("src").replace("%0A","");

				// 判断字符串是否以 "https://" 开头
		        boolean isHttps = src.startsWith("https://");
		        if (isHttps) {
		        	src = ImageUtil.imageUrlToBase64(src);
		        }
		        Python_Run_IMG = ImageUtil.GenerateImage(src, Python_Run_IMG);
				String[] args = new String[] { Python_Path, Python_Run, Python_Run_IMG, Python_Run_IMG_CODE };
				Process proc = Runtime.getRuntime().exec(args);
				BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
				String line = null;
				while ((line = in.readLine()) != null) {
					code = line.split("=")[0];
				}
				in.close();
				proc.waitFor();

				log.info("『正常测试』开始执行: " + "<" + step.getId() + "." + step.getName() + ">[" + code+"]");
				RunUnitService.Step.put("name", step.getId() + "." + step.getName() + ">[" + code+"]");
				
//				Thread.sleep(2000);
//				WebElement e = SeleniumUtil.getElement(step);
//				if (e != null && !code.equals("1234")) {
//					e.sendKeys(Keys.CONTROL, "a");
//					e.sendKeys(Keys.DELETE);
//					e.sendKeys(code);
//				}

				WebElement webElement = ClickActionHandler.webClick(step);
				if (webElement != null &&!code.equals("1234")) {
					webElement.sendKeys(Keys.CONTROL,"a");
					webElement.sendKeys(Keys.DELETE);
					webElement.sendKeys(code);
				}

//				step.getWebDriver().findElement(By.xpath(step.getElement())).click();
				step.setType("elementToBeClickable");
				step.setLocator(SeleniumUtil.parseStringHasEls(step.getElement()));
				SeleniumUtil.getElement(step).click();

//				Thread.sleep(500);
//				String Actual = step.getWebDriver().findElement(By.xpath(step.getValue())).getText();
				step.setLocator(SeleniumUtil.parseStringHasEls(step.getValue()));
				step.setType("textToBe");
				String Actual = SeleniumUtil.getElement(step).getText();

				log.info(Actual);
				String Expected = SeleniumUtil.parseStringHasEls(step.getExpect());
				List<String> ExpectedList = Arrays.asList(Expected.split(","));
				for(String Expect : ExpectedList) {
					log.info(Expect);
					if (Actual.isEmpty() || Actual.equals(Expect)) {
//						step.getWebDriver().findElement(By.xpath(step.getMessage())).click();
						step.setType("elementToBeClickable");
						step.setLocator(SeleniumUtil.parseStringHasEls(step.getMessage()));
						SeleniumUtil.getElement(step).click();
						state = true;
						continue outer;
					}else {
						continue;
					}
				}
				state = false;
//				log.info(state);
			} catch (Exception e) {
//				log.error("", e);
				break outer;
			}
		}
	}

	public static void main(String[] args) throws InterruptedException {
//		String src = "data:image/jpg;base64,/9j/4AAQSkZJRgABAgAAAQABAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0a\r\n"
//				+ "HBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/2wBDAQkJCQwLDBgNDRgyIRwhMjIyMjIy\r\n"
//				+ "MjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjL/wAARCAAZAF8DASIA\r\n"
//				+ "AhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQA\r\n"
//				+ "AAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3\r\n"
//				+ "ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWm\r\n"
//				+ "p6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEA\r\n"
//				+ "AwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSEx\r\n"
//				+ "BhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElK\r\n"
//				+ "U1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3\r\n"
//				+ "uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwD22O7u\r\n"
//				+ "YUubaRPPvIULwAkIbpQBg56A7jtPocHADAVet7iK6toriFt0UqB0bBGVIyDz7Vk3Vp9jktY438u3\r\n"
//				+ "87/RnIyLWUqwAI6GNgSoGQQWAXquyxo3/HtMfuM0zO9ueTbuwDOhP8XzFmB6EMMcYoA4/RfFPivX\r\n"
//				+ "NMbUIF0KGFbgW375ZgS5KgcKTwS6iuvnhkjskv7q7hsryGAG5nTHk4UZYNu6xg5OcggZwRk58v8A\r\n"
//				+ "CPhWy1nw6L6W3aWePUUhdQW+aImLd0IwQCxz6FvYr3HjrR7q4+Gup6VogeOVLVUijjZizRoQWjGM\r\n"
//				+ "liyKVx3zg9ayg3y3YUlzNJ9TK0z4i+GZdSi0S11ZVkuJTbRxxIzi3lyVHlyFdjxkj5fTI42nCa+u\r\n"
//				+ "+LZfC4gg1C0a9urvctoLJSPOcY4ZTkpyygYLk8nA6Vw/hfxZo76FovheSztLzUIjHbNo02mSRPFM\r\n"
//				+ "rYeRmw6ZU7nY4BxuJwQVq/4kuF8OfE/RtUvpJpNOgtxEZthZog/mhQx/ixknP3iFP3iCTrT97c3q\r\n"
//				+ "01CVrNevU3R4+nstXj0vXNAubG8uFBtY4biOfzScgLnKhSWAUZOMnkqOal0fx14e8VaTrUk1tc28\r\n"
//				+ "Wk5OoW2o23zRqoLZKjdnBRuOoK9OmeX+It1aeJtU0nR9DvILu6upB5xhbzEULuCFmXOAN8pOOccn\r\n"
//				+ "tXH/ABY8Op/wmemDTF+xXuvt9mvbOI4V5PMQiTOQGVyynoBmPJ+bdjqo0oVHyy0b/T/gGEtFc9R8\r\n"
//				+ "LeIoPElil/o7axYW0spht/7UQTR3DhWLbfnZ8AKedyrkcZO4V29c5cW+meHPD+ly2cAtNN02VGWE\r\n"
//				+ "Iy4RwYySD82R5pY5BJIOeTmruqPq8Fys+nxfaIggBg3J8zZJ77dvGBu3Nj+4etc83FyfLsA3VEvb\r\n"
//				+ "CxvdRg1KZ2gjeZYJo4zFwCdvyqrY9Pm9M56G3pM893psF1NLHJ56LKhSEx4VgCAQWbn8axP7cTXY\r\n"
//				+ "tR0OS3ksb+a2dYFuVZRMChBZcgHAOe2cDOOoG7EkOkaXDCPOeG2jSJdsZkcgAKOFGSfoKkCsNRS6\r\n"
//				+ "igiltlaO6up7NlY5GE83JIxyD5fT/a745u26pCv2UTtK8YBxI+5wpJ257kcEAnk7eSTk1h/8x/8A\r\n"
//				+ "7jH/ALY1oz/8jLY/9edx/wChw0AQ6Dapp76hZKWzHOCob/nn5aBMDrtAXZk5yUY5zkCyZHi8QLEX\r\n"
//				+ "Zo7i1LKueEMbjJx6t5o/74HXtzMH/JQ77/r8t/8A0kmro5/+Rlsf+vO4/wDQ4aAHyW9/byvLZ3Cz\r\n"
//				+ "IxLG2uScDP8AdkAJUZJOCG7AbRWZfRtqt9aH7Ncwuqy28sc0O5FDruy+CUkQ+XtKgn/WDlWAroqK\r\n"
//				+ "AMmPTrbRJWuNN0+3it3AFxDbW6o3GcONoyxGTleePu8jDR6r4c0bxJc6VqN3F502nyrc2U8cpAU7\r\n"
//				+ "lbIwcMDsXqDx0xW1Wbof/HhL/wBfl1/6PkpqTTumA/W43k0e5aJGeaECeJFGSzxkOox1ILKBgc1F\r\n"
//				+ "o0iRxPYI6vHbhWt2U5DW7Z8sgjsMMnUk7Nx+9WpXFN/x4eGP+vOD/wBH2lIDU17Qb2/iT7JdQyCO\r\n"
//				+ "cTrbX0YliJ5BGcZCkM3B3Y427QKbYxeGb5xF/Y9nb3Bd0ENxaIrMyEhgpxtfGDnaTjviujrN03/j\r\n"
//				+ "/wBY/wCvxf8A0RFQB//Z";
//
//		String src1 = "data:image/jpg;base64,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";
//
//		String value = System.getProperty("user.dir") + "/TestData/Code/getCode.png";
//
//		String imgFilePath = ImageUtil.GenerateImage(src1, value);
//		log.info(imgFilePath);
//
//		String args3 = "D:/Program/Python/3.9.0/python.exe " + System.getProperty("user.dir")
//				+ "/TestData/Code/getCode.py " + imgFilePath + " " + System.getProperty("user.dir")
//				+ "/TestData/Code/getCode.txt";
//		log.info(args3);
//
//		try {
//			String code = "";
//			Process proc = Runtime.getRuntime().exec(args3);
//			BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
//			String line = null;
//			while ((line = in.readLine()) != null) {
//				code = line.split("=")[0];
//			}
//			log.info(code);
//			in.close();
//			proc.waitFor();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		String Expected = "a";
		List<String> list = Arrays.asList(Expected.split(","));
		log.info(list.get(0));
	}
}
package cspi_custom_xpath_functions;

import java.io.File;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.UUID;

import com.tibco.xml.cxf.common.annotations.XPathFunction;
import com.tibco.xml.cxf.common.annotations.XPathFunctionGroup;
import com.tibco.xml.cxf.common.annotations.XPathFunctionParameter;

@XPathFunctionGroup(category = "Custom Functions", prefix = "cspi", namespace = "http://cspi.custom.functions", helpText = "Custom defined function")
public class CustomFunctions {
	
	public CustomFunctions(){
		
	}
	
	/**
	 * 유니크 ID 36자리를 생성합니다. 로깅 시 사용할 유니크 아이디를 생성할 때 사용합니다.
	 *
	 * @return String 유니크 아이디 36자리
	 */
	@XPathFunction(helpText = "로깅 시 사용할 유니크 ID 36자리를 생성합니다.", parameters = {})
	public String getUUID() {
		String uuid = UUID.randomUUID().toString();
		return uuid;
	}
	


	/**
	 * 유니크 아이디 5자리를 생성합니다. DB Insert 시 사용할 유니크 아이디를 생성할 때 사용합니다.
	 *
	 * @return String 유니크 아이디 5자리
	 */
	@XPathFunction(helpText = "DB Insert 시 사용할 유니크 아이디 5자리를 생성합니다.", parameters = {
	         @XPathFunctionParameter(name = "position", optional = false, optionalValue = "")
	   })
	public static String getIF_seq(String position) {
		char chrword;
		if (position.length() <= 6) {
			while (position.length() != 6)
				position = "0" + position;
		}

		int intword = Integer.parseInt(position.substring(0,
				position.length() - 4));
		String seq = position.substring(position.length() - 4,
				position.length());

		if (intword >= 26) {
			intword = intword + 6 + 65;
		} else {
			intword = intword + 65;
		}

		chrword = (char) intword;

		return chrword + seq;
	}

	/**
	 * 한 주의 요일을 숫자로 추출합니다. 배치 스케줄러 실행 시 요일을 추출하기 위해서 사용됩니다.
	 *
	 * @return int 각 요일별 숫자(일요일1 ---- 토요일7)
	 */	
	@XPathFunction(helpText = "한 주의 요일을 숫자로 추출합니다. 배치 스케줄러 실행 시 요일을 추출하기 위해서 사용됩니다.", parameters = {})
	public static int getDayofWeek() {
		Calendar today = Calendar.getInstance();

		int _dayOfTheWeek = today.get(Calendar.DAY_OF_WEEK);

		return _dayOfTheWeek;
	}


	/**
	 * 한글이 포함된 문자열을 필요한 길이만큼 추출합니다.
	 * 
	 * @param String
	 *            한글이 포함된 문자열
	 * @param int 추출하고자 하는 문자열의 시작점
	 * @param int 추출하고자 하는 문자열의 길이
	 * @return String 추출된 문자열
	 */
	@XPathFunction(helpText = "한글(MS949)이 포함된 문자열을 필요한 길이만큼 추출합니다.", parameters = {
	         @XPathFunctionParameter(name = "str", optional = false, optionalValue = ""),
	         @XPathFunctionParameter(name = "startLen", optional = false, optionalValue = ""),
	         @XPathFunctionParameter(name = "substrLen", optional = false, optionalValue = "")
	   })	
	public static String substringKO(String str, int startLen, int substrLen) {
		int endLen;
		endLen = startLen + substrLen - 1;

		try {
			if (str == null)
				return "";

			byte abyte0[] = str.getBytes("MS949");

			if (abyte0.length < startLen)
				return "";

			// if( abyte0.length <= endLen )
			// endLen = abyte0.length;
			if (abyte0.length <= endLen) {
				endLen = abyte0.length;
				substrLen = abyte0.length - startLen + 1;
			}

			int count = 0;

			byte abyte1[] = new byte[substrLen];

			for (int l = startLen; l < endLen + 1; l++) {
				abyte1[count++] = abyte0[l - 1];
			}

			return new String(abyte1, "MS949");
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 문자열의 길이(byte)를 추출합니다.
	 * 
	 * @param String
	 *            문자열
	 * @return String 문자열의 길이(byte)
	 */
	@XPathFunction(helpText = "문자열의 길이(byte)를 추출합니다.", parameters = {
	         @XPathFunctionParameter(name = "s", optional = false, optionalValue = "")
	})
	public static String stringLengthKO(String s) {
		int strlen = 0;

		byte abyte0[] = s.getBytes();
		strlen = abyte0.length;

		return Integer.toString(strlen);
	}

	/**
	 * 문자열의 뒷부분을 필요한 길이만큼 필요한 문자로 채운다.
	 * 
	 * @param String
	 *            한글이 포함된 문자열
	 * @param int 채우고자 하는 총 문자열 길이
	 * @param String
	 *            채우고자하는 문자
	 * @return String 추출된 문자열
	 */
	@XPathFunction(helpText = "문자열의 뒷부분을 필요한 길이만큼 필요한 문자로 채운다.", parameters = {
	         @XPathFunctionParameter(name = "str", optional = false, optionalValue = ""),
	         @XPathFunctionParameter(name = "totLen", optional = false, optionalValue = ""),
	         @XPathFunctionParameter(name = "padStr", optional = false, optionalValue = "")
	   })
	public static String padKO(String str, int totLen, String padStr) {
		String result = "";

		if (str == null)
			return "";

		byte abyte0[] = str.getBytes();
		byte abyte1[] = new byte[totLen];

		int j = abyte0.length;

		byte byte0 = padStr.getBytes()[0];

		if (j >= totLen)
			return str;

		for (int k = 0; k < j; k++) {
			abyte1[k] = abyte0[k];
		}

		for (int l = j; l < totLen; l++) {
			abyte1[l] = byte0;
		}

		result = new String(abyte1);

		return result;
	}

	/**
	 * 문자열의 앞부분을 필요한 길이만큼 필요한 문자로 채운다.
	 * 
	 * @param String
	 *            한글이 포함된 문자열
	 * @param int 채우고자 하는 총 문자열 길이
	 * @param String
	 *            채우고자하는 문자
	 * @return String 추출된 문자열
	 */
	@XPathFunction(helpText = "문자열의 앞부분을 필요한 길이만큼 필요한 문자로 채운다.", parameters = {
	         @XPathFunctionParameter(name = "str", optional = false, optionalValue = ""),
	         @XPathFunctionParameter(name = "totLen", optional = false, optionalValue = ""),
	         @XPathFunctionParameter(name = "padStr", optional = false, optionalValue = "")
	   })
	public static String padKOfront(String str, int totLen, String padStr) {
		String result = "";

		if (str == null)
			return "";

		byte abyte0[] = str.getBytes();

		int j = abyte0.length;

		int point = totLen - j;

		if (j >= totLen) {
			return str;
		}

		byte abyte1[] = new byte[point];

		byte byte0 = padStr.getBytes()[0];

		for (int k = 0; k < point; k++) {
			abyte1[k] = byte0;
		}

		result = new String(abyte1) + str;

		return result;
	}

	/**
	 * Ip Address를 가져온다.
	 * 
	 * @return String IP Address
	 */
	@XPathFunction(helpText = "Ip Address를 가져온다.", parameters = {})
	public static String getIPAddress() {
		String returnValue = null;
		try {
			returnValue = ((Inet4Address) InetAddress.getLocalHost())
					.getHostAddress();
		} catch (UnknownHostException e) {
			returnValue = "UnknownHost";
		}
		return returnValue;
	}
	
	@XPathFunction(helpText = "문자열을 치환합니다.", parameters = {
	         @XPathFunctionParameter(name = "source", optional = false, optionalValue = ""),
	         @XPathFunctionParameter(name = "fromCharList", optional = false, optionalValue = ""),
	         @XPathFunctionParameter(name = "toCharList", optional = false, optionalValue = "")
	   })
	public static String replace(String source, String fromCharList,
			String toCharList) {
		String result = "";
		result = source.replaceAll(fromCharList, toCharList);
		return result;
	}

	@XPathFunction(helpText = "정수를 16진수 문자열로 치환합니다.", parameters = {
	         @XPathFunctionParameter(name = "num", optional = false, optionalValue = "")
	   })
	public static String hexString(int num) {
		return Integer.toHexString(num).toUpperCase();
	}
	
	@XPathFunction(helpText = "입력받은 경로 이하의 폴더와 파일 및 해당 폴더를 삭제하고 true를 리턴합니다.", parameters = {
	         @XPathFunctionParameter(name = "parentPath", optional = false, optionalValue = "")
	   })	
	public static boolean deleteFolder(String parentPath) {

	    File file = new File(parentPath);
	    String[] fnameList = file.list();
	    if(fnameList == null)  
	    	return false;
	    int fCnt = fnameList.length;
	    String childPath = "";
	    
	    for(int i = 0; i < fCnt; i++) {
	      childPath = parentPath+"/"+fnameList[i];
	      File f = new File(childPath);
	      if( ! f.isDirectory()) {
	        f.delete();   //파일이면 바로 삭제
	      }
	      else {
	        deleteFolder(childPath);
	      }
	    }
	    
	    File f = new File(parentPath);
	    f.delete();   //폴더는 맨 나중에 삭제
	    
	    return true;
	    
	  }
	
}


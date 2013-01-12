import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MacSpeechFilter {
	
	private static MacSpeechFilter singleton = null;
	
	public static MacSpeechFilter singleton(String regex){
		if(singleton == null){
			singleton = new MacSpeechFilter(regex); 
		}
		return singleton;
	}
	
	private final Pattern memberMessage;
	
	private MacSpeechFilter(String regex) {
		this.memberMessage = Pattern.compile(regex);
	}
	
	public String full(String paramString) {
		paramString = onlyMessage(paramString);
		paramString = color(paramString);
		paramString = space(paramString);
		return paramString;
	}

	private String onlyMessage(String paramString) {
		if (paramString == null || paramString.isEmpty())
			return "";
		Matcher matcher = memberMessage.matcher(paramString);
		if (!matcher.matches())
			return "";
		return matcher.group(1);
	}

	private String color(String paramString) {
		return paramString.replaceAll("§.", "");
	}

	private String space(String paramString) {
		return paramString.replaceAll(" {2,}", "  ");
	}

}
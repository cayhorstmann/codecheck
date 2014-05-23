import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class ScriptRunner {
	private ScriptEngineManager manager;
	private ScriptEngine engine;
	
	public ScriptRunner() {
		System.out.println("ScriptRunner constructor");
		manager = new ScriptEngineManager();
		engine = manager.getEngineByName("nashorn");
	}
	
	public void executeScriptFromFile(String scriptFile) throws ScriptException, FileNotFoundException {
		InputStream inputStream = new FileInputStream(scriptFile);
		Reader reader = new BufferedReader(new InputStreamReader(inputStream));
		engine.eval(reader);
	}
	
	public String executeScript(String script) throws ScriptException {
		String result = engine.eval(script).toString();
		return result;
	}
	
	public void putValue(String key, Object value) {
		engine.put(key, value);
	}
	
	public String getValue(String key) throws ScriptException {
		String result = "";
		Object v = engine.get(key);
		
		if (v == null) return "";
		
		if (v.toString().charAt(0) == '[') {
			engine.eval("var strArrType = Java.type(\"java.lang.Object[]\")");
			engine.eval("var keyArr = Java.to(" + key + ", strArrType)");
			Object[] t = (Object[]) engine.get("keyArr");
			for (int i = 0; i < t.length; i++) {
				if (t[i] == null) {
					break;
				}
				result += t[i].toString() + " ";
			}
		} else {
			String t = v.toString();
			boolean isInteger = true;
			for (int i = t.indexOf(".") + 1; i < t.length(); i++)
				if (t.charAt(i) != '0') {
					isInteger = false;
					break;
				}
			if (isInteger && t.indexOf(".") != -1) {
				t = t.substring(0, t.indexOf("."));
			}
			result = t;
		}
		
		return result;
	}

}

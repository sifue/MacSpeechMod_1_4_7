import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MacSpeech extends Thread {
	private Properties conf = new Properties();

	// 設定項目
	private static Boolean enable = Boolean.TRUE;
	private static String speech_engine = "/usr/bin/say";
	private static String speech_regex = "<[A-Za-z0-9_]+> (.*)";
	
	private static String charsetname = "UTF-8";
	private static File tmpFile = null;

	private static Queue<String> queue = new ConcurrentLinkedQueue<String>();
	private static final MacSpeech instance = new MacSpeech();

	private MacSpeech() {
		String s = "";
		File file = new File(net.minecraft.client.Minecraft.b(), "macspeech.properties");
		System.out.println(file);
		try {
			InputStream is = new FileInputStream(file);
			conf.load(is);

			/* MacSpeech option */
			s = conf.getProperty("enable", "true");
			enable = Boolean.parseBoolean(s);
			s = conf.getProperty("speech_engine", "/usr/bin/say");
			speech_engine = s;
			s = conf.getProperty("speech_regex", "<[A-Za-z0-9_]+> (.*)");
			speech_regex = s;
			is.close();
		} catch (IOException e) {
			// e.printStackTrace();
		}

		/* 設定書き出し */
		try {
			/* MacSpeech option */
			conf.setProperty("enable", enable.toString());
			conf.setProperty("speech_engine", speech_engine);
			conf.setProperty("speech_regex", speech_regex);

			conf.store(new FileOutputStream(file), "");
		} catch (IOException e) {
			// e.printStackTrace();
		}
		try {
			tmpFile = File.createTempFile("macspeechmod_", "");
			tmpFile.deleteOnExit();
		} catch (IOException e) {
		}
	}

	public static MacSpeech get() {
		return instance;
	}

	public static void post(String paramString) {
		if (Thread.State.NEW == instance.getState()) {
			instance.setDaemon(true);
			instance.start();
		}
		MacSpeechFilter filter =  MacSpeechFilter.singleton(speech_regex);
		String full = filter.full(paramString);
		if(!full.isEmpty()) queue.offer(full);
	}

	public void run() {
		if(!enable) return;
		
		while (true)
			if (queue.isEmpty()) {
				try {
					Thread.sleep(100L);
				} catch (InterruptedException localInterruptedException) {
				}
			} else {
				try {
					
					StringBuilder sb = new StringBuilder();
					while (!queue.isEmpty()) {
						sb.append(queue.poll());
					}
					FileOutputStream fos = new FileOutputStream(tmpFile, false);
					OutputStreamWriter osw = new OutputStreamWriter(
							new BufferedOutputStream(fos), charsetname);
					osw.append(sb.toString());
					osw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

				List<String> sayCommand = new ArrayList<String>();
				sayCommand.add(speech_engine);
				sayCommand.add("-f");
				sayCommand.add(tmpFile.getAbsolutePath());
				try {
					ProcessBuilder pb = new ProcessBuilder(sayCommand);
					Process p = pb.start();
					
					// read wait
					InputStreamReader reader = new InputStreamReader(p.getInputStream());
			        StringBuilder builder = new StringBuilder();
			        char[] buf = new char[1024];
			        int numRead;
			        while (0 <= (numRead = reader.read(buf))) {
			            builder.append(buf, 0, numRead);
			        }
			        System.out.println(builder.toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	}
}
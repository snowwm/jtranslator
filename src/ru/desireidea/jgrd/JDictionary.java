package ru.desireidea.jgrd;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class JDictionary {

	protected ArrayList<String> words;
	protected TreeMap<String, ArrayList<String>> dict;
	protected int maxCap;
	protected URI uri;
	protected ArrayList<ActionListener> listeners = new ArrayList<ActionListener>();
	protected String name;

	public JDictionary() {
		this(0);
	}

	public JDictionary(String name) {
		this(0, name);
	}

	public JDictionary(int maxCapacity) {
		maxCap = maxCapacity;
		setDictionary(null);
	}

	public JDictionary(int maxCapacity, String name) {
		maxCap = maxCapacity;
		setDictionary(null);
		this.name = name;
	}

	public JDictionary(ArrayList<String> dictionary) {
		this(dictionary, 0);
	}

	public JDictionary(ArrayList<String> dictionary, String name) {
		this(dictionary, 0);
		this.name = name;
	}

	public JDictionary(ArrayList<String> dictionary, int maxCapacity) {
		maxCap = maxCapacity;
		setDictionary(dictionary);
	}

	public JDictionary(ArrayList<String> dictionary, int maxCapacity,
			String name) {
		maxCap = maxCapacity;
		setDictionary(dictionary);
		this.name = name;
	}

	public JDictionary(File file) throws IOException {
		this(file, 0);
	}

	public JDictionary(File file, String name) throws IOException {
		this(file, 0);
		this.name = name;
	}

	public JDictionary(File file, int maxCapacity) throws IOException {
		maxCap = maxCapacity;
		download(file);
	}

	public JDictionary(File file, int maxCapacity, String name)
			throws IOException {
		maxCap = maxCapacity;
		download(file);
		this.name = name;
	}

	public JDictionary(InputStream stream) throws IOException {
		this(stream, 0);
	}

	public JDictionary(InputStream stream, String name) throws IOException {
		this(stream, 0);
		this.name = name;
	}

	public JDictionary(InputStream stream, int maxCapacity) throws IOException {
		maxCap = maxCapacity;
		download(stream);
	}

	public JDictionary(InputStream stream, int maxCapacity, String name)
			throws IOException {
		maxCap = maxCapacity;
		download(stream);
		this.name = name;
	}

	public JDictionary(URI uri) throws IOException {
		this(uri, 0);
	}

	public JDictionary(URI uri, String name) throws IOException {
		this(uri, 0);
		this.name = name;
	}

	public JDictionary(URI uri, int maxCapacity) throws IOException {
		maxCap = maxCapacity;
		download(uri);
	}

	public JDictionary(URI uri, int maxCapacity, String name)
			throws IOException {
		maxCap = maxCapacity;
		download(uri);
		this.name = name;
	}

	public ArrayList<String> getDictionary() {
		return (ArrayList<String>) words.clone();
	}

	public TreeMap<String, ArrayList<String>> getParsedDictionary() {
		return (TreeMap<String, ArrayList<String>>) dict.clone();
	}

	public void setDictionary(List<String> dictionary) {
		dict = new TreeMap<String, ArrayList<String>>();
		words = new ArrayList<String>();
		boolean shouldStop = maxCap > 0;
		int capacity = maxCap;
		for (String word : dictionary) {
			if (word == null || word.length() < 1 || word.startsWith("#"))
				continue;
			words.add(word);
			StringBuilder wordBuilder = new StringBuilder();
			boolean state = false;
			for (int i = 0; i < word.length(); i++) {
				char ch = word.charAt(i);
				if (Character.isLetter(ch) || ch == '-') {
					if (state) {
						state = false;
						wordBuilder.append(' ');
					}
					wordBuilder.append(ch);
				} else {
					state = true;
				}
			}
			word = parseWord(wordBuilder.toString());
			String first = word.substring(0, 1).toLowerCase();
			if (!dict.containsKey(first))
				dict.put(first, new ArrayList<String>() {
					public int indexOf(Object object) {
						String word = null;
						for (int i = 0; i < size(); i++) {
							word = get(i);
							if (object == null && word == null)
								return i;
							else if (object != null
									&& object.toString().equalsIgnoreCase(word))
								return i;
						}
						return -1;
					}
				});
			// if (!dict.get(first).contains(word)) { - should be, but runs
			// very-very slow :(
			dict.get(first).add(word);
			capacity--;
			// }
			if (shouldStop && capacity < 2)
				break;
		}
	}

	public int getMaxCapacity() {
		return maxCap;
	}

	public void setMaxCapacity(int maxCapacity) {
		maxCap = maxCapacity;
	}

	public String getHash() {
		String string = dict.toString();
		if (string == null || string.equals(""))
			return "!Словарь не загружен";
		String hash;
		try {
			byte[] bytes = MessageDigest.getInstance("MD5").digest(
					string.getBytes("UTF-8"));
			hash = String.format("%0" + (bytes.length << 1) + "x",
					new BigInteger(1, bytes));
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			hash = "!Не определено";
		}
		return hash;
	}

	public String getWord(int index) {
		try {
			return words.get(index - 1);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	public int getIndex(String word) {
		word = parseWord(word);
		int length = 0;
		String first = word.substring(0, 1).toLowerCase();
		for (String key : dict.keySet()) {
			ArrayList<String> list = dict.get(key);
			if (!key.equals(first))
				length += list.size();
			else {
				int index = list.indexOf(word);
				return index == -1 ? index : index + length + 1;
			}
		}
		return -1;
	}

	public void download() throws IOException {
		if (uri != null)
			download(uri);
		else
			throw new IOException();
	}

	public void download(InputStream stream) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(stream));
		ArrayList<String> list = new ArrayList<String>();
		try {
			String line = in.readLine();
			if (line != null) {
				if (line.startsWith("!#name")) {
					name = line.substring(7);
				}
				while ((line = in.readLine()) != null)
					list.add(line);
			}
		} finally {
			in.close();
		}
		setDictionary(list);
	}

	public void download(File file) throws IOException {
		if (file.exists())
			download(file.toURI());
		else
			throw new IOException();
	}

	public void download(URI uri) throws IOException {
		this.uri = uri;
		if (uri.getScheme().equalsIgnoreCase("jar")
				|| uri.getScheme().equalsIgnoreCase("zip")) {
			ZipFile file = null;
			try {
				String path = uri.getSchemeSpecificPart();
				file = new ZipFile(path.substring(path.indexOf(':') + 1,
						path.indexOf('!')));
				download(file.getInputStream(file.getEntry(path.substring(path
						.indexOf('!') + 2))));
				return;
			} catch (RuntimeException e) {
				throw new IOException(e);
			} finally {
				if (file != null)
					file.close();
			}
		} else
			download(uri.toURL().openStream());
	}

	public void upload() throws IOException {
		if (uri != null)
			upload(uri);
		else
			throw new IOException();
	}

	public void upload(OutputStream stream) throws IOException {
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(stream));
		try {
			for (String word : words) {
				out.newLine();
				out.write(word);
			}
		} finally {
			out.flush();
			out.close();
		}
	}

	public void upload(File file) throws IOException {
		uri = file.toURI();
		upload(new FileOutputStream(file));
	}

	public void upload(URI uri) throws IOException {
		this.uri = uri;
		if (uri.getScheme().equalsIgnoreCase("jar")
				|| uri.getScheme().equalsIgnoreCase("zip")) {
			ZipFile file = null, temp = null;
			ZipOutputStream out = null;
			InputStream in = null;
			try {
				String path_ = uri.getSchemeSpecificPart(), path = path_
						.substring(path_.indexOf(':') + 2, path_.indexOf('!'));
				File tmp = Files.copy(Paths.get(path),
						Files.createTempFile(null, null),
						StandardCopyOption.REPLACE_EXISTING).toFile();
				tmp.deleteOnExit();
				temp = new ZipFile(tmp);
				file = new ZipFile(path);
				Enumeration<? extends ZipEntry> entries = temp.entries();
				out = new ZipOutputStream(new FileOutputStream(path));
				while (entries.hasMoreElements()) {
					ZipEntry entry = entries.nextElement();
					String name = path_.substring(path_.indexOf('!') + 2);
					if (entry.getName().equals(name)) {
						out.putNextEntry(new ZipEntry(entry.getName()));
						BufferedWriter writer = new BufferedWriter(
								new OutputStreamWriter(out));
						try {
							for (String word : words) {
								writer.newLine();
								writer.write(word);
							}
						} finally {
							writer.flush();
						}
					} else {
						out.putNextEntry(entry);
						in = temp.getInputStream(entry);
						int i;
						while ((i = in.read()) != -1)
							out.write(i);
					}
					out.closeEntry();
				}
				return;
			} catch (RuntimeException e) {
				throw new IOException(e);
			} finally {
				if (file != null)
					file.close();
				if (temp != null)
					temp.close();
				if (out != null)
					out.close();
				if (in != null)
					in.close();
			}
		} else
			upload(new File(uri));
	}

	public boolean add(String... words) {
		boolean result = false;
		for (String word : words) {
			String parsed = parseWord(word), first = parsed.substring(0, 1)
					.toLowerCase();
			if (maxCap > 0 && maxCap < this.words.size())
				break;
			if (word == null || "".equals(word))
				continue;
			else {
				ArrayList<String> list = dict.get(first);
				if (list == null) {
					list = new ArrayList<String>();
					dict.put(first, list);
				}
				if (!list.contains(parsed)) {
					dict.get(first).add(parsed);
					this.words.add(getIndex(word) - 1, word);
					result = true;
				}
			}
		}
		if (result)
			for (ActionListener listener : listeners) {
				listener.actionPerformed(new ActionEvent(this,
						ActionEvent.ACTION_PERFORMED, "!#update"));
			}
		return result;
	}

	public String toString() {
		return getName();
	}

	public static JDictionary getDefaultDictionary() throws IOException {
		return getDefaultDictionary(0);
	}

	public static JDictionary getDefaultDictionary(int maxCapacity)
			throws IOException {
		try {
			return new JDictionary(new Object().getClass()
					.getResource("/ru/desireidea/jgrd/res/dict.txt").toURI(),
					maxCapacity, "Стандартный словарь");
		} catch (URISyntaxException e) {
			throw new IOException(e);
		}
	}

	public void setDefaultURI(URI uri) {
		this.uri = uri;
	}

	public URI getDefaultURI() {
		return uri;
	}

	public void addActionListener(ActionListener listener) {
		listeners.add(listener);
	}

	public void removeActionListener(ActionListener listener) {
		listeners.remove(listener);
	}

	public String getName() {
		if (name != null)
			return name;
		else
			return "[Без имени]";
	}

	public void setName(String name) {
		this.name = name;
	}

	protected String parseWord(String word) {
		return word.replace('ё', 'е').replace('Ё', 'е');
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dict == null) ? 0 : dict.hashCode());
		result = prime * result
				+ ((listeners == null) ? 0 : listeners.hashCode());
		result = prime * result + maxCap;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((uri == null) ? 0 : uri.hashCode());
		result = prime * result + ((words == null) ? 0 : words.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JDictionary other = (JDictionary) obj;
		if (dict == null) {
			if (other.dict != null)
				return false;
		} else if (!dict.equals(other.dict))
			return false;
		if (listeners == null) {
			if (other.listeners != null)
				return false;
		} else if (!listeners.equals(other.listeners))
			return false;
		if (maxCap != other.maxCap)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (uri == null) {
			if (other.uri != null)
				return false;
		} else if (!uri.equals(other.uri))
			return false;
		if (words == null) {
			if (other.words != null)
				return false;
		} else if (!words.equals(other.words))
			return false;
		return true;
	}

}

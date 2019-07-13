package ru.desireidea.jgrd;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class JTranslater {

	public static final int SMART_TRANSLATE_DEPTH = 10;
	public final String JGRD;
	private static final String EETEXT = "� ���� �������, ���� �� ������� ��� ���������, � ������ ������ �����, ������ �����. ��� ������� � ��� ������ � �������. ������� ������, ��� �� ������. ����� ��� ����� �������� ��������� �� ������ ������� �����, � ��� ��������� ��������� �� ��. ������� � ������� ������� ����������� �������� ���� �������� �� ������� �����. ��� ����� ���� � ���������������� � ������ ��������� � ���������� �����-���������, � ������ ����� � ���� �����������, � �� ��� ���������� ������� �� � �� ����������� �������. �� ������ � ��� �� ����� ���� ������� ������, ������� ������� �����. ��������� ������ ���������� �����( ��, �����... ����), �� �� ���� ������ ������ �������� �������� ����������� �� ������, � ���� ����������� ��������� :( ��� ���� ����� ����� �������, ������� �������� �������������� � ����� ����������, ��� �� �� ���������������� �������� �������������������������������������������, �� ��� �����... �� �����������, ��� ����������� ������� ����� �� 11 ������, � ��� �� ���� ���� � ���, ��� �� ������, � � ��� ����� ����� ������ �������� �������. ������ ����� �������� ���� �����������, �� �� ����� ��� ������ ��� ����� �� ������� � ��������. ��� �����, ��� ����� ������ � ������� ������, �� �� ���� ��� ������, ��� ���� ������� ����� ����� � ������� ����� �����������. ��� ������� ������� � ����� ����������. ������� � ����� ��������� � ������, � ������� ��������� �� �������.�� ������ �� � �����. ��� �������� �� �����������, �� � ������� �������� ��� � �������� � ��� ����������� ���. ������� �������� ����� ���������, ������� � ���, ��� �����-�� ������ ���� �������� ����� � �����������. � ���� ��������� ����� ������� � ����������. ������ � ����� �������� ��� ����� � ����� ����� � ������ �����. �������� � ����������� ��������. �������� ������� ��������� �������� �����, � �� �������� � �� �������� ������. ����� ������ ��������� �������� � �������� ��������� � � ������. ��������� �����. �������� ����� ��� � ������. ��� ��������� ����� � ����� ��� �� ������� \"���� ������\". ������� ���� ����� �������-����������-��������, ����� ���������� �� ���� ���������� �����. ������ ������ � ����� � ����� � ���������� � �������� ����� ������ � ����, ������, ��� �� � ���������� ����� �������� ������ ��� ���������� ��� �����. �� ��� ����� �������, ����� ��� ����� ���������� �������� �� ������� �������� ��������, ���������� �� ������� �����. ��� ������� ���������� � ����� ����, ����������� � ���� ��������. ����� ��� ������ ����� �������� �����. �������� ��������� ���������, ������� - �������������, ����� � �������� - ����������� ����� ������� ���������, � ��� ��� ����������� �� ���������� ��������� ������. ����� ����������� ����-�������� � ������� ���������� ���� �������� ����� ��������� ������ ����� �����-������, � �� ����� ��� ������� ��������� � ���������� � ������� ������ � �����������. �������� � ��� ����� ��������� ������, � ������� �������� ����� � ������� ��� ���������. ����� ������� �������� ������ �����������, � ����� ���������. � ��� ����� ������� � �������� ������ �� ������ �������� � ����������� �������� ����� �� ������. �������� � ������ �������� ������� � ����� ����������� ���� ������, ������ �������� � �������� �������� ���� �� ������ ������� � ���������, ������ ������� ������� ������ (� ������ ������ ���� ���������). ��� ����� ��� ���� ����� ������������ ��������... ������� � ��������� ������� ���������� ����������� �������� � ����� ��� ���������������. ������ �������� ���������� ������� �����, � ������� ������������� � �������. �����-��������� ������ �������� �� ���, ����� �� ���� ���-�� ��� ������� (�� ��, �� ��� �� ������, ��� ����� �������� ��������� �� �����������, ��� ����� � �� ���� � ����������������, �� �������� � �������� ��� �� ���������, � ������� �������� �������� ����� ������������ ����)...";
	private static String EECONDITION = "��� � ������";
	protected JDictionary dict;
	protected ArrayList<ActionListener> listeners = new ArrayList<ActionListener>();

	public JTranslater() {
		this(null, null);
	}

	public JTranslater(String jgrd) {
		this(null, jgrd);
	}

	public JTranslater(JDictionary dict) {
		this(dict, null);
	}

	public JTranslater(JDictionary dict, String jgrd) {
		if (jgrd == null || jgrd.equals(""))
			JGRD = "�Ш����������������";
		else
			JGRD = jgrd;
		if (dict == null)
			try {
				this.dict = JDictionary.getDefaultDictionary((int) Math.pow(2,
						JGRD.length()) - 2);
			} catch (IOException e) {
			}
		else
			this.dict = dict;
	}

	public String translateRus2Dja(String text) {
		return translateRus2Dja(text, false);
	}

	public String translateRus2Dja(String text, boolean autoAddWords) {
		String ee = getEasterEgg(text);
		if (ee != null)
			return ee;
		if (dict == null)
			return null;
		StringBuilder result = new StringBuilder(), word = new StringBuilder(), separator = new StringBuilder();
		ArrayList<String> words = new ArrayList<String>(), separators = new ArrayList<String>(), newWords = new ArrayList<String>();
		int i = 0, j = 1;
		for (; i < text.length(); i++) {
			if (Character.isLetter(text.charAt(i)) || text.charAt(i) == '-') {
				word.append(text.charAt(i));
				if (!separator.toString().equals("")) {
					if (j == 0)
						separators.add(separator.toString());
					else
						result.append(separator.toString());
					separator.setLength(0);
				}
			} else {
				separator.append(text.charAt(i));
				if (!word.toString().equals("")) {
					words.add(word.toString());
					j = 0;
					word.setLength(0);
				}
			}
		}
		if (!separator.toString().equals(""))
			separators.add(separator.toString());
		if (!word.toString().equals(""))
			words.add(word.toString());
		i = 0;
		j = 0;
		while (i < words.size()) {
			for (j = Math.min(i + 1 + SMART_TRANSLATE_DEPTH, words.size() - 1); j >= i; j--) {
				StringBuilder str = new StringBuilder();
				for (int c = i; c <= j; c++) {
					str.append(words.get(c) + (c < j ? " " : ""));
				}
				String trslt = translateRusWord2Dja(str.toString());
				if (!str.toString().equals(trslt)) {
					result.append(trslt);
					i = j;
					j--;
					break;
				} else if (j == i) {
					if (autoAddWords)
						newWords.add(str.toString());
					result.append(str);
				}
			}
			if (i < separators.size())
				result.append(separators.get(i));
			i = j + 2;
		}
		if (dict.add(newWords.toArray(new String[newWords.size()])))
			return translateRus2Dja(text);
		return result.toString();
	}

	public String translateDja2Rus(String text) {
		if (dict == null)
			return null;
		int index = 0;
		while ((index = text.indexOf("!#", index)) != -1) {
			int i = text.indexOf("!", index + 2);
			String cmd = text.substring(index, i == -1 ? text.length() : i + 1);
			text = text.replaceFirst(cmd, "");
			index = i + 1;
			for (ActionListener listener : listeners) {
				listener.actionPerformed(new ActionEvent(this,
						ActionEvent.ACTION_PERFORMED, cmd.substring(0,
								cmd.length() + (cmd.endsWith("!") ? 0 : 1))));
			}
		}
		StringBuilder result = new StringBuilder();
		String txt = text.toLowerCase();
		int i = -1, j = 0;
		do {
			i = txt.indexOf(JGRD.toLowerCase(), i + 1);
			if (i != -1) {
				result.append(text.substring(j, i));
				j = i + JGRD.length();
				result.append(translateDjaWord2Rus(text.substring(i, j)));
			}
		} while (i != -1);
		result.append(text.substring(j));
		return result.toString();
	}

	public String translateRusWord2Dja(String text) {
		if (dict == null)
			return null;
		if (text.equalsIgnoreCase("��������") || text.equalsIgnoreCase("jgrd"))
			return JGRD.toUpperCase();
		else if (text.equalsIgnoreCase("��������")
				|| text.equalsIgnoreCase("�����������")
				|| text.equalsIgnoreCase("lgrd"))
			return JGRD.toLowerCase();
		StringBuilder result = new StringBuilder();
		int index = dict.getIndex(text);
		if (index > -1) {
			String id = Integer.toBinaryString(index);
			if (id.length() > JGRD.length())
				return text;
			while (JGRD.length() - id.length() > 0)
				id = "0" + id;
			for (int i = 0; i < JGRD.length(); i++) {
				result.append(id.charAt(i) == '0' ? Character.toLowerCase(JGRD
						.charAt(i)) : Character.toUpperCase(JGRD.charAt(i)));
			}
		} else
			return text;
		return result.toString();
	}

	public String translateDjaWord2Rus(String text) {
		if (dict == null)
			return null;
		if (JGRD.toUpperCase().equals(text))
			return "��������";
		else if (JGRD.toLowerCase().equals(text))
			return "��������";
		else if (JGRD.equalsIgnoreCase(text)) {
			StringBuilder result = new StringBuilder();
			for (int i = 0; i < text.length(); i++)
				result.append(Character.isLowerCase(text.charAt(i)) ? '0' : '1');
			String word = dict.getWord(Integer.parseInt(result.toString(), 2));
			if (word != null && !word.equals(""))
				return word;
		}
		return text;
	}

	public JDictionary getDictionary() {
		return dict;
	}

	public String toString() {
		return dict.getName();
	}

	public void addActionListener(ActionListener listener) {
		listeners.add(listener);
		dict.listeners.add(listener);
	}

	public void removeActionListener(ActionListener listener) {
		listeners.remove(listener);
		dict.listeners.remove(listener);
	}

	public static String getEasterEgg(String text) {
		if (EECONDITION.equals(text))
			return EETEXT;
		else
			return null;
	}

}

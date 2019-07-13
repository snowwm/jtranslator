package ru.desireidea.jgrd;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;

public class JGUI implements ActionListener {

	protected JTextPane rus, dja;
	protected ImageButton up, down, copyRus, pasteRus, copyDja, pasteDja,
			copyHash, dictIcon, info;
	protected JButton add, addAll, importDict, exportDict, saveDict, browse;
	protected JTextField hashLabel;
	protected JComboBox<JTranslater> transBox;
	protected JCheckBox autoAddWords, autoAddHash;
	protected JPanel changeDict;
	protected JFrame frame;
	protected JTranslater trans;
	protected JTextField path;
	protected HashMap<JDictionary, String> hashes;

	public JGUI() {
		UIManager.put("OptionPane.yesButtonText", "Да");
		UIManager.put("OptionPane.noButtonText", "Нет");
		UIManager.put("OptionPane.cancelButtonText", "Отмена");
		UIManager.put("OptionPane.okButtonText", "Ок");
		UIManager.put("FileChooser.acceptAllFileFilterText", "Все файлы");
		UIManager.put("FileChooser.cancelButtonText", "Отмена");
		UIManager.put("FileChooser.directoryDescriptionText", "Папка");
		UIManager.put("FileChooser.directoryOpenButtonText", "Открыть");
		UIManager.put("FileChooser.fileDescriptionText", "Файл");
		UIManager.put("FileChooser.helpButtonText", "Справка");
		UIManager.put("FileChooser.newFolderErrorText",
				"Во время создания папки возникла ошибка");
		UIManager.put("FileChooser.openButtonText", "Открыть");
		UIManager.put("FileChooser.openDialogTitleText",
				"Выберите файл для импорта/экспорта словаря");
		UIManager.put("FileChooser.saveButtonText", "Сохранить");
		UIManager.put("FileChooser.saveDialogTitleText", "Сохранить");
		UIManager.put("FileChooser.updateButtonText", "Обновить");
		UIManager.put("FileChooser.saveDialogFileNameLabelText",
				"Сохранить как:");
		UIManager.put("FileChooser.newFolderExistsErrorText",
				"Это имя уже занято");
		UIManager.put("FileChooser.saveTitleText", "Сохранить");
		UIManager.put("FileChooser.chooseButtonText", "Выбор");
		UIManager.put("FileChooser.desktopName", "Рабочий стол");
		UIManager.put("FileChooser.mac.newFolder", "Новая папка");
		UIManager.put("FileChooser.newFolderAccessibleName", "Новая папка");
		UIManager.put("FileChooser.byDateText", "По дате");
		UIManager.put("FileChooser.byNameText", "По имени");
		UIManager.put("FileChooser.untitledFileName", "Файл");
		UIManager.put("FileChooser.fileNameLabelText", "Имя файла:");
		UIManager.put("FileChooser.newFolderButtonText", "Создать папку");
		UIManager.put("FileChooser.filesOfTypeLabelText",
				"Допустимые типы файлов:");
		UIManager.put("FileChooser.newFolderPromptText", "Имя папки:");
		UIManager.put("FileChooser.untitledFolderName", "Новая папка");
		UIManager.put("FileChooser.createButtonText", "Создать");
		UIManager.put("FileChooser.openTitleText", "Открыть");
		UIManager
				.put("FileChooser.mac.newFolder.subsequent", "Новая папка {0}");
		UIManager.put("FileChooser.newFolderTitleText", "Новая папка");
		UIManager.put("FileChooser.lookInLabelText", "Искать в:");
		UIManager.put("FileChooser.cancelButtonToolTipText",
				"Закрыть окно выбора файлов");
		UIManager.put("FileChooser.openButtonToolTipText",
				"Подтвердить выбор файла");
		UIManager.put("FileChooser.listViewButtonToolTipText", "Список");
		UIManager.put("FileChooser.listViewButtonAccessibleName", "Список");
		UIManager
				.put("FileChooser.detailsViewButtonToolTipText", "Подробности");
		UIManager.put("FileChooser.detailsViewButtonAccessibleName",
				"Подробности");
		UIManager.put("FileChooser.upFolderToolTipText", "Вверх");
		UIManager.put("FileChooser.upFolderAccessibleName", "Вверх");
		UIManager.put("FileChooser.homeFolderToolTipText", "Домой");
		UIManager.put("FileChooser.homeFolderAccessibleName", "Домой");
		UIManager.put("FileChooser.fileNameHeaderText", "Имя");
		UIManager.put("FileChooser.fileSizeHeaderText", "Размер");
		UIManager.put("FileChooser.fileTypeHeaderText", "Тип");
		UIManager.put("FileChooser.fileDateHeaderText", "Изменён");
		UIManager.put("FileChooser.fileAttrHeaderText", "Атрибуты");
		frame = new JFrame();
		frame.setLayout(new GridBagLayout());
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				final ArrayList<JDictionary> list = new ArrayList<JDictionary>();
				String items = "";
				for (int i = 0; i < transBox.getItemCount(); i++) {
					JDictionary dict = transBox.getItemAt(i).getDictionary();
					if (!dict.getHash().equals(hashes.get(dict))) {
						list.add(dict);
						items += "        - " + dict + "\n";
					}
				}
				if (!items.equals("")) {
					int option = JOptionPane.showOptionDialog(frame,
							"Следующие словари имеют несохранённые изменения:\n"
									+ items, "Предупреждение!",
							JOptionPane.OK_CANCEL_OPTION,
							JOptionPane.WARNING_MESSAGE, null, new Object[] {
									"Сохранить все словари и выйти",
									"Выйти без сохранения",
									"Вернуться в программу" }, null);
					if (option == 0) {
						new SwingWorker<Void, Integer>() {

							public Void doInBackground() {
								for (int i = 0; i < list.size(); i++) {
									try {
										list.get(i).upload();
										publish(i + 1);
									} catch (Exception e) {
									}
								}
								System.exit(0);
								return null;
							}

							public void process(List<Integer> chunks) {
								frame.setTitle("Выполняется сохраение словарей - "
										+ chunks.get(chunks.size() - 1)
										+ " из " + list.size());
							}

						}.execute();
						frame.setTitle("Выполняется сохраение словарей...");
						return;
					} else if (option == 2)
						return;
				}
				System.exit(0);
			}
		});
		frame.setIconImage(new ImageIcon(getResource("icon.png")).getImage());
		hashes = new HashMap<JDictionary, String>();
		transBox = new JComboBox<JTranslater>();
		transBox.addActionListener(this);
		JTranslater translater = new JTranslater();
		translater.getDictionary().addActionListener(this);
		addTranslater(translater);
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		copyRus = new ImageButton(
				new ImageIcon(getResource("copy.png")).getImage(),
				new ImageIcon(getResource("copy_.png")).getImage(), this);
		copyRus.setToolTipText("Скопировать всё");
		GridBagConstraints gbc = new GridBagConstraints(1, 1, 1, 1, 0, 0,
				GridBagConstraints.LINE_START, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0);
		panel.add(copyRus, gbc);
		pasteRus = new ImageButton(
				new ImageIcon(getResource("paste.png")).getImage(),
				new ImageIcon(getResource("paste_.png")).getImage(), this);
		pasteRus.setToolTipText("Вставить");
		gbc.gridx = 2;
		panel.add(pasteRus, gbc);
		gbc.gridx = 3;
		gbc.weightx = 1;
		gbc.insets = new Insets(0, 20, 0, 20);
		panel.add(new JLabel("Русский"), gbc);
		dictIcon = new ImageButton(
				new ImageIcon(getResource("dict.png")).getImage(),
				new ImageIcon(getResource("dict_.png")).getImage(), this);
		dictIcon.setToolTipText("Настройки словаря");
		gbc.gridx = 4;
		gbc.weightx = 0;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.insets = new Insets(0, 0, 0, 0);
		panel.add(dictIcon, gbc);
		gbc.gridx = 5;
		add = new JButton("Добавить выделенный текст в словарь");
		add.addActionListener(this);
		panel.add(add, gbc);
		gbc.gridx = 6;
		addAll = new JButton("Добавить в словарь все отсутствующие слова");
		addAll.addActionListener(this);
		panel.add(addAll, gbc);
		gbc.gridx = 7;
		gbc.insets = new Insets(0, 5, 0, 0);
		info = new ImageButton(
				new ImageIcon(getResource("info.png")).getImage(),
				new ImageIcon(getResource("info_.png")).getImage(), this);
		panel.add(info, gbc);
		gbc.gridx = 1;
		gbc.weightx = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(7, 7, 7, 7);
		frame.add(panel, gbc);
		changeDict = new JPanel();
		changeDict.setVisible(false);
		changeDict.setLayout(new GridBagLayout());
		importDict = new JButton("Импортировать словарь");
		importDict.addActionListener(this);
		importDict
				.setToolTipText("Загрузить новый словарь из выбранного расположения.");
		gbc.weightx = 0;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(0, 0, 0, 0);
		changeDict.add(importDict, gbc);
		exportDict = new JButton("Экспортировать словарь");
		exportDict.addActionListener(this);
		exportDict
				.setToolTipText("Сохранить текущий словарь в выбранное расположение.");
		gbc.gridx = 2;
		changeDict.add(exportDict, gbc);
		saveDict = new JButton("Сохранить словарь");
		saveDict.setToolTipText("Сохранить текущий словарь там, откуда он был загружен.");
		saveDict.addActionListener(this);
		gbc.gridx = 3;
		gbc.insets = new Insets(0, 0, 0, 5);
		changeDict.add(saveDict, gbc);
		path = new JTextField();
		path.setToolTipText("Расположение файла.");
		gbc.gridx = 4;
		gbc.weightx = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(0, 5, 0, 5);
		changeDict.add(path, gbc);
		browse = new JButton("Обзор");
		browse.addActionListener(this);
		gbc.gridx = 5;
		gbc.weightx = 0;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(0, 5, 0, 0);
		changeDict.add(browse, gbc);
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(0, 7, 0, 7);
		frame.add(changeDict, gbc);
		rus = new JTextPane();
		rus.setToolTipText("Используйте Ctrl+C для копирования, а Ctrl+X для вырезания выделенного текста. Ctrl+V - вставка.");
		gbc.gridy = 3;
		gbc.weightx = gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(7, 7, 7, 7);
		frame.add(new JScrollPane(rus), gbc);
		panel = new JPanel();
		GroupLayout gl = new GroupLayout(panel);
		panel.setLayout(gl);
		autoAddWords = new JCheckBox(
				"Автоматически добавлять в словарь отсутствующие слова");
		down = new ImageButton(
				new ImageIcon(getResource("down.png")).getImage(),
				new ImageIcon(getResource("down_.png")).getImage(), this);
		down.setToolTipText("Перевести с русского на джигурляндский");
		up = new ImageButton(new ImageIcon(getResource("up.png")).getImage(),
				new ImageIcon(getResource("up_.png")).getImage(), this);
		up.setToolTipText("Перевести с джигурляндского на русский");
		autoAddHash = new JCheckBox(
				"Автоматически добавлять хеш словаря в переводимый текст");
		autoAddHash
				.setToolTipText("Хеш используется для сравнения кодирующего и декодирующего словарей.");
		gl.setHorizontalGroup(gl
				.createSequentialGroup()
				.addComponent(autoAddHash, GroupLayout.PREFERRED_SIZE,
						GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
				.addGap(7)
				.addComponent(down, GroupLayout.PREFERRED_SIZE,
						GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
				.addGap(14)
				.addComponent(up, GroupLayout.PREFERRED_SIZE,
						GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
				.addGap(7)
				.addComponent(autoAddWords, GroupLayout.PREFERRED_SIZE,
						GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE));
		gl.setVerticalGroup(gl.createParallelGroup().addComponent(autoAddWords)
				.addComponent(down).addComponent(up).addComponent(autoAddHash));
		gl.linkSize(SwingConstants.HORIZONTAL, autoAddHash, autoAddWords);
		gbc.gridx = 1;
		gbc.gridy = 4;
		gbc.weightx = gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.NONE;
		frame.add(panel, gbc);
		dja = new JTextPane();
		dja.setToolTipText("Используйте Ctrl+C для копирования, а Ctrl+X для вырезания выделенного текста. Ctrl+V - вставка.");
		gbc.gridy = 5;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		frame.add(new JScrollPane(dja), gbc);
		panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		gbc.gridy = 1;
		gbc.weightx = gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(0, 0, 0, 0);
		copyDja = new ImageButton(copyRus.getReleasedImage(),
				copyRus.getPressedImage(), this);
		copyDja.setToolTipText("Скопирповать всё");
		panel.add(copyDja, gbc);
		gbc.gridx = 2;
		pasteDja = new ImageButton(pasteRus.getReleasedImage(),
				pasteRus.getPressedImage(), this);
		pasteDja.setToolTipText("Вставить");
		panel.add(pasteDja, gbc);
		gbc.gridx = 3;
		gbc.weightx = 1;
		gbc.insets = new Insets(0, 20, 0, 20);
		panel.add(new JLabel("Джигурляндский"), gbc);
		gbc.gridx = 4;
		gbc.weightx = 0;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.insets = new Insets(0, 0, 0, 7);
		panel.add(new JLabel("Словарь:"), gbc);
		gbc.gridx = 5;
		panel.add(transBox, gbc);
		JLabel label = new JLabel("Хеш словаря:");
		label.setToolTipText("Хеш используется для сравнения кодирующего и декодирующего словарей.");
		gbc.gridx = 6;
		panel.add(label, gbc);
		hashLabel = new JTextField();
		hashLabel.setEditable(false);
		FontMetrics fm = hashLabel.getFontMetrics(hashLabel.getFont());
		hashLabel.setPreferredSize(new Dimension(fm
				.stringWidth("55555555555555555555555555555555"), fm
				.getHeight() + 8));
		hashLabel
				.setToolTipText("Хеш используется для сравнения кодирующего и декодирующего словарей.");
		gbc.gridx = 7;
		panel.add(hashLabel, gbc);
		updateHash();
		copyHash = new ImageButton(copyRus.getReleasedImage(),
				copyRus.getPressedImage(), this);
		copyHash.setToolTipText("Скопировать хеш");
		gbc.gridx = 8;
		gbc.insets = new Insets(0, 0, 0, 0);
		panel.add(copyHash, gbc);
		gbc.gridx = 1;
		gbc.gridy = 6;
		gbc.weightx = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(7, 7, 7, 7);
		frame.add(panel, gbc);
		frame.pack();
		frame.setMinimumSize(new Dimension((int) (frame.getWidth() * 1.1),
				frame.getHeight() * 2));
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new JGUI();
			}
		});
	}

	public void actionPerformed(ActionEvent evt) {
		Object source = evt.getSource();
		if (source == down) {
			if (!rus.getText().equals("")) {
				new SwingWorker<String, Void>() {

					public String doInBackground() {
						return trans.translateRus2Dja(rus.getText(),
								autoAddWords.isSelected());
					}

					public void done() {
						String hash = "";
						if (autoAddHash.isSelected()) {
							hash = trans.getDictionary().getHash();
							hash = "!#md"
									+ (hash.startsWith("!") ? "n/d" : hash)
									+ "!";
						}
						try {
							dja.setText(hash + get());
						} catch (InterruptedException | ExecutionException e) {
						}
						frame.setTitle(trans.JGRD);
					}

				}.execute();
				frame.setTitle(trans.JGRD + "  -  Выполняется перевод...");
			}
		} else if (source == up) {
			if (!dja.getText().equals("")) {
				new SwingWorker<String, Void>() {

					public String doInBackground() {
						return trans.translateDja2Rus(dja.getText());
					}

					public void done() {
						try {
							dja.setText(get());
						} catch (InterruptedException | ExecutionException e) {
						}
						frame.setTitle(trans.JGRD);
					}

				}.execute();
				frame.setTitle(trans.JGRD + "  -  Выполняется перевод...");
			}
		} else if (source == copyRus) {
			rus.selectAll();
			rus.copy();
		} else if (source == pasteRus)
			rus.paste();
		else if (source == copyDja) {
			dja.selectAll();
			dja.copy();
		} else if (source == pasteDja)
			dja.paste();
		else if (source == copyHash) {
			hashLabel.selectAll();
			hashLabel.copy();
		} else if (source == add) {
			if (trans.getDictionary().add(rus.getSelectedText()))
				showMessage("Готово!", "Словарь обновлён.",
						JOptionPane.DEFAULT_OPTION,
						JOptionPane.INFORMATION_MESSAGE);
			else
				showMessage(
						"Ошибка!",
						"Не удалось обновить словарь. Возможно, выбранное слово уже присутствует в нём.",
						JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
		} else if (source == addAll) {
			if (!rus.getText().equals("")) {
				new SwingWorker<Void, Void>() {

					public Void doInBackground() {
						trans.translateRus2Dja(rus.getText(), true);
						return null;
					}

					public void done() {
						frame.setTitle(trans.JGRD);
					}

				}.execute();
				frame.setTitle(trans.JGRD
						+ "  -  Выполняется обновление словаря...");
			}
		} else if (source == dictIcon)
			changeDict.setVisible(!changeDict.isVisible());
		else if (source == browse) {
			JFileChooser chooser = new JFileChooser(path.getText());
			chooser.setApproveButtonText("Принять");
			if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION)
				path.setText(chooser.getSelectedFile().toString());
		} else if (source == importDict) {
			new SwingWorker<Boolean, Void>() {

				public Boolean doInBackground() {
					try {
						addTranslater(new JTranslater(new JDictionary(new File(
								path.getText()))));
						return true;
					} catch (Exception e) {
						return false;
					}
				}

				public void done() {
					frame.setTitle(trans.JGRD);
					try {
						if (get()) {
							showMessage("Готово!",
									"Словарь успешно импортирован.",
									JOptionPane.DEFAULT_OPTION,
									JOptionPane.INFORMATION_MESSAGE);
							return;
						}
					} catch (InterruptedException | ExecutionException e) {
					}
					showMessage(
							"Ошибка!",
							"Не удалось импортировать словарь. Убедитесь, что указанный файл существует и имеет допустимую структуру.",
							JOptionPane.DEFAULT_OPTION,
							JOptionPane.ERROR_MESSAGE);
				}

			}.execute();
			frame.setTitle(trans.JGRD
					+ "  -  Выполняется импортирование словаря...");
		} else if (source == exportDict) {
			boolean flag = true;
			File file = new File(path.getText());
			if (file.exists())
				if (showMessage(
						"Предупреждение!",
						"В расположении "
								+ file
								+ " уже существует другой файл.\nВы хотите перезаписать его?",
						JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) != JOptionPane.OK_OPTION)
					flag = false;
			if (flag) {
				new SwingWorker<Boolean, Void>() {

					public Boolean doInBackground() {
						try {
							JDictionary dict = trans.getDictionary();
							File file = new File(path.getText());
							file.mkdirs();
							file.delete();
							file.createNewFile();
							dict.upload(file);
							hashes.put(dict, dict.getHash());
							return true;
						} catch (Exception e) {
							return false;
						}
					}

					public void done() {
						frame.setTitle(trans.JGRD);
						try {
							if (get()) {
								showMessage("Готово!",
										"Словарь успешно экспортирован в файл "
												+ path.getText(),
										JOptionPane.DEFAULT_OPTION,
										JOptionPane.INFORMATION_MESSAGE);
								return;
							}
						} catch (InterruptedException | ExecutionException e) {
						}
						showMessage(
								"Ошибка!",
								"Не удалось экспортировать словарь. Попробуйте указать другой целевой файл.",
								JOptionPane.DEFAULT_OPTION,
								JOptionPane.ERROR_MESSAGE);
					}

				}.execute();
				frame.setTitle(trans.JGRD
						+ "  -  Выполняется экспортирование словаря...");
			}
		} else if (source == transBox) {
			trans = (JTranslater) transBox.getSelectedItem();
			frame.setTitle(trans.JGRD);
			updateHash();
		} else if (source == saveDict) {
			new Object();
			new SwingWorker<Boolean, Void>() {

				public Boolean doInBackground() {
					try {
						JDictionary dict = trans.getDictionary();
						dict.upload();
						hashes.put(dict, dict.getHash());
						return true;
					} catch (Exception e) {
						return false;
					}
				}

				public void done() {
					frame.setTitle(trans.JGRD);
					try {
						if (get()) {
							showMessage("Готово!",
									"Словарь успешно сохранён по адресу\n"
											+ trans.getDictionary()
													.getDefaultURI(),
									JOptionPane.DEFAULT_OPTION,
									JOptionPane.INFORMATION_MESSAGE);
							return;
						}
					} catch (InterruptedException | ExecutionException e) {
					}
					showMessage("Ошибка!",
							"Не удалось сохранить словарь по адресу\n"
									+ trans.getDictionary().getDefaultURI(),
							JOptionPane.DEFAULT_OPTION,
							JOptionPane.ERROR_MESSAGE);
				}

			}.execute();
			frame.setTitle(trans.JGRD
					+ "  -  Выполняется сохранение словаря...");
		} else if (source == info) {
			JOptionPane
					.showConfirmDialog(
							frame,
							"Джигурляндско-русский, русско-джигурляндский переводчик.\n© Desireidea 2013. All rights reserved.\n\nРазработчик: Павел Андреев aka. jgrd ГРЁЗОБЛАЖЕНСТВУЮЩИЙ\nhttp://vk.com/jgrdlgrd\nhttp://vk.com/jgrd_official\nj.g.r.d@bk.ru",
							"Информация", JOptionPane.DEFAULT_OPTION,
							JOptionPane.INFORMATION_MESSAGE, new ImageIcon(
									frame.getIconImage()));
		} else if (evt.getActionCommand().startsWith("!#update")
				&& source instanceof JDictionary) {
			SwingUtilities.invokeLater(new Runnable() {

				public void run() {
					updateHash();
				}

			});
		} else if (source instanceof JTranslater) {
			String cmd = evt.getActionCommand(), hash = cmd.substring(4,
					Math.min(cmd.length(), 36));
			if (cmd.startsWith("!#md")
					&& !hash.equals(trans.getDictionary().getHash()))
				SwingUtilities.invokeLater(new Runnable() {

					public void run() {
						showMessage(
								"Предупреждение!",
								"Хеш словаря, используемого программой в данный момент, не совпадает с хешем, прикреплённым к переводимому тексту.\nЭто может вызвать ошибки при переводе.\nРекомендуется использовать для декодирования сообщения тот же словарь, что использовался для кодирования.",
								JOptionPane.DEFAULT_OPTION,
								JOptionPane.WARNING_MESSAGE);
					}

				});
		}
	}

	public void updateHash() {
		try {
			hashLabel.setText(trans.getDictionary().getHash().replace("!", ""));
		} catch (NullPointerException e) {
		}
	}

	public int showMessage(String header, Object message, int options, int type) {
		return JOptionPane.showConfirmDialog(frame, message, header, options,
				type);
	}

	private URL getResource(String name) {
		return getClass().getResource("/ru/desireidea/jgrd/res/" + name);
	}

	public void addTranslater(JTranslater trans) {
		JDictionary dict = trans.getDictionary();
		transBox.addItem(trans);
		transBox.setSelectedItem(trans);
		trans.addActionListener(this);
		hashes.put(dict, dict.getHash());
	}

	public void removeTranslater(JTranslater trans) {
		transBox.removeItem(trans);
		trans.removeActionListener(this);
		hashes.remove(trans.getDictionary());
	}

}

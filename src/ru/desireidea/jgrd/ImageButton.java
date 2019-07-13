package ru.desireidea.jgrd;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.AbstractButton;

public class ImageButton extends AbstractButton implements MouseListener {

	protected Image imgR, imgP;
	protected ActionListener listener;
	protected boolean state;

	public ImageButton() {
		super();
	}

	public ImageButton(ActionListener listener) {
		this(null, null, listener);
	}

	public ImageButton(Image img) {
		this(img, null, null);
	}

	public ImageButton(Image img, ActionListener listener) {
		this(img, null, listener);
	}

	public ImageButton(Image imgR, Image imgP) {
		this(imgR, imgP, null);
	}

	public ImageButton(Image imgR, Image imgP, ActionListener listener) {
		super();
		this.imgR = imgR;
		this.imgP = imgP;
		this.listener = listener;
		addMouseListener(this);
		computeSize();
	}

	public void paint(Graphics g) {
		g.drawImage(state && imgP != null ? imgP : imgR, 0, 0, getWidth() - 1,
				getHeight() - 1, null);
	}

	public Image createImage() {
		imgR = createImage(getWidth() - 1, getHeight() - 1);
		return imgR;
	}

	public Image getPressedImage() {
		return imgP;
	}

	public Image getReleasedImage() {
		return imgR;
	}

	public void setPressedImage(Image img) {
		imgP = img;
		computeSize();
		repaint();
	}

	public void setReleasedImage(Image img) {
		imgR = img;
		computeSize();
		repaint();
	}

	public boolean getState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	public ActionListener getListener() {
		return listener;
	}

	public void setListener(ActionListener listener) {
		this.listener = listener;
	}

	public void computeSize() {
		Dimension size = new Dimension(Math.max(imgP.getWidth(this),
				imgR.getWidth(this)), Math.max(imgP.getHeight(this),
				imgR.getHeight(this)));
		setMinimumSize(size);
		setPreferredSize(size);
	}

	public void mouseClicked(MouseEvent arg0) {
	}

	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseExited(MouseEvent arg0) {
	}

	public void mousePressed(MouseEvent arg0) {
		state = true;
		repaint();
	}

	public void mouseReleased(MouseEvent arg0) {
		state = false;
		repaint();
		if (listener != null)
			listener.actionPerformed(new ActionEvent(this,
					ActionEvent.ACTION_PERFORMED, null));
	}

}

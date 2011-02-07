/*
 * This file is part of the OSMembrane project.
 * More informations under www.osmembrane.de
 * 
 * The project is licensed under the Creative Commons
 * Attribution-NonCommercial-ShareAlike 3.0 Unported License.
 * for more details about the license see
 * http://www.osmembrane.de/license/
 * 
 * Source: $HeadURL$ ($Revision$)
 * Last changed: $Date$
 */


package de.osmembrane.view.dialogs;

import java.awt.GraphicsEnvironment;
import java.awt.Point;

import de.osmembrane.view.interfaces.IBoundingBoxDialog;
import de.unistuttgart.iev.osm.bboxchooser.BBoxChooserDialog;
import de.unistuttgart.iev.osm.bboxchooser.Bounds;
import de.unistuttgart.iev.osm.bboxchooser.DialogResponse;

/**
 * 
 * Simple dialog to display the generated command line, export it, or copy it to
 * the clipboard.
 * 
 * @see Spezifikation.pdf, chapter 2.4
 * 
 * @author tobias_kuhn
 * 
 */
public class BoundingBoxDialog implements IBoundingBoxDialog {

	private static final long serialVersionUID = 5182327519016989905L;

	/**
	 * The external {@link BBoxChooserDialog} that will be used.
	 */
	private BBoxChooserDialog dialog;

	/**
	 * Creates a new {@link BoundingBoxDialog}
	 */
	public BoundingBoxDialog() {
		dialog = new BBoxChooserDialog();
		dialog.setModal(true);
	}

	@Override
	public void hideWindow() {
		dialog.setVisible(false);
	}

	@Override
	public void setWindowTitle(String viewTitle) {
		dialog.setTitle(viewTitle);
	}

	@Override
	public void centerWindow() {
		Point screenCenter = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getCenterPoint();
		Point edgeLeftTop = new Point(screenCenter.x - (dialog.getWidth() / 2),
				screenCenter.y - (dialog.getHeight() / 2));
		dialog.setLocation(edgeLeftTop.x, edgeLeftTop.y);
	}

	@Override
	public void bringToFront() {
		dialog.toFront();
	}

	@Override
	public void showWindow() {
		dialog.setVisible(true);
	}

	@Override
	public Bounds getBoundingBox() {
		return (dialog.getResponse() == DialogResponse.OK) ? dialog
				.getBoundingBox() : null;
	}

	@Override
	public void setBoundingBox(Bounds bounds) {
		dialog.setBoundingBox(bounds);
	}

	@Override
	public void dispose() {
		dialog.dispose();
	}

}

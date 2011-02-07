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


package de.osmembrane.view;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import de.osmembrane.Application;
import de.osmembrane.Main;
import de.osmembrane.controller.ActionRegistry;
import de.osmembrane.exceptions.ControlledException;
import de.osmembrane.exceptions.ExceptionSeverity;
import de.osmembrane.model.settings.SettingType;
import de.osmembrane.model.settings.SettingsObserverObject;
import de.osmembrane.tools.I18N;
import de.osmembrane.view.dialogs.ExceptionDialog;
import de.osmembrane.view.frames.MainFrame;
import de.osmembrane.view.interfaces.IView;

/**
 * The view registry implements the View-Handler pattern to organize the
 * {@link IView}s.
 * 
 * @author tobias_kuhn
 * 
 */
public class ViewRegistry extends Observable implements Observer {

	/**
	 * implements the error-handling dialog Note: This is important to be static
	 * and external to the {@link ViewRegistry} instance, because anyway else
	 * occurring exceptions during anything static would cause an
	 * {@link ExceptionInInitializerError} because the error handling would
	 * require the ViewRegistry instance to be available.
	 */
	private static ExceptionDialog exceptionDialog;

	/**
	 * implements the Singleton pattern
	 */
	private static ViewRegistry instance = new ViewRegistry();

	/**
	 * internal storage of the views, indexed by class
	 */
	private Map<Class<? extends IView>, IView> views = new HashMap<Class<? extends IView>, IView>();

	/**
	 * initializes the view registry
	 */
	private ViewRegistry() {
		// try to set LnF to Nimbus, if available
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			// if setLookAndFeel() failed
			Application.handleException(e);
		}
	}

	/**
	 * 
	 * @return the one and only instance of {@link ViewRegistry}
	 */
	public static ViewRegistry getInstance() {
		return instance;
	}

	/**
	 * Adds a view to the registry
	 * 
	 * @param view
	 *            IView to add
	 */
	public void register(IView view) {
		views.put(view.getClass(), view);
	}

	/**
	 * Returns a view from the registry. If not already registered, creates it
	 * and registers it.
	 * 
	 * @param clazz
	 *            desired class to return
	 * @return the registered object for that class
	 */
	public IView get(Class<? extends IView> clazz) {
		IView result = views.get(clazz);

		// if it does not exist, create it
		if (result == null) {
			try {
				result = clazz.newInstance();
			} catch (Exception e) {
				Application.handleException(e);
			}
			views.put(clazz, result);
		}

		return result;
	}

	/**
	 * Returns a view from the registry. If not already registered, creates it
	 * and registers it. Tries to cast it to castTo. Throws
	 * {@link ClassCastException}, if not possible. Causes critical failure, if
	 * break-in is tried (when clazz is not an interface).
	 * 
	 * @param castTo
	 *            the desired type to return
	 * @param clazz
	 *            desired <b>interface</b> to return
	 * @return the registered object for that class casted to type castTo
	 * @throws ClassCastException
	 *             if clazz cannot be of type castTo. Test clazz instanceof
	 *             castTo, if you want to make sure (Java 1.6 Generics don't
	 *             support this)
	 */
	@SuppressWarnings("unchecked")
	public <T extends IView> T getCasted(Class<? extends IView> clazz,
			Class<? extends T> castTo) {
		// prevent break-ins
		if (!castTo.isInterface()) {
			Application.handleException(new ControlledException(this,
					ExceptionSeverity.CRITICAL_UNEXPECTED_BEHAVIOR, I18N
							.getInstance().getString("View.IllegalCast")));
		}

		IView result = get(clazz);
		return (T) result;
	}

	@Override
	public void update(Observable o, Object arg) {
		// if this means language change, do your worst
		if (arg instanceof SettingsObserverObject) {
			SettingsObserverObject soo = (SettingsObserverObject) arg;
			if (soo.getChangedEntry() == SettingType.ACTIVE_LANGUAGE) {
				exceptionDialog = null;
				for (IView iv : views.values()) {
					iv.hideWindow();
					iv.dispose();
				}

				views.clear();
				deleteObservers();
				// if Java GC understands that, I'm impressed
				System.gc();

				ActionRegistry.getInstance().reinitialize();

				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						Main.getApplication().showMainFrame();
					}
				});
			}
		}

		// forward update() for Model classes
		setChanged();
		notifyObservers(arg);
	}

	/**
	 * Note: The {@link ViewRegistry} can be exchanged along with all view
	 * components, so it has to identify its main window for the purpose of
	 * showing this window at startup.
	 * 
	 * @return the {@link MainFrame} of this view component
	 */
	public IView getMainFrame() {
		return get(MainFrame.class);
	}

	/**
	 * Displays an occurred exception using the {@link ExceptionDialog}. This
	 * method should not be called directly. Use the
	 * {@link Application#handleException} method instead.
	 * 
	 * @param t
	 *            {@link Throwable} that caused the exception
	 * @param severity
	 *            indicates what happened and how fatal it is
	 * @param causingObject
	 *            object that caused the
	 */
	public static void showException(Throwable t, ExceptionSeverity severity,
			Object causingObject) {
		if (exceptionDialog == null) {
			exceptionDialog = new ExceptionDialog();
		}
		exceptionDialog.showException(t, severity, causingObject);
	}
}

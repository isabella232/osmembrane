package de.osmembrane.controller.actions;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import de.osmembrane.Application;
import de.osmembrane.controller.ActionRegistry;
import de.osmembrane.exceptions.ControlledException;
import de.osmembrane.exceptions.ExceptionSeverity;
import de.osmembrane.model.ModelProxy;
import de.osmembrane.model.persistence.FileException;
import de.osmembrane.resources.Resource;
import de.osmembrane.tools.I18N;
import de.osmembrane.tools.IconLoader.Size;

/**
 * Action to store a OSMembrane pipeline in file.
 * 
 * @author tobias_kuhn
 * 
 */
public class SavePipelineAction extends AbstractAction {

	private static final long serialVersionUID = 5036259208332239931L;

	/**
	 * Creates a new {@link SavePipelineAction}
	 */
	public SavePipelineAction() {
		putValue(Action.NAME, "Save Pipeline");
		putValue(Action.SMALL_ICON, Resource.PROGRAM_ICON.getImageIcon(
				"save_pipeline.png", Size.SMALL));
		putValue(Action.LARGE_ICON_KEY, Resource.PROGRAM_ICON.getImageIcon(
				"save_pipeline.png", Size.NORMAL));
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		setEnabled(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (ModelProxy.getInstance().accessPipeline().getFilename() == null) {
			ActionRegistry.getInstance().get(SaveAsPipelineAction.class)
					.actionPerformed(null);
		} else {
			try {
				ModelProxy.getInstance().accessPipeline().savePipeline();
			} catch (FileException e1) {
				Application.handleException(new ControlledException(this,
						ExceptionSeverity.WARNING, e1, I18N.getInstance()
								.getString(
										"Controller.Actions.Save.Failed."
												+ e1.getType())));
			}
		}
	}
}
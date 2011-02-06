package de.osmembrane.view.interfaces;

import de.osmembrane.model.settings.FunctionPreset;
import de.osmembrane.view.dialogs.FunctionPresetDialog;

/**
 * The interface for {@link FunctionPresetDialog}.
 * 
 * @author tobias_kuhn
 * 
 */
public interface IFunctionPresetDialog extends IView {

	/**
	 * Opens the dialog for a specific set of presets.
	 * 
	 * @param presets
	 *            The presets to be able to choose from. May be empty.
	 */
	void open(FunctionPreset[] presets);

	/**
	 * @return the {@link FunctionPreset} that was selected, or null if none was
	 *         selected
	 */
	FunctionPreset getSelectedPreset();

	/**
	 * @return whether the Load button was clicked
	 */
	boolean loadSelected();

	/**
	 * @return whether the Delete button was clicked
	 */
	boolean deleteSelected();
}

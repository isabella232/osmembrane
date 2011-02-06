package de.osmembrane.model.settings;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Observable;

import de.osmembrane.Application;
import de.osmembrane.exceptions.ControlledException;
import de.osmembrane.exceptions.ExceptionSeverity;
import de.osmembrane.model.persistence.AbstractPersistence;
import de.osmembrane.model.persistence.FileException;
import de.osmembrane.model.persistence.PersistenceFactory;
import de.osmembrane.model.persistence.SettingPersistence;
import de.osmembrane.model.pipeline.AbstractFunction;
import de.osmembrane.resources.Constants;
import de.osmembrane.tools.I18N;

/**
 * Implementation of {@link AbstractSettings}.
 * 
 * @author jakob_jarosch
 */
public class Settings extends AbstractSettings {

	private static final long serialVersionUID = 2011020217010001L;

	private static final String FUNCTION_PRESET_KEY = "functionPresetKey";

	Map<Object, Object> settingsMap = new HashMap<Object, Object>();

	@Override
	public void initiate() {
		AbstractPersistence persistence = PersistenceFactory.getInstance()
				.getPersistence(SettingPersistence.class);

		File file = new File(Constants.DEFAULT_SETTINGS_FILE.toString()
				.replace("file:", ""));

		/*
		 * register the persistence as observer for automatic saving of the
		 * settings
		 */
		addObserver(PersistenceFactory.getInstance());

		try {
			if (!file.isFile()) {
				saveSettings();
			}

			Object obj = persistence.load(Constants.DEFAULT_SETTINGS_FILE);

			/* is checked by persistence */
			@SuppressWarnings("unchecked")
			Map<Object, Object> settingsMap = (Map<Object, Object>) obj;
			this.settingsMap = settingsMap;
		} catch (FileException e) {
			Application.handleException(new ControlledException(this,
					ExceptionSeverity.WARNING, I18N.getInstance().getString(
							"Model.Settings.FileException", file)));
		}
	}

	@Override
	public Object getValue(SettingType type) {
		Object result = settingsMap.get(type);
		if (result != null) {
			return result;
		} else {
			return type.getDefaultValue();
		}
	}

	@Override
	public void setValue(SettingType type, Object value)
			throws UnparsableFormatException {
		if (!type.getType().isInstance(value)) {
			value = type.parse(value);
		}

		/* perform required actions as change the localization */
		type.doRequiredActions(value);

		settingsMap.put(type, value);
		changedNotifyObservers(new SettingsObserverObject(type));
	}

	@Override
	public Locale[] getLanguages() {
		return Constants.AVAILABLE_LOCALES;
	}

	@Override
	public void saveSettings() throws FileException {
		AbstractPersistence persistence = PersistenceFactory.getInstance()
				.getPersistence(SettingPersistence.class);

		persistence.save(Constants.DEFAULT_SETTINGS_FILE, settingsMap);
	}

	@Override
	public void saveFunctionPreset(String name, AbstractFunction function) {
		FunctionPreset preset = new FunctionPreset(name, function);
		getFPList().add(preset);

		changedNotifyObservers(new SettingsObserverObject());
	}

	@Override
	public AbstractFunctionPreset[] getAllFunctionPresets(
			AbstractFunction function) {
		List<AbstractFunctionPreset> fpList = new ArrayList<AbstractFunctionPreset>();
		for (FunctionPreset preset : getFPList()) {
			if (preset.getInheritedFunction().getId().equals(function.getId())) {
				fpList.add(preset);
			}
		}

		return fpList.toArray(new AbstractFunctionPreset[fpList.size()]);
	}

	@Override
	public boolean deleteFunctionPreset(AbstractFunctionPreset preset) {
		boolean returnValue = getFPList().remove(preset);

		changedNotifyObservers(new SettingsObserverObject());

		return returnValue;
	}

	/**
	 * Returns the FunctionPreset list.
	 * 
	 * @return the {@link FunctionPreset} list.
	 */
	private List<FunctionPreset> getFPList() {
		Object result = settingsMap.get(FUNCTION_PRESET_KEY);

		if (result == null || !(result instanceof List)) {
			settingsMap.put(FUNCTION_PRESET_KEY,
					new ArrayList<FunctionPreset>());
		}

		@SuppressWarnings("unchecked")
		List<FunctionPreset> presetMap = (List<FunctionPreset>) settingsMap
				.get(FUNCTION_PRESET_KEY);

		return presetMap;
	}

	@Override
	public void update(Observable o, Object arg) {
		notifyObservers(settingsMap);
	}

	@Override
	protected void changedNotifyObservers(SettingsObserverObject soo) {
		soo.setSettingsModel(this);
		setChanged();
		notifyObservers(soo);
	}
}
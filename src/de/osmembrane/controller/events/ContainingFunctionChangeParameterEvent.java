package de.osmembrane.controller.events;

import de.osmembrane.model.pipeline.AbstractFunction;
import de.osmembrane.model.pipeline.AbstractTask;

/**
 * ActionEvent to contain a specific {@link AbstractFunction} and the changed
 * parameter index, value it was changed to, and/or the task that it was changed
 * to.
 * 
 * @author tobias_kuhn
 * 
 */
public class ContainingFunctionChangeParameterEvent extends ContainingEvent {

	private static final long serialVersionUID = 9051024945589013774L;

	/**
	 * index of the parameter that was changed
	 */
	private int changedParameter;

	/**
	 * the new value of the parameter with index changedParameter
	 */
	private String newParameterValue;

	/**
	 * the new AbstractTask the function was set to
	 */
	private AbstractTask newTask;

	/**
	 * Creates a ContainingFunctionChangeParameterEvent for the function
	 * contained, that yet yields no changes.
	 * 
	 * @param source
	 * @param contained
	 */
	public ContainingFunctionChangeParameterEvent(Object source,
			AbstractFunction contained) {
		super(source, contained);
		changedParameter = -1;
		newParameterValue = null;
		newTask = null;
	}

	/**
	 * @param changedParameter
	 *            the changedParameter to set
	 */
	public void setChangedParameter(int changedParameter) {
		this.changedParameter = changedParameter;
	}

	/**
	 * @return the changedParameter
	 */
	public int getChangedParameter() {
		return changedParameter;
	}

	/**
	 * @param newParameterValue
	 *            the newParameterValue to set
	 */
	public void setNewParameterValue(String newParameterValue) {
		this.newParameterValue = newParameterValue;
	}

	/**
	 * @return the newParameterValue
	 */
	public String getNewParameterValue() {
		return newParameterValue;
	}

	/**
	 * @return whether a newParameter was set
	 */
	public boolean wasNewParameterSet() {
		return (changedParameter > -1) && (newParameterValue != null);
	}

	/**
	 * @param newTask
	 *            the newTask to set
	 */
	public void setNewTask(AbstractTask newTask) {
		this.newTask = newTask;
	}

	/**
	 * @return the newTask
	 */
	public AbstractTask getNewTask() {
		return newTask;
	}

	/**
	 * @return whether a newTask was set
	 */
	public boolean wasNewTaskSet() {
		return (newTask != null);
	}

}

package de.osmembrane.model;

import java.util.ArrayList;
import de.osmembrane.model.Function;
import de.osmembrane.model.xml.XMLFunctionGroup;

public class FunctionGroup extends AbstractFunctionGroup {
	public ArrayList<Function> unnamed_Function_ = new ArrayList<Function>();

	public XMLFunctionGroup getXMLFunctionGroup() {
		throw new UnsupportedOperationException();
	}

	public AbstractFunction[] getFunctions() {
		throw new UnsupportedOperationException();
	}
}
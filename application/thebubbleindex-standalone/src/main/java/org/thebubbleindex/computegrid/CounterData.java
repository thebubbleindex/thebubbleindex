package org.thebubbleindex.computegrid;

import org.openspaces.core.GigaSpace;

import com.gigaspaces.document.SpaceDocument;
import com.gigaspaces.metadata.SpaceTypeDescriptor;
import com.gigaspaces.metadata.SpaceTypeDescriptorBuilder;
import com.gigaspaces.metadata.index.SpaceIndexType;

/**
 * https://docs.gigaspaces.com/xap/12.1/dev-java/the-space-counters.html
 * 
 * @author thebubbleindex
 *
 */
public class CounterData extends SpaceDocument {
	public static final String TYPE_NAME = "CounterData";

	public CounterData() {
		super(TYPE_NAME);
	}

	static public void registerType(final GigaSpace gigaspace) {
		// Create type descriptor:
		final SpaceTypeDescriptor typeDescriptor = new SpaceTypeDescriptorBuilder(TYPE_NAME)
				.documentWrapperClass(CounterData.class).addFixedProperty("id", String.class)
				.idProperty("id", false, SpaceIndexType.BASIC).create();
		// Register type:
		gigaspace.getTypeManager().registerTypeDescriptor(typeDescriptor);
	}
}

package org.thebubbleindex.computegrid;

import org.openspaces.core.GigaSpace;

import com.gigaspaces.client.ChangeSet;
import com.gigaspaces.document.SpaceDocument;
import com.gigaspaces.query.IdQuery;

/**
 * https://docs.gigaspaces.com/xap/12.1/dev-java/the-space-counters.html
 * 
 * @author thebubbleindex
 *
 */
public class XAPCounter {
	private final GigaSpace space;
	private final String id;

	public XAPCounter(final GigaSpace space, final String id, final String name, final int initialValue) {
		this.space = space;
		this.id = id;

		CounterData.registerType(space);

		final CounterData counterData = new CounterData();
		counterData.setProperty(name, initialValue);
		counterData.setProperty("id", id);

		final CounterData existing = space.read(counterData);
		
		if (existing == null)
			space.write(counterData);
	}

	public Integer get(final String name) throws Exception {
		final SpaceDocument spaceDocument = space.read(getSpaceDocumentIdQuery(id));
		return spaceDocument.getProperty(name);
	}

	public void increment(final String name, final int value) {
		space.change(getSpaceDocumentIdQuery(id), new ChangeSet().increment(name, value));
	}

	public void decrement(final String name, final int value) {
		space.change(getSpaceDocumentIdQuery(id), new ChangeSet().decrement(name, value));
	}

	public void unset(final String name) {
		space.change(getSpaceDocumentIdQuery(id), new ChangeSet().unset(name));
	}

	private IdQuery<SpaceDocument> getSpaceDocumentIdQuery(final String id) {
		return new IdQuery<SpaceDocument>(CounterData.TYPE_NAME, id);
	}
}

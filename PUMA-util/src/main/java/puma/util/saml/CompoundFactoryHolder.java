package puma.util.saml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class CompoundFactoryHolder<T extends RetrievableCompoundFactory<?>> implements Iterator<T> {
	private Integer pointer;
	private List<T> compoundFactories;
	
	public CompoundFactoryHolder() {
		this.pointer = 0;
		this.compoundFactories = new ArrayList<T>();
	}
	
	public void addFactory(T factory) {
		this.compoundFactories.add(factory);
	}

	@Override
	public boolean hasNext() {
		return this.pointer < this.compoundFactories.size();
	}

	@Override
	public T next() {
		if (!this.hasNext())
			throw new NoSuchElementException();
		return this.compoundFactories.get(this.pointer++);
	}

	@Override
	public void remove() {
		if (this.pointer == 0)
			throw new IllegalStateException();
		this.compoundFactories.remove(this.pointer - 1);
	}
}

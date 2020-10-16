package org.panacea.drmp.age.procedures.util.network_only;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import org.neo4j.graphdb.traversal.UniquenessFactory;
import org.neo4j.graphdb.traversal.UniquenessFilter;

public enum MyUniqueness implements UniquenessFactory {
	NODE_HOSTNAME_PATH {
		public UniquenessFilter create(Object optionalParameter) {
			this.acceptNull(optionalParameter);
			return new PathNodeHostnameUnique(MyPrimitiveTypeFetcher.NODE);
		}

		public boolean eagerStartBranches() {
			return true;
		}
	};
	private static final UniquenessFilter notUniqueInstance = new MyNotUnique();

	private MyUniqueness() {
	}

	public static void acceptNull(Object optionalParameter) {
		if (optionalParameter != null) {
			throw new IllegalArgumentException("Only accepts null parameter, was " + optionalParameter);
		}
	}

	public static void acceptIntegerOrNull(Object parameter) {
		if (parameter != null) {
			boolean isDecimalNumber = parameter instanceof Number && !(parameter instanceof Float) && !(parameter instanceof Double);
			if (!isDecimalNumber) {
				throw new IllegalArgumentException("Doesn't accept non-decimal values, like '" + parameter + "'");
			}
		}
	}
}

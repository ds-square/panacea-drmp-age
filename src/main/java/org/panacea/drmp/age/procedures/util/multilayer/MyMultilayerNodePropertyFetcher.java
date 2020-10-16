package org.panacea.drmp.age.procedures.util.multilayer;

import lombok.extern.slf4j.Slf4j;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;

import java.util.HashSet;
import java.util.Set;

@Slf4j
enum MyMultilayerNodePropertyFetcher
{
	NODE
			{
				@Override
				long getId( Path source )
				{
					if ( source == null )
						System.out.println( "source null" );
					if ( source.endNode() == null )
						System.out.println( "endNode null for " + source );
					return source.endNode().getId();
				}

				@Override
				String getDeviceId(Path source) {
					if (source == null)
						System.out.println("source null");
					if (source.endNode() == null)
						System.out.println("endNode null for " + source);
					return (String) source.endNode().getProperty("deviceId");
				}

				@Override
				String getName(Path source) {
					if (source == null)
						System.out.println("source null");
					if (source.endNode() == null)
						System.out.println("endNode null for " + source);
					return (String) source.endNode().getProperty("name");
				}


				@Override
				String getPrivLevel(Path source) {
					if (source == null)
						System.out.println("source null");
					if (source.endNode() == null)
						System.out.println("endNode null for " + source);

					return (String) source.endNode().getProperty("privLevel");
				}

				@Override
				boolean hasLabel(Path source, String l) {
					return source.endNode().hasLabel(Label.label(l));
				}

				@Override
				boolean idEquals( Path source, long idToCompare )
				{
					return getId( source ) == idToCompare;
				}

				@Override
				boolean deviceIdEquals(Path source, String idToCompare )
				{
					return getDeviceId( source ).equals(idToCompare);
				}

				@Override
				boolean nameEquals( Path source, String nameToCompare )
				{
					return getName( source ).equals(nameToCompare);
				}



				@Override
				boolean containsDuplicates( Path source )
				{
					log.info("called");
					Set<Node> nodes = new HashSet<Node>();
					Set<String> ids = new HashSet<String>();
					for (Node node : source.reverseNodes())

						if (!nodes.add(node) || !ids.add((String) node.getProperty("deviceId")))
							return true;
					return false;
				}
			};

	abstract long getId(Path path);

	abstract String getDeviceId(Path path);

	abstract String getName(Path path);

	abstract String getPrivLevel(Path path);

	abstract boolean hasLabel(Path path, String label);

	abstract boolean idEquals(Path path, long idToCompare);

	abstract boolean deviceIdEquals(Path path, String idToCompare);

	abstract boolean nameEquals(Path path, String nameToCompare);

	abstract boolean containsDuplicates(Path path);
}

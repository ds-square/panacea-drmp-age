package org.panacea.drmp.age.procedures.util.network_only;

import lombok.extern.slf4j.Slf4j;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;

import java.util.HashSet;
import java.util.Set;

@Slf4j
enum MyPrimitiveTypeFetcher
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
				String getHostname(Path source) {
					if (source == null)
						System.out.println("source null");
					if (source.endNode() == null)
						System.out.println("endNode null for " + source);
					return (String) source.endNode().getProperty("deviceId");
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
				boolean idEquals( Path source, long idToCompare )
				{
					return getId( source ) == idToCompare;
				}

				@Override
				boolean hostnameEquals( Path source, String hostnameToCompare )
				{
					return getHostname( source ).equals(hostnameToCompare);
				}

				@Override
				boolean containsDuplicates( Path source )
				{
					log.info("called");
					Set<Node> nodes = new HashSet<Node>();
					Set<String> hostnames = new HashSet<String>();
					for (Node node : source.reverseNodes())

						if (!nodes.add(node) || !hostnames.add((String) node.getProperty("deviceId")))
							return true;
					return false;
				}
			};

	abstract long getId(Path path);

	abstract String getHostname(Path path);

	abstract String getPrivLevel(Path path);

	abstract boolean idEquals(Path path, long idToCompare);

	abstract boolean hostnameEquals(Path path, String hostnameToCompare);

	abstract boolean containsDuplicates(Path path);
}

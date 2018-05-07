package Server;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating TBGPProtocol objects.
 * implements ServerProtocolFactory 
 */
class TBGPProtocolFactory implements ServerProtocolFactory {
	
	@Override
	public AsyncServerProtocol<?> create(){
		return new TBGPProtocol();
	}
}

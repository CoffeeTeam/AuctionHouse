package network;

import commands.serializableCommands.SerializableLaunchOfferReq;

public class ClientUtils {

	public static void chooseAction(Object recvObject) {
		System.out.println(recvObject.getClass());

		if (recvObject instanceof SerializableLaunchOfferReq) {
			handleLaunchOfferRequest((SerializableLaunchOfferReq) recvObject);
		}
	}

	private static void handleLaunchOfferRequest(
			SerializableLaunchOfferReq launchedOffer) {
		System.out.println("Handle launch offer");
	}
}

package user;

import gui.GUI;

import javax.swing.SwingWorker;

import constants.StatusMessages;

public class UserTransferStatus {
	private String seller;
	private String status;
	/* progress is measured in percentages (0-100)*/
	private int progress;
	
	public UserTransferStatus(String seller, String status) {
		this.seller = seller;
		this.status = status;
		
		// TODO => remove mockup when service transfer will be up to date
		startMockupTransfer();
	}
	
	/**
	 * Mockup method to simulate service transfer
	 */
	private void startMockupTransfer() {
		final long period = 1000;
		final int inc = 10;

		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			long time = System.currentTimeMillis();

			@Override
			protected Void doInBackground() throws Exception {
				while (progress < 100) {
					if (System.currentTimeMillis() - time > period) {
						time = System.currentTimeMillis();
						progress += inc;
						
						if (progress > 20) {
							status = StatusMessages.transferInProgress;
						}
						
						GUI.repaintUserTable();
					}
				}
				
				return null;
			}
			
			@Override
			public void done() {
				status = StatusMessages.transferCompleted;
				GUI.repaintUserTable();
			}
			
		};

		worker.execute();
	}
	
	public String getSeller() {
		return seller;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public int getProgress() {
		return progress;
	}
}

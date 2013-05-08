package user;

import gui.GUI;

import javax.swing.SwingWorker;

import constants.StatusMessages;

public class UserTransferStatus {
	private String seller;
	private String status;
	/* progress is measured in percentages (0-100) */
	private int progress;

	public UserTransferStatus(String seller, String status) {
		this.seller = seller;
		this.status = status;
		this.progress = 0;

		startMockupTransfer();
	}

	/**
	 * Mockup method to simulate service transfer
	 */
	private void startMockupTransfer() {
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				while (progress < 100) {
					if (progress > 20) {
						status = StatusMessages.transferInProgress;
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

	public void setProgress(int progress) {
		this.progress = progress;
	}
	
	public UserTransferStatus getUserTransferStatus() {
		return this;
	}
}

package gui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class Actions {
	
	public static interface FAction {
		void actionPerformed(ActionEvent e);
	}
	
	private Actions()  {
		
	}
	
	public static AbstractAction createAction(FAction f) {
		return new AbstractAction() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				f.actionPerformed(e);
			}
			
		};
	}

	public static class Exit extends AbstractAction {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(1);
		}
		
	}	
	
}

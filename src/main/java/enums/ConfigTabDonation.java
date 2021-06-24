package enums;

import enums.classes.InfoGeralDonation;
import enums.classes.PermissionsDonation;
import enums.interfaces.ConfigTab;

public enum ConfigTabDonation {

	INFOGERAL("info-geral") {
		@Override
		public ConfigTab configTab() {
			return new InfoGeralDonation();
			
		}
	},
	PERMISSIONS("permission") {
		@Override
		public ConfigTab configTab() {
			return new PermissionsDonation();
			
		}
	},
	REQDONATION("req-donation") {
		@Override
		public ConfigTab configTab() {
			return new InfoGeralDonation();
			
		}
	},
	EXTRATO("extrato") {
		@Override
		public ConfigTab configTab() {
			return new InfoGeralDonation();
			
		}
	},
	DONATIONRECEIVED("donation-received") {
		@Override
		public ConfigTab configTab() {
			return new InfoGeralDonation();
			
		}
	},
	PROJECTS("project") {
		@Override
		public ConfigTab configTab() {
			return new InfoGeralDonation();
			
		}
	},
	PRESTCONT("prest-cont") {
		@Override
		public ConfigTab configTab() {
			return new InfoGeralDonation();
			
		}
	},
	OVERHEAD("overhead") {
		@Override
		public ConfigTab configTab() {
			return new InfoGeralDonation();
			
		}
	},
	COMPODONATION("compo-donation") {
		@Override
		public ConfigTab configTab() {
			return new InfoGeralDonation();
			
		}
	};
	
	
	private String nome;
	
	public abstract ConfigTab configTab();
	
	private ConfigTabDonation(String nome){
		this.nome = nome;
	}
	
	public String getNome() {
		return nome;
	}


	
	
}

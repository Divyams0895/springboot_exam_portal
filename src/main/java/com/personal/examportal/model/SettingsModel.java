package com.personal.examportal.model;

import jakarta.persistence.*;

@Entity
@Table(name="settings")
public class SettingsModel {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private String portalName;

	    private String logo; // filename or image path

	    private String footerText;

	    private String contactEmail;

	    private String themeColor; // optional for future enhancements

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getPortalName() {
			return portalName;
		}

		public void setPortalName(String portalName) {
			this.portalName = portalName;
		}

		public String getLogo() {
			return logo;
		}

		public void setLogo(String logo) {
			this.logo = logo;
		}

		public String getFooterText() {
			return footerText;
		}

		public void setFooterText(String footerText) {
			this.footerText = footerText;
		}

		public String getContactEmail() {
			return contactEmail;
		}

		public void setContactEmail(String contactEmail) {
			this.contactEmail = contactEmail;
		}

		public String getThemeColor() {
			return themeColor;
		}

		public void setThemeColor(String themeColor) {
			this.themeColor = themeColor;
		}
	    
	    
    
}

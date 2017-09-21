package com.infotrends.bean;

public class ProblemBean {
	private BasicProblemInfoBean fetBasicProblemInfoBean = new BasicProblemInfoBean();
	private DetailProblemInfoBean fetDetailProblemInfoBean = new DetailProblemInfoBean();
	private DetailProblemFormOneBean fetDetailProblemFormOneBean = new DetailProblemFormOneBean();
	private ContactPersonalInfoBean fetContactPersonalInfoBean = new ContactPersonalInfoBean();

	public BasicProblemInfoBean getFetBasicProblemInfoBean() {
		return fetBasicProblemInfoBean;
	}

	public void setFetBasicProblemInfoBean(
			BasicProblemInfoBean fetBasicProblemInfoBean) {
		this.fetBasicProblemInfoBean = fetBasicProblemInfoBean;
	}

	public DetailProblemInfoBean getFetDetailProblemInfoBean() {
		return fetDetailProblemInfoBean;
	}

	public void setFetDetailProblemInfoBean(
			DetailProblemInfoBean fetDetailProblemInfoBean) {
		this.fetDetailProblemInfoBean = fetDetailProblemInfoBean;
	}

	public DetailProblemFormOneBean getFetDetailProblemFormOneBean() {
		return fetDetailProblemFormOneBean;
	}

	public void setFetDetailProblemFormOneBean(
			DetailProblemFormOneBean fetDetailProblemFormOneBean) {
		this.fetDetailProblemFormOneBean = fetDetailProblemFormOneBean;
	}

	public ContactPersonalInfoBean getFetContactPersonalInfoBean() {
		return fetContactPersonalInfoBean;
	}

	public void setFetContactPersonalInfoBean(
			ContactPersonalInfoBean fetContactPersonalInfoBean) {
		this.fetContactPersonalInfoBean = fetContactPersonalInfoBean;
	}
	
}

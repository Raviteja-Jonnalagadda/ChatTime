package com.chat.db.dto;

public class QueryRepo {
	private String queryId;
	private String prj_id;
	private String dec;
	private String query;
	private String qry_typ;
	private String crud_type;
	private String qry_cnd;
	private String qry_table;
	private String qry_prm_cnr;
	private String qry_clm;
	private String mkr_id;
	private String return_type;

	public String getqueryId() {
		return queryId;
	}
	public void setqueryId(String name) {
		this.queryId = name;
	}
	public String getPrj_id() {
		return prj_id;
	}
	public void setPrj_id(String prj_id) {
		this.prj_id = prj_id;
	}
	public String getDec() {
		return dec;
	}
	public void setDec(String dec) {
		this.dec = dec;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public String getQry_typ() {
		return qry_typ;
	}
	public void setQry_typ(String qry_typ) {
		this.qry_typ = qry_typ;
	}
	public String getCrud_type() {
		return crud_type;
	}
	public void setCrud_type(String crud_type) {
		this.crud_type = crud_type;
	}
	public String getQry_cnd() {
		return qry_cnd;
	}
	public void setQry_cnd(String qry_cnd) {
		this.qry_cnd = qry_cnd;
	}
	public String getQry_table() {
		return qry_table;
	}
	public void setQry_table(String qry_table) {
		this.qry_table = qry_table;
	}
	public String getQry_prm_cnr() {
		return qry_prm_cnr;
	}
	public void setQry_prm_cnr(String qry_prm_cnr) {
		this.qry_prm_cnr = qry_prm_cnr;
	}
	public String getQry_clm() {
		return qry_clm;
	}
	public void setQry_clm(String qry_clm) {
		this.qry_clm = qry_clm;
	}
	public String getMkr_id() {
		return mkr_id;
	}
	public void setMkr_id(String mkr_id) {
		this.mkr_id = mkr_id;
	}
	public String getReturn_type() {
		return return_type;
	}
	public void setReturn_type(String return_type) {
		this.return_type = return_type;
	}
}

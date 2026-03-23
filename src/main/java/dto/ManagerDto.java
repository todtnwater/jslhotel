package dto;

public class ManagerDto {

	private String id, password, first_name, last_name, area, 
				address, email_1, email_2, 
				mobile_1, mobile_2, mobile_3, 
				gender, rank, sns, position, 
				reg_date, update_date, exit_date;
	private String membership, mb_reg_date, mb_update_date, mb_exit_date;
	
	// 회원정보 [
	public ManagerDto(String id, String password, String first_name, String last_name, String area, String address,
			String email_1, String email_2, String mobile_1, String mobile_2, String mobile_3, String gender,
			String rank, String sns, String position, String reg_date, String update_date, String exit_date) {
		this.id = id;
		this.password = password;
		this.first_name = first_name;
		this.last_name = last_name;
		this.area = area;
		this.address = address;
		this.email_1 = email_1;
		this.email_2 = email_2;
		this.mobile_1 = mobile_1;
		this.mobile_2 = mobile_2;
		this.mobile_3 = mobile_3;
		this.gender = gender;
		this.rank = rank;
		this.sns = sns;
		this.position = position;
		this.reg_date = reg_date;
		this.update_date = update_date;
		this.exit_date = exit_date;
	}


	// 회원리스트용 [관리자페이지]
	public ManagerDto(String id, String first_name, String last_name, String email_1, String email_2, String reg_date,
			String exit_date, String membership) {
		this.id = id;
		this.first_name = first_name;
		this.last_name = last_name;
		this.email_1 = email_1;
		this.email_2 = email_2;
		this.reg_date = reg_date;
		this.exit_date = exit_date;
		this.membership = membership;
	}


	public String getId() {
		return id;
	}


	public String getPassword() {
		return password;
	}


	public String getFirst_name() {
		return first_name;
	}


	public String getLast_name() {
		return last_name;
	}


	public String getArea() {
		return area;
	}


	public String getAddress() {
		return address;
	}


	public String getEmail_1() {
		return email_1;
	}


	public String getEmail_2() {
		return email_2;
	}


	public String getMobile_1() {
		return mobile_1;
	}


	public String getMobile_2() {
		return mobile_2;
	}


	public String getMobile_3() {
		return mobile_3;
	}


	public String getGender() {
		return gender;
	}


	public String getRank() {
		return rank;
	}


	public String getSns() {
		return sns;
	}


	public String getPosition() {
		return position;
	}


	public String getReg_date() {
		return reg_date;
	}


	public String getUpdate_date() {
		return update_date;
	}


	public String getExit_date() {
		return exit_date;
	}


	public String getMembership() {
		return membership;
	}


	public String getMb_reg_date() {
		return mb_reg_date;
	}


	public String getMb_update_date() {
		return mb_update_date;
	}


	public String getMb_exit_date() {
		return mb_exit_date;
	}
	
	
	
	
	
}

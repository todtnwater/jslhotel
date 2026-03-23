package dto;

public class MemberDto {

    private String id, password, first_name, last_name, area, 
                    address, email_1, email_2, 
                    mobile_1, mobile_2, mobile_3, 
                    gender, m_rank, sns, 
                    reg_date, update_date, exit_date;
    
    private String position;        // 직급
    private String login_date;      // 마지막 로그인
    private String membership;      // 멤버십 등급 (silver/gold/premium)
    
    // ⭐ 포인트 필드 추가
    private int m_point;            // 현재 보유 포인트
    private int m_total_point;      // 누적 적립 포인트
    
    // 기본 생성자
    public MemberDto() {
    }
    
    public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setEmail_1(String email_1) {
		this.email_1 = email_1;
	}

	public void setEmail_2(String email_2) {
		this.email_2 = email_2;
	}

	public void setMobile_1(String mobile_1) {
		this.mobile_1 = mobile_1;
	}

	public void setMobile_2(String mobile_2) {
		this.mobile_2 = mobile_2;
	}

	public void setMobile_3(String mobile_3) {
		this.mobile_3 = mobile_3;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public void setRank(String rank) {
		this.m_rank = rank;  // ✅ 수정!
	}

	public void setSns(String sns) {
		this.sns = sns;
	}

	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}

	public void setUpdate_date(String update_date) {
		this.update_date = update_date;
	}

	public void setExit_date(String exit_date) {
		this.exit_date = exit_date;
	}

	// 로그인
    public MemberDto(String first_name, String last_name, String rank) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.m_rank = rank;  // ✅ 수정!
    }
    
    // 회원가입
    public MemberDto(String id, String password, String first_name, String last_name, String area, String address,
            String email_1, String email_2, String mobile_1, String mobile_2, String mobile_3, String gender, String sns,
            String reg_date) {
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
        this.sns = sns;
        this.reg_date = reg_date;
    }

    // 마이페이지
    public MemberDto(String id, String first_name, String last_name, String area, String address,
            String email_1, String email_2, String mobile_1, String mobile_2, String mobile_3, String gender,
            String rank, String sns, String reg_date, String update_date) {
        this.id = id;
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
        this.m_rank = rank;  // ✅ 수정!
        this.sns = sns;
        this.reg_date = reg_date;
        this.update_date = update_date;
    }

    // 멤버 테이블 항목 전체 [회원정보 수정]
    public MemberDto(String id, String password, String first_name, String last_name, String area, String address,
            String email_1, String email_2, String mobile_1, String mobile_2, String mobile_3, String gender,
            String rank, String sns, String reg_date, String update_date, String exit_date) {
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
        this.m_rank = rank;  // ✅ 수정!
        this.sns = sns;
        this.reg_date = reg_date;
        this.update_date = update_date;
        this.exit_date = exit_date;
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
        return m_rank;  // ✅ 수정!
    }
    
    // ✅ 추가!
    public String getM_rank() {
        return m_rank;
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

    public String getSns() {
        return sns;
    }

    public String getPosition() {
        return position;
    }

    public String getLogin_date() {
        return login_date;
    }

    public String getMembership() {
        return membership;
    }

    // ⭐ 포인트 Getter
    public int getM_point() {
        return m_point;
    }

    public int getM_total_point() {
        return m_total_point;
    }

    // ⭐ 포인트 Setter
    public void setM_point(int m_point) {
        this.m_point = m_point;
    }

    public void setM_total_point(int m_total_point) {
        this.m_total_point = m_total_point;
    }

    // Setter 추가
    public void setId(String id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setLogin_date(String login_date) {
        this.login_date = login_date;
    }

    public void setMembership(String membership) {
        this.membership = membership;
    }

    /**
     * ✅ 통합 이메일 주소 반환
     * email_1@email_2 형식으로 조합
     * 
     * @return 완전한 이메일 주소 (예: user@example.com)
     */
    public String getEmail() {
        if (email_1 != null && email_2 != null) {
            return email_1 + "@" + email_2;
        } else if (email_1 != null) {
            return email_1;
        }
        return "";
    }

    /**
     * ✅ 통합 휴대전화 번호 반환
     * mobile_1-mobile_2-mobile_3 형식으로 조합
     * 
     * @return 완전한 전화번호 (예: 010-1234-5678)
     */
    public String getM_mobile_phone() {
        if (mobile_1 != null && mobile_2 != null && mobile_3 != null) {
            return mobile_1 + "-" + mobile_2 + "-" + mobile_3;
        } else if (mobile_1 != null) {
            return mobile_1;
        }
        return "";
    }
    
    /**
     * ✅ 포인트 포맷팅 (1000 → 1,000)
     * @return 포맷팅된 포인트 문자열
     */
    public String getFormattedPoint() {
        return String.format("%,d", m_point);
    }
}
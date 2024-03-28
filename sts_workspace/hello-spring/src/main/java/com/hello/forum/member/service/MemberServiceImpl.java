package com.hello.forum.member.service;

import org.apache.tika.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hello.forum.beans.SHA;
import com.hello.forum.member.dao.MemberDao;
import com.hello.forum.member.vo.MemberVO;

//회원가입 업무로직 작성을 위함
@Service
public class MemberServiceImpl implements MemberService{

	@Autowired
	private SHA sha; //sha bean을 가져온다!
	
	@Autowired
	private MemberDao memberDao;
	
	@Override
	public boolean createNewMember(MemberVO memberVO) {
		int emailCount = memberDao.getEmailCount(memberVO.getEmail());
		
		if (emailCount > 0) {
			throw new IllegalArgumentException("Email이 이미 사용중입니다.");
		}
		
		//사용자가 입력한 password
		String password = memberVO.getPassword();
		String salt = this.sha.generateSalt();
		password = this.sha.getEncrypt(password, salt); //암호화가 이루어진(salt도 함) password
		
		memberVO.setPassword(password); //회원가입때 사용한 salt를 기억하기 위함(DB에서 사용하기 위해 저장) -> members테이블에 salt를 넣음
		memberVO.setSalt(salt);
		
		int insertCount = memberDao.createNewMember(memberVO);
		return insertCount > 0;

	}

	//DB에서 체크를 했을때 이메일이 한건도 없다면 사용할 수 있는 이메일이다
	@Override
	public boolean checkAvailableEmail(String email) {
		return this.memberDao.getEmailCount(email) == 0; //0이라면 true가 반환될것이고, true라면 사용가능하다는 뜻
	}

	// SALT값을 이용해 다시한번 SHA를 이용해서 암호가 같은지 비교
	@Override
	public MemberVO getMember(MemberVO memberVO) {

		// 1. 이메일로 저장되어있는 salt를 조회한다
		String storedSalt = this.memberDao.selectSalt(memberVO.getEmail());
		
		// 만약, salt값이 null이라면, 회원정보가 없는것이므로 사용자에게 예외를 저달한다
		if (StringUtils.isEmpty(storedSalt)) {
			throw new IllegalArgumentException("이메일또는 비밀번호가 잘못되었습니다.");
		}
		
		// 2. salt값이 있을경우, salt를 이용해 sha를 암호화한다
		String password = memberVO.getPassword();
		password = this.sha.getEncrypt(password, storedSalt);
		memberVO.setPassword(password);
		
		// 3. DB에서 암호화된 비밀번호와 이메일을 비교해 회원정보를 가져온다
		MemberVO member = this.memberDao.selectMemberByEmailAndPassword(memberVO);
		
		
		// 만약, 회원정보가 null이라면 회원정보가 없는것이므로 사용자에게 예외를 전달한다
		if(member==null) {
			throw new IllegalArgumentException("이메일또는 비밀번호가 잘못되었습니다.");
		}
		
		return member;
	}

	@Override
	public boolean deleteMe(String email) {
		int deleteCount = memberDao.deleteMemberByEmail(email);
		return deleteCount > 0;
	}
}

package com.hello.forum.beans;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

public class FileHandler {

	private String baseDir;
	private boolean enableObfuscation;
	private boolean enableObfuscationHideExt;
	private List<String> availableFileList;
	
	public void setBaseDir(String baseDir) {
		this.baseDir = baseDir;
	}
	
	public void setEnableObfuscation(boolean enableObfuscation) {
		this.enableObfuscation = enableObfuscation;
	}
	
	public void setEnableObfuscationHideExt(boolean enableObfuscationHideExt) {
		this.enableObfuscationHideExt = enableObfuscationHideExt;
	}
	
	public void setAvailableFileList(List<String> availableFileList) {
		this.availableFileList = availableFileList;
	}
	
	/**
	 * 사용자가 업로드한 파일을 서버에 저장한다.
	 * 
	 * @param multipartFile 사용자가 업로드한 파일.
	 * 						(Spring에서 사용자가 업로드한 파일은 MultipartFile로 받아올수있다)
	 * @return 업로드 결과(사용자가 업로드한 파일명, 저장된 파일명, 저장된 파일의 크기, 파일의 경로
	 */
	public StoredFile storeFile(MultipartFile multipartFile) {
		
		//사용자가 업로드한 파일의 이름
		String uplodedFileName = multipartFile.getOriginalFilename();
		
		//난독화정책에 의해서 만들어진 파일의 이름
		//서버에 저장될 파일의 이름
		String fileName = this.getObfuscationFileName(uplodedFileName);
		
		//파일이 저장될 경로
		//this.baseDir : app.multipart.base-dir에 할당된 값.
		File storePath = new File(this.baseDir, fileName);
		
		// storePath.getParentFile() = this.baseDir. 
		// 즉, 업로드한 경로가 존재하지 않을 경우(=폴더가 없을경우)
		if (! storePath.getParentFile().exists()) {
			//업로드할 경로(폴더)를 만들어준다.
			storePath.getParentFile().mkdirs();
		}
		
		//사용자가 업로드한 파일을 storePath 경로로 저장시킨다
		try {
			multipartFile.transferTo(storePath);
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
			return null; //업로드에 실패했으므로 null반환
		}
		
		//업로드된 파일의 마임타입을 가져온다.
		Tika tika= new Tika();
		//서버에 저장된 파일의 마임타입을 알고싶다!
		try {
			String mimeType = tika.detect(storePath);
			if ( ! this.availableFileList.contains(mimeType)) {
				System.out.println(mimeType + "파일을 업로드 할 수 없습니다.");
				storePath.delete();
				return null;
			}
			
			System.out.println(mimeType + "파일을 업로드했습니다.");
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} 
		
		//업로드 결과를 반환한다.(여기까지 왔다는건 업로드 성공했다는것)
		return new StoredFile(multipartFile.getOriginalFilename(), storePath); 
	}
	
	
	/**
	 * 사용자가 파일 업로드를 했을때
	 * application.yml에 정의된 난독화 정책에 의해서 파일명을 난독화 한 뒤
	 * 난독화된 파일명을 반환한다.
	 * 만약, 난독화 정책을 사용하지 않겠다 라고 설정한 경우는
	 * 업로드한 파일의 이름을 그대로 반환한다.
	 * 
	 * @param fileName 사용자가 업로드한 파일의 이름
	 * @return 난독화된 파일의 이름
	 */
	private String getObfuscationFileName(String fileName) {
		
		//application.yml파일의 
		// app.multipart.obfuscation.enable의 값이 true일 경우
		if(this.enableObfuscation) {
			
			//업로드한 파일의 이름에서 확장자만 분리한다.
			//app.multipart.obfuscation.hide-ext.enable의 값이 true일때
			// 파일의 확장자를 숨겨야 하고 false일때 확장자를 붙여야 하기 때문
			// 만약 파일의 이름이 uploadtest.xml일 경우 ".xml"이 할당된다.
			String ext = fileName.substring(fileName.lastIndexOf("."));
			
			// 파일의 이름을 난독화하기 위해서 난수를 생성한다.
			// 생성되어야 하는 난수는 절대 중복이 생성되어서는 안된다!
			// 현재시간(연월일시분초밀리초) 기반의 난수(UUID.randomUUID())를 생성하면, 중복은 발생하지 않는다.
			String obfuscationName = UUID.randomUUID().toString();
			
			/*
			 * app.multipart.obfuscation.hide-ext.enable의 값이 true일때
			 */
			if (this.enableObfuscationHideExt) {
				//난독화된 파일의 이름을 반환
				return obfuscationName;
			}
			//app.multipart.obfuscation.hide-ext.enable의 값이 false일때
			else {
				//난독화된 파일의 이름.확장자
				return obfuscationName + ext;
			}
			
			
			
		}
		
		return fileName;
	}
	
	
	public class StoredFile{
		private String fileName;
		private String realFileName;
		private String realFilePath;
		private long fileSize;
		
		
		/* 
		 * StoredFile: 업로드가 완료된 파일의 정보를 반환시켜주기 위해 만들어줌
		 */
		public StoredFile(String fileName, File storeFile) {
	
			this.fileName = fileName;
			this.realFileName = storeFile.getName();
			this.realFilePath = storeFile.getAbsolutePath();
			this.fileSize = storeFile.length();
		}
		
		public String getFileName() {
			return fileName;
		}
		public String getRealFileName() {
			return realFileName;
		}
		public String getRealFilePath() {
			return realFilePath;
		}
		public long getFileSize() {
			return fileSize;
		}
	}
}

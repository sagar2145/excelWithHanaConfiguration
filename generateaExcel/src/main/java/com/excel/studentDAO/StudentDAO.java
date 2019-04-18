package com.excel.studentDAO;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import javax.persistence.Query;
import javax.xml.bind.DatatypeConverter;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;
import com.excel.entity.ClassA;
import com.excel.entity.ClassB;
import com.excel.entity.ClassC;
import com.excel.entity.List121;
import com.excel.entity.Sample;
import com.excel.entity.StudentInfo;
import com.excel.response.Response;
import com.excel.writer.ExcelAdd;
import com.excel.writer.ExcelWriter;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
@Repository
public class StudentDAO {
	private Configuration configuration = new Configuration().configure("hibernate.cfg.xml");

	private SessionFactory sf = configuration.buildSessionFactory();
	private Session s;
	private Transaction tx;
	
	Response response=new Response();
	
	// create excel sheet with all columns
	public Response createExcel(List<StudentInfo> studentInfo){
        ExcelWriter writer=new ExcelWriter();        		 
		try{
			s=sf.openSession();
			tx=s.beginTransaction();
			writer.createExcel121(studentInfo);	
			response.setStatus(200);
			response.setMessage("succesfull");
			tx.commit();
		}
		catch (Exception e) {
			System.err.println("Exception : " + e.getMessage());
			response.setStatus(500);
			response.setMessage("unsuccesfull");
			tx.rollback();
		} finally {
			s.close();
		}
		return response;
	}	
	
	// add data to studentInfo table
	public Response add(StudentInfo studentInfo){            		 
		try{
			s=sf.openSession();
			tx=s.beginTransaction();
			s.save(studentInfo);
			response.setStatus(200);
			response.setMessage("succesfull");
			tx.commit();
		}
		catch (Exception e) {
			System.err.println("Exception : " + e.getMessage());
			response.setStatus(500);
			response.setMessage("unsuccesfull");
			tx.rollback();
		} finally {
			s.close();
		}
		return response;
	}
	 
	
	public Response view(){            		 
		try{
			s=sf.openSession();
			tx=s.beginTransaction();			
			Query query=s.createSQLQuery("CREATE VIEW abc  AS SELECT rollNo,name,english FROM ClassA");
			query.executeUpdate();
			response.setStatus(200);
			response.setMessage("succesfull");
			tx.commit();
		}
		catch (Exception e) {
			System.err.println("Exception : " + e.getMessage());
			response.setStatus(500);
			response.setMessage("unsuccesfull");
			tx.rollback();
		} finally {
			s.close();
		}
		return response;
	}
	
	public Response view1(){            		 
		try{
			s=sf.openSession();
			tx=s.beginTransaction();			
			Query query=s.createSQLQuery("CREATE VIEW view1   AS SELECT b1.name ,b1.percentage ,b2.english ,b2.maths FROM ClassA b1 "
					+ "INNER JOIN ClassB b2 On b1.rollNo=b2.rollNo");
			query.executeUpdate();
			response.setStatus(200);
			response.setMessage("succesfull");
			tx.commit();
		}
		catch (Exception e) {
			System.err.println("Exception : " + e.getMessage());
			response.setStatus(500);
			response.setMessage("unsuccesfull");
			tx.rollback();
		} finally {
			s.close();
		}
		return response;
	}
	// adding data to classA table
	public Response addToClassA(ClassA classA){	      	        	       		 
			try{
				s=sf.openSession();
				tx=s.beginTransaction();
				s.merge(classA);
				response.setStatus(200);
				response.setMessage("succesfull");
				tx.commit();
			}
			catch (Exception e) {
				System.err.println("Exception : " + e.getMessage());
				response.setStatus(500);
				response.setMessage("unsuccesfull");
				tx.rollback();
			} finally {
				s.close();
			}
			return response;
		}
	
	// adding data to classB table
	public Response addToClassB(ClassB classB){	     	        	       		 
			try{
				s=sf.openSession();
				tx=s.beginTransaction();
				s.save(classB);
				response.setStatus(200);
				response.setMessage("succesfull");
				tx.commit();
			}
			catch (Exception e) {
				System.err.println("Exception : " + e.getMessage());
				response.setStatus(500);
				response.setMessage("unsuccesfull");
				tx.rollback();

			} finally {
				s.close();
			}
			return response;
		}
	
	// adding data to classC table
	public Response addToClassC(ClassC classC){	   	     	       		 
			try{
				s=sf.openSession();
				tx=s.beginTransaction();
				s.save(classC);
				response.setStatus(200);
				response.setMessage("succesfull");
				tx.commit();
			}
			catch (Exception e) {
				System.err.println("Exception : " + e.getMessage());
				response.setStatus(500);
				response.setMessage("unsuccesfull");
				tx.rollback();
			} finally {
				s.close();
			}
			return response;
		}

	// adding data from excel to DB
	public String addDataToDB( byte[] bs) throws IOException{
		try{
			s=sf.openSession();
			tx=s.beginTransaction();			
			System.out.println("entering base64");
			Base64.Encoder myencoder = Base64.getEncoder().withoutPadding();
			String dc=myencoder.encodeToString(bs);
			byte[] decodedString =Base64.getMimeDecoder().decode(dc);
			System.out.println("base64" +decodedString);
			ByteArrayInputStream st = new ByteArrayInputStream(decodedString);
			/*FileInputStream file = new FileInputStream(new File("D:/eclipse_neon/StudentInfo.xlsx")); */
			XSSFWorkbook workbook = new XSSFWorkbook(st); 
			String Sheetname=workbook.getSheetName(0);
			XSSFSheet sheet = workbook.getSheetAt(0); 
			  FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
			    int endRow = sheet.getLastRowNum();
			    System.out.println(endRow);
	        Row row;
	        if(Sheetname.equalsIgnoreCase("ClassA")){
	        Query query=s.createNativeQuery("select * from ClassA").addEntity(ClassA.class);
			List<ClassA> list=query.getResultList();
              for(int i=1; i<endRow; i++){ 	        		        	
	            row = (Row) sheet.getRow(i); 	           
	            ClassA classA=new ClassA();
	                       Cell rollNo=row.getCell(0);
	                       rollNo.setCellType(CellType.NUMERIC);	                       
	                       if(list.get(i).getRollNo()!=rollNo.getNumericCellValue()){
	                    	   classA.setRollNo((int) rollNo.getNumericCellValue());}
                 Cell name=row.getCell(1);
                 if(!list.get(i).getName().equals(name.toString())){
                	 classA.setName(name.toString());
                 }
                 Cell english=row.getCell(2);
                 if(list.get(i).getEnglish()!=english.getNumericCellValue()){
                	 classA.setEnglish(english.getNumericCellValue());
                 }
                 Cell maths=row.getCell(3);
                 if(list.get(i).getMaths()!=maths.getNumericCellValue()){
                	 classA.setMaths(maths.getNumericCellValue());
                 }
                 Cell science=row.getCell(4);
                 if(list.get(i).getScience()!=science.getNumericCellValue()){
                	 classA.setScience(science.getNumericCellValue());
                 }
                 Cell total=row.getCell(5);
                 CellValue cell5 =evaluator.evaluate(total);               
                 if(list.get(i).getTotalMarks()!=cell5.getNumberValue()){
                	 classA.setTotalMarks(cell5.getNumberValue());}
                 System.out.println("total :"+cell5.getNumberValue());
                 Cell percentage=row.getCell(6);
                 CellValue cellValue =evaluator.evaluate(percentage);              
                 if(list.get(i).getPercentage()!=cellValue.getNumberValue()){
                	 classA.setPercentage(cellValue.getNumberValue());}
                 System.out.println("percentage :"+cellValue.getNumberValue());
                 Cell status=row.getCell(7);              
                 if(list.get(i).isStatus()!=status.getBooleanCellValue()){
                	 classA.setStatus(status.getBooleanCellValue());
                 }                 
                 System.out.println("status :"+status.getCellTypeEnum());            
              s.merge(classA);            
	        }
	        }
	        else if(Sheetname.equalsIgnoreCase("ClassB")){
		        Query query=s.createNativeQuery("select * from ClassB").addEntity(ClassB.class);
				List<ClassB> list=query.getResultList();
				for(int i=1; i<endRow; i++){ 		        			        	
		            row = (Row) sheet.getRow(i); 
		           		            ClassB classA=new ClassB();
		                       Cell rollNo=row.getCell(0);
		                       rollNo.setCellType(CellType.NUMERIC);		                       
		                       if(list.get(i).getRollNo()!=rollNo.getNumericCellValue()){
		                    	   classA.setRollNo((int) rollNo.getNumericCellValue());}
	                 Cell name=row.getCell(1);
	                 if(!list.get(i).getName().equals(name.toString())){
	                	 classA.setName(name.toString());
	                 }
	                 Cell english=row.getCell(2);
	                 if(list.get(i).getEnglish()!=english.getNumericCellValue()){
	                	 classA.setEnglish(english.getNumericCellValue());
	                  }
	                 Cell maths=row.getCell(3);
	                 if(list.get(i).getMaths()!=maths.getNumericCellValue()){
	                	 classA.setMaths(maths.getNumericCellValue());
	                 }
	                 Cell science=row.getCell(4);
	                 if(list.get(i).getScience()!=science.getNumericCellValue()){
	                	 classA.setScience(science.getNumericCellValue());
	                 }
	                 Cell total=row.getCell(5);
	                 CellValue cell5 =evaluator.evaluate(total);	               
	                 if(list.get(i).getTotalMarks()!=cell5.getNumberValue()){
	                	 classA.setTotalMarks(cell5.getNumberValue());}
	                 System.out.println("total :"+cell5.getNumberValue());
	                 Cell percentage=row.getCell(6);
	                 CellValue cellValue =evaluator.evaluate(percentage);	              
	                 if(list.get(i).getPercentage()!=cellValue.getNumberValue()){
	                	 classA.setPercentage(cellValue.getNumberValue());}
	                 System.out.println("percentage :"+cellValue.getNumberValue());
	                 Cell status=row.getCell(7);	               
	                 if(list.get(i).isStatus()!=status.getBooleanCellValue()){
	                	 classA.setStatus(status.getBooleanCellValue());
	                 }	                 
	                 System.out.println("status :"+status.getCellTypeEnum());            
	              s.merge(classA);	             
		        }
		        }
	        else if(Sheetname.equalsIgnoreCase("ClassC")){
		        Query query=s.createNativeQuery("select * from ClassC").addEntity(ClassC.class);
				List<ClassC> list=query.getResultList();
				for(int i=1; i<endRow; i++){ 		        			        	
		            row = (Row) sheet.getRow(i); 		           
		            ClassC classA=new ClassC();
		                       Cell rollNo=row.getCell(0);
		                       rollNo.setCellType(CellType.NUMERIC);		                       
		                       if(list.get(i).getRollNo()!=rollNo.getNumericCellValue()){
		                    	   classA.setRollNo((int) rollNo.getNumericCellValue());}
	                 Cell name=row.getCell(1);
	                 if(!list.get(i).getName().equals(name.toString())){
	                	 classA.setName(name.toString());
	                 }
	                 Cell english=row.getCell(2);
	                 if(list.get(i).getEnglish()!=english.getNumericCellValue()){
	                	 classA.setEnglish(english.getNumericCellValue());
	                  }
	                 Cell maths=row.getCell(3);
	                 if(list.get(i).getMaths()!=maths.getNumericCellValue()){
	                	 classA.setMaths(maths.getNumericCellValue());
	                 }
	                 Cell science=row.getCell(4);
	                 if(list.get(i).getScience()!=science.getNumericCellValue()){
	                	 classA.setScience(science.getNumericCellValue());
	                 }
	                 Cell total=row.getCell(5);
	                 CellValue cell5 =evaluator.evaluate(total);	               
	                 if(list.get(i).getTotalMarks()!=cell5.getNumberValue()){
	                	 classA.setTotalMarks(cell5.getNumberValue());}
	                 System.out.println("total :"+cell5.getNumberValue());
	                 Cell percentage=row.getCell(6);
	                 CellValue cellValue =evaluator.evaluate(percentage);	              
	                 if(list.get(i).getPercentage()!=cellValue.getNumberValue()){
	                	 classA.setPercentage(cellValue.getNumberValue());}
	                 System.out.println("percentage :"+cellValue.getNumberValue());
	                 Cell status=row.getCell(7);	               
	                 if(list.get(i).isStatus()!=status.getBooleanCellValue()){
	                	 classA.setStatus(status.getBooleanCellValue());
	                 }	                 
	                 System.out.println("status :"+status.getCellTypeEnum());            
	              s.merge(classA);	             
		        }
		        }	          
	        String encoded=Base64.getEncoder().encodeToString(decodedString);
			response.setStatus(200);
			response.setMessage("succesfull");
			tx.commit();
			return encoded;
		}
		catch (Exception e) {
			System.err.println("Exception : " + e.getMessage());
			response.setStatus(500);
			response.setMessage("unsuccesfull");
			tx.rollback();
             return "error";
		} finally {
			s.close();
		}		
	}
	
	// update excel sheet
      public Response updateExcel(StudentInfo studentInfo){    	
    	try{
    		FileInputStream file = new FileInputStream("D:/eclipse_neon/StudentInfo.xlsx");
            HSSFWorkbook workbook = new HSSFWorkbook(file);
            HSSFSheet sheet = workbook.getSheetAt(0);           
            HSSFRow row = sheet.getRow(5);            
            row.getCell(0).setCellValue(studentInfo.getRollNo());
            row.getCell(1).setCellValue(studentInfo.getName());
            row.getCell(2).setCellValue(studentInfo.getEnglish());
            row.getCell(3).setCellValue(studentInfo.getMaths());
            row.getCell(4).setCellValue(studentInfo.getScience());
            row.getCell(5).setCellValue(studentInfo.getTotalMarks());
            row.getCell(6).setCellValue(studentInfo.getPercentage());
            file.close();
            FileOutputStream outFile =new FileOutputStream(new File("D:/eclipse_neon/StudentInfo.xlsx"));
            workbook.write(outFile);
            outFile.close();
            response.setStatus(200);
            response.setMessage("successfull");
    	}catch (FileNotFoundException e) {
            e.printStackTrace();
            response.setStatus(500);
            response.setMessage("unsuccessfull");
        } catch (IOException e) {
            e.printStackTrace();
        }
		return response;
    	
    }

	// adding data from DB to excel
	public Response dbToExcel() {
		ExcelWriter writer = new ExcelWriter();
		try {
			s = sf.openSession();
			tx = s.beginTransaction();
			NativeQuery query = s.createNativeQuery("select * from studentinfo").addEntity(StudentInfo.class);
			List<StudentInfo> infoList = query.list();
			writer.createExcel121(infoList);
			response.setStatus(200);
			response.setMessage("succesfull");
			tx.commit();
		} catch (Exception e) {
			System.err.println("Exception : " + e.getMessage());

			response.setStatus(500);
			response.setMessage("unsuccesfull");
			tx.rollback();

		} finally {
			s.close();
		}
		return response;
	}

	// generating excel with data from DB
	public String addClassData() throws IOException {
		ExcelWriter writer = new ExcelWriter();
		try {
			System.out.println("Object addclassdata");
			s = sf.openSession();
			tx = s.beginTransaction();
			List<Object[]> object = s.createSQLQuery("select rollNo,name from ClassA").list();
			List<ClassA> list = new ArrayList<ClassA>();
			for (Object[] add : object) {
				ClassA data = new ClassA();
				data.setRollNo( (Integer) add[0]);
				data.setName((String) add[1]);
				list.add(data);
			}
			String encodedString=writer.genericExcel(list);
			response.setStatus(200);
			response.setMessage("succesfull");
			tx.commit();			
			/*String originalInput = "D:/eclipse_neon/StudentInfo.xlsx";
			String encodedString = fetching(originalInput);		*/	
			return encodedString;
		} catch (Exception e) {
			System.err.println("Exception : " + e.getMessage());
			response.setStatus(500);
			response.setMessage("unsuccesfull");
			tx.rollback();
			return "null";
		} finally {
			s.close();
		}
	}

	// Adding data to excel
	public String addDataToExcel(List<ClassA> classA) {
		System.out.println("entering");
		ExcelAdd writer = new ExcelAdd();
		try {
			s = sf.openSession();
			tx = s.beginTransaction();
			writer.add(classA);
			String base64encodedString = fetching("D:/eclipse_neon/StudentInfo.xlsx");
			response.setStatus(200);
			response.setMessage("succesfull");
			tx.commit();
			return base64encodedString;
		} catch (Exception e) {
			System.err.println("Exception : " + e.getMessage());
			response.setStatus(500);
			response.setMessage("unsuccesfull");
			tx.rollback();
			return "null";
		} finally {
			s.close();
		}
	}

	// generic method to add data to excel
	public String addGenericDataToExcel(List121 list121) {
		System.out.println("entering");
		ExcelAdd writer = new ExcelAdd();
		try {
			s = sf.openSession();
			tx = s.beginTransaction();
			String base64encodedString = writer.genericAdd(list121);
			response.setStatus(200);
			response.setMessage("succesfull");
			tx.commit();
			return base64encodedString;
		} catch (Exception e) {
			System.err.println("Exception : " + e.getMessage());
			response.setStatus(500);
			response.setMessage("unsuccesfull");
			tx.rollback();
			return "null";
		} finally {
			s.close();
		}
	}

	// generic method to cretev
	public String addGenericData(List121 list121) {
		System.out.println("entering");
		ExcelWriter writer = new ExcelWriter();

		try {
			s = sf.openSession();
			tx = s.beginTransaction();
			String base64encodedString = writer.genericExcelAdd(list121);

			response.setStatus(200);
			response.setMessage("succesfull");
			tx.commit();
			return base64encodedString;
		} catch (Exception e) {
			System.err.println("Exception : " + e.getMessage());
			response.setStatus(500);
			response.setMessage("unsuccesfull");
			tx.rollback();
			return "null";
		} finally {
			s.close();
		}

	}

	
	public String addGenericData121(List121 list121) {
		
		try {
			s = sf.openSession();
			tx = s.beginTransaction();
			String base64encodedString = genericExcelAdd121(list121);

			response.setStatus(200);
			response.setMessage("succesfull");
			tx.commit();
			return base64encodedString;
		} catch (Exception e) {
			System.err.println("Exception : " + e.getMessage());
			response.setStatus(500);
			response.setMessage("unsuccesfull");
			tx.rollback();
			return "null";
		} finally {
			s.close();
		}

	}

public Response addObjA(List121 objA){
	System.out.println("entering");
   
   List121 c=new List121();
   
	try{
		s=sf.openSession();
		tx=s.beginTransaction();
		System.out.println("sdfghjk");
		for(Object classa :objA.getClassList() ) {
			System.out.println("abcggg");
			ClassA a1=new ClassA();					
			a1.setRollNo(((ClassA) classa).getRollNo());					
			a1.setName(((ClassA) classa).getName());			
			a1.setEnglish(((ClassA) classa).getEnglish());			
			a1.setMaths(((ClassA) classa).getMaths());			
			a1.setScience(((ClassA) classa).getScience());			
			a1.setTotalMarks(((ClassA) classa).getTotalMarks());			
			a1.setPercentage(((ClassA) classa).getPercentage());			
			a1.setStatus(((ClassA) classa).isStatus());
			
			s.save(a1);
        }
		
		
		response.setStatus(200);
		response.setMessage("succesfull");
		tx.commit();
		return response;
	}
	catch (Exception e) {
		System.err.println("Exception : " + e.getMessage());
		response.setStatus(500);
		response.setMessage("unsuccesfull");
		tx.rollback();
return response;
	} finally {
		s.close();
	}

}

public  String genericExcelAdd121(List121 list) throws IOException, InvalidFormatException {
	
System.out.println("entering List");

			 String[] columns = {"rollNo", "name", "english", "maths", "science",
		 				 "totalMarks", "percentage","status"};

		 	        // Create a Workbook
		 	        Workbook workbook = new XSSFWorkbook();     // new HSSFWorkbook() for generating `.xls` file

		 	        /* CreationHelper helps us create instances for various things like DataFormat,
		 	           Hyperlink, RichTextString etc in a format (HSSF, XSSF) independent way */
		 	        CreationHelper createHelper = workbook.getCreationHelper();
		 	       
		 	       if(list.getClassList()!=null){
		 	        // Create a Sheet
		 	        workbook.createSheet("ClassA");}
		 	       else if(list.getClassListB()!=null){
		 	    	workbook.createSheet("ClassB");
		 	       }
		 	      else if(list.getClassListC()!=null){
		 	    	   workbook.createSheet("ClassC");
		 	       }
		 	       Sheet sheet=workbook.getSheetAt(0);
		 	        // Create a Font for styling header cells
		 	        Font headerFont = workbook.createFont();
		 	        headerFont.setBold(true);
		 	        headerFont.setFontHeightInPoints((short) 14);
		 	        headerFont.setColor(IndexedColors.RED.getIndex());

		 	        // Create a CellStyle with the font
		 	        CellStyle headerCellStyle = workbook.createCellStyle();
		 	        headerCellStyle.setFont(headerFont);

		 	        // Create a Row
		 	        Row headerRow = sheet.createRow(0);

		 	        // Creating cells
		 	        for(int i = 0; i < columns.length; i++) {
		 	            Cell cell = headerRow.createCell(i);
		 	            cell.setCellValue(columns[i]);
		 	            cell.setCellStyle(headerCellStyle);
		 	        }
		 	        // Create Other rows and cells with student data
		 	        int rowNum = 1;
		 	        int a=2;
		 	       if(list.getClassList()!=null){
		 	    	  List<Object[]> object = s.createSQLQuery("select rollNo,name from ClassA").list();
		 				
		 				for (Object[] add : object) {
		 					 Row row = sheet.createRow(rowNum++);
		 					row.createCell(0).setCellValue( (Integer) add[0]);
		 					row.createCell(1).setCellValue((String) add[1]);
		 					
		 				}
		 	    	   
						/*for(Object classa:list1){
							 Row row = sheet.createRow(rowNum++);
							 row.createCell(0).setCellValue(((ClassA) classa).getRollNo());
							 row.createCell(1).setCellValue(((ClassA) classa).getName());
					}*/
						
						
						 for(int i = 0; i < columns.length; i++) {
				 	            sheet.autoSizeColumn(i);
				 	        }

				 	        // Write the output to a file
				 	        FileOutputStream fileOut = new FileOutputStream("ClassA.xlsx");
				 	       ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				 	        workbook.write(outputStream);
				 	        fileOut.close();
				 	        workbook.close();
				 	       byte[] b1=outputStream.toByteArray();
				 	      String base64encodedString= Base64.getEncoder().encodeToString(b1);
				 	       return base64encodedString;
		 	       }
					else if(list.getClassListB()!=null){
						List<Object[]> object = s.createSQLQuery("select rollNo,name from ClassB").list();
		 				for (Object[] add : object) {
		 					 Row row = sheet.createRow(rowNum++);		 		
		 					row.createCell(0).setCellValue( (Integer) add[0]);
		 					row.createCell(1).setCellValue((String) add[1]);	
		 				}
						 for(int i = 0; i < columns.length; i++) {
				 	            sheet.autoSizeColumn(i);
				 	        }
						 
						 FileOutputStream fileOut = new FileOutputStream("ClassB.xlsx");
						  ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				 	        workbook.write(outputStream);
				 	        fileOut.close();
				 	        workbook.close();
				 	       byte[] b1=outputStream.toByteArray();
				 	      String base64encodedString= Base64.getEncoder().encodeToString(b1);
				 	       return base64encodedString;
				 	       
					}
					else if(list.getClassListC()!=null){
						List<Object[]> object = s.createSQLQuery("select rollNo,name from ClassC").list();
		 				for (Object[] add : object) {
		 					 Row row = sheet.createRow(rowNum++);		 		
		 					row.createCell(0).setCellValue( (Integer) add[0]);
		 					row.createCell(1).setCellValue((String) add[1]);	
		 				}
						for(int i = 0; i < columns.length; i++) {
			 	            sheet.autoSizeColumn(i);
			 	        }
						
						FileOutputStream fileOut = new FileOutputStream("ClassC.xlsx");
						  ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				 	        workbook.write(outputStream);
				 	        fileOut.close();
				 	        workbook.close();
				 	       byte[] b1=outputStream.toByteArray();
				 	      String base64encodedString= Base64.getEncoder().encodeToString(b1);
				 	       return base64encodedString;
					}
				return "error";
		 	        
		 	        
		 	        
		 	       /* for(ClassA info: classA) {
		 	        	
		 	            Row row = sheet.createRow(rowNum++);

		 	            row.createCell(0).setCellValue(info.getRollNo());
		 	            System.out.println(info.getRollNo());
		 	            row.createCell(1).setCellValue(info.getName());
		 	            
		 	         a++;
		 	           
		 	        }*/

		 	        // Resize all columns to fit the content size
		 	       
		 	    }

public Response addObjB(List121 objB){
	System.out.println("entering");
   
   
   
	try{
		s=sf.openSession();
		tx=s.beginTransaction();
		for(ClassB classa :objB.getClassListB() ) {
			ClassB a1=new ClassB();
			a1.setRollNo(classa.getRollNo());
			
			a1.setName(classa.getName());
			a1.setEnglish(classa.getEnglish());
			a1.setMaths(classa.getMaths());
			a1.setScience(classa.getScience());
			a1.setTotalMarks(classa.getTotalMarks());
			a1.setPercentage(classa.getPercentage());
			a1.setStatus(classa.isStatus());
			
			s.save(a1);
        }
		
		
		response.setStatus(200);
		response.setMessage("succesfull");
		tx.commit();
		return response;
	}
	catch (Exception e) {
		System.err.println("Exception : " + e.getMessage());
		response.setStatus(500);
		response.setMessage("unsuccesfull");
		tx.rollback();
return response;
	} finally {
		s.close();
	}

}


public Response addObjC(List121 objC){
	System.out.println("entering");
   
   
   
	try{
		s=sf.openSession();
		tx=s.beginTransaction();
		for(ClassC classa :objC.getClassListC() ) {
			ClassC a1=new ClassC();
			a1.setRollNo(classa.getRollNo());
		
			a1.setName(classa.getName());
			a1.setEnglish(classa.getEnglish());
			a1.setMaths(classa.getMaths());
			a1.setScience(classa.getScience());
			a1.setTotalMarks(classa.getTotalMarks());
			a1.setPercentage(classa.getPercentage());
			a1.setStatus(classa.isStatus());
			a1.setFile(classa.getFile());
			s.save(a1);
        }
		
		
		response.setStatus(200);
		response.setMessage("succesfull");
		tx.commit();
		return response;
	}
	catch (Exception e) {
		System.err.println("Exception : " + e.getMessage());
		response.setStatus(500);
		response.setMessage("unsuccesfull");
		tx.rollback();
return response;
	} finally {
		s.close();
	}

}
public String upload(byte[] bs){
	Base64.Encoder myencoder = Base64.getEncoder().withoutPadding();
	String dc=myencoder.encodeToString(bs);
	byte[] decodedString =Base64.getMimeDecoder().decode(dc);
	String base64encodedString=Base64.getEncoder().encodeToString(decodedString);
	// Encode using basic encoder
	/*String base64encodedString = Base64.getEncoder().encodeToString(
	   "D:/eclipse_neon/tester.xlsx".getBytes("utf-8"));
	System.out.println("Base64 Encoded String (Basic) :" + base64encodedString);*/
	/*String path="D:/eclipse_neon/tester.xlsx";
	 String base64encodedString =fetching(path);*/
	return base64encodedString;
	
}
public Response addSample(Sample sample){
	 
	try{
		s=sf.openSession();
		tx=s.beginTransaction();
		
		int j=30;
		for(int i=1;i<=100000;i++){
			Sample s1=new Sample();
			s1.setId(i);
			s1.setName(sample.getName());
			s1.setPassword(sample.getPassword());
			s.save(s1);
			 if( j % 50 == 0 ) { // Same as the JDBC batch size
			      //flush a batch of inserts and release memory:
			      s.flush();
			      s.clear();
			   }
		}
		
		response.setStatus(200);
		response.setMessage("succesfull");
		tx.commit();
	}
	catch (Exception e) {
		System.err.println("Exception : " + e.getMessage());
		response.setStatus(500);
		response.setMessage("unsuccesfull");
		tx.rollback();

	} finally {
		s.close();
	}
	return response;
}

public Response updateSample(Sample sample){
	
	try{
		s=sf.openSession();
		tx=s.beginTransaction();
		
		
		for(int i=1;i<=100000;i++){
			/*Sample s1=new Sample();*/
			
			Query query=s.createQuery("update Sample set name= :name, password= :password where id= :id");
		    
			query.setParameter("name",sample.getName());
			query.setParameter("password",sample.getPassword() );
			query.setParameter("id",i);
			/*s1.setId(i);
			s1.setName(sample.getName());
			s1.setPassword(sample.getPassword());
			s.update(s1);*/
			query.executeUpdate();
			if ( i % 50 == 0 ) {
			      s.flush();
			      s.clear();
			   }
			if (!tx.isActive()) {    
			      tx.commit();
			    }
		}
		
		response.setStatus(200);
		response.setMessage("succesfull");
		
	}
	catch (Exception ex) {
     
        s.getTransaction().rollback();  //------> getting error at this line
        response.setStatus(500);
		response.setMessage("unsuccesfull");
        
    }finally{
        if(s != null){
            s.close();
        }
    }
	return response;
}


public Response addS1(Sample sample){
	 
	try{
		s=sf.openSession();
		tx=s.beginTransaction();
		
		int j=30;
		for(int i=1;i<=100000;i++){
			Sample s1=new Sample();
			s1.setId(i);
			s1.setName(sample.getName());
			s1.setPassword(sample.getPassword());
			s.save(s1);
			if( i % 40 == 0 ) { // Same as the JDBC batch size
			      //flush a batch of inserts and release memory:
			      s.flush();
			      s.clear();
			   }
		}
		
		response.setStatus(200);
		response.setMessage("succesfull");
		tx.commit();
	}
	catch (Exception e) {
		System.err.println("Exception : " + e.getMessage());
		response.setStatus(500);
		response.setMessage("unsuccesfull");
		tx.rollback();

	} finally {
		s.close();
	}
	return response;
}

public Response updateS1(Sample sample){
	
	try{
		s=sf.openSession();
		tx=s.beginTransaction();
		
		
		for(int i=1;i<=100000;i++){
			/*Sample s1=new Sample();*/
			
			Query query=s.createQuery("update Sample set name= :name, password= :password where id= :id");
		    
			query.setParameter("name",sample.getName());
			query.setParameter("password",sample.getPassword() );
			query.setParameter("id",i);
			
			query.executeUpdate();
			if( i %  40 == 0 ) { // Same as the JDBC batch size
			      //flush a batch of inserts and release memory:
			      s.flush();
			      s.clear();
			   }
			if (!tx.isActive()) {    
			      tx.commit();
			    }
		}
		
		response.setStatus(200);
		response.setMessage("succesfull");
		
	}
	catch (Exception ex) {
     
        s.getTransaction().rollback();  
        response.setStatus(500);
		response.setMessage("unsuccesfull");
       
    }finally{
        if(s != null){
            s.close();
        }
    }
	return response;
}


@SuppressWarnings("unchecked")
public <LinkedHashMap> Response generate(List<?> list) {

	
		List<ClassA> List1 = new ArrayList<ClassA>();
		List1=(List<ClassA>) list;
		List<ClassB> List2 = new ArrayList<ClassB>();
		List<ClassC> List3 = new ArrayList<ClassC>();
		System.out.println("entering generate");
		if(list instanceof List<?>){
			ClassA C1=new ClassA();
	System.out.println("entering if");
			for(Object classa:list){
			
				List1.addAll((Collection<? extends ClassA>) classa);
			}
			addDataToExcel(List1);
		}
		
		else if(list instanceof List<?>){
			System.out.println("entering 2nd if");
			List2=(List<ClassB>) list;
			addDataToExcel(List1);
		}
		else if(list instanceof List<?>){
			System.out.println("entering 3rd if");
			List3=(List<ClassC>) list;
			addDataToExcel(List1);
		}	
		
response.setStatus(200);
response.setMessage("succesfull");
	return response;
	
}

@SuppressWarnings("unchecked")
public  Response generic(List121 list) {
	try{
		s=sf.openSession();
		tx=s.beginTransaction();
		System.out.println("entering generic");
		
			if(list.getClassList()!=null){
				for(Object classa:list.getClassList()){
				 ClassA a1=new ClassA();					
					a1.setRollNo(((ClassA) classa).getRollNo());			
					
					a1.setName(((ClassA) classa).getName());			
					a1.setEnglish(((ClassA) classa).getEnglish());			
					a1.setMaths(((ClassA) classa).getMaths());			
					a1.setScience(((ClassA) classa).getScience());			
					a1.setTotalMarks(((ClassA) classa).getTotalMarks());			
					a1.setPercentage(((ClassA) classa).getPercentage());			
					a1.setStatus(((ClassA) classa).isStatus());
					
					s.save(a1);
			}}
			else if(list.getClassListB()!=null){
				 for(Object obj1:list.getClassListB()){
					 ClassB a1=new ClassB();					
						a1.setRollNo(((ClassB) obj1).getRollNo());			
							
						a1.setName(((ClassB) obj1).getName());			
						a1.setEnglish(((ClassB) obj1).getEnglish());			
						a1.setMaths(((ClassB) obj1).getMaths());			
						a1.setScience(((ClassB) obj1).getScience());			
						a1.setTotalMarks(((ClassB) obj1).getTotalMarks());			
						a1.setPercentage(((ClassB) obj1).getPercentage());			
						a1.setStatus(((ClassB) obj1).isStatus());
						
						s.save(a1);
				 }
			}
			else if(list.getClassListC()!=null){
				for(Object obj3:list.getClassListC()){ 
					 System.out.println("third loop");
					 ClassC a1=new ClassC();					
						a1.setRollNo(((ClassC) obj3).getRollNo());			
								
						a1.setName(((ClassC) obj3).getName());			
						a1.setEnglish(((ClassC) obj3).getEnglish());			
						a1.setMaths(((ClassC) obj3).getMaths());			
						a1.setScience(((ClassC) obj3).getScience());			
						a1.setTotalMarks(((ClassC) obj3).getTotalMarks());			
						a1.setPercentage(((ClassC) obj3).getPercentage());			
						a1.setStatus(((ClassC) obj3).isStatus());
						
						s.save(a1);
				 }
			}

 response.setStatus(200);
 response.setMessage("succesfull");
 tx.commit();
	}catch (Exception e) {
		System.err.println("Exception : " + e.getMessage());
		response.setStatus(500);
		response.setMessage("unsuccesfull");
		tx.rollback();

	} finally {
		s.close();
	}

	return response;
	
}




public Response addClassA(ClassA classA){
  ExcelWriter writer=new ExcelWriter();
	try{
		s=sf.openSession();
		tx=s.beginTransaction();
		NativeQuery query= s.createNativeQuery("select rollNo,name from ClassA");
		List<Object[]> sample =query.list();
		List<ClassA> classList=new ArrayList<ClassA>();
		for(Object[] data:sample){
			ClassA class1=new ClassA();
			class1.setRollNo( (Integer) data[0]);
			class1.setName((String) data[1]);
			classList.add(class1);
		}
		writer.genericExcel(classList);
		
		
		response.setStatus(200);
		response.setMessage("succesfull");
		tx.commit();
	}
	catch (Exception e) {
		System.err.println("Exception : " + e.getMessage());
		response.setStatus(500);
		response.setMessage("unsuccesfull");
		tx.rollback();

	} finally {
		s.close();
	}
	return response;
}



public String fetching(String path) throws IOException
{
	String inputFile=path;
	byte[] inputs=loadfile(inputFile);
	String fileInbase64=DatatypeConverter.printBase64Binary(inputs);
	System.out.println(fileInbase64);
	/*byte[] decodeFile=DatatypeConverter.parseBase64Binary(fileInbase64);
	FileOutputStream fileoutputstream=new FileOutputStream("D:/eclipse_neon/StudentInfo.xlsx");
	fileoutputstream.write(decodeFile);
	fileoutputstream.flush();
	fileoutputstream.close();*/
	return fileInbase64;
	
	
	
} 
public void 	decode(String base64) throws IOException{
	byte[] decodeFile=DatatypeConverter.parseBase64Binary(base64);
	/*byte[] decodedBytes = Base64.getDecoder().decode(base64);
	String decodedString = new String(decodedBytes);*/		
	String decodedString = new String(decodeFile);
	System.out.println("decoded string"+decodedString);
	FileOutputStream fileoutputstream=new FileOutputStream("D:/eclipse_neon/tester.xlsx");
	fileoutputstream.write(decodeFile);
	fileoutputstream.flush();
	fileoutputstream.close();
	
}
public byte[] loadfile(String Sourcepath) throws IOException
{
	InputStream input= null;
	try
	{
		input=new FileInputStream(Sourcepath);
		return readFully(input);
	}
	finally
	{
		if(input!=null)
		{
			input.close();
		}
	}
	
	
}

private byte[] readFully(InputStream input) throws IOException {
	byte[] buffer =new byte[90000];
	ByteArrayOutputStream baos=new ByteArrayOutputStream();
	int bytesRead;
	while((bytesRead=input.read(buffer))!=-1)
	{
		baos.write(buffer,0,bytesRead);
	}
	return baos.toByteArray();
} }                        
/*public Response addPurchaseOrder(StudentInfo studentInfo){
    ExcelWriter writer=new ExcelWriter();
    Excel123 a1=new Excel123();
   		 
	try{
		s=sf.openSession();
		tx=s.beginTransaction();
	writer.main(studentInfo);
		s.save(studentInfo);
		response.setStatus(200);
		response.setMessage("succesfull");
		tx.commit();
	}
	catch (Exception e) {
		System.err.println("Exception : " + e.getMessage());

		response.setStatus(500);
		response.setMessage("unsuccesfull");
		tx.rollback();

	} finally {
		s.close();
	}
	return response;
}*/
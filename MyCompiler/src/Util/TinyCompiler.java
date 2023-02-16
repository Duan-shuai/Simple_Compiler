package Util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Stack;





public class TinyCompiler {

	/**
	 * @param args
	 */
	private final static String KEY="�ؼ���";
	private final static String OPERATOR="�����";
	private final static String INT_CONST="���γ���";
	private final static String CHAR_CONST="�ַ�����";
	//private final static String BOOL_CONST="��������";
	private final static String IDENTIFIER="��־��";
	private final static String BOUNDARYSIGN="���";
	private final static String END="������";
	private final static String UNIDEF="δ֪����";
	static ArrayList<Word>wordList=new ArrayList<Word>();//���ʱ�
	static ArrayList<Word>indentifierList=new ArrayList<Word>();//��ʶ������
	static ArrayList<Word>variable=new ArrayList<Word>();//��������
	static ArrayList<Word>backup=new ArrayList<Word>();//���ݼ���
	static Stack<String>analyseStack=new Stack<String>();//����ջ
	static ArrayList<String>key=new ArrayList<String>();//�ؼ��ּ���
	static ArrayList<String>boundarySign=new ArrayList<String>();//�������
	static ArrayList<String>operator=new ArrayList<String>();//���������
	static ArrayList<String>result=new ArrayList<String>();//�����
	static ArrayList<FourElement>fourElemList=new ArrayList<FourElement>();//��Ԫʽ����
	static ArrayList<Error>errorList=new ArrayList<Error>();//������Ϣ�б�
	static StringBuffer bf;//����ջ������
	static int wordCount=0;//ͳ�Ƶ��ʸ���
	static int errorCount=0;//ͳ�ƴ������
	static int tempCount=0;//��Ԫʽ����ʱ����������
	static boolean noteFlag=false;//����ע�ͱ�־
	static boolean lexErrorFlag=false;//�ʷ����������־
	static boolean graErrorFlag=false;//�﷨���������־
	static {
		TinyCompiler.operator.add("+");
		TinyCompiler.operator.add("-");
		TinyCompiler.operator.add("++");
		TinyCompiler.operator.add("--");
		TinyCompiler.operator.add("*");
		TinyCompiler.operator.add("/");
		TinyCompiler.operator.add(">");
		TinyCompiler.operator.add("<");
		TinyCompiler.operator.add(">=");
		TinyCompiler.operator.add("<=");
		TinyCompiler.operator.add("==");
		TinyCompiler.operator.add("!=");
		TinyCompiler.operator.add("=");
		TinyCompiler.operator.add("&&");
		TinyCompiler.operator.add("||");
		TinyCompiler.operator.add("!");
		TinyCompiler.operator.add(".");
		TinyCompiler.operator.add("?");
		TinyCompiler.operator.add("|");
		TinyCompiler.operator.add("&");
		TinyCompiler.boundarySign.add("(");
		TinyCompiler.boundarySign.add(")");
		TinyCompiler.boundarySign.add("{");
		TinyCompiler.boundarySign.add("}");
		TinyCompiler.boundarySign.add(";");
		TinyCompiler.boundarySign.add(",");
		TinyCompiler.key.add("void");
		TinyCompiler.key.add("main");
		TinyCompiler.key.add("int");
		TinyCompiler.key.add("char");
		TinyCompiler.key.add("if");
		TinyCompiler.key.add("else");
		TinyCompiler.key.add("while");
		TinyCompiler.key.add("for");
		TinyCompiler.key.add("printf");
		TinyCompiler.key.add("scanf");	
	}
	public void clear(){
		wordList.clear();
		indentifierList.clear();
		variable.clear();
		backup.clear();
		analyseStack.clear();
	 result.clear();
		fourElemList.clear();
		errorList.clear();
		bf=new StringBuffer("");
		 wordCount=0;
		errorCount=0;
		 noteFlag=false;
		 lexErrorFlag=false;
		graErrorFlag=false;
	}
	public String newTemp(){
		tempCount++;
		return "T"+tempCount;
	}
	public void grammerAnalyse(){//LL1�������������﷨����
		if(lexErrorFlag)javax.swing.JOptionPane.showMessageDialog(null, "�ʷ�����δͨ�������ܽ����﷨����");
		bf=new StringBuffer();
		int gcount=0;
		Error error=null;
		String OP;
		Object ARG1,ARG2,RES;
		analyseStack.add(0,"S");
		analyseStack.add(1,"#");
		while(!analyseStack.empty()&&!wordList.isEmpty()){
			bf.append('\n');
			bf.append("����"+gcount+"\t");
			if(gcount++>1000){
				graErrorFlag=true;
				break;
			}
			//System.out.println(gcount++);
			//System.out.println(wordList.size());
			String top=analyseStack.get(0);//��ǰջ��Ԫ��
			Word firstWord=wordList.get(0);//����������
			if(firstWord.value.equals("#")//��������
					&&top.equals("#")){
				bf.append("\n");
				analyseStack.remove(0);
				wordList.remove(0);
				
			}
			else if(top.equals("#")){
				analyseStack.remove(0);
				graErrorFlag=true;
				break;
				
			}
			else if(key.contains(top)||operator.contains(top)||boundarySign.contains(top)
					||top.equals("id")||top.equals("num")||top.equals("ch")){//�ռ���ʱ�Ĵ���
				if(firstWord.type.equals(INT_CONST)||firstWord.type.equals(CHAR_CONST)||
						top.equals(firstWord.value)||
						(top.equals("id")&&firstWord.type.equals(IDENTIFIER)
								)){
					analyseStack.remove(0);
					wordList.remove(0);
				}
					else{
					errorCount++;
					analyseStack.remove(0);
					wordList.remove(0);
					error=new Error(errorCount,"�﷨����",firstWord.line,firstWord);
					errorList.add(error);
					graErrorFlag=true;
				}
			}else{
				if(top.equals("Z'"))top="1";
				if(top.equals("U'"))top="2";
				if(top.equals("E'"))top="3";
				if(top.equals("H'"))top="4";
				if(top.equals("L'"))top="5";
				if(top.equals("I'"))top="6";
				if(top.equals("K'"))top="7";
				if(top.equals("T'"))top="8";
				switch(top.trim().charAt(0)){//ջ��Ϊ���ս������
					//N:S,B,A,C,,X,R,Z,Z��,U,U��,E,E��,H,H��,G,M,D,L,L��,I,I��,K,K��,T,T��,F,O,P
				case 'S':
				if(firstWord.value.equals("void")){
					analyseStack.remove(0);
					analyseStack.add(0,"void");
					analyseStack.add(1,"main");
					analyseStack.add(2,"(");
					analyseStack.add(3,")");
					analyseStack.add(4,"{");
					analyseStack.add(5,"A");
					analyseStack.add(6,"}");
				}else{
					errorCount++;
					analyseStack.remove(0);
					wordList.remove(0);
					error=new Error(errorCount,"������û�з���ֵ",firstWord.line,firstWord);
					errorList.add(error);	
					graErrorFlag=true;
				}
					break;
				case 'A':
					if(firstWord.value.equals("int")||firstWord.value.equals("char")
							||firstWord.value.equals("bool")){
						analyseStack.remove(0);
						analyseStack.add(0,"C");
						analyseStack.add(1,"A");
					}else if(firstWord.value.equals("printf")){
						analyseStack.remove(0);
						analyseStack.add(0,"C");
						analyseStack.add(1,"A");
						
					}
					else if(firstWord.value.equals("scanf")){
						analyseStack.remove(0);
						analyseStack.add(0,"C");
						analyseStack.add(1,"A");
					}
					else if(firstWord.value.equals("if")){
						analyseStack.remove(0);
						analyseStack.add(0,"C");
						analyseStack.add(1,"A");
					}
					else if(firstWord.value.equals("while")){
						analyseStack.remove(0);
						analyseStack.add(0,"C");
						analyseStack.add(1,"A");
					}
					else if(firstWord.value.equals("for")){
						analyseStack.remove(0);
						analyseStack.add(0,"C");
						analyseStack.add(1,"A");
						}
					else if(firstWord.type.equals(IDENTIFIER)){
						analyseStack.remove(0);
						analyseStack.add(0,"C");
						analyseStack.add(1,"A");
					}else{
						analyseStack.remove(0);
					}
					break;
				
				case 'B':
			 if(firstWord.value.equals("printf")){
						analyseStack.remove(0);
						analyseStack.add(0,"printf");
						analyseStack.add(1,"(");
						analyseStack.add(2,"P");
						analyseStack.add(3,")");
						analyseStack.add(4,"A");
						analyseStack.add(5,";");
						
					}
					else if(firstWord.value.equals("scanf")){
						analyseStack.remove(0);
						analyseStack.add(0,"scanf");
						analyseStack.add(1,"(");
						analyseStack.add(2,"id");
						analyseStack.add(3,")");
						analyseStack.add(4,"A");
						analyseStack.add(5,";");
					}
					else if(firstWord.value.equals("if")){
						analyseStack.remove(0);
						analyseStack.add(0,"if");
						analyseStack.add(1,"(");
						analyseStack.add(2,"E");
						analyseStack.add(3,")");
						analyseStack.add(4,"{");
						analyseStack.add(5,"A");
						analyseStack.add(6,"}");
						analyseStack.add(7,"else");
						analyseStack.add(8,"{");
						analyseStack.add(9,"A");
						analyseStack.add(10,"}");
						//analyseStack.add(11,"A");
					}
					else if(firstWord.value.equals("while")){
						analyseStack.remove(0);
						analyseStack.add(0,"while");
						analyseStack.add(1,"(");
						analyseStack.add(2,"E");
						analyseStack.add(3,")");
						analyseStack.add(4,"{");
						analyseStack.add(5,"A");
						analyseStack.add(6,"}");
						//analyseStack.add(7,"A");
					}
					else if(firstWord.value.equals("for")){
						analyseStack.remove(0);
						analyseStack.add(0,"for");
						analyseStack.add(1,"(");
						analyseStack.add(2,"X");
						analyseStack.add(3,"G");
						analyseStack.add(4,";");
						analyseStack.add(5,"Q");
						analyseStack.add(6,")");
						analyseStack.add(7,"{");
						analyseStack.add(8,"A");
						analyseStack.add(9,"}");
						//analyseStack.add(11,"A");
					}
					else{
						analyseStack.remove(0);
					}
					break;
				case 'C':
					
						analyseStack.remove(0);
						analyseStack.add(0,"X");
						analyseStack.add(1,"B");
						analyseStack.add(2,"R");
					break;
				case 'X':
					if(firstWord.value.equals("int")||firstWord.value.equals("char")||firstWord.value.equals("bool")){
						analyseStack.remove(0);
						analyseStack.add(0,"Y");
						analyseStack.add(1,"Z");
						analyseStack.add(2,";");
					}else{
						analyseStack.remove(0);
					}
					break;
				case 'Y':
					if(firstWord.value.equals("int")){
						analyseStack.remove(0);
						analyseStack.add(0,"int");
					}
					else if(firstWord.value.equals("char")){
						analyseStack.remove(0);
						analyseStack.add(0,"char");
					}
					else if(firstWord.value.equals("bool")){
						analyseStack.remove(0);
						analyseStack.add(0,"bool");
					}
					else{
						errorCount++;
						analyseStack.remove(0);
						wordList.remove(0);
						error=new Error(errorCount,"�Ƿ���������",firstWord.line,firstWord);
						errorList.add(error);	
						graErrorFlag=true;
					}
					break;
				case 'Z':
					if(firstWord.type.equals(IDENTIFIER)){
						analyseStack.remove(0);
						analyseStack.add(0,"U");
						analyseStack.add(1,"Z'");
					}
					else{
						errorCount++;
						analyseStack.remove(0);
						wordList.remove(0);
						error=new Error(errorCount,"�Ƿ���ʶ��",firstWord.line,firstWord);
						errorList.add(error);
						graErrorFlag=true;
					}
					break;
				case '1'://z'
					if(firstWord.value.equals(",")){
						analyseStack.remove(0);
						analyseStack.add(0,",");
						analyseStack.add(1,"Z");
					}
					else{
						analyseStack.remove(0);
					}
					break;
				case 'U':
					if(firstWord.type.equals(IDENTIFIER)){
						analyseStack.remove(0);
						analyseStack.add(0,"id");
						analyseStack.add(1,"U'");
					}
					else{
						errorCount++;
						analyseStack.remove(0);
						wordList.remove(0);
						error=new Error(errorCount,"�Ƿ���ʶ��",firstWord.line,firstWord);
						errorList.add(error);
						graErrorFlag=true;
					}
					break;
				case '2'://U'
					if(firstWord.value.equals("=")){
						analyseStack.remove(0);
						analyseStack.add(0,"=");
						analyseStack.add(1,"L");
					}
					else{			
						analyseStack.remove(0);
					}
					break;
				case 'R':
					if(firstWord.type.equals(IDENTIFIER)){
						analyseStack.remove(0);
						analyseStack.add(0,"id");
						analyseStack.add(1,"=");
						analyseStack.add(2,"L");
						analyseStack.add(3,";");
					}
					else{
						analyseStack.remove(0);
					}
					break;
				case 'P':
					if(firstWord.type.equals(IDENTIFIER)){
						analyseStack.remove(0);
						analyseStack.add(0,"id");
					}else if(firstWord.type.equals(INT_CONST)){
						analyseStack.remove(0);
						analyseStack.add(0,"num");
					}else if(firstWord.type.equals(CHAR_CONST)){
						analyseStack.remove(0);
						analyseStack.add(0,"ch");
					}
					else{
						errorCount++;
						analyseStack.remove(0);
						wordList.remove(0);
						error=new Error(errorCount,"�����������������",firstWord.line,firstWord);
						errorList.add(error);	
						graErrorFlag=true;
					}
					break;
				case 'E':
					if(firstWord.type.equals(IDENTIFIER)){
						analyseStack.remove(0);
						analyseStack.add(0,"H");
						analyseStack.add(1,"E'");
					}else if(firstWord.type.equals(INT_CONST)){
						analyseStack.remove(0);
						analyseStack.add(0,"H");
						analyseStack.add(1,"E'");
					}
					else{
						errorCount++;
						analyseStack.remove(0);
						wordList.remove(0);
						error=new Error(errorCount,"���ܽ��������������������",firstWord.line,firstWord);
						errorList.add(error);	
						graErrorFlag=true;
					}
					break;
				case '3'://E'
					if(firstWord.value.equals("&&")){
						analyseStack.remove(0);
						analyseStack.add(0,"&&");
						analyseStack.add(1,"E");
					}else {
						analyseStack.remove(0);
					}
					break;
				case 'H':
					if(firstWord.type.equals(IDENTIFIER)){
						analyseStack.remove(0);
						analyseStack.add(0,"G");
						analyseStack.add(1,"H'");
					}else if(firstWord.type.equals(INT_CONST)){
						analyseStack.remove(0);
						analyseStack.add(0,"G");
						analyseStack.add(1,"H'");
					}
					else{
						errorCount++;
						analyseStack.remove(0);
						wordList.remove(0);
						error=new Error(errorCount,"���ܽ��������������������",firstWord.line,firstWord);
						errorList.add(error);	
						graErrorFlag=true;
					}
					break;
				case '4'://H'
					if(firstWord.value.equals("||")){
						analyseStack.remove(0);
						analyseStack.add(0,"||");
						analyseStack.add(1,"E");
					}else {
						analyseStack.remove(0);
					}
					break;
				case 'M':
					if(firstWord.type.equals(IDENTIFIER)){
						analyseStack.remove(0);
						analyseStack.add(0,"id");
					}else if(firstWord.type.equals(INT_CONST)){
						analyseStack.remove(0);
						analyseStack.add(0,"num");
					}
					else{
						errorCount++;
						analyseStack.remove(0);
						wordList.remove(0);
						error=new Error(errorCount,"���ܽ��в����������������",firstWord.line,firstWord);
						errorList.add(error);
						graErrorFlag=true;
					}
					break;
				case 'D':
					if(firstWord.value.equals("==")){
						analyseStack.remove(0);
						analyseStack.add(0,"==");
					}else if(firstWord.value.equals("!=")){
						analyseStack.remove(0);
						analyseStack.add(0,"!=");
					}else if(firstWord.value.equals(">")){
						analyseStack.remove(0);
						analyseStack.add(0,">");
					}else if(firstWord.value.equals("<")){
						analyseStack.remove(0);
						analyseStack.add(0,"<");
					}
					else{
						errorCount++;
						analyseStack.remove(0);
						wordList.remove(0);
						error=new Error(errorCount,"�Ƿ������",firstWord.line,firstWord);
						errorList.add(error);	
						graErrorFlag=true;
					}
					break;
				case 'G':
					if(firstWord.type.equals(IDENTIFIER)){
						analyseStack.remove(0);
						analyseStack.add(0,"M");
						analyseStack.add(1,"D");
						analyseStack.add(2,"M");
					}else if(firstWord.type.equals(INT_CONST)){
						analyseStack.remove(0);
						analyseStack.add(0,"M");
						analyseStack.add(1,"D");
						analyseStack.add(2,"M");
					}
					else if(firstWord.value.equals("(")){
						analyseStack.remove(0);
						analyseStack.add(0,"(");
						analyseStack.add(1,"E");
						analyseStack.add(2,")");
					}
					else if(firstWord.value.equals("!")){
						analyseStack.remove(0);
						analyseStack.add(0,"!");
						analyseStack.add(1,"E");
					}
					else{
						errorCount++;
						analyseStack.remove(0);
						wordList.remove(0);
						error=new Error(errorCount,"���ܽ�������������������ͻ����Ų�ƥ��",firstWord.line,firstWord);
						errorList.add(error);
						graErrorFlag=true;
					}
					break;
				case 'L':
					if(firstWord.type.equals(IDENTIFIER)){
						analyseStack.remove(0);
						analyseStack.add(0,"I");
						analyseStack.add(1,"L'");
					}else if(firstWord.type.equals(INT_CONST)){
						analyseStack.remove(0);
						analyseStack.add(0,"I");
						analyseStack.add(1,"L'");
					}
					else if(firstWord.value.equals("(")){
						analyseStack.remove(0);
						analyseStack.add(0,"I");
						analyseStack.add(1,"L'");
					}
					else{
						errorCount++;
						analyseStack.remove(0);
						wordList.remove(0);
						error=new Error(errorCount,"���ܽ�������������������ͻ����Ų�ƥ��",firstWord.line,firstWord);
						errorList.add(error);
						graErrorFlag=true;
					}
					break;
				case '5'://l'
					if(firstWord.value.equals("+")){
						analyseStack.remove(0);
						analyseStack.add(0,"+");
						analyseStack.add(1,"L"); 
					}else {
						analyseStack.remove(0);
					}
					break;
				case 'I':
					if(firstWord.type.equals(IDENTIFIER)){
						analyseStack.remove(0);
						analyseStack.add(0,"K");
						analyseStack.add(1,"I'");
					}else if(firstWord.type.equals(INT_CONST)){
						analyseStack.remove(0);
						analyseStack.add(0,"K");
						analyseStack.add(1,"I'");
					}
					else{
						errorCount++;
						analyseStack.remove(0);
						wordList.remove(0);
						error=new Error(errorCount,"���ܽ��������������������",firstWord.line,firstWord);
						errorList.add(error);	
						graErrorFlag=true;
					}
					break;
				case '6'://I'
					if(firstWord.value.equals("-")){
						analyseStack.remove(0);
						analyseStack.add(0,"-");
						analyseStack.add(1,"I");
					}else {
						analyseStack.remove(0);
					}
					break;
				case 'K':
					if(firstWord.type.equals(IDENTIFIER)){
						analyseStack.remove(0);
						analyseStack.add(0,"T");
						analyseStack.add(1,"K'");
					}else if(firstWord.type.equals(INT_CONST)){
						analyseStack.remove(0);
						analyseStack.add(0,"T");
						analyseStack.add(1,"K'");
					}
					else{
						errorCount++;
						analyseStack.remove(0);
						wordList.remove(0);
						error=new Error(errorCount,"���ܽ��������������������",firstWord.line,firstWord);
						errorList.add(error);	
						graErrorFlag=true;
					}
					break;
				case '7'://K'
					if(firstWord.value.equals("/")){
						analyseStack.remove(0);
						analyseStack.add(0,"/");
						analyseStack.add(1,"K");
					}else {
						analyseStack.remove(0);
					}
					break;
				case 'T':
					if(firstWord.type.equals(IDENTIFIER)){
						analyseStack.remove(0);
						analyseStack.add(0,"F");
						analyseStack.add(1,"T'");
					}else if(firstWord.type.equals(INT_CONST)){
						analyseStack.remove(0);
						analyseStack.add(0,"F");
						analyseStack.add(1,"T'");
					}
					else{
						errorCount++;
						analyseStack.remove(0);
						wordList.remove(0);
						error=new Error(errorCount,"���ܽ��������������������",firstWord.line,firstWord);
						errorList.add(error);	
						graErrorFlag=true;
					}
					break;
				case '8'://T'
					if(firstWord.value.equals("*")){
						analyseStack.remove(0);
						analyseStack.add(0,"*");
						analyseStack.add(1,"T");
					}else {
						analyseStack.remove(0);
					}
					break;
				case 'F':
					if(firstWord.type.equals(IDENTIFIER)){
						analyseStack.remove(0);
						analyseStack.add(0,"id");
						analyseStack.add(1,"O");
					}else if(firstWord.type.equals(INT_CONST)){
						analyseStack.remove(0);
						analyseStack.add(0,"num");
					}
					else{
						errorCount++;
						analyseStack.remove(0);
						wordList.remove(0);
						error=new Error(errorCount,"���ܽ��������������������",firstWord.line,firstWord);
						errorList.add(error);
						graErrorFlag=true;
					}
					break;
				case 'O':
					if(firstWord.value.equals("++")){
						analyseStack.remove(0);
						analyseStack.add(0,"++");
					}else if(firstWord.value.equals("--")){
						analyseStack.remove(0);
						analyseStack.add(0,"--");	
					}else {
						analyseStack.remove(0);
					}
					break;
				case 'Q'://Q
					if(firstWord.type.equals(IDENTIFIER)){
						analyseStack.remove(0);
						analyseStack.add(0,"id");
						analyseStack.add(1,"O");
					}else {
						analyseStack.remove(0);
					}
					break;
					
				}
				
			}
			
			bf.append("��ǰ����ջ:");
			for(int i=0;i<analyseStack.size();i++){
				bf.append(analyseStack.get(i));
			}
			bf.append("\t").append("�������Ŵ���");
			for(int j=0;j<wordList.size();j++){
				bf.append(wordList.get(j).value);
			}
		}
	}
	public String outputLL1() throws IOException{
		File file=new File("./output/");
		if(!file.exists()){
			file.mkdirs();
			file.createNewFile();//�������ļ������ھʹ�����
		}
		String path=file.getAbsolutePath();
		FileOutputStream fos=new FileOutputStream(path+"/LL1.txt");  
		BufferedOutputStream bos=new BufferedOutputStream(fos); 
		OutputStreamWriter osw1=new OutputStreamWriter(bos,"utf-8");
		PrintWriter pw1=new PrintWriter(osw1);
		pw1.println(bf.toString());
		bf.delete(0, bf.length());
		if(graErrorFlag){
			Error error;
			pw1.println("������Ϣ���£�");
		
		pw1.println("�������\t������Ϣ\t���������� \t���󵥴�");
		for(int i=0;i<errorList.size();i++){
			error=errorList.get(i);
			pw1.println(error.id+"\t"+error.info+"\t\t"+error.line+"\t"+error.word.value);
		}
		}else {
			pw1.println("�﷨����ͨ����");
		}
		pw1.close();
		return path+"/LL1.txt";
	}
	/*
	*/

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

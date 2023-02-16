package test;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;


public class Compiler {

	static int row=1;//�ַ��б���
	static int []line=new int[10000];//�ַ���
	static int []wordLine=new int[300];//����������
	static int wordCount;//������
	static char []buffer=new char [10000];//�ַ�������
	/**
	 * ��ջ
	 * @param ip
	 * @param name
	 * @param num
	 * @return
	 */
	Stack markPush(Stack ip,char name,int num){
		Stack s=new Stack();
		s.name=name;
		s.num=num;
		s.next=ip;
		ip=s;
		return ip;
	}
	/**
	 * ��ջ
	 * @param ip
	 */
	void MarkPop(Stack ip){
		Stack s;
		char name;
		name=ip.name;
		s=ip.next;
		if(ip.next!=null){
			ip.name=ip.next.name;
			ip.num=ip.next.num;
			ip.next=ip.next.next;
		}
		
	}
/**
 * �ж��ַ������: int judge(char ch)
   ���ݴ���: �β�ch���մ��ж��ַ�������flag�����ַ����
 */
	int judge(char ch){
		int flag;
		if(ch=='!'||ch=='$'||ch=='&'||ch=='*'||ch=='('||ch==')'||ch=='-'||ch=='_'||
				   ch=='+'||ch=='='||ch=='|'||ch=='{'||ch=='}'||ch=='['||ch==']'||ch==';'||
				   ch==':'||ch=='"'||ch=='<'||ch==','||ch=='>'||ch=='.'||ch=='/'||ch=='\'')     
																	{flag=1;}///�����ַ�
				else if('0'<=ch&&ch<='9')                          {flag=2;}//����
				else if(('a'<=ch&&ch<='z')||('A'<=ch&&ch<='Z'))    {flag=3;}//��ĸ
				else if(ch==' ')                                   {flag=4;}//�ո�
				else if(ch=='\n')                                  {flag=5;}//����
				else if(ch=='?')                                   {flag=6;} 
				else                                               {flag=0;}      //�Ƿ��ַ�
		return flag;
	}
	/**
	 * �ʷ���������: void scan(String fileName)
	���ݴ���: �β�fileName�����ļ�·����
    buffer��line��Ӧ����Դ�ļ��ַ������кţ�char_num�����ַ�������
	 * @throws IOException 
	 */
	void scan(String fileName) throws IOException{
		FileInputStream fis=new FileInputStream(fileName);
		BufferedInputStream bis=new BufferedInputStream(fis);
		InputStreamReader isr=new InputStreamReader(bis,"utf-8");
		BufferedReader inbr=new BufferedReader(isr);	
		char ch;
		int flag,j=0,i=-1;
		while((ch=(char) inbr.read())!=-1){
			flag=judge(ch);
			System.out.print(ch);
			if(flag==1||flag==2||flag==3){
				i++;
				buffer[i]=ch;
				line[i]=row;
			}else if (flag==4){
				i++;
				buffer[i]='?';
				line[i]=row;
			}else if(flag==5){
				i++;
				buffer[i]='~';
				row++;
			}else{
				System.out.println("\n��ע�⣬��"+row+"�е�"+ch+"�ǷǷ��ַ�");
			}
		}
		wordCount=i;
		/* *****************ȷ���������ڵ���******************/
		int one,two,k=0;
		for(i=0;i<wordCount;i++){
			one=judge(buffer[i]);
			two=judge(buffer[i+1]);
			if((one!=two&&buffer[i]!='?'&&buffer[i]!='~')||one==1){
				wordLine[k]=line[i];
				k++;
			}
		}
	}
	
	/*
//======================================================================================================
//  ��ʼ�����ʱ���: struct Word *InitWord()
//          ���ݴ���: head���ص��ʱ��ͷָ��
//              ��ע: ��ʼ�����ʱ��������ָ�ʡ���ʶ���ʡ����ɱ������ű����Ƶ������Ա��ĸ�����
struct Word *InitWord(){
	struct Word *head,*ft,*news,*p;
	struct Sign *s_first,*s_look;
	s_first=s_look=(struct Sign *)malloc(sizeof(struct Sign));
	s_first->kind='\0';
	s_first->name[0]='\0';
	news=head=ft=(struct Word *)malloc(sizeof(struct Word));
	ft->link=s_first;
	ft->next=NULL;
//====================================�ָ�ʹ���==========================================================
	int i=0,k,flag,jud=0;
	for(k=0;k<w_num;k++)
	{
		flag=judge(buffer[k]);
		if(jud==0){//1~
           if(flag==2||flag==3) {
			        news->name[i]=buffer[k];
			        news->name[++i]='\0';
		   }
		   else {//2~
                  i=0;
                  ft=news;
		          if(news->name[0]>=33&&news->name[0]<=125){
				               news=(struct Word *)malloc(sizeof(struct Word));
                               ft->next=news;
                               news->next=NULL;
				  }
		          if(flag==1){//3~
					     if(buffer[k]=='/'&&buffer[k+1]=='/') jud=1;
						 else if(buffer[k]=='/'&&buffer[k+1]=='*') jud=2;
						 else{//4~
			                news->name[i]=buffer[k];
			                if((buffer[k]=='='&&buffer[k+1]=='=')||(buffer[k]=='&'&&buffer[k+1]=='&')||
						  	   (buffer[k]=='|'&&buffer[k+1]=='|')||(buffer[k]=='>'&&buffer[k+1]=='=')||
				               (buffer[k]=='<'&&buffer[k+1]=='=')||(buffer[k]=='!'&&buffer[k+1]=='=')){
								               k=k+1;
											   i=i+1;
											   printf("%d",i);
								               news->name[i]=buffer[k];
							}
			                news->name[1+i]='\0';
			                ft=news;
			                news=(struct Word *)malloc(sizeof(struct Word));
			                ft->next=news;
			                news->next=NULL;
						 }//4~
				  }//3~
				}//2~
		}//1~
		else if(jud==1)
			if(buffer[k]=='~') jud=0;
			   else ;
		else if(jud==2)
			if(buffer[k]=='*'&&buffer[k+1]=='/') {  jud=0;  k=k+1;}
			   else ;
	}
     if(news->name[0]<33||news->name[0]>125) ft->next=NULL;

	/*******************����ת���ɱ�ʶ��******************
	ft=head;
	while(ft){
		if(strcmp(ft->name,"main")==0){ft->mark_name='m';}
		else if(strcmp(ft->name,"void")==0){ft->mark_name='v';}
		else if(strcmp(ft->name,"while")==0){ft->mark_name='w';}
		else if(strcmp(ft->name,"if")==0){ft->mark_name='f';}
		else if(strcmp(ft->name,"else")==0){ft->mark_name='e';}
		else if(strcmp(ft->name,"int")==0){ft->mark_name='a';}
		else if(strcmp(ft->name,"float")==0){ft->mark_name='b';}
		else if(strcmp(ft->name,"double")==0){ft->mark_name='d';}
		else if(strcmp(ft->name,"char")==0){ft->mark_name='c';}
		else if(ft->name[0]>='0'&&ft->name[0]<='9'){ft->mark_name='n';}
		else if(ft->name[0]=='+'||ft->name[0]=='-'||ft->name[0]=='*'||ft->name[0]=='/'
			  ||ft->name[0]=='='||ft->name[0]=='<'||ft->name[0]=='>'
			  ||ft->name[0]==','||ft->name[0]==';'||ft->name[0]=='('||ft->name[0]==')'
			  ||ft->name[0]=='{'||ft->name[0]=='}'){ft->mark_name=ft->name[0];}
		else if(strcmp(ft->name,"&&")==0){ft->mark_name='&';}
		else if(strcmp(ft->name,"||")==0){ft->mark_name='|';}
		else if(strcmp(ft->name,"!=")==0){ft->mark_name='@';}
		else if(strcmp(ft->name,"==")==0){ft->mark_name='#';}
		else {ft->mark_name='i';}
		ft=ft->next;
	}
	/********************��ʼ�����ʱ����ź��к�*******************
	i=0;
	ft=head;
	while(ft){
		ft->num=i;
		ft->line=Lin[i];
		i++;
		ft=ft->next;
	}
	/*************************��ʼ�����ű�************************
	ft=head;
	char word_type;
	while(ft){//1~
		if(ft->mark_name=='a'||ft->mark_name=='b'||ft->mark_name=='c'||ft->mark_name=='d'){//2~
			p=ft->next;
			word_type=ft->mark_name;
			while(p->mark_name!=';'){
				if(p->mark_name!=','){
					s_look=(struct Sign *)malloc(sizeof(struct Sign));
					s_look->kind=word_type;
					strcpy(s_look->name,p->name);
					s_first->next=s_look;
					s_first=s_look;
					s_look->next=NULL;
				}
				p=p->next;
			}
			ft=p;
		}//2~
		ft=ft->next;
	}//1~
	return(head);
}

	 */
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

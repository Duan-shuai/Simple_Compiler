package test;

/**
 * ջ������: 1.���; 2.����; 3.��һ�����
 */
class Stack{
	int num;
	char name;
	Stack next;
	public Stack(){
		this.next=null;
	}
}
/**
 * ����
    ����: 1.���; 2.���򳤶�; 3.���� 4.������һ���Ķ�������
 * @author Administrator
 *
 */
class Guiyue{
	int num;
	int count;
	char name;
	Guiyue next;	
	public Guiyue(){
		this.next=null;
	}
}
/**
 *  ���ű�
����: 1.�Ա�����; 2.��ʶ����; 3.������һ���Ķ�������
 * @author Administrator
 *
 */
class Sign{
char [] name=new char[20];	
char kind;
Sign next;
public Sign(){
	this.next=null;
}
}
/**
 * ���ʱ� class word
  ����: 1.��������; 2.��ʶ����; 3.״̬ 4.���; 5.�к�; 6.���ӷ��ű������; 7.������һ���Ķ�������
 * @author Administrator
 *
 */
class Word{
	char []name=new char[20];
	char mark_name;
	int state;
	int num;
	int line;
	Word link;
	Word next;
	public Word(){
		this.link=null;
		this.next=null;
	}
}

public class Bean {

}

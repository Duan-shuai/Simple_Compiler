package backup;

import java.util.ArrayList;

/**
 * �﷨���ڵ�
 */
public class Node {
public final static String NONTERMINAL="���ս��";
public final static String TERMINAL="�ս��";
Word word=null;//����ֵ
String type;//�ڵ�����
String nonterminal=null; 
ArrayList<Node> child=new ArrayList<Node>();//���Ӽ���
public void addChild(Node node){
	child.add(node);
}
public Node(){
	
}
public Node(Word word,String type){
	this.type=type;
	this.word=word;
}
public Node(String nonterminal,String type){
	this.type=type;
	this.nonterminal=nonterminal;
}
public boolean isTerminal(){
	return this.type.equals(Node.NONTERMINAL);
}
public boolean isArOP(){//�жϽڵ�ֵ�Ƿ�Ϊ���������
if((this.word!=null)&&(word.value.equals("+")||word.value.equals("-")||word.value.equals("*")
		||word.value.equals("/")))return true;
else return false;
}
public boolean isBoolOP(){//�жϽڵ�ֵ�Ƿ�Ϊ���������
	if((this.word!=null)&&(word.value.equals(">")||word.value.equals("<")||word.value.equals("==")
			||word.value.equals("!=")||word.value.equals("!")||
			word.value.equals("&&")||word.value.equals("||")))return true;
	else return false;
	}
}

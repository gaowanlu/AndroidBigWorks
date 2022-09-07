#include<stdio.h>
#include<string.h>
#define LINE_LENGTH_MAX 1000
/*
Contents:
���ݵ�ǰĿ¼�µ�model.js
�ڵ�IMPORT���ݴ����
./build/app.js�� 
*/

/*�ر��ļ� */
void fileClose(FILE*filePointer){
	fclose(filePointer);
}

/*��ʼ��app.js */ 
void InitApp(FILE*app){
	printf(">>��ʼ��app.js\n");
	const char*LANG1="window.SlagoModel={};";
	int LANG1_Length=strlen(LANG1);
	int i=0;
	for(i=0;i<LANG1_Length;i++){
		fputc(LANG1[i], app);
	}
}

/*ȥ���ַ�����ͷ�Ŀո�*/
void DeleteLeftBlankSpace(char*Line){
	if(Line==NULL){return;}
	int LineLength=strlen(Line);
	if(LineLength<=1){return;}//���ַ���"\0"
	int noBlank=-1;
	int i=0;
	for(i=0;i<LineLength;i++){
		if(Line[i]!=' '){
			noBlank=i;
			break;
		}
	}
	if(noBlank==-1){//��һ��ȫΪ�ո� 
		Line[0]='\0';
	}else{//ȥ��ǰ��Ŀո� 
		int before=0;
		int i=noBlank;
		while(Line[i]!='\0'){
			Line[before++]=Line[i++];
		}
		Line[before]='\0';
	}
}


/*�ж�һ���ַ����Ƿ�Ϊ��һ���ַ����Ŀ�ͷ�Ӵ�*/
int StringLeftChildTest(char*Line,const char*KeyWord){
	if(strlen(Line)<strlen(KeyWord)){
		return 0;
	}
	int result=1;
	int i_Line=0;
	int j_Line=0;
	while(i_Line<strlen(Line)&&j_Line<strlen(KeyWord)){
		if(Line[i_Line++]!=KeyWord[j_Line++]){
			result=0;
		}
	}
	return result;
}

void FilePushToApp(const char*file,FILE*app){
	FILE*fp=fopen(file,"r");
	if(fp==NULL){printf(">>��%sʧ��\n",file);return;}
	/*��file�ļ��ڵ�����׷�ӵ�app.js*/
	char buffer[LINE_LENGTH_MAX]={'\0'};
	/*���ļ�·��׷�ӵ�app.js��Ӧ����֮ǰ*/
	fputs("\n//=>",app);
	fputs(file,app);
	fputc('\n',app);
	while( fgets(buffer, LINE_LENGTH_MAX, fp) != NULL ) {
    	fputs(buffer,app);
    }
    //�رմ򿪵��ļ�
	fileClose(fp);
}

/*�ؼ��ʼ��*/
int KeyWordTest(char*Line,FILE*app){
	/*
	KeyWords List
	return      keyword
	0             none
	1             IMPORT
	2             SlagoModel
	3             //
	*/
	if(StringLeftChildTest(Line,"\\\0")){
		//����Ƿ�Ϊע��
		return 3; 
	}
	if(StringLeftChildTest(Line,"IMPORT\0")){
		//��ȡ�ļ�·��
		/*
			IMPORT("./js/FindPage/Header.js");
			�ҵ���һ�� " �������洢 ֱ���ڶ��� " 
		*/
		int i=0;
		int flag=0;
		char buffer[LINE_LENGTH_MAX];
		int before=0;
		while(i<strlen(Line)){
			if(Line[i]=='"'&&flag){
				break;//��ȡ��ϳ��� 
			}
			if(flag){
				buffer[before++]=Line[i];
			}
			if(Line[i]=='"'){
				flag=1;
			}
			i++;
		}
		buffer[before]='\0';
		/*д���ļ�*/
		FilePushToApp(buffer,app);
		return 1;
	}
	if(StringLeftChildTest(Line,"SlagoModel\0")){
		//ֱ�ӽ���һ������д�뵽app.js
		fputc('\n',app);
		fputs(Line, app);
		return 2;
	}
	return 0;//default
}

/*��ȡ����model.js*/
void ReadModel(FILE*model,FILE*app){
	char Line[LINE_LENGTH_MAX];//ÿ�����1000�ַ�
	/*KeyWords List
		IMPORT SlagoModel
	*/
	//���ж�ȡmodel.js
	//ѭ����ȡ�ļ���ÿһ������
	int min=6;//keyword min length is 6
    while( fgets(Line, LINE_LENGTH_MAX, model) != NULL ) {
    	int LineLength=strlen(Line);
    	DeleteLeftBlankSpace(Line);
        if(strlen(Line)<min+1){//��������һ�У����峤�Ȳ��������keyword length 
        	continue;
		}else{//keyword test
			int result=KeyWordTest(Line,app);
		}
    } 
}
int main(int argc,char**argv){
	FILE*model=fopen("./js/model.js","r");
	if(model!=NULL){
		printf(">>model.js�򿪳ɹ�\n");
	}else{
		printf(">>ERROR:model.js��ʧ��\n");
		return -1;
	}
	FILE*app=fopen("./build/app.js","w");
	if(app!=NULL){

		printf(">>./build/app.js�򿪳ɹ�\n");
	}else{
		printf(">>ERROR:./build/app.js��ʧ��\n");
		return -1;
	}
	InitApp(app);
	ReadModel(model,app);
	fileClose(model);
	fileClose(app);
	printf(">>������\n");
	printf(">>");
	char end;
	scanf("%c",&end); 
	return 0;
}

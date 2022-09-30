#include <stdio.h> 
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/types.h>
#include <sys/wait.h>

//GLOBAL
char check;
char check1;

//prototypes
void input(char* cwd);
void execute(char *command);

int main()
{
	check = 1;
	//runs until the user inputs the exit command
	while(check)
	{
		check1 = 0;
		char cwd[1024];
		getcwd(cwd, sizeof(cwd)); //grabs the current working directory
		printf("%s$ ",cwd);
		input(cwd); //sends the cwd to the input function
	}
	return 0;
}

void input(char* cwd)
{
	char uinput[64];
	fgets(uinput, 64, stdin); //grabs the user's input
	char *command = strtok(uinput, " ");//parses it by spaces
	if(!strcmp(command, "cd"))
	{
		//iterates it by one
		command = strtok(NULL, "");
		char *dir = strtok(command, "\n"); //removes \n from the command
		int change = chdir(dir); //changes to desired directory
		if(change)
			printf("Cannot change dir to %s\n", dir);
	}
	else if(!strcmp(command, "pwd\n"))
		printf("%s\n", cwd);
	else if(!strcmp(command, "exit\n"))
		check = 0;
	else
		execute(command);
}


void execute(char *command)
{
	//creates a child process
	pid_t pid = fork();
	if(!pid)
	{
		char *exec_args[256];
		char i = 1;
		char str[32];
		//checks for substrings in command
		char *test1;
		char *test2;
		test1 = strstr(command, "\n");
		test2 = strstr(command, "/bin/");
		//removes \n if it is in the command
		if(test1)
			command = strtok(command, "\n");
		if(test2)
			strcpy(str, command);
		else	
			sprintf(str,"/bin/%s", command);
			command = strtok(NULL, " ");
		exec_args[0] = str;
		
		//exits loop once \n or NULL is reached
		while(command != "\n" && command != NULL)
		{
			char *test;
			test = strstr(command, "\n");
			if(test)
				command = strtok(command, "\n");
			//checking for redirection. Updates a bool
			if(strstr(command, ">") || strstr(command, "<")) 
				check1 =1;
			exec_args[i] = strdup(command);
			command = strtok(NULL, "");
			i++;
		}
		//needs a terminating NULL
		exec_args[i] = NULL;
		fflush(stdout);
		if(check1)
		{
			char str2[32];
			int j = 0;
			//creating a string from exec_args
			while(exec_args[j] != NULL)
			{
				//needs spaces to function
				//echoyeet>text.txt -> echo yeet > text.txt
				strcat(str2, exec_args[j]);
				strcat(str2, " ");
				j++;
			}
			//executes the user's redirection command
			system(str2);
			exit(0);
		}
		else
			//executes the users command
			execvp(exec_args[0], exec_args);
	}
	else
		//must wait to reap the child
		wait(NULL);
}

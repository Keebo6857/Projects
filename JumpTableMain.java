import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

import java.util.Stack;

import javax.lang.model.util.ElementScanner6;

import java.util.Queue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.HashMap;

public class JumpTableMain
{
	public static void main(String[] args) throws FileNotFoundException
    {
        System.out.println("1. Stack");
        System.out.println("2. Queue");
        System.out.println("3. List");
        System.out.println("4. Quit");
        System.out.print("? ");
        Screen screen = new Screen();
        boolean keepRunning = true;
        while(keepRunning) 
        {
            keepRunning = screen.doState();
        }
    }

}

    

interface StateEnterExitMeth
{
    public void invoke();
}

interface StateStayMeth
{
    public boolean invoke();
}

enum State
{
    IDLE,
    STACK,
    QUEUE,
    LIST,
}

class Screen 
{
    Stack<Character> stack = new Stack<>();
    Queue<Character> queue = new LinkedList<>();
    ArrayList<Character> list = new ArrayList<>();



    private State state;
    private HashMap<State, StateEnterExitMeth> stateEnterMeths;
    private HashMap<State, StateStayMeth> stateStayMeths;
    private HashMap<State, StateEnterExitMeth> stateExitMeths;
    public Screen() throws FileNotFoundException
    {
        
        stateEnterMeths = new HashMap<>();
        stateStayMeths = new HashMap<>();
        stateExitMeths = new HashMap<>();

        stateEnterMeths.put(State.IDLE, () -> { StateEnterIdle(); }); 
        stateEnterMeths.put(State.STACK, () -> { StateEnterStack(); }); 
        stateEnterMeths.put(State.QUEUE, () -> { StateEnterQueue(); });
        stateEnterMeths.put(State.LIST, () -> { StateEnterList(); });

        stateStayMeths.put(State.IDLE, () -> { return StateStayIdle(); });
        stateStayMeths.put(State.STACK, () ->{ return StateStayStack(); });
        stateStayMeths.put(State.QUEUE, () -> { return StateStayQueue(); });
        stateStayMeths.put(State.LIST, () -> { return StateStayList(); });

        stateExitMeths.put(State.IDLE, () -> { StateExitIdle(); });
        stateExitMeths.put(State.STACK, () -> { StateExitStack(); });
        stateExitMeths.put(State.QUEUE, () -> { StateExitQueue(); });
        stateExitMeths.put(State.LIST, () -> { StateExitList(); });

        readStack();
        readQueue();
        readList();

        state = State.IDLE;
    }

    //State methods
    public void changeState(State newState)
    {
        if(state != newState)
        {
            stateExitMeths.get(state).invoke();
            state = newState;
            stateEnterMeths.get(state).invoke();
        }
    }

    public boolean doState()
    {
        stateStayMeths.get(state).invoke();
        return stateStayMeths.get(state).invoke();
    }


    //Enter Methods
    private void StateEnterIdle()
    {
        changeState(State.IDLE);
    }
    private void StateEnterStack()
    {
        // System.out.println("oooop");
        printStack(stack);
        changeState(State.STACK);
    }
    private void StateEnterQueue()
    {
        printQueue(queue);
        changeState(State.QUEUE);
    }
    private void StateEnterList()
    {
        printList(list);
        changeState(State.LIST);
    }


    //Stay Methods
    private boolean StateStayIdle()
    {   
        String input = getInput();
        if(input.equals("1"))
        {
            changeState(State.STACK);
            return false;
        }
        else if(input.equals("2"))
        {
            changeState(State.QUEUE);
            return false;
        }
        else if(input.equals("3"))
        {
            changeState(State.LIST);
            return false;
        }

        else if(input.equals("4"))
        {
            System.exit(0);
            return false;
        }
        else
        {
            return true;
        }
    }
    private boolean StateStayStack()
    {
        String input = getInput();
        if(input.charAt(0) == '1')
        {
            String[] inArr = input.split(" ");
            stack.push(inArr[1].charAt(0));
            printStack(stack);
            return true;
        }
        else if(input.equals("2"))
        {
            stack.pop();
            printStack(stack);
            return true;
        }
        else if(input.equals("3"))
        {
            changeState(State.QUEUE);
            return false;
        }

        else if(input.equals("4"))
        {
            changeState(State.LIST);
            return false;
        }
        else if(input.equals("5"))
        {
            System.exit(0);
            return true;
        }
        else
        {
            System.out.println("try again");
            return true;
        }

    }
    private boolean StateStayQueue()
    {
        String input = getInput();
        if(input.charAt(0) == '1')
        {
            String[] inArr = input.split(" ");
            queue.add(inArr[1].charAt(0));
            printQueue(queue);
            return true;
        }
        else if(input.equals("2"))
        {
            queue.remove();
            printQueue(queue);
            return true;
        }
        else if(input.equals("3"))
        {
            changeState(State.STACK);
            return false;
        }

        else if(input.equals("4"))
        {
            changeState(State.LIST);
            return false;
        }
        else if(input.equals("5"))
        {
            System.exit(0);
            return true;
        }
        else
        {
            System.out.println("try again");
            return true;
        }
    }
    private boolean StateStayList()
    {
        String input = getInput();
        if(input.charAt(0) == '1')
        {
            String[] inArr = input.split(" ");
            list.add(inArr[1].charAt(0));
            printList(list);
            return true;
        }
        else if(input.equals("2"))
        {
            // System.out.println("size is: " + list.size());
            list.remove(list.size() - 1);
            printList(list);
            return true;
        }
        else if(input.equals("3"))
        {
            changeState(State.STACK);
            return false;
        }

        else if(input.equals("4"))
        {
            changeState(State.QUEUE);
            return false;
        }
        else if(input.equals("5"))
        {
            System.exit(0);
            return true;
        }
        else
        {
            System.out.println("try again");
            return true;
        }
    }


    //Exit Methods
    private void StateExitIdle()
    {

    }
    private void StateExitStack()
    {
        String str1 = stack.toString();
        str1 = str1.replaceAll("\\[", "");
        str1 = str1.replaceAll("\\]", "");
        str1 = str1.replaceAll(" ", "");
        // System.out.println(str1);
        writeStack(str1);
    }
    private void StateExitQueue()
    {
        String str1 = queue.toString();
        str1 = str1.replaceAll("\\[", "");
        str1 = str1.replaceAll("\\]", "");
        str1 = str1.replaceAll(" ", "");
        // System.out.println(str1);
        writeQueue(str1);
    }
    private void StateExitList()
    {
        String str1 = list.toString();
        str1 = str1.replaceAll("\\[", "");
        str1 = str1.replaceAll("\\]", "");
        str1 = str1.replaceAll(" ", "");
        // System.out.println(str1);
        writeList(str1);
    }


    //helper funcs
    public static String getInput()
    {
        Scanner tempIn = new Scanner(System.in);
        String in = tempIn.nextLine();
        return(in);
    }

    public static void printStack(Stack<Character> s)
    {
        String str = s.toString();
        str = str.replaceAll("\\[", "");
        str = str.replaceAll("\\]", "");
        str = str.replaceAll(" ", "");
        str = str.replaceAll(",", "");

        for(int i = 0; i < str.length(); i++)
        {

            System.out.println("|" + str.charAt(i) + "|");
            System.out.println("|_|");
        }
        System.out.println("1. Push");
        System.out.println("2. Pop");
        System.out.println("3. Save & Move to Queue");
        System.out.println("4. Save & Move to List");
        System.out.println("5. Quit");
        System.out.println("? ");
    }
    
    public void readStack() throws FileNotFoundException
    {
        File f1 = new File("stack.txt");
        Scanner s1 = new Scanner(f1);
        String stackStr = s1.nextLine();
        ArrayList<String> sarr = new ArrayList<String>(Arrays.asList(stackStr.split(",")));
        for(int i = 0; i < sarr.size(); i++)
        {
            stack.push(sarr.get(i).charAt(0));
        }
        s1.close();
    }

    public void writeStack(String input)
    {
        try
        {
            String str = input;
            FileWriter file = new FileWriter("stack.txt", false);
            file.write(str);
            file.close();
        }
        catch(IOException ioe)
        {
            System.out.println("file not found");
        }
    }

    public void printQueue(Queue<Character> q)
    {
        String str = q.toString();
        str = str.replaceAll("\\[", "|");
        str = str.replaceAll("\\]", "|");
        str = str.replaceAll(",", "|");
        str = str.replaceAll(" ", "");
        System.out.println(str);
        System.out.println("1. Enqueue");
        System.out.println("2. Dequeue");
        System.out.println("3. Save & Move to Stack");
        System.out.println("4. Save & Move to List");
        System.out.println("5. Quit");
        System.out.println("? ");
    }

    public void readQueue() throws FileNotFoundException
    {
        File f1 = new File("queue.txt");
        Scanner s1 = new Scanner(f1);
        String queueStr = s1.nextLine();
        ArrayList<String> qarr = new ArrayList<String>(Arrays.asList(queueStr.split(",")));
        for(int i = 0; i < qarr.size(); i++)
        {
            queue.add(qarr.get(i).charAt(0));
        }
        s1.close();
    }

    public void writeQueue(String input)
    {
        try
        {
            String str = input;
            FileWriter file = new FileWriter("queue.txt", false);
            file.write(str);
            file.close();
        }
        catch(IOException ioe)
        {
            System.out.println("file not found");
        }
    }

    public void printList(ArrayList<Character> a)
    {
        String str = a.toString();
        str = str.replaceAll("\\[", "{");
        str = str.replaceAll("\\]", "}");
        str = str.replaceAll(" ", "");
        System.out.println(str);
        System.out.println("1. Append");
        System.out.println("2. Remove");
        System.out.println("3. Save & Move to Stack");
        System.out.println("4. Save & Move to Queue");
        System.out.println("5. Quit");
        System.out.println("? ");
    }

    public void readList() throws FileNotFoundException
    {
        File f1 = new File("list.txt");
        Scanner s1 = new Scanner(f1);
        String listStr = s1.nextLine();
        ArrayList<String> larr = new ArrayList<String>(Arrays.asList(listStr.split(",")));
        for(int i = 0; i < larr.size(); i++)
        {
            list.add(larr.get(i).charAt(0));
        }
        s1.close();
    }

    public void writeList(String input)
    {
        try
        {
            String str = input;
            FileWriter file = new FileWriter("list.txt", false);
            file.write(str);
            file.close();
        }
        catch(IOException ioe)
        {
            System.out.println("file not found");
        }
    }

    // private void StateQuit()
    // {

    // }
}





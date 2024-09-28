#include <stdio.h>

int main() {
    // This is a comment
    /* escape sequence = character combination consisting of a backslash \ 
                         followed by a letter or combination of digits.
                         They specify actions within a line of text (string)
                         \n = newline
                         \t = tab 
                         \\ = display \
                         \' = display '
                         \" = display "
    */
   
    printf("1\t2\t3\n4\t5\t6\n7\t8\t9\n");
    printf("\"I like Sushi\" - Musashi Miyamoto probably");

    // variable =   Allocated space in memory to store a value.
    //              We refer to a variable's name to access the stored value.
    //              That variable now behaves as if it was the value it contains.
    //              BUT we need to declare what type of data we are storing.

    int x;            //declaration
    x = 123;       //initialization
    int y = 321; //declaration + initialization

    int age = 21;              //integer
    float gpa = 2.05;       //floating point number
    char grade = 'C';        //single character
    char name[] = "Bro";  //array of characters
    
    // % = format specifier
    printf("Hello %s\n", name);
    printf("You are %d years old\n", age);
    printf("Your average grade is %c\n", grade);
    printf("Your gpa is %f\n", gpa);

    return 0;

}
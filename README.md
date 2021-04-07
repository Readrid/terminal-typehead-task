# Парсер цепочки операций над целочисленными массивами
Выражения описываются следующей грамматикой:
```
<digit>   ::= “0” | “1" | “2” | “3" | “4” | “5" | “6” | “7" | “8” | “9"
<number> ::= <digit> | <digit> <number>
<operation> ::= “+” | “-” | “*” | “>” | “<” | “=” | “&” | “|”
<constant-expression> ::= “-” <number> | <number>
<binary-expression> ::= “(” <expression> <operation> <expression> “)”
<expression> ::= “element” | <constant-expression> | <binary-expression>
<map-call> ::= “map{” <expression> “}”
<filter-call> ::= “filter{” <expression> “}”
<call> ::= <map-call> | <filter-call>
<call-chain> ::= <call> | <call> “%>%” <call-chain>
```

Также реализован преобразователь выражений описываемых правилом ```<call-chain>``` в выражения вида ```<filter-call> “%>%” <map-call>```, эквивалентные исходному.

Запуск консольного приложения:
```bash
$ ./gredlew build
$ java -jar build/libs/terminal-typehead-task-1.0-SNAPSHOT.jar
```
После запуска можно вводить цепочку команд. 
После ввода одной строки будет сразу же упрощённая версия выражения или сообщение об ошибке в случае, если выражение некорректно. 
Для выхода из консольного приложения введите ```exit```.

insert into table_1 (col, col2 , col3)values('default','DEFAULT*24\/[]?' , '''Default''''123''''') ; 
insert into table_1 (col, col2 , col3)values('default','DEFAULT*24\/[]?' , '''Default''''123''''')
insert into table_2 (col, col2 , col3)values('default','DEFAULT*24\/[]?' , '''Default''''123''''') --这是一个注释
insert into table_2 (col, col2 , col3)values('default','DEFAULT*24\/[]?' , '''Default''''123'''''); --这是一个注释
insert into table_2 (col, col2 , col3)values('default','DEFAULT*24\/[]?' , '--这不是一个注释'); --这是一个注释
insert into table_2 (col, col2 , col3)values('default','DEFAULT*24\/[]?' , ';--这不是一个注释'); --这是一个注释
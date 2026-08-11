# A06 Consideraciones de Entities con PostgreSQL

## Contexto
En la asignatura de TAW las tablas que definían las entidades estaban nombras con mayúsculas, no se si por convención o por preferencia. La base de datos que usamos era embebida, concretamente, Apache Derby. Durante el desarrollo de las entidades de este proyecto me encontré con características de nombramiento con PostgreSQL que conviene comentar, pues determinaron la forma en la que se trabajaba con ellas.

## Características de PostgreSQL - Nombre de Tablas
PostgreSQL convierte automáticamente los nombres de las columnas a minúsculas a menos que esté encerrado entre comillas `" "`.[(1)] Sin embargo, como es el motor de Hibernate es el que hace la traducción ORM y pasa las entidades a SQL, depende de la configuración que tenga, aunque escriba `@Table(name = "AMIGO")` puede descoordinarse con Postgres e interpretar `amigo`, generando implícitamente una tabla que no debería existir.

Con esta regla en mente usamos nombres de atributos y tablas para entidades como snake_case y en minúscula siempre para no dar oportunidad a errores inesperados entre la base de datos y las entidades en Java.


## Fuentes Consultadas
- Don't do this: [https://wiki.postgresql.org/wiki/Don't_Do_This#Don.27t_use_upper_case_table_or_column_names](https://wiki.postgresql.org/wiki/Don't_Do_This#Don.27t_use_upper_case_table_or_column_names)


[(1)]: https://wiki.postgresql.org/wiki/Don't_Do_This#Don.27t_use_upper_case_table_or_column_names (Don't do this)